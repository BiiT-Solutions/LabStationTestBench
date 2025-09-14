package com.biit.labstation.components;

import com.biit.labstation.CustomChromeDriver;
import com.biit.labstation.ToolTest;
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

    public boolean goTo(String id) {
        final String classAttribute = getMenuItem(id).getAttribute("class");
        if (classAttribute == null || !classAttribute.contains("active")) {
            ComponentLogger.debug(this.getClass().getName(), "Pressing '{}' on navigation menu.", id);
            getMenuItem(id).click();
            return true;
        } else {
            ComponentLogger.debug(this.getClass().getName(), "Menu item '{}' already selected.", id);
        }
        return false;
    }

    public void goTo(String menuId, String submenuId) {
        ComponentLogger.debug(this.getClass().getName(), "Pressing '{}' and '{}' on navigation menu.", menuId, submenuId);
        getSubmenuItem(menuId, submenuId).click();
    }

    public WebElement getMenuItem(String id) {
        return customChromeDriver.findElementWaiting(By.id("nav-menu")).findElement(By.id(id));
    }

    public WebElement getSubmenuItem(String menuId, String submenuId) {
        customChromeDriver.findElementWaiting(By.id("nav-menu")).findElement(By.id(menuId)).click();
        ToolTest.waitComponent();
        return customChromeDriver.findElementWaiting(By.id(submenuId));
    }
}
