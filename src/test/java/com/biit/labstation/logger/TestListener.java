package com.biit.labstation.logger;

import com.biit.labstation.ScreenShooter;
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
        final ITestWithWebDriver currentClass = getCurrentClass(result);
        final ScreenShooter screenShooter = currentClass.getScreenShooter();
        final String fileName = result.getMethod().getMethodName() + "_" + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        screenShooter.takeScreenshot(fileName);
        TestLogging.errorMessage(this.getClass().getName(),
                "### Test failed '" + result.getMethod().getMethodName() + "' from '" + result.getTestClass().getName() + "'. File generated:\n" + fileName);
    }

    @Override
    public void onTestSkipped(ITestResult result) {

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }

    @Override
    public void onStart(ITestContext context) {
        TestLogging.info(this.getClass().getName(), "##### Starting tests from '" + context.getName() + "'.");
    }

    @Override
    public void onFinish(ITestContext context) {
        TestLogging.info(this.getClass().getName(), "##### Tests finished from '" + context.getName() + "'.");
    }

//    private WebDriver getWebDriver(ITestResult result) {
//
//    }

    private ITestWithWebDriver getCurrentClass(ITestResult result) {
        final String testName = result.getMethod().getMethodName();
        return (ITestWithWebDriver) result.getInstance();
    }
}
