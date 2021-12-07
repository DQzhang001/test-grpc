package com.convertlab.testNG;

import com.atf.cap.CapLogger;
import com.atf.cap.dataprovider.ATFData;
import com.atf.cap.device.model.AppInfo;
import com.atf.cap.device.model.DeviceInfo;
import com.atf.cap.internal.suite.ReporterSuiteListener;
import com.atf.cap.internal.suite.SuiteManListener;
import com.atf.cap.lanucher.Config;
import com.atf.cap.plugin.CapPlugin;
import com.atf.cap.plugin.JsonPlugin;
import com.atf.cap.plugin.SystemPlugin;
import com.atf.cap.reporter.*;
import com.google.common.reflect.ClassPath;
import com.google.gson.GsonBuilder;
import lombok.SneakyThrows;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GRPCReporter implements IReporter {

    private static final Logger logger = LoggerFactory.getLogger(GRPCReporter.class);

    private static final String relativeDir = "cap-reporter";

    private com.convertlab.testNG.SuiteResultWriter writer = new SuiteResultWriter();

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        try {
            generateReportFromJSONArray(suites, outputDirectory);
        } catch (IOException e) {
            logger.warn("", e);
        }
    }

    private void generateReportFromJSONArray(List<ISuite> suites, String outputDirectory) throws IOException{
        if(SystemPlugin.getProperty("CAP_Report")!=null)
        {
            logger.info("don't create the Report");
        }
        else {
            logger.info("create the Report");
            String result = parseTestResultToString(suites);
            copyFiles(new File(outputDirectory, "cap-reporter"));
            FileUtils.write(new File(outputDirectory + "/cap-reporter/js/data.js"),
                    "var testSuits = " + result.toString(), "UTF-8");
            writer.generateOldDirectory(outputDirectory);
            SendMd5Report(result.toString());
        }
    }

    private void SendMd5Report(String result) {
        try {
            if (StringUtils.isNotBlank(SystemPlugin.getProperty("JENKINSRUNTYPE"))) {
                JSONArray suitCases = JsonPlugin.GetJSONObject(result).getJSONArray("suits");
                String jenkinsJob = ATFData.GetJenkinsJob();
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < suitCases.size(); i++) {
                    JSONObject suitCase = suitCases.getJSONObject(i);
                    String md5SuitCase = suitCase.getString("suitname");
                    JSONArray testclass = suitCase.getJSONArray("testclass");
                    for (int j = 0; j < testclass.size(); j++) {
                        JSONObject testclas = testclass.getJSONObject(j);
                        String md5testclas = testclas.getString("classname");
                        JSONArray cases = testclas.getJSONArray("cases");
                        for (int k = 0; k < cases.size(); k++) {
                            JSONObject testcase = cases.getJSONObject(k);
                            if (null != testcase) {
                                int runResult = testcase.getInt("result");
                                String md5testcase=String.valueOf(System.currentTimeMillis());
                                if(testcase.has("caseId"))
                                {
                                    md5testcase = testcase.getString("caseId");
                                    String repoorturl = ATFData.GetJenkinsJOBURL()+ATFData.GetJenkinsBatchId()+"/Report/logs.html?suitid=" + md5SuitCase + "&testclassid=" + md5testclas + "&testcaseid=" + md5testcase;
                                    JSONObject jsonObject = new JSONObject();
                                    String testRailsName ="未关联用例";
                                    if(testcase.has("TestRailName")) {
                                        testRailsName = testcase.getString("TestRailName");
                                    }
                                    jsonObject.put("caseName", testRailsName);
                                    jsonObject.put("jenkinsJob", ATFData.GetJenkinsJOBURL());
                                    jsonObject.put("suitIdMd5", repoorturl);
                                    jsonObject.put("classIdMd5", md5testclas);
                                    jsonObject.put("caseIdMd5", md5testcase);
                                    jsonObject.put("result", runResult);
                                    jsonArray.add(jsonObject);
                                }

                            }
                        }
                    }
                }
                CapPlugin.sendReport(jsonArray);
            }
        }catch (Exception ex)
        {
            CapLogger.info("SendMd5Report Exception"+ex.getMessage());
        }
    }


    private void copyFiles(File descDir) throws IOException {
        ClassPath classPath = ClassPath.from(CapReporter.class.getClassLoader());

        Set<ClassPath.ResourceInfo> resourceInfos = classPath.getResources();
        String dir = CapReporter.class.getPackage().getName().replace('.', '/') + "/" + relativeDir;

        for (ClassPath.ResourceInfo info : resourceInfos) {
            String resourceName = info.getResourceName();
            if (info.getResourceName().startsWith(dir) && !StringUtils.endsWith(resourceName, ".class")) {

                InputStream stream = CapReporter.class.getClassLoader().getResourceAsStream(resourceName);
                FileUtils.copyInputStreamToFile(stream, new File(descDir, StringUtils.replace(resourceName, dir + "/", "")));
            }
        }
    }

    private String parseTestResultToString(List<ISuite> suites) {
        TestJob testJob = new TestJob();
        List<TestSuite> testSuites = new ArrayList<TestSuite>();
        int total = 0;
        int success = 0;
        int fail = 0;
        int skip = 0;
        String startTime = null;
        String endTime = null;
        for (ISuite suit : suites) {
            TestSuite testSuite = parseTestSuit(suit);
            testSuites.add(testSuite);// Add Suit to JSON Array

            total += testSuite.getTotal();
            success += testSuite.getSuccess();
            fail += testSuite.getFail();
            skip += testSuite.getSkip();
            // get job start/end time.
            String suiteStartTime = "2020-08-08";
            String suiteEndTime = "2020-08-08";
            if(testSuite.getStarttime()!=null) {
                suiteStartTime = testSuite.getStarttime();
                suiteEndTime = testSuite.getEndtime();
            }
            if (startTime == null || endTime == null) {
                startTime = suiteStartTime;
                endTime = suiteEndTime;
            }
            if (startTime.compareTo(suiteStartTime) > 0) {
                startTime = suiteStartTime;
            }
            if (endTime.compareTo(suiteEndTime) < 0) {
                endTime = suiteEndTime;
            }
            if(ATFData.GetProjectName()!=null && ATFData.GetATPTestData())
            {
                testSuite.setProjectName(ATFData.GetProjectName());
                testSuite.setEnv(ATFData.GetNowEnv());
                testSuite.setTestRailPlanId(ATFData.GetTestRailTestPlanId());
                testSuite.setSuitname(ATFData.GetSuitName());
            }
        }
        ISuite suit = suites.get(0);
        Config config = (Config) suit.getAttribute(SuiteManListener.CAP_CONFIG);

        if(config!=null) {
            switch (config.getDriverType()) {
                case HttpClient:
                    break;
                default:
                    AppInfo appInfo = (AppInfo) suit.getAttribute(ReporterSuiteListener.APP_INFO);
                    DeviceInfo deviceInfo = (DeviceInfo) suit.getAttribute(ReporterSuiteListener.DEVICE_INFO);

                    testJob.setAppInfo(appInfo);
                    testJob.setDeviceInfo(deviceInfo);
                    break;
            }
            testJob.setDriverType(config.getDriverType().toString());
            testJob.setPlatform(config.getPlatformName());

            testJob.setTotal(total);
            testJob.setSuccess(success);
            testJob.setFail(fail);
            testJob.setSkip(skip);
            testJob.setStarttime(startTime);
            testJob.setEndtime(endTime);
            testJob.setSuits(testSuites);
            Map<String, Object> filesMap = CapLogger.filesMap;
            if (!filesMap.isEmpty()) {
                testJob.setFilesMap(filesMap);
            }
            CapPlugin.sentJenkinsJobStatus(testJob,"Finish");
            CapPlugin.SendNotice(testJob);
        }
        return new GsonBuilder().setPrettyPrinting().create().toJson(testJob);
    }

    private TestSuite parseTestSuit(ISuite suite) {
        List<TestClass> testClasses = new ArrayList<TestClass>();
        List<ITestResult> listTR = writer.getTestMethods(suite);
        Map<String, List<ITestResult>> classGroupMethod = writer.getTestClassGroups(listTR);
        for (Map.Entry<String, List<ITestResult>> classMethods : classGroupMethod.entrySet()) {
            TestClass testClass=parseTestClass(classMethods);
            testClasses.add(testClass);
        }
        return writer.getPropertyToSuite(suite, testClasses);
    }

    @SneakyThrows
    private TestClass parseTestClass(Map.Entry<String, List<ITestResult>> classMethods) {
        TestClass testClass = new TestClass();
        for (ITestResult testResult : classMethods.getValue()) {
            TestMethod testMethod=writer.getTestMethod(testResult);
            if(testMethod.getTestRailName().contains(";"))
            {
                String[] arr=testMethod.getTestRailName().split(";");
                for(int i=0;i<arr.length;i++)
                {
                    TestMethod temp=new TestMethod();
                    BeanUtils.copyProperties(temp,testMethod);
                    temp.setTestRailName(arr[i]);
                    testClass.getCases().add(temp);
                }
            }else {
                testClass.getCases().add(testMethod);
            }
        }
        testClass.setClassname(classMethods.getKey());
        return testClass;
    }
}
