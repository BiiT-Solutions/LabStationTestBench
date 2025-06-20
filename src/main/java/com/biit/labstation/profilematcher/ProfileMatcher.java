package com.biit.labstation.profilematcher;

import com.biit.labstation.CustomChromeDriver;
import com.biit.labstation.ToolTest;
import com.biit.labstation.components.Login;
import com.biit.labstation.components.NavBar;
import com.biit.labstation.components.Popup;
import com.biit.labstation.components.Tab;
import com.biit.labstation.components.Table;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProfileMatcher extends ToolTest {

    private final Login login;
    private final NavBar navBar;
    private final Table table;
    private final Popup popup;
    private final Tab tab;

    @Value("${testing.server.domain}")
    private String serverDomain;

    @Value("${profilematcher.context}")
    private String context;


    public ProfileMatcher(CustomChromeDriver customChromeDriver, Login login, NavBar navBar, Table table, Popup popup, Tab tab) {
        super(customChromeDriver);
        this.login = login;
        this.navBar = navBar;
        this.table = table;
        this.popup = popup;
        this.tab = tab;
    }

    @Override
    public void access() {
        access(serverDomain, context);
    }

    @Override
    public void login(String username, String password) {
        try {
            login.acceptCookies();
        } catch (Exception e) {
            //Ignored.
        }
        login.logIn(username, password);
    }

    @Override
    public void logout() {
        getCustomChromeDriver().findElementWaiting(By.id("profilematcher-menu")).click();
        getCustomChromeDriver().findElementWaiting(By.id("profilematcher-menu-logout")).click();
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
}
