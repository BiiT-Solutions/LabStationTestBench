package com.biit.labstation.profilematcher;

import com.biit.labstation.CustomChromeDriver;
import com.biit.labstation.ToolTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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
        ToolTest.waitComponent();
    }

    public String getMetaviewerElementHeader() {
        return customChromeDriver.findElementWaiting(By.id("metadata-viewer")).findElement(By.id("metaviewer-data"))
                .findElement(By.id("metaviewer-header")).findElement(By.id("title")).getText();
    }

    public String getMetaviewerElementData(int index) {
        return customChromeDriver.findElementWaiting(By.id("metadata-viewer")).findElement(By.id("metaviewer-data")).findElement(By.id("metaviewer-info"))
                .findElements(By.id("metaviewer-info-row")).get(index).findElement(By.id("field")).findElement(By.id("input")).getAttribute("value");
    }

    public void metaviewerElementClose() {
        customChromeDriver.findElementWaiting(By.id("metadata-viewer")).findElement(By.id("metaviewer-data")).findElement(By.id("close-button")).click();
    }

    public WebElement getMetaviewerFilter(int index) {
        return customChromeDriver.findElementWaiting(By.id("metadata-filter")).findElement(By.id("metaviewer-data")).findElement(By.id("metaviewer-info"))
                .findElements(By.id("metaviewer-info-row")).get(index).findElement(By.id("field")).findElement(By.id("input"));
    }
}
