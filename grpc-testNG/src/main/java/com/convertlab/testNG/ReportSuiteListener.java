package com.convertlab.testNG;

import com.atf.cap.internal.suite.ReporterSuiteListener;
import com.convertlab.testNG.SuiteResultWriter;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ISuite;
import org.testng.ISuiteListener;

import java.io.File;

public class ReportSuiteListener implements ISuiteListener {

    private static Logger logger = LoggerFactory.getLogger(ReporterSuiteListener.class);

    @Override
    public void onStart(ISuite suite) {

    }

    @Override
    public void onFinish(ISuite suite) {

    }

    private void prepareWorkspace(ISuite suite) {
        try {
            File suiteDir = new File(suite.getOutputDirectory());
            FileUtils.forceMkdir(suiteDir);
            File parent = suiteDir.getParentFile();
            File capReporter = new File(parent, SuiteResultWriter.REPORTER_DIRECTORY);
            File capCapture = new File(capReporter, SuiteResultWriter.REPORTER_IMAGES);

            FileUtils.forceMkdir(capCapture);

        } catch (Exception e2) {
            logger.error("prepare workspace directory", e2);
        }
    }
}
