package com.biit.labstation.profilematcher;

import com.biit.labstation.CustomChromeDriver;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

@Component
public class Metaviewer {

    private final CustomChromeDriver customChromeDriver;

    public Metaviewer(CustomChromeDriver customChromeDriver) {
        this.customChromeDriver = customChromeDriver;
    }

    public int countMetaviewerElements() {
        return customChromeDriver.findElementWaiting(By.id("metaviewer-board")).findElements(By.className("metaviewer-element"))
                .size();
    }

    public String getMetaviewerColor(int bulletIndex) {
        return customChromeDriver.findElementWaiting(By.id("metaviewer-board")).findElements(By.className("metaviewer-element"))
                .get(bulletIndex).getCssValue("background-color");
    }

    public void openElement(int bulletIndex) {
        customChromeDriver.findElementWaiting(By.id("metaviewer-board")).findElements(By.className("metaviewer-element"))
                .get(bulletIndex).click();
    }

    public String getMetaviewerElementHeader() {
        return customChromeDriver.findElementWaiting(By.id("metaviewer-data")).findElement(By.id("metaviewer-header"))
                .findElement(By.id("title")).getText();
    }

    public String getMetaviewerElementData(int index) {
        return customChromeDriver.findElementWaiting(By.id("metaviewer-data")).findElement(By.id("metaviewer-info"))
                .findElements(By.id("metadata-info-.row")).get(index).findElement(By.id("field")).findElement(By.id("input")).getText();
    }

    public void metaviewerElementClose() {
        customChromeDriver.findElementWaiting(By.id("metaviewer-data")).findElement(By.id("close-button")).click();
    }
}
