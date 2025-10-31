package com.biit.labstation.components;

/*-
 * #%L
 * Lab Station Test Bench
 * %%
 * Copyright (C) 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.labstation.CustomChromeDriver;
import com.biit.labstation.ToolTest;
import com.biit.labstation.logger.ComponentLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

@Component
public class Login {
    private static final int LOGIN_WAITING_TIME = 500;

    private final CustomChromeDriver customChromeDriver;
    private final Tab tab;

    public Login(CustomChromeDriver customChromeDriver, Tab tab) {
        this.customChromeDriver = customChromeDriver;
        this.tab = tab;
    }

    public void logIn(String username, String password) {
        ComponentLogger.debug(this.getClass().getName(), "Login in with user '{}' and password '{}'.", username, password);
        try {
            selectLoginTab();
        } catch (Exception e) {
            //Maybe signup is not active.
        }
        ToolTest.waitComponent();
        getUsernameOnLoginPage().sendKeys(username);
        getPasswordOnLoginPage().sendKeys(password);
        getLoginButton().click();
        ComponentLogger.debug(this.getClass().getName(), "User '{}' logged in.", username);
        ToolTest.waitComponent(LOGIN_WAITING_TIME);
    }


    public void signUp(String username, String password, String name, String lastname, String email) {
        ComponentLogger.debug(this.getClass().getName(), "Signing up in with user '{}' and password '{}'.", username, password);
        selectSignUpTab();
        if (username != null) {
            getUsernameOnSignUpPage().sendKeys(username);
        }
        getPasswordOnSignUpPage().sendKeys(password);
        getNameOnSignUpPage().sendKeys(name);
        getLastnameOnSignUpPage().sendKeys(lastname);
        getEmailOnSignUpPage().sendKeys(email);
        getSignUpButton().click();
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
        ComponentLogger.debug(this.getClass().getName(), "Accepting cookies...");
        getCookiesButton().click();
        try {
            getLoginTab().click();
        } catch (Exception e) {
            //No login tab
        }
    }

    public WebElement getUsernameOnSignUpPage() {
        return customChromeDriver.findElementWaiting(By.id("signup-username")).findElement(By.className("input-object"));
    }

    public String getUsernameOnSignUpPageError() {
        return customChromeDriver.findElementWaiting(By.id("signup-username")).findElement(By.id("error-message")).findElement(By.id("message")).getText();
    }

    public WebElement getNameOnSignUpPage() {
        return customChromeDriver.findElementWaiting(By.id("signup-name")).findElement(By.className("input-object"));
    }

    public WebElement getLastnameOnSignUpPage() {
        return customChromeDriver.findElementWaiting(By.id("signup-lastname")).findElement(By.className("input-object"));
    }

    public WebElement getEmailOnSignUpPage() {
        return customChromeDriver.findElementWaiting(By.id("signup-email")).findElement(By.className("input-object"));
    }

    public WebElement getPasswordOnSignUpPage() {
        return customChromeDriver.findElementWaiting(By.id("signup-password")).findElement(By.className("input-object"));
    }

    public WebElement getSignUpButton() {
        return customChromeDriver.findElementWaiting(By.id("signup-button"));
    }

    public void selectLoginTab() {
        tab.selectTab(TabId.LOGIN, "tab-login");
    }

    public void selectSignUpTab() {
        tab.selectTab(TabId.LOGIN, "tab-signup");
    }
}
