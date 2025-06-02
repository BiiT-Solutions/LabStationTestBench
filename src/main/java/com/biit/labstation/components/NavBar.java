package com.biit.labstation.components;

import com.biit.labstation.CustomChromeDriver;
import com.biit.labstation.logger.ComponentLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

@Component
public class NavBar {

    private final CustomChromeDriver customChromeDriver;


    public NavBar(CustomChromeDriver customChromeDriver) {
        this.customChromeDriver = customChromeDriver;
    }

    public void goTo(String id) {
        ComponentLogger.debug(this.getClass().getName(), "Pressing '{}' on navigation menu.", id);
        customChromeDriver.findElementWaiting(By.id("nav-menu")).findElement(By.id(id)).click();
    }

    public WebElement getMenuItem(String id) {
        return customChromeDriver.findElementWaiting(By.id("nav-menu")).findElement(By.id(id));
    }

    public WebElement getSubmenuItem(String menuId, String submenuId) {
        customChromeDriver.findElementWaiting(By.id("nav-menu")).findElement(By.id(menuId)).click();
        return customChromeDriver.findElementWaiting(By.id(submenuId));
    }

    public WebElement getUserSubmenuItem(String menuId, String submenuId) {
        customChromeDriver.findElementWaiting(By.id("nav-menu")).findElement(By.id(menuId)).click();
        return customChromeDriver.findElementWaiting(By.id(submenuId));
    }
}
