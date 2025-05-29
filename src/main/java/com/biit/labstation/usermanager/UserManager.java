package com.biit.labstation.usermanager;

import com.biit.labstation.CustomChromeDriver;
import com.biit.labstation.components.Dropdown;
import com.biit.labstation.components.Login;
import com.biit.labstation.components.NavBar;
import com.biit.labstation.components.Popup;
import com.biit.labstation.components.PopupId;
import com.biit.labstation.components.Table;
import com.biit.labstation.components.TableId;
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

    public void pressTableButton(TableId tableId, String id) {
        table.getMenuItem(tableId, id).click();
        try {
            Thread.sleep(WAITING_TIME);
        } catch (InterruptedException e) {
            //Ignore
            Thread.currentThread().interrupt();
        }
    }

    public String getTableContent(TableId tableId, int row, int column) {
        return table.getContent(tableId, row, column);
    }

    public void selectTableRow(TableId tableId, int row) {
        table.selectRow(tableId, row);
    }

    public void selectTableRow(TableId tableId, String label, int column) {
        table.selectRow(tableId, label, column);
    }

    public int getTableSize(TableId tableId) {
        return table.countRows(tableId);
    }

    public void unselectTableRow(TableId tableId, int row) {
        table.unselectRow(tableId, row);
    }

    public void addService(String name, String description) {
        selectServicesOnMenu();
        pressTableButton(TableId.SERVICE_TABLE, "button-plus");
        popup.findElement(PopupId.SERVICE, "service-name").findElement(By.id("input")).sendKeys(name);
        popup.findElement(PopupId.SERVICE, "service-description").findElement(By.id("input")).sendKeys(description);
        popup.findElement(PopupId.SERVICE, "popup-service-save-button").click();
    }

    public void addServiceRole(String service, String role) {
        try {
            selectServicesOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        selectTableRow(TableId.SERVICE_TABLE, service, 1);
        pressTableButton(TableId.SERVICE_TABLE, "button-linkage");
        popup.findElement(PopupId.ROLE, "popup-service-button-plus").click();
        popup.findElement(PopupId.ROLE, "create-service-role").findElement(By.id("input")).sendKeys(role);
        popup.findElement(PopupId.ROLE, "popup-save-button").click();
        popup.close(PopupId.ROLE);
    }

    public void addApplication(String name, String description) {
        selectApplicationsOnMenu();
        pressTableButton(TableId.APPLICATION_TABLE, "button-plus");
        popup.findElement(PopupId.APPLICATION, "application-name").findElement(By.id("input")).sendKeys(name);
        popup.findElement(PopupId.APPLICATION, "application-description").findElement(By.id("input")).sendKeys(description);
        popup.findElement(PopupId.APPLICATION, "popup-save-button").click();
    }

    public void addApplicationRole(String application, String role) {
        try {
            selectApplicationsOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        selectTableRow(TableId.APPLICATION_TABLE, application, 1);
        pressTableButton(TableId.APPLICATION_TABLE, "button-linkage");
        popup.findElement(PopupId.APPLICATION_ROLE, "popup-application-roles-button-plus").click();
        dropdown.selectItem(PopupId.APPLICATION_ROLE_SELECTOR.getId(), role);
        popup.findElement(PopupId.APPLICATION_ROLE, "assign-button").click();
        popup.close(PopupId.APPLICATION_ROLE);
    }

    public void linkApplicationRoleWithServiceRole(String application, String backendService, String role, String backendRole) {
        try {
            selectApplicationsOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        selectTableRow(TableId.APPLICATION_TABLE, application, 1);
        pressTableButton(TableId.APPLICATION_TABLE, "button-linkage");
        popup.selectTableRow(TableId.ROLE_TABLE, role, 1);
        popup.findElement(PopupId.APPLICATION_ROLE, "popup-application-roles-button-linkage").click();
        popup.findElement(PopupId.APPLICATION_ROLE_ASSIGN, "popup-button-plus").click();
        dropdown.selectItem("service-selector", backendService);
        dropdown.selectItem("role-selector", backendRole);
        popup.findElement(PopupId.APPLICATION_ROLE_ASSIGN, "assign-role-button").click();
        popup.close(PopupId.APPLICATION_ROLE_ASSIGN);
        //One close for the role assigner, the second for the role list.
        popup.close(PopupId.APPLICATION_ROLE);
    }

    public String getCurrentPage(TableId tableId) {
        return table.getCurrentPage(tableId);
    }

    public String getTotalPages(TableId tableId) {
        return table.getTotalPages(tableId);
    }


    public void goFirstPage(TableId tableId) {
        table.goFirstPage(tableId);
    }

    public void goPreviousPage(TableId tableId) {
        table.goPreviousPage(tableId);
    }

    public void goNextPage(TableId tableId) {
        table.goNextPage(tableId);
    }

    public void goLastPage(TableId tableId) {
        table.goLastPage(tableId);
    }

    public void addRole(String name, String description) {
        selectRolesOnMenu();
        pressTableButton(TableId.ROLE_TABLE, "button-plus");
        popup.findElement(PopupId.ROLE, "role-name").findElement(By.id("input")).sendKeys(name);
        if (description != null) {
            popup.findElement(PopupId.ROLE, "role-description").findElement(By.id("input")).sendKeys(name);
        }
        popup.findElement(PopupId.ROLE, "save-role-button").click();
    }

    public void linkRoleWithApplication(String role, String application, String service, String backendRole) {
        try {
            selectApplicationsOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        selectTableRow(TableId.APPLICATION_TABLE, role, 1);
        pressTableButton(TableId.APPLICATION_TABLE, "button-linkage");
        popup.findElement(null, "application-button-plus").click();
        final Select applicationSelector = new Select(popup.findElement(null, "application-selector").findElement(By.id("input")));
        applicationSelector.selectByVisibleText(application);
        popup.findElement(null, "assign-application-button").click();

        popup.selectTableRow(TableId.APPLICATION_ROLE_TABLE, application, 1);
        popup.findElement(null, "application-button-linkage").click();
        popup.findElement(null, "popup-button-plus").click();
        final Select serviceSelector = new Select(popup.findElement(null, "service-selector").findElement(By.id("input")));
        serviceSelector.selectByVisibleText(service);
        final Select roleSelector = new Select(popup.findElement(null, "role-selector").findElement(By.id("input")));
        roleSelector.selectByVisibleText(backendRole);
        popup.findElement(null, "assign-role-button").click();
        popup.selectTableRow(TableId.APPLICATION_ROLE_TABLE, role, 1);
    }

}
