package com.biit.labstation.tests;

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

import com.biit.labstation.CustomChromeDriver;
import com.biit.labstation.ScreenShooter;
import com.biit.labstation.ToolTest;
import com.biit.labstation.components.SnackBar;
import com.biit.labstation.logger.LabStationLogger;
import org.awaitility.Awaitility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public abstract class BaseTest extends AbstractTestNGSpringContextTests implements ITestWithWebDriver {

    @Value("${screenshots.folder:/tmp/SeleniumOutput}")
    private String screenShotsFolder;

    @Autowired
    private CustomChromeDriver customChromeDriver;

    @Autowired
    private ScreenShooter screenShooter;

    @Autowired
    private SnackBar snackBar;

    @Value("${admin.user}")
    private String adminUser;
    @Value("${admin.password}")
    private String adminPassword;

    @Value("${stop.after.failure}")
    private boolean stopTestsAfterFailure = false;

    public static boolean testFailureDetected = false;

    @Override
    public CustomChromeDriver getDriver() {
        return customChromeDriver;
    }

    @Override
    public ScreenShooter getScreenShooter() {
        return screenShooter;
    }

    protected void waitUntilReady(ToolTest toolTest) {
        toolTest.login(adminUser, adminPassword);
        try {
            if (Objects.equals(SnackBar.REQUEST_FAILED, snackBar.getMessage())) {
                snackBar.closeLatest();
                LabStationLogger.info(this.getClass(), "System is not ready yet! Waiting...");
                Thread.sleep(2000);
                waitUntilReady(toolTest);
            }
        } catch (Exception e) {
            //Ignore. Has logged in.
        }
        try {
            toolTest.logout();
        } catch (Exception e) {
            //Ignore. Has logged in.
        }
    }

    protected boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    @BeforeSuite
    public void prepareFolder() {
        try {
            Files.createDirectories(Paths.get(screenShotsFolder));
        } catch (Exception e) {
            //Already created.
        }
    }


    @BeforeSuite
    public void configureAwait() {
        Awaitility.setDefaultPollInterval(250, TimeUnit.MILLISECONDS);
        Awaitility.setDefaultPollDelay(Duration.ZERO);
        Awaitility.setDefaultTimeout(Duration.ofSeconds(3));
    }

    @BeforeMethod
    public void stopTestsIfHasFailed() {
        if (stopTestsAfterFailure && testFailureDetected) {
            LabStationLogger.warning(this.getClass(), "Ignoring tests due to failure!");
            throw new SkipException("Test skipped as one has failed!");
        }
    }

    @AfterSuite
    public void removeFolder() {
        //Assert.assertTrue(deleteDirectory(new File(screenShotsFolder)));
    }
}
