package com.biit.labstation.logger;

import com.biit.labstation.ScreenShooter;
import com.biit.labstation.tests.BaseTest;
import com.biit.labstation.tests.ITestWithWebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        TestLogging.info("### Test started '" + result.getMethod().getMethodName() + "' from '" + result.getTestClass().getName() + "'.");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        TestLogging.info("### Test finished '" + result.getMethod().getMethodName() + "' from '" + result.getTestClass().getName() + "'.");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        final ITestWithWebDriver currentClass = (ITestWithWebDriver) result.getInstance();
        final ScreenShooter screenShooter = currentClass.getScreenShooter();
        final String fileName = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) + "_" + result.getMethod().getMethodName();
        LabStationLogger.debug(this.getClass().getName(), "Creating screenshot on '{}.png'.", fileName);
        screenShooter.takeScreenshot(fileName);
        LabStationLogger.debug(this.getClass().getName(), "!!! Showing console output.");
        currentClass.getDriver().analyzeConsoleLog();
        TestLogging.errorMessage(this.getClass().getName(),
                "### Test failed '" + result.getMethod().getMethodName() + "' from '" + result.getTestClass().getName() + "'. File generated:\n" + fileName);
        //Stop next tests as they can fail now.
        BaseTest.testFailureDetected = true;
    }

    @Override
    public void onTestSkipped(ITestResult result) {

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }

    @Override
    public void onStart(ITestContext context) {
        TestLogging.info(this.getClass().getName(), "######## Starting tests from '" + context.getName() + "' ########.");
    }

    @Override
    public void onFinish(ITestContext context) {
        TestLogging.info(this.getClass().getName(), "######## Tests finished from '" + context.getName() + "' ########.");
    }
}
