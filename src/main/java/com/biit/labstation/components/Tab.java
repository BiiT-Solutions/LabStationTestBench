package com.biit.labstation.components;

import com.biit.labstation.CustomChromeDriver;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

@Component
public class Tab {

    private final CustomChromeDriver customChromeDriver;


    public Tab(CustomChromeDriver customChromeDriver) {
        this.customChromeDriver = customChromeDriver;
    }

    public void selectTab(String tabId, String element) {
        customChromeDriver.findElementWaiting(By.id(tabId)).findElement(By.id(element)).click();
    }
}
