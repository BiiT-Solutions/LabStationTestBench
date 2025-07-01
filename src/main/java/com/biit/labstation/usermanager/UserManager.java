package com.biit.labstation.usermanager;

import com.biit.labstation.CustomChromeDriver;
import com.biit.labstation.ToolTest;
import com.biit.labstation.components.Dropdown;
import com.biit.labstation.components.Login;
import com.biit.labstation.components.NavBar;
import com.biit.labstation.components.Popup;
import com.biit.labstation.components.PopupId;
import com.biit.labstation.components.Tab;
import com.biit.labstation.components.TabId;
import com.biit.labstation.components.Table;
import com.biit.labstation.components.TableId;
import com.biit.labstation.logger.LabStationLogger;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserManager extends ToolTest {
    private static final int USERNAME_COLUMN = 3;
    private static final int USERNAME_GROUP_TABLE_COLUMN = 4;
    private static final int USERNAME_USER_TABLE_COLUMN = 3;

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
        super(customChromeDriver, login);
        this.navBar = navBar;
        this.table = table;
        this.popup = popup;
        this.dropdown = dropdown;
        this.tab = tab;
    }

    @Override
    public void access() {
        access(serverDomain, context);
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


    public void addService(String name, String description) {
        LabStationLogger.debug(this.getClass().getName(), "@@ Adding service '{}'.", name);
        selectServicesOnMenu();
        table.pressButton(TableId.SERVICE_TABLE, "button-plus");
        if (name != null) {
            popup.findElement(PopupId.SERVICE, "service-name").findElement(By.id("input")).sendKeys(name);
        }
        if (description != null) {
            popup.findElement(PopupId.SERVICE, "service-description").findElement(By.id("input")).sendKeys(description);
        }
        popup.findElement(PopupId.SERVICE, "popup-service-save-button").click();
    }

    public void addServiceRole(String service, String role) {
        LabStationLogger.debug(this.getClass().getName(), "@@ Adding service '{}' role '{}'.", service, role);
        try {
            selectServicesOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        table.selectRow(TableId.SERVICE_TABLE, service, 1);
        table.pressButton(TableId.SERVICE_TABLE, "button-linkage");
        popup.findElement(PopupId.ROLE, "popup-service-button-plus").click();
        popup.findElement(PopupId.ROLE, "create-service-role").findElement(By.id("input")).sendKeys(role);
        popup.findElement(PopupId.ROLE, "popup-save-button").click();
        popup.close(PopupId.ROLE);
        table.unselectRow(TableId.SERVICE_TABLE, service, 1);
    }

    public void addApplication(String name, String description) {
        LabStationLogger.debug(this.getClass().getName(), "@@ Adding application '{}'.", name);
        selectApplicationsOnMenu();
        table.pressButton(TableId.APPLICATION_TABLE, "button-plus");
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
        table.selectRow(TableId.APPLICATION_TABLE, application, 1);
        table.pressButton(TableId.APPLICATION_TABLE, "button-linkage");
        popup.findElement(PopupId.APPLICATION_ROLE, "popup-application-roles-button-plus").click();
        dropdown.selectItem(PopupId.APPLICATION_ROLE_SELECTOR.getId(), role);
        popup.findElement(PopupId.APPLICATION_ROLE, "assign-button").click();
        popup.close(PopupId.APPLICATION_ROLE);
        table.unselectRow(TableId.APPLICATION_TABLE, application, 1);
    }

    public void linkApplicationRoleWithServiceRole(String application, String role, String backendService, String backendRole) {
        try {
            selectApplicationsOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        LabStationLogger.debug(this.getClass().getName(), "@@ Linking role '{}' from application '{}' to service '{}' role '{}'.",
                role, application, backendService, backendRole);
        table.selectRow(TableId.APPLICATION_TABLE, application, 1);
        table.pressButton(TableId.APPLICATION_TABLE, "button-linkage");
        table.selectRow(TableId.ROLE_TABLE, role, 1);
        popup.findElement(PopupId.APPLICATION_ROLE, "popup-application-roles-button-linkage").click();
        popup.findElement(PopupId.APPLICATION_ROLE_ASSIGN, "popup-button-plus").click();
        dropdown.selectItem("service-selector", backendService);
        dropdown.selectItem("role-selector", backendRole);
        popup.findElement(PopupId.APPLICATION_ROLE_ASSIGN, "assign-role-button").click();
        popup.close(PopupId.APPLICATION_ROLE_ASSIGN);
        //One close for the role assigner, the second for the role list.
        popup.close(PopupId.APPLICATION_ROLE);
        table.unselectRow(TableId.APPLICATION_TABLE, application, 1);
    }

    public void addRole(String name, String description) {
        LabStationLogger.debug(this.getClass().getName(), "@@ Adding role '{}'.", name);
        selectRolesOnMenu();
        table.pressButton(TableId.ROLE_TABLE, "button-plus");
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
        table.selectRow(TableId.ROLE_TABLE, role, 1);
        table.pressButton(TableId.ROLE_TABLE, "button-linkage");
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
        table.selectRow(TableId.ROLE_TABLE, role, 1);
        table.pressButton(TableId.ROLE_TABLE, "button-linkage");
        table.selectRow(TableId.APPLICATION_ROLE_TABLE, application, 1);
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
        table.pressButton(TableId.USERS_GROUP_TABLE, "button-plus");
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
        table.selectRow(TableId.USERS_GROUP_TABLE, group, 1);
        table.pressButton(TableId.USERS_GROUP_TABLE, "button-linkage");
        popup.findElement(PopupId.ROLE, "user-group-role-button-plus").click();
        dropdown.selectItem(PopupId.APPLICATION_ROLE.getId(), "application-selector", application);
        dropdown.selectItem(PopupId.APPLICATION_ROLE.getId(), "role-selector", role);
        popup.findElement(PopupId.APPLICATION_ROLE, "popup-assign-button").click();
        popup.close(PopupId.ROLE);
        table.unselectRow(TableId.USERS_GROUP_TABLE, group, 1);
    }

    public void addUserToGroup(String username, String group) {
        LabStationLogger.debug(this.getClass().getName(), "@@ Adding username '{}' to group '{}'.", username, group);
        try {
            selectGroupsOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        table.selectRow(TableId.USERS_GROUP_TABLE, group, 1);
        table.pressButton(TableId.USERS_GROUP_TABLE, "button-group");
        table.selectRow(TableId.USERS_TABLE, username, USERNAME_GROUP_TABLE_COLUMN);
        popup.findElement(PopupId.ASSIGN_USERS_TO_GROUP, "popup-assign-button").click();
        popup.findElement(PopupId.CONFIRMATION, "assign-button").click();
        popup.close(PopupId.ASSIGN_USERS_TO_GROUP);
        table.unselectRow(TableId.USERS_GROUP_TABLE, group, 1);
    }

    public void removeUserFromGroup(String username, String group) {
        LabStationLogger.debug(this.getClass().getName(), "@@ Removing username '{}' to group '{}'.", username, group);
        try {
            selectGroupsOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        table.selectRow(TableId.USERS_GROUP_TABLE, group, 1);
        table.pressButton(TableId.USERS_GROUP_TABLE, "button-group");
        table.selectRow(TableId.USERS_TABLE, username, USERNAME_GROUP_TABLE_COLUMN);
        popup.findElement(PopupId.ASSIGN_USERS_TO_GROUP, "popup-unassign-button").click();
        popup.findElement(PopupId.CONFIRMATION_DELETE, "unassign-button").click();
        popup.close(PopupId.ASSIGN_USERS_TO_GROUP);
        table.unselectRow(TableId.USERS_GROUP_TABLE, group, 1);
    }

    public int getTotalRolesByGroup(String group) {
        try {
            selectGroupsOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        table.selectRow(TableId.USERS_GROUP_TABLE, group, 1);
        table.pressButton(TableId.USERS_GROUP_TABLE, "button-linkage");

        final int elements = table.getTotalNumberOfItems(TableId.USERS_GROUP_ROLE_TABLE);
        popup.close(PopupId.ROLE);
        table.unselectRow(TableId.USERS_GROUP_TABLE, group, 1);
        LabStationLogger.debug(this.getClass().getName(), "Total roles by group '{}' are '{}'.", group, elements);
        return elements;
    }


    public void addUser(String user, String email, String name, String lastName, String password) {
        try {
            selectUserOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        LabStationLogger.debug(this.getClass().getName(), "@@ Creating user '{}'.", user);
        table.pressButton(TableId.USERS_TABLE, "button-plus");
        tab.selectTab(TabId.USERS, "account-tab");
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


    public void editUser(String user, String email, String name, String lastName) {
        try {
            selectUserOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        table.selectRow(TableId.USERS_TABLE, user, USERNAME_USER_TABLE_COLUMN);
        LabStationLogger.debug(this.getClass().getName(), "@@ Editing user '{}'.", user);
        table.pressButton(TableId.USERS_TABLE, "button-edit");
        tab.selectTab(TabId.USERS, "account-tab");
        popup.findElement(PopupId.USER, "email").findElement(By.id("input")).clear();
        popup.findElement(PopupId.USER, "email").findElement(By.id("input")).sendKeys(email);
        popup.findElement(PopupId.USER, "name").findElement(By.id("input")).clear();
        popup.findElement(PopupId.USER, "name").findElement(By.id("input")).sendKeys(name);
        popup.findElement(PopupId.USER, "lastname").findElement(By.id("input")).clear();
        popup.findElement(PopupId.USER, "lastname").findElement(By.id("input")).sendKeys(lastName);
        waitAndExecute(() -> popup.findElement(PopupId.USER, "popup-user-save-button").click());
    }


    public void deleteUser(String username) {
        LabStationLogger.debug(this.getClass().getName(), "@@ Delegting user '{}'.", username);
        try {
            selectUserOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        table.selectRow(TableId.USERS_TABLE, username, USERNAME_USER_TABLE_COLUMN);
        table.pressButton(TableId.USERS_TABLE, "button-minus");
        popup.findElement(PopupId.CONFIRMATION_DELETE, "confirm-delete-button").click();
    }


    public void addUserRoles(String user, String application, String role) {
        try {
            selectUserOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        LabStationLogger.debug(this.getClass().getName(), "@@ Adding role '{}' to user '{}' on application.", role, user, application);
        table.selectRow(TableId.USERS_TABLE, user, USERNAME_COLUMN);
        table.pressButton(TableId.USERS_TABLE, "button-linkage");
        //Wait until a confirmation message is closed.
        waitAndExecute(() -> popup.findElement(PopupId.ROLE, "user-role-button-plus").click());
        dropdown.selectItem(PopupId.USER_ROLE.getId(), "application-selector", application);
        dropdown.selectItem(PopupId.USER_ROLE.getId(), "role-selector", role);
        popup.findElement(PopupId.USER_ROLE, "role-assign-button").click();
        popup.close(PopupId.ROLE);
        table.unselectRow(TableId.USERS_TABLE, user, USERNAME_COLUMN);
    }


}
