package com.biit.labstation;

import com.biit.labstation.logger.LabStationLogger;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class ScreenShooter {

    @Value("${screenshots.folder:/tmp/SeleniumOutput}")
    private String screenShotsFolder;

    private final CustomChromeDriver customChromeDriver;

    public ScreenShooter(CustomChromeDriver customChromeDriver) {
        this.customChromeDriver = customChromeDriver;
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
