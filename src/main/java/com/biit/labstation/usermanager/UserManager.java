package com.biit.labstation.usermanager;

import com.biit.labstation.CustomChromeDriver;
import com.biit.labstation.components.Login;
import com.biit.labstation.components.NavBar;
import com.biit.labstation.components.Popup;
import com.biit.labstation.components.Table;
import com.biit.labstation.logger.LabStationLogger;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserManager {

    private final CustomChromeDriver customChromeDriver;
    private final Login login;
    private final NavBar navBar;
    private final Table table;
    private final Popup popup;

    @Value("${testing.server.domain}")
    private String serverDomain;

    @Value("${usermanager.context}")
    private String context;

    public UserManager(CustomChromeDriver customChromeDriver, Login login, NavBar navBar, Table table, Popup popup) {
        this.customChromeDriver = customChromeDriver;
        this.login = login;
        this.navBar = navBar;
        this.table = table;
        this.popup = popup;
    }

    public void access() {
        try {
            customChromeDriver.getDriver().get(serverDomain + context);
        } catch (Exception e) {
            LabStationLogger.errorMessage(this.getClass(), e);
        }
    }

    public void login(String username, String password) {
        try {
            login.acceptCookies();
        } catch (Exception e) {
            //Ignored.
        }
        login.logIn(username, password);
    }

    public void logout() {
        customChromeDriver.findElementWaiting(By.id("usermanager-menu")).click();
        customChromeDriver.findElementWaiting(By.id("usermanager-menu-logout")).click();
    }

    public void selectUserOnMenu() {
        navBar.getMenuItem("nav-item-Users").click();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            //Ignore
            Thread.currentThread().interrupt();
        }
    }

    public void selectGroupsOnMenu() {
        navBar.getMenuItem("nav-item-Groups").click();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            //Ignore
            Thread.currentThread().interrupt();
        }
    }

    public void selectRolesOnMenu() {
        navBar.getMenuItem("nav-item-Roles").click();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            //Ignore
            Thread.currentThread().interrupt();
        }
    }

    public void selectApplicationsOnMenu() {
        navBar.getMenuItem("nav-item-Applications").click();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            //Ignore
            Thread.currentThread().interrupt();
        }
    }

    public void selectServicesOnMenu() {
        navBar.getMenuItem("nav-item-Services").click();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            //Ignore
            Thread.currentThread().interrupt();
        }
    }

    public void selectOrganizationsOnMenu() {
        navBar.getMenuItem("nav-item-Organizations").click();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            //Ignore
            Thread.currentThread().interrupt();
        }
    }

    public void pressTableButton(String id) {
        table.getMenuItem(id).click();
    }

    public String getTableContent(int row, int column) {
        return table.getContent(row, column);
    }

    public void selectTableRow(int row) {
        table.selectRow(row);
    }

    public int getTableSize() {
        return table.countRows();
    }

    public void unselectTableRow(int row) {
        table.unselectRow(row);
    }

    public void addService(String name, String description) {
        selectServicesOnMenu();
        pressTableButton("button-plus");
        popup.findElement("service-name").findElement(By.id("input")).sendKeys(name);
        popup.findElement("service-description").findElement(By.id("input")).sendKeys(description);
        popup.findElement("save-button").click();
    }

    public void addServiceRole(int row, String name) {
        selectServicesOnMenu();
        selectTableRow(row);
        pressTableButton("button-linkage");
        popup.findElement("button-plus").click();
        popup.findElement("create-role").findElement(By.id("input")).sendKeys(name);
        popup.findElement("save-button").click();
    }
}
