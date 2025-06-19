package com.biit.labstation.usermanager;

import com.biit.labstation.CustomChromeDriver;
import com.biit.labstation.ToolTest;
import com.biit.labstation.components.Dropdown;
import com.biit.labstation.components.Login;
import com.biit.labstation.components.NavBar;
import com.biit.labstation.components.Popup;
import com.biit.labstation.components.PopupId;
import com.biit.labstation.components.Tab;
import com.biit.labstation.components.Table;
import com.biit.labstation.components.TableId;
import com.biit.labstation.logger.LabStationLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserManager extends ToolTest {
    private static final int USERNAME_COLUMN = 3;
    private static final int USERNAME_GROUP_TABLE_COLUMN = 4;
    private static final int USERNAME_USER_TABLE_COLUMN = 3;

    private final CustomChromeDriver customChromeDriver;
    private final Login login;
    private final NavBar navBar;
    private final Table table;
    private final Popup popup;
    private final Dropdown dropdown;
    private final Tab tab;

    @Value("${testing.server.domain}")
    private String serverDomain;

    @Value("${usermanager.context}")
    private String context;

    public UserManager(CustomChromeDriver customChromeDriver, Login login, NavBar navBar, Table table, Popup popup, Dropdown dropdown, Tab tab) {
        this.customChromeDriver = customChromeDriver;
        this.login = login;
        this.navBar = navBar;
        this.table = table;
        this.popup = popup;
        this.dropdown = dropdown;
        this.tab = tab;
    }

    public void access() {
        try {
            LabStationLogger.debug(this.getClass().getName(), "Accessing to URL '{}{}'.", serverDomain, context);
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
        navBar.goTo("nav-item-Users");
        ToolTest.waitComponent();
    }

    public void selectGroupsOnMenu() {
        navBar.goTo("nav-item-Groups");
        ToolTest.waitComponent();
    }

    public void selectRolesOnMenu() {
        navBar.goTo("nav-item-Roles");
        ToolTest.waitComponent();
    }

    public void selectApplicationsOnMenu() {
        navBar.goTo("nav-item-Applications");
        ToolTest.waitComponent();
    }

    public void selectServicesOnMenu() {
        navBar.goTo("nav-item-Services");
        ToolTest.waitComponent();
    }

    public void selectOrganizationsOnMenu() {
        navBar.goTo("nav-item-Organizations");
        ToolTest.waitComponent();
    }

    public void pressTableButton(TableId tableId, String id) {
        LabStationLogger.debug(this.getClass().getName(), "Pressing '{}' button on table '{}'.", id, tableId);
        table.getMenuItem(tableId, id).click();
        ToolTest.waitComponent();
    }

    public String getTableContent(TableId tableId, int row, int column) {
        return table.getContent(tableId, row, column);
    }

    public void selectTableRow(TableId tableId, int row) {
        table.selectRow(tableId, row);
    }

    public void selectTableRow(TableId tableId, String label, int column) {
        try {
            table.selectRow(tableId, label, column);
        } catch (Exception e) {
            //Already selected.
        }
    }

    public int getTableSize(TableId tableId) {
        return table.countRows(tableId);
    }

    public void unselectTableRow(TableId tableId, int row) {
        table.unselectRow(tableId, row);
    }

    public void unselectTableRow(TableId tableId, String label, int column) {
        try {
            table.unselectRow(tableId, label, column);
        } catch (Exception e) {
            //Already unselected.
        }
    }

    public void addService(String name, String description) {
        LabStationLogger.debug(this.getClass().getName(), "@@ Adding service '{}'.", name);
        selectServicesOnMenu();
        pressTableButton(TableId.SERVICE_TABLE, "button-plus");
        popup.findElement(PopupId.SERVICE, "service-name").findElement(By.id("input")).sendKeys(name);
        popup.findElement(PopupId.SERVICE, "service-description").findElement(By.id("input")).sendKeys(description);
        popup.findElement(PopupId.SERVICE, "popup-service-save-button").click();
    }

    public void addServiceRole(String service, String role) {
        LabStationLogger.debug(this.getClass().getName(), "@@ Adding service '{}' role '{}'.", service, role);
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
        unselectTableRow(TableId.SERVICE_TABLE, service, 1);
    }

    public void addApplication(String name, String description) {
        LabStationLogger.debug(this.getClass().getName(), "@@ Adding application '{}'.", name);
        selectApplicationsOnMenu();
        pressTableButton(TableId.APPLICATION_TABLE, "button-plus");
        popup.findElement(PopupId.APPLICATION, "application-name").findElement(By.id("input")).sendKeys(name);
        popup.findElement(PopupId.APPLICATION, "application-description").findElement(By.id("input")).sendKeys(description);
        popup.findElement(PopupId.APPLICATION, "popup-save-button").click();
    }

    public void addApplicationRole(String application, String role) {
        LabStationLogger.debug(this.getClass().getName(), "@@ Adding role '{}' to application '{}'.", role, application);
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
        unselectTableRow(TableId.APPLICATION_TABLE, application, 1);
    }

    public void linkApplicationRoleWithServiceRole(String application, String role, String backendService, String backendRole) {
        try {
            selectApplicationsOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        LabStationLogger.debug(this.getClass().getName(), "@@ Linking role '{}' from application '{}' to service '{}' role '{}'.",
                role, application, backendService, backendRole);
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
        unselectTableRow(TableId.APPLICATION_TABLE, application, 1);
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
        LabStationLogger.debug(this.getClass().getName(), "@@ Adding role '{}'.", name);
        selectRolesOnMenu();
        pressTableButton(TableId.ROLE_TABLE, "button-plus");
        popup.findElement(PopupId.ROLE, "role-name").findElement(By.id("input")).sendKeys(name);
        if (description != null) {
            popup.findElement(PopupId.ROLE, "role-description").findElement(By.id("input")).sendKeys(description);
        }
        popup.findElement(PopupId.ROLE, "save-role-button").click();
    }

    public void linkRoleWithApplication(String role, String application) {
        LabStationLogger.debug(this.getClass().getName(), "@@ Linking role '{}' to application '{}'.", role, application);
        try {
            selectRolesOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        selectTableRow(TableId.ROLE_TABLE, role, 1);
        pressTableButton(TableId.ROLE_TABLE, "button-linkage");
        popup.findElement(PopupId.ROLE, "application-button-plus").click();
        dropdown.selectItem(PopupId.ASSIGN_APPLICATION_SELECTOR.getId(), application);

        popup.findElement(PopupId.ASSIGN_APPLICATION_SELECTOR, "assign-application-button").click();
        popup.close(PopupId.ROLE);
    }

    public void linkRoleWithApplicationService(String role, String application, String service, String backendRole) {
        LabStationLogger.debug(this.getClass().getName(), "@@ Linking application '{}' for role '{}' to service '{}' role '{}'.",
                application, role, service, backendRole);
        try {
            selectRolesOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        selectTableRow(TableId.ROLE_TABLE, role, 1);
        pressTableButton(TableId.ROLE_TABLE, "button-linkage");
        popup.selectTableRow(TableId.APPLICATION_ROLE_TABLE, application, 1);
        popup.findElement(PopupId.ROLE, "application-button-linkage").click();
        popup.findElement(PopupId.SERVICE_ROLE, "popup-button-plus").click();
        dropdown.selectItem(PopupId.APPLICATION_ROLE_SERVICES.getId(), "service-selector", service);
        dropdown.selectItem(PopupId.APPLICATION_ROLE_SERVICES.getId(), "role-selector", backendRole);
        popup.findElement(PopupId.APPLICATION_ROLE_SERVICES, "assign-role-button").click();
        popup.close(PopupId.SERVICE_ROLE);
        popup.close(PopupId.ROLE);
    }

    public void addGroup(String name, String description) {
        LabStationLogger.debug(this.getClass().getName(), "@@ Adding group '{}'.", name);
        selectGroupsOnMenu();
        pressTableButton(TableId.USERS_GROUP_TABLE, "button-plus");
        popup.findElement(PopupId.USER_GROUP, "group-name").findElement(By.id("input")).sendKeys(name);
        popup.findElement(PopupId.USER_GROUP, "group-description").findElement(By.id("input")).sendKeys(description);
        popup.findElement(PopupId.USER_GROUP, "popup-group-save-button").click();
    }

    public void addGroupRole(String group, String application, String role) {
        LabStationLogger.debug(this.getClass().getName(), "@@ Adding group '{}' role '{}'.", group, role);
        try {
            selectGroupsOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        selectTableRow(TableId.USERS_GROUP_TABLE, group, 1);
        pressTableButton(TableId.USERS_GROUP_TABLE, "button-linkage");
        popup.findElement(PopupId.ROLE, "user-group-role-button-plus").click();
        dropdown.selectItem(PopupId.APPLICATION_ROLE.getId(), "application-selector", application);
        dropdown.selectItem(PopupId.APPLICATION_ROLE.getId(), "role-selector", role);
        popup.findElement(PopupId.APPLICATION_ROLE, "popup-assign-button").click();
        popup.close(PopupId.ROLE);
        unselectTableRow(TableId.USERS_GROUP_TABLE, group, 1);
    }

    public void addUserToGroup(String username, String group) {
        LabStationLogger.debug(this.getClass().getName(), "@@ Adding username '{}' to group '{}'.", username, group);
        try {
            selectGroupsOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        selectTableRow(TableId.USERS_GROUP_TABLE, group, 1);
        pressTableButton(TableId.USERS_GROUP_TABLE, "button-group");
        table.selectRow(TableId.USERS_TABLE, username, USERNAME_GROUP_TABLE_COLUMN);
        popup.findElement(PopupId.ASSIGN_USERS_TO_GROUP, "popup-assign-button").click();
        popup.findElement(PopupId.CONFIRMATION, "assign-button").click();
        popup.close(PopupId.ASSIGN_USERS_TO_GROUP);
        unselectTableRow(TableId.USERS_GROUP_TABLE, group, 1);
    }

    public int getTotalRolesByGroup(String group) {
        try {
            selectGroupsOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        selectTableRow(TableId.USERS_GROUP_TABLE, group, 1);
        pressTableButton(TableId.USERS_GROUP_TABLE, "button-linkage");

        final int elements = getTotalNumberOfItems(TableId.USERS_GROUP_ROLE_TABLE);
        popup.close(PopupId.ROLE);
        unselectTableRow(TableId.USERS_GROUP_TABLE, group, 1);
        LabStationLogger.debug(this.getClass().getName(), "Total roles by group '{}' are '{}'.", group, elements);
        return elements;
    }

    public int getTotalNumberOfItems(TableId tableId) {
        ToolTest.waitComponent();
        final int items = Integer.parseInt(table.getTotalNumberOfItems(tableId));
        LabStationLogger.debug(this.getClass().getName(), "Total items in table '{}' are '{}'.", tableId, items);
        return items;
    }

    public int getNumberOfItemsSelected(TableId tableId) {
        ToolTest.waitComponent();
        final int selectedITems = Integer.parseInt(table.getTotalNumberOfItems(tableId));
        LabStationLogger.debug(this.getClass().getName(), "Total items selected in table '{}' are '{}'.", tableId, selectedITems);
        return selectedITems;
    }

    public void addUser(String user, String email, String name, String lastName, String password) {
        try {
            selectUserOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        LabStationLogger.debug(this.getClass().getName(), "@@ Creating user '{}'.", user);
        pressTableButton(TableId.USERS_TABLE, "button-plus");
        tab.selectTab("user-tabs", "account-tab");
        popup.findElement(PopupId.USER, "username").findElement(By.id("input")).sendKeys(user);
        popup.findElement(PopupId.USER, "email").findElement(By.id("input")).sendKeys(email);
        popup.findElement(PopupId.USER, "name").findElement(By.id("input")).sendKeys(name);
        popup.findElement(PopupId.USER, "lastname").findElement(By.id("input")).sendKeys(lastName);
        popup.findElement(PopupId.USER, "password").findElement(By.id("input")).clear();
        popup.findElement(PopupId.USER, "password").findElement(By.id("input")).sendKeys(password);
        popup.findElement(PopupId.USER, "repeat-password").findElement(By.id("input")).clear();
        popup.findElement(PopupId.USER, "repeat-password").findElement(By.id("input")).sendKeys(password);
        waitAndExecute(() -> popup.findElement(PopupId.USER, "popup-user-save-button").click());
    }


    public void deleteUser(String username) {
        try {
            selectUserOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        selectTableRow(TableId.USERS_TABLE, username, USERNAME_USER_TABLE_COLUMN);
        pressTableButton(TableId.USERS_TABLE, "button-minus");
        popup.findElement(PopupId.CONFIRMATION_DELETE, "confirm-delete-button").click();
    }

    public void addUserRoles(String user, String application, String role) {
        try {
            selectUserOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        LabStationLogger.debug(this.getClass().getName(), "@@ Adding role '{}' to user '{}' on application.", role, user, application);
        selectTableRow(TableId.USERS_TABLE, user, USERNAME_COLUMN);
        pressTableButton(TableId.USERS_TABLE, "button-linkage");
        //Wait until a confirmation message is closed.
        waitAndExecute(() -> popup.findElement(PopupId.ROLE, "user-role-button-plus").click());
        dropdown.selectItem(PopupId.USER_ROLE.getId(), "application-selector", application);
        dropdown.selectItem(PopupId.USER_ROLE.getId(), "role-selector", role);
        popup.findElement(PopupId.USER_ROLE, "role-assign-button").click();
        popup.close(PopupId.ROLE);
        unselectTableRow(TableId.USERS_TABLE, user, 1);
    }

    public WebElement getSearchField(TableId tableId) {
        return table.getSearchField(tableId);
    }

    public void clearSearchField(TableId tableId) {
        getSearchField(tableId).clear();
        getSearchField(tableId).sendKeys(Keys.ENTER);
    }

}
