package com.convertlab.testNG;

import com.atf.cap.CapLogger;
import com.atf.cap.common.exception.NestedExceptionUtils;
import com.atf.cap.dataprovider.DataParameter;
import com.atf.cap.internal.suite.ReporterSuiteListener;
import com.atf.cap.plugin.CapPlugin;
import com.atf.cap.reporter.TestClass;
import com.atf.cap.reporter.TestMethod;
import com.atf.cap.reporter.TestMethodLog;
import com.atf.cap.reporter.TestSuite;
import com.qa.testrail.annotations.TestRailCase;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.collections.Lists;
import org.testng.collections.Maps;

import java.lang.reflect.Method;
import java.util.*;

public class SuiteResultWriter {
    private static final Logger logger = LoggerFactory.getLogger(com.atf.cap.reporter.SuiteResultWriter.class);

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final FastDateFormat dateFormat = FastDateFormat.getInstance(DATE_FORMAT);
    private static final int SKIP = ITestResult.SKIP;
    private static final int PASS = ITestResult.SUCCESS;
    private static final int FAIL = ITestResult.FAILURE;

    public static final FastDateFormat logDirectoryName = FastDateFormat.getInstance("yyyy-MM-dd-HH-mm-ss");
    public static final String OLD_LOG_DIRECTORY = "old-cap-logger";
    public static final String REPORTER_DIRECTORY = "cap-reporter";
    public static final String REPORTER_IMAGES = "images";

    public TestSuite getPropertyToSuite(ISuite suite, List<TestClass> jsaTestClasses) {

        int passed = 0;
        int failed = 0;
        int skipped = 0;

        for(TestClass testClass:jsaTestClasses) {
            for (TestMethod testMethod : testClass.getCases()) {
                if (testMethod.getResult() == 1) {
                    passed += 1;
                }
                if (testMethod.getResult() == 2) {
                    failed += 1;
                }
                if (testMethod.getResult() == 3) {
                    skipped += 1;
                }
            }
        }

        Date start = (Date) suite.getAttribute(ReporterSuiteListener.SUITE_START_DATE);
        Date end = (Date) suite.getAttribute(ReporterSuiteListener.SUITE_END_DATE);
        TestSuite testSuite = new TestSuite();
        testSuite.setSuitname(suite.getName());
        testSuite.setSuccess(passed);
        testSuite.setFail(failed);
        testSuite.setSkip(skipped);
        testSuite.setTotal(passed + failed + skipped);

        if(start!=null) {
            testSuite.setStarttime(dateFormat.format(start));
            testSuite.setEndtime(dateFormat.format(end));
            //testSuite.setTestclass(jsaTestClasses); // Add Cases to Suite
        }
        return testSuite;
    }

    public List<ITestResult> getTestMethods(ISuite suite) {
        List<ITestResult> allResults = new ArrayList<ITestResult>();
        Map<String, ISuiteResult> suiteResults = suite.getResults();
        for (Map.Entry<String, ISuiteResult> suiteResult : suiteResults.entrySet()) {
            ISuiteResult sr = suiteResult.getValue();

            allResults.addAll(sr.getTestContext().getPassedTests().getAllResults());
            allResults.addAll(sr.getTestContext().getFailedTests().getAllResults());
            allResults.addAll(sr.getTestContext().getSkippedTests().getAllResults());
        }
        return allResults;
    }

    public void generateOldDirectory(String outputDirectory) {
    }

    public Map<String, List<ITestResult>> getTestClassGroups(List<ITestResult> testResults) {
        Map<String, List<ITestResult>> map = Maps.newHashMap();
        for (ITestResult result : testResults) {
            String className = result.getTestClass().getName();
            List<ITestResult> list = map.get(className);
            if (list == null) {
                list = Lists.newArrayList();
                map.put(className, list);
            }
            list.add(result);
        }
        return map;
    }

    public TestMethod getTestMethod(ITestResult result) {
        TestMethod testCase = new TestMethod();

        Throwable error = result.getThrowable();
        if (error != null) {
            String eroMsg = NestedExceptionUtils.buildStackTrace(error);
            testCase.setException(eroMsg);
        } else {
            testCase.setException("");
        }

        String suffix = "";
        if (result.getParameters() != null && result.getParameters().length > 0) {
            for (Object o : result.getParameters()) {
                if (o instanceof DataParameter) {
                    suffix += ((DataParameter) o).title();
                }
            }
        }
        String caseName = result.getName();
        Method method = result.getMethod().getConstructorOrMethod().getMethod();
        TestRailCase trCase = method.getAnnotation(TestRailCase.class);
        String TestRailCase = caseName;
        if(trCase!=null)
        {
            if(StringUtils.isNotEmpty(suffix))
            {
                TestRailCase = suffix;
            }else {
                TestRailCase = trCase.value();
            }

        }
        Object[] parameters = result.getParameters();
        if(parameters.length > 0){
            for(int i= 0; i<parameters.length; i++){
                if(parameters[i].toString().toLowerCase().contains(result.getName().trim().toLowerCase())){
                    caseName +="(" + parameters[i] + ")";
                    break;
                }
            }
        }
        testCase.setCasename(caseName);
        JSONObject CaseInfo= CapPlugin.getCaseInfo(TestRailCase);
        if(CaseInfo.size() >0 && CaseInfo.has("caseId"))
        {
            testCase.setCaseId(CapPlugin.getCaseInfo(TestRailCase).getString("caseId"));
        }else{
            testCase.setCaseId(DigestUtils.md5DigestAsHex(caseName.getBytes()));
        }
        testCase.setTestRailName(TestRailCase);
        testCase.setResult(result.getStatus());
        testCase.setStarttime(dateFormat.format(new Date(result
                .getStartMillis())));
        testCase.setEndtime(dateFormat.format(new Date(result.getEndMillis())));
        testCase.setLog(parseReportLog(result));
        return testCase;
    }

    private List<TestMethodLog> parseReportLog(ITestResult testMethodResult) {
        List<TestMethodLog> logs = new ArrayList<TestMethodLog>();
        List<String> logOutput = Reporter.getOutput(testMethodResult);
        for (String line : logOutput) {
            TestMethodLog tml = new TestMethodLog();
            String[] log = line.split(CapLogger.SplitChar);
            if (log.length != 3) {
                continue;
            }
            tml.setType(log[0]);
            tml.setMessage(log[1]);
            tml.setTime(log[2]);
            logs.add(tml);
        }
        Set<ITestResult> allMethodResult = testMethodResult.getTestContext()
                .getPassedConfigurations().getAllResults();
        for (ITestResult result : allMethodResult) {
            List<String> confiMethodLog = Reporter.getOutput(result);
            for (String line : confiMethodLog) {
                String[] log = line.split(CapLogger.SplitChar);
                if (log.length != 3) {
                    continue;
                }
                if (log[1].contains(CapLogger.CaptureChar)) {
                    String[] detail = log[1].split(CapLogger.CaptureChar);
                    if (detail[1].equals(testMethodResult.getTestClass()
                            .getName() + "." + testMethodResult.getName())) {
                        TestMethodLog tml = new TestMethodLog();
                        tml.setType(log[0]);
                        tml.setMessage(detail[0]);
                        tml.setTime(log[2]);
                        logs.add(tml);
                    }
                }
            }
        }
        return logs;
    }
}
