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
    private static final int USERNAME_USER_TABLE_COLUMN = 4;


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
        super(customChromeDriver, login);
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

    public void selectProfilesOnMenu() {
        navBar.goTo("nav-item-Profiles");
        ToolTest.waitComponent();
    }

    public void selectMatchingsOnMenu() {
        navBar.goTo("nav-item-Matchings");
        ToolTest.waitComponent();
    }

    public void selectTriageOnMenu() {
        navBar.goTo("nav-item-Triage");
        ToolTest.waitComponent();
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
            popup.findElement(PopupId.PROFILE, "profile-name").findElement(By.id("input")).sendKeys(name);
        }
        if (type != null) {
            popup.findElement(PopupId.PROFILE, "profile-type").findElement(By.id("input")).sendKeys(type);
        }
        if (code != null) {
            popup.findElement(PopupId.PROFILE, "profile-tracking-code").findElement(By.id("input")).sendKeys(code);
        }
        if (description != null) {
            popup.findElement(PopupId.PROFILE, "profile-description").findElement(By.id("input")).sendKeys(description);
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
            popup.findElement(PopupId.PROFILE, "profile-name").findElement(By.id("input")).clear();
            popup.findElement(PopupId.PROFILE, "profile-name").findElement(By.id("input")).sendKeys(newName);
        }
        if (type != null) {
            popup.findElement(PopupId.PROFILE, "profile-type").findElement(By.id("input")).clear();
            popup.findElement(PopupId.PROFILE, "profile-type").findElement(By.id("input")).sendKeys(type);
        }
        if (code != null) {
            popup.findElement(PopupId.PROFILE, "profile-tracking-code").findElement(By.id("input")).clear();
            popup.findElement(PopupId.PROFILE, "profile-tracking-code").findElement(By.id("input")).sendKeys(code);
        }
        if (description != null) {
            popup.findElement(PopupId.PROFILE, "profile-description").findElement(By.id("input")).clear();
            popup.findElement(PopupId.PROFILE, "profile-description").findElement(By.id("input")).sendKeys(description);
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
        //This table has no checkbox. Sow the standard
        table.selectRowWithoutCheckbox(TableId.PROFILES_TABLE, profile, 0);
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
    }
}
