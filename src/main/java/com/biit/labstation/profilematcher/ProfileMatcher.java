package com.biit.labstation.profilematcher;

import com.biit.labstation.CustomChromeDriver;
import com.biit.labstation.ToolTest;
import com.biit.labstation.components.Login;
import com.biit.labstation.components.NavBar;
import com.biit.labstation.components.Popup;
import com.biit.labstation.components.PopupId;
import com.biit.labstation.components.Tab;
import com.biit.labstation.components.TabId;
import com.biit.labstation.components.Table;
import com.biit.labstation.components.TableId;
import com.biit.labstation.components.Toggle;
import com.biit.labstation.logger.LabStationLogger;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProfileMatcher extends ToolTest {

    private static final int NAME_PROFILE_TABLE_COLUMN = 1;
    private static final int NAME_PROJECT_TABLE_COLUMN = 1;
    private static final int USERNAME_USER_TABLE_COLUMN = 1;
    private static final int USERNAME_PROFILES_TABLE_COLUMN = 4;


    private final NavBar navBar;
    private final Table table;
    private final Popup popup;
    private final Tab tab;
    private final Toggle toggle;

    @Value("${testing.server.domain}")
    private String serverDomain;

    @Value("${profilematcher.context}")
    private String context;


    public ProfileMatcher(CustomChromeDriver customChromeDriver, Login login, NavBar navBar, Table table, Popup popup, Tab tab,
                          Toggle toggle) {
        super(customChromeDriver, login, popup);
        this.navBar = navBar;
        this.table = table;
        this.popup = popup;
        this.tab = tab;
        this.toggle = toggle;
    }

    @Override
    public void access() {
        access(serverDomain, context);
    }

    @Override
    public void logout() {
        getCustomChromeDriver().findElementWaiting(By.id("profilematcher-menu")).click();
        getCustomChromeDriver().findElementWaiting(By.id("profilematcher-menu-logout")).click();
    }

    public void selectProjectsOnMenu() {
        if (navBar.goTo("nav-item-Projects")) {
            ToolTest.waitComponent();
        }
    }

    public void selectProfilesOnMenu() {
        if (navBar.goTo("nav-item-Profiles")) {
            ToolTest.waitComponent();
        }
    }

    public void selectMatchingsOnMenu() {
        if (navBar.goTo("nav-item-Matchings")) {
            ToolTest.waitComponent();
        }
    }

    public void selectTriageOnMenu() {
        if (navBar.goTo("nav-item-Triage")) {
            ToolTest.waitComponent();
        }
    }


    public void addProfile(String name, String description, String type, String code, CadtOptions... cadtOptions) {
        LabStationLogger.debug(this.getClass().getName(), "@@ Adding profile '{}'.", name);
        try {
            selectProfilesOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        table.pressButton(TableId.PROFILES_TABLE, "button-plus");
        tab.selectTab(TabId.PROFILES, "tab-General");
        if (name != null) {
            popup.findElement(PopupId.PROFILE, "profile-name").findElement(By.className("input-object")).sendKeys(name);
        }
        if (type != null) {
            popup.findElement(PopupId.PROFILE, "profile-type").findElement(By.className("input-object")).sendKeys(type);
        }
        if (code != null) {
            popup.findElement(PopupId.PROFILE, "profile-tracking-code").findElement(By.className("input-object")).sendKeys(code);
        }
        if (description != null) {
            popup.findElement(PopupId.PROFILE, "profile-description").findElement(By.className("input-object")).sendKeys(description);
        }
        if (cadtOptions.length > 0) {
            tab.selectTab(TabId.PROFILES, "tab-Profile");
            for (CadtOptions cadtOption : cadtOptions) {
                toggle.click(cadtOption.getId());
            }
            tab.selectTab(TabId.PROFILES, "tab-General");
        }
        popup.findElement(PopupId.PROFILE, "popup-profile-save-button").click();
    }


    public void deleteProfile(String name) {
        LabStationLogger.debug(this.getClass().getName(), "@@ Deleting profile '{}'.", name);
        try {
            selectProfilesOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        table.selectRow(TableId.PROFILES_TABLE, name, NAME_PROFILE_TABLE_COLUMN);
        table.pressButton(TableId.PROFILES_TABLE, "button-minus");
        popup.findElement(PopupId.CONFIRMATION_DELETE, "confirm-delete-button").click();
    }


    public void editProfile(String oldName, String newName, String description, String type, String code, CadtOptions... cadtOptions) {
        LabStationLogger.debug(this.getClass().getName(), "@@ Editing profile '{}'.", oldName);
        try {
            selectProfilesOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        table.selectRow(TableId.PROFILES_TABLE, oldName, NAME_PROFILE_TABLE_COLUMN);
        table.pressButton(TableId.PROFILES_TABLE, "button-edit");
        tab.selectTab(TabId.PROFILES, "tab-General");
        if (newName != null) {
            popup.findElement(PopupId.PROFILE, "profile-name").findElement(By.className("input-object")).clear();
            popup.findElement(PopupId.PROFILE, "profile-name").findElement(By.className("input-object")).sendKeys(newName);
        }
        if (type != null) {
            popup.findElement(PopupId.PROFILE, "profile-type").findElement(By.className("input-object")).clear();
            popup.findElement(PopupId.PROFILE, "profile-type").findElement(By.className("input-object")).sendKeys(type);
        }
        if (code != null) {
            popup.findElement(PopupId.PROFILE, "profile-tracking-code").findElement(By.className("input-object")).clear();
            popup.findElement(PopupId.PROFILE, "profile-tracking-code").findElement(By.className("input-object")).sendKeys(code);
        }
        if (description != null) {
            popup.findElement(PopupId.PROFILE, "profile-description").findElement(By.className("input-object")).clear();
            popup.findElement(PopupId.PROFILE, "profile-description").findElement(By.className("input-object")).sendKeys(description);
        }
        if (cadtOptions.length > 0) {
            tab.selectTab(TabId.PROFILES, "tab-Profile");
            for (CadtOptions cadtOption : cadtOptions) {
                toggle.click(cadtOption.getId());
            }
            tab.selectTab(TabId.PROFILES, "tab-General");
        }
        waitAndExecute(() -> popup.findElement(PopupId.PROFILE, "popup-profile-save-button").click());
    }

    public void openProfileForMatching(String profile) {
        LabStationLogger.debug(this.getClass().getName(), "@@ Selecting profile '{}' for matching.", profile);
        try {
            selectMatchingsOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        //This table has no checkbox. Show the standard
        table.selectRowWithoutCheckbox(TableId.PROFILES_TABLE, profile, 0);
        ToolTest.waitComponent();
        table.pressButton(TableId.PROFILES_TABLE, "open-profile");
        ToolTest.waitComponent();
        getCustomChromeDriver().findElement(By.id("profile-details")).findElement(By.id("compare-button")).click();
    }


    public void assignCandidate(String profile, String username) {
        LabStationLogger.debug(this.getClass().getName(), "@@ Assigning candidate '{}' to profile '{}'.", username, profile);
        try {
            selectMatchingsOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        table.selectRowWithoutCheckbox(TableId.PROFILES_TABLE, profile, 0);
        table.selectColumnOption(TableId.CANDIDATES_TABLE, "Username");
        table.selectRow(TableId.CANDIDATES_TABLE, username, USERNAME_USER_TABLE_COLUMN);
        table.pressButton(TableId.CANDIDATES_TABLE, "button-assign");
        popup.findElement(PopupId.ASSIGN_USER_POPUP, "assign-user-button").click();
        ToolTest.waitComponent();
    }


    public void addProject(String name, String description) {
        LabStationLogger.debug(this.getClass().getName(), "@@ Adding project '{}'.", name);
        try {
            selectProjectsOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        table.pressButton(TableId.PROJECTS_TABLE, "button-plus");
        if (name != null) {
            popup.findElement(PopupId.PROJECT, "project-name").findElement(By.className("input-object")).sendKeys(name);
        }
        if (description != null) {
            popup.findElement(PopupId.PROJECT, "project-description").findElement(By.className("input-object")).sendKeys(description);
        }
        popup.findElement(PopupId.PROJECT, "popup-project-save-button").click();
    }


    public void deleteProject(String name) {
        LabStationLogger.debug(this.getClass().getName(), "@@ Deleting project '{}'.", name);
        try {
            selectProjectsOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        table.selectRow(TableId.PROJECTS_TABLE, name, NAME_PROJECT_TABLE_COLUMN);
        table.pressButton(TableId.PROJECTS_TABLE, "button-minus");
        popup.findElement(PopupId.CONFIRMATION_DELETE, "confirm-delete-button").click();
    }

    public void assignProfile(String project, String profile) {
        LabStationLogger.debug(this.getClass().getName(), "@@ Assigning profile '{}' to project '{}'.", profile, project);
        try {
            selectProjectsOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        table.selectRow(TableId.PROJECTS_TABLE, project, NAME_PROJECT_TABLE_COLUMN);
        ToolTest.waitComponent();
        table.pressButton(TableId.PROJECTS_TABLE, "button-linkage");

        table.selectRow(TableId.PROFILES_TABLE, profile, NAME_PROFILE_TABLE_COLUMN);
        popup.findElement(PopupId.ASSIGN_PROFILES_POPUP, "popup-assign-button").click();
        ToolTest.waitComponent();
        popup.findElement(PopupId.CONFIRMATION, "assign-button").click();
        ToolTest.waitComponent();
        popup.close(PopupId.ASSIGN_PROFILES_POPUP);
        ToolTest.waitComponent();
    }

    public void assignUserToProfile(String project, String profile, String user) {
        LabStationLogger.debug(this.getClass().getName(), "@@ Assigning user '{}' to profile '{}' on project.", user, profile, project);
        try {
            selectProjectsOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        table.selectRow(TableId.PROJECTS_TABLE, project, NAME_PROJECT_TABLE_COLUMN);
        ToolTest.waitComponent();
        table.pressButton(TableId.PROJECTS_TABLE, "button-linkage");

        table.selectRow(TableId.PROFILES_TABLE, profile, NAME_PROFILE_TABLE_COLUMN);
        popup.findElement(PopupId.ASSIGN_PROFILES_POPUP, "popup-assign-users").click();
        ToolTest.waitComponent();
        table.selectRow(TableId.USERS_TABLE, user, USERNAME_PROFILES_TABLE_COLUMN);
        popup.findElement(PopupId.USERS_PROFILES_POPUP, "popup-assign-button").click();
        ToolTest.waitComponent();
        popup.findElement(PopupId.CONFIRMATION, "assign-button").click();
        ToolTest.waitComponent();
        popup.close(PopupId.USERS_PROFILES_POPUP);
        ToolTest.waitComponent();
        popup.close(PopupId.ASSIGN_PROFILES_POPUP);
        ToolTest.waitComponent();
    }

    public void unassignProfile(String project, String profile) {
        LabStationLogger.debug(this.getClass().getName(), "@@ Unassigning profile '{}' from project '{}'.", profile, project);
        try {
            selectProjectsOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        table.selectRow(TableId.PROJECTS_TABLE, project, NAME_PROJECT_TABLE_COLUMN);
        ToolTest.waitComponent();
        table.pressButton(TableId.PROJECTS_TABLE, "button-linkage");

        table.selectRow(TableId.PROFILES_TABLE, profile, NAME_PROFILE_TABLE_COLUMN);
        popup.findElement(PopupId.ASSIGN_PROFILES_POPUP, "popup-unassign-button").click();
        ToolTest.waitComponent();
        popup.findElement(PopupId.CONFIRMATION_DELETE, "unassign-button").click();
        ToolTest.waitComponent();
        popup.close(PopupId.ASSIGN_PROFILES_POPUP);
        ToolTest.waitComponent();
    }
}
