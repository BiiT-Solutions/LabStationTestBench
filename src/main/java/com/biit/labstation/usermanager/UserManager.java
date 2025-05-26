package com.biit.labstation.usermanager;

import com.biit.labstation.CustomChromeDriver;
import com.biit.labstation.components.Login;
import com.biit.labstation.components.NavBar;
import com.biit.labstation.logger.LabStationLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserManager {

    private final CustomChromeDriver customChromeDriver;
    private final Login login;
    private final NavBar navBar;

    @Value("${testing.server.domain}")
    private String serverDomain;

    @Value("${usermanager.context}")
    private String context;

    public UserManager(CustomChromeDriver customChromeDriver, Login login, NavBar navBar) {
        this.customChromeDriver = customChromeDriver;
        this.login = login;
        this.navBar = navBar;
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
        navBar.logOut();
    }
}
