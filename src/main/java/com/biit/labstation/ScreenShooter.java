package com.biit.labstation;

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

import com.biit.labstation.logger.LabStationLogger;
import jakarta.annotation.PostConstruct;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class ScreenShooter {

    private static boolean folderCleaned = false;

    @Value("${screenshots.folder:/tmp/SeleniumOutput}")
    private String screenShotsFolder;

    private final CustomChromeDriver customChromeDriver;

    public ScreenShooter(CustomChromeDriver customChromeDriver) {
        this.customChromeDriver = customChromeDriver;
    }

    @PostConstruct
    public void cleanFolder() {
        if (!folderCleaned) {
            LabStationLogger.debug(this.getClass(), "@@ Cleaning folder '" + screenShotsFolder + "'.");
            final File folder = new File(screenShotsFolder);
            folder.mkdirs();
            for (File file : folder.listFiles()) {
                file.delete();
            }
            folderCleaned = true;
        }
    }

    public void takeScreenshot(String screenshotName) {
        final File sourceFile = ((TakesScreenshot) customChromeDriver.getDriver()).getScreenshotAs(OutputType.FILE);
        try {
            final File output = new File(screenShotsFolder + File.separator + screenshotName + ".png");
            LabStationLogger.info(this.getClass(), "Screenshot generated on '" + output.getAbsolutePath() + "'.");
            FileUtils.copyFile(sourceFile, output, true);
        } catch (IOException e) {
            LabStationLogger.errorMessage(this.getClass(), e);
        }
    }
}
