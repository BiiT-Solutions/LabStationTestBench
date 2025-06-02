package com.biit.labstation.components;

import com.biit.labstation.CustomChromeDriver;
import com.biit.labstation.logger.LabStationLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

@Component
public class Login {

    private final CustomChromeDriver customChromeDriver;

    public Login(CustomChromeDriver customChromeDriver) {
        this.customChromeDriver = customChromeDriver;
    }

    public void logIn(String username, String password) {
        LabStationLogger.debug(this.getClass().getName(), "Login in with user '{}' and password '{}'.", username, password);
        getUsernameOnLoginPage().sendKeys(username);
        getPasswordOnLoginPage().sendKeys(password);
        getLoginButton().click();
    }

    private WebElement getLoginTab() {
        return customChromeDriver.getDriver().findElement(By.id("login-column"));
    }

    private WebElement getUsernameOnLoginPage() {
        return customChromeDriver.findElementWaiting(By.id("login-username")).findElement(By.className("input-object"));
    }

    private WebElement getPasswordOnLoginPage() {
        return customChromeDriver.findElementWaiting(By.id("login-password")).findElement(By.className("input-object"));
    }

    private WebElement getLoginButton() {
        return customChromeDriver.findElementWaiting(By.id("login-button"));
    }

    private WebElement getCookiesButton() {
        return customChromeDriver.findElementWaiting(By.id("cookies-button"));
    }

    public void acceptCookies() {
        LabStationLogger.debug(this.getClass().getName(), "Accepting cookies...");
        getCookiesButton().click();
        try {
            getLoginTab().click();
        } catch (Exception e) {
            //No login tab
        }
    }
}
