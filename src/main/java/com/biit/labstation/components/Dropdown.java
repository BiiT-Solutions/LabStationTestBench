package com.biit.labstation.components;

import com.biit.labstation.CustomChromeDriver;
import com.biit.labstation.logger.LabStationLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.stereotype.Component;

@Component
public class Dropdown {

    private final CustomChromeDriver customChromeDriver;

    public Dropdown(CustomChromeDriver customChromeDriver) {
        this.customChromeDriver = customChromeDriver;
    }

    public void selectItem(String parent, String item) {
        LabStationLogger.debug(this.getClass().getName(), "Selecting item '{}' on parent '{}'.", item, parent);
        customChromeDriver.findElementWaiting(By.id(parent)).findElement(By.id("input")).click();
        final WebElement element = customChromeDriver.findElementWaiting(By.id(parent)).findElement(By.id("dropdown"))
                .findElement(By.xpath(".//div[@id='content']/a[contains(text(), '" + item + "')]"));
        new Actions(customChromeDriver.getDriver()).scrollToElement(element).perform();
        element.click();
    }

    public void selectItem(String parent, String selector, String item) {
        LabStationLogger.debug(this.getClass().getName(), "Selecting item '{}' on dropdown '{}' in '{}'.", item, selector, parent);
        customChromeDriver.findElementWaiting(By.id(parent)).findElement(By.id(selector)).findElement(By.id("input")).click();
        final WebElement element = customChromeDriver.findElementWaiting(By.id(parent)).findElement(By.id(selector))
                .findElement(By.id("dropdown")).findElement(By.xpath(".//div[@id='content']/a[contains(text(), '" + item + "')]"));
        new Actions(customChromeDriver.getDriver()).scrollToElement(element).perform();
        element.click();
    }
}
