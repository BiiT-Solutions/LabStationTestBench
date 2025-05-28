package com.biit.labstation.usermanager;

import com.biit.labstation.CustomChromeDriver;
import com.biit.labstation.components.Dropdown;
import com.biit.labstation.components.Login;
import com.biit.labstation.components.NavBar;
import com.biit.labstation.components.Popup;
import com.biit.labstation.components.Table;
import com.biit.labstation.logger.LabStationLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserManager {
    private static final int WAITING_TIME = 500;
    private final CustomChromeDriver customChromeDriver;
    private final Login login;
    private final NavBar navBar;
    private final Table table;
    private final Popup popup;
    private final Dropdown dropdown;

    @Value("${testing.server.domain}")
    private String serverDomain;

    @Value("${usermanager.context}")
    private String context;

    public UserManager(CustomChromeDriver customChromeDriver, Login login, NavBar navBar, Table table, Popup popup, Dropdown dropdown) {
        this.customChromeDriver = customChromeDriver;
        this.login = login;
        this.navBar = navBar;
        this.table = table;
        this.popup = popup;
        this.dropdown = dropdown;
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
            Thread.sleep(WAITING_TIME);
        } catch (InterruptedException e) {
            //Ignore
            Thread.currentThread().interrupt();
        }
    }

    public void selectGroupsOnMenu() {
        navBar.getMenuItem("nav-item-Groups").click();
        try {
            Thread.sleep(WAITING_TIME);
        } catch (InterruptedException e) {
            //Ignore
            Thread.currentThread().interrupt();
        }
    }

    public void selectRolesOnMenu() {
        navBar.getMenuItem("nav-item-Roles").click();
        try {
            Thread.sleep(WAITING_TIME);
        } catch (InterruptedException e) {
            //Ignore
            Thread.currentThread().interrupt();
        }
    }

    public void selectApplicationsOnMenu() {
        navBar.getMenuItem("nav-item-Applications").click();
        try {
            Thread.sleep(WAITING_TIME);
        } catch (InterruptedException e) {
            //Ignore
            Thread.currentThread().interrupt();
        }
    }

    public void selectServicesOnMenu() {
        navBar.getMenuItem("nav-item-Services").click();
        try {
            Thread.sleep(WAITING_TIME);
        } catch (InterruptedException e) {
            //Ignore
            Thread.currentThread().interrupt();
        }
    }

    public void selectOrganizationsOnMenu() {
        navBar.getMenuItem("nav-item-Organizations").click();
        try {
            Thread.sleep(WAITING_TIME);
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

    public void selectTableRow(String label, int column) {
        table.selectRow(label, column);
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
        popup.findElement("popup-service-save-button").click();
    }

    public void addServiceRole(String service, String role) {
        try {
            selectServicesOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        selectTableRow(service, 1);
        pressTableButton("button-linkage");
        popup.findElement("popup-service-button-plus").click();
        popup.findElement("create-service-role").findElement(By.id("input")).sendKeys(role);
        popup.findElement("popup-save-button").click();
        popup.close();
    }

    public void addApplication(String name, String description) {
        selectApplicationsOnMenu();
        pressTableButton("button-plus");
        popup.findElement("application-name").findElement(By.id("input")).sendKeys(name);
        popup.findElement("application-description").findElement(By.id("input")).sendKeys(description);
        popup.findElement("popup-save-button").click();
    }

    public void addApplicationRole(String application, String role) {
        try {
            selectApplicationsOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        selectTableRow(application, 1);
        pressTableButton("button-linkage");
        popup.findElement("popup-application-button-plus").click();
        dropdown.selectItem("role-selector", role);
        popup.findElement("assign-button").click();
        popup.close();
    }

    public void linkApplicationRoleWithServiceRole(String backendService, String role, String backendRole) {
        try {
            selectApplicationsOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        selectTableRow(backendService, 1);
        pressTableButton("button-linkage");
        popup.selectTableRow(role, 1);
        popup.findElement("popup-button-linkage").click();
        popup.findElement("popup-button-plus").click();
        dropdown.selectItem("service-selector", backendService);
        dropdown.selectItem("role-selector", backendRole);
        popup.findElement("assign-role-button").click();
        popup.close();
        //One close for the role assigner, the second for the role list.
        popup.close();
    }

    public String getCurrentPage() {
        return table.getCurrentPage();
    }

    public String getTotalPages() {
        return table.getTotalPages();
    }


    public void goFirstPage() {
        table.goFirstPage();
    }

    public void goPreviousPage() {
        table.goPreviousPage();
    }

    public void goNextPage() {
        table.goNextPage();
    }

    public void goLastPage() {
        table.goLastPage();
    }

    public void addRole(String name, String description) {
        selectRolesOnMenu();
        pressTableButton("button-plus");
        popup.findElement("create-role").findElement(By.id("role-name")).findElement(By.id("input")).sendKeys(name);
        if (description != null) {
            popup.findElement("create-role").findElement(By.id("role-description")).findElement(By.id("input")).sendKeys(name);
        }
        popup.findElement("popup-save-button").click();
    }

    public void linkRoleWithApplication(String role, String application, String service, String backendRole) {
        try {
            selectApplicationsOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        selectTableRow(role, 1);
        pressTableButton("button-linkage");
        popup.findElement("application-button-plus").click();
        final Select applicationSelector = new Select(popup.findElement("application-selector").findElement(By.id("input")));
        applicationSelector.selectByVisibleText(application);
        popup.findElement("assign-application-button").click();

        popup.selectTableRow(application, 1);
        popup.findElement("application-button-linkage").click();
        popup.findElement("popup-button-plus").click();
        final Select serviceSelector = new Select(popup.findElement("service-selector").findElement(By.id("input")));
        serviceSelector.selectByVisibleText(service);
        final Select roleSelector = new Select(popup.findElement("role-selector").findElement(By.id("input")));
        roleSelector.selectByVisibleText(backendRole);
        popup.findElement("assign-role-button").click();
        popup.selectTableRow(role, 1);
    }

}
