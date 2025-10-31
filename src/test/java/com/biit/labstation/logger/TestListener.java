package com.biit.labstation.logger;

/*-
 * #%L
 * Lab Station Test Bench
 * %%
 * Copyright (C) 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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
