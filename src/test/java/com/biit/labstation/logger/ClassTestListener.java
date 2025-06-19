package com.biit.labstation.logger;

import org.testng.IClassListener;
import org.testng.ITestClass;

public class ClassTestListener implements IClassListener {

    @Override
    public void onBeforeClass(ITestClass testClass) {
        TestLogging.info("###### Class started '" + testClass.getRealClass().getName() + "' ######");
    }

    @Override
    public void onAfterClass(ITestClass testClass) {
        TestLogging.info("###### Completed class '" + testClass.getRealClass().getName() + "' ######");
    }
}
