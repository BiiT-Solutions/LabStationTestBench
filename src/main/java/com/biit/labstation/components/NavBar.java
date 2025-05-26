package com.biit.labstation.components;

import com.biit.labstation.CustomChromeDriver;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

@Component
public class NavBar {

    private final CustomChromeDriver customChromeDriver;


    public NavBar(CustomChromeDriver customChromeDriver) {
        this.customChromeDriver = customChromeDriver;
    }

    public void logOut() {
        customChromeDriver.findElementWaiting(By.id("usermanager-menu")).click();
        customChromeDriver.findElementWaiting(By.id("usermanager-menu-logout")).click();
    }
}
