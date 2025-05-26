package com.biit.labstation.components;

import com.biit.labstation.CustomChromeDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

@Component
public class LoginComponent {

    private final CustomChromeDriver customChromeDriver;

    public LoginComponent(CustomChromeDriver customChromeDriver) {
        this.customChromeDriver = customChromeDriver;
    }

    public void logIn(String username, String password) {
        getCookiesButton().click();
        try {
            getLoginTab().click();
        } catch (Exception e) {
            //No login tab
        }
        getUsernameOnLoginPage().sendKeys(username);
        getPasswordOnLoginPage().sendKeys(password);
        getLoginButton().click();
    }

    private WebElement getLoginTab() {
        return customChromeDriver.getDriver().findElement(By.id("login-column"));
    }

    private WebElement getUsernameOnLoginPage() {
        return customChromeDriver.getDriver().findElement(By.id("login-username")).findElement(By.className("input-object"));
    }

    private WebElement getPasswordOnLoginPage() {
        return customChromeDriver.getDriver().findElement(By.id("login-password")).findElement(By.className("input-object"));
    }

    private WebElement getLoginButton() {
        return customChromeDriver.getDriver().findElement(By.id("login-button"));
    }

    private WebElement getCookiesButton() {
        return customChromeDriver.getDriver().findElement(By.id("cookies-button"));
    }
}
