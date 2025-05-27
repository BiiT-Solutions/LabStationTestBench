package com.biit.labstation.components;

import com.biit.labstation.CustomChromeDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

@Component
public class Popup {

    private final CustomChromeDriver customChromeDriver;

    public Popup(CustomChromeDriver customChromeDriver) {
        this.customChromeDriver = customChromeDriver;
    }

    public WebElement findElement(String id) {
        return customChromeDriver.findElementWaiting(By.id("biit-popup")).findElement(By.id("content")).findElement(By.id(id));
    }
}
