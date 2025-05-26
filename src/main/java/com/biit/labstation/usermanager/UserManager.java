package com.biit.labstation.usermanager;

import com.biit.labstation.CustomChromeDriver;
import com.biit.labstation.components.LoginComponent;
import com.biit.labstation.logger.LabStationLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserManager {

    private final CustomChromeDriver customChromeDriver;
    private final LoginComponent loginComponent;

    @Value("${testing.server.domain}")
    private String serverDomain;

    @Value("${usermanager.context}")
    private String context;

    public UserManager(CustomChromeDriver customChromeDriver, LoginComponent loginComponent) {
        this.customChromeDriver = customChromeDriver;
        this.loginComponent = loginComponent;
    }

    public void access() {
        try {
            customChromeDriver.getDriver().get(serverDomain + context);
        } catch (Exception e) {
            LabStationLogger.errorMessage(this.getClass(), e);
        }
    }

    public void login(String username, String password) {
        loginComponent.logIn(username, password);
    }
}
