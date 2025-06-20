package com.biit.labstation.components;

import com.biit.labstation.CustomChromeDriver;
import com.biit.labstation.logger.ComponentLogger;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

@Component
public class Tab {

    private final CustomChromeDriver customChromeDriver;


    public Tab(CustomChromeDriver customChromeDriver) {
        this.customChromeDriver = customChromeDriver;
    }

    public void selectTab(TabId tabId, String element) {
        ComponentLogger.debug(this.getClass().getName(), "Selecting tab '{}' on '{}'.", element, tabId);
        customChromeDriver.findElementWaiting(By.id(tabId.getId())).findElement(By.id(element)).click();

    }
}
