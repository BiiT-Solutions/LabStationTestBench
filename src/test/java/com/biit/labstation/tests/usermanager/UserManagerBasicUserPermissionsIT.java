package com.biit.labstation.tests.usermanager;

import com.biit.labstation.components.Popup;
import com.biit.labstation.components.SnackBar;
import com.biit.labstation.logger.TestListener;
import com.biit.labstation.tests.BaseTest;
import com.biit.labstation.tests.ITestWithWebDriver;
import com.biit.labstation.usermanager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.biit.labstation.tests.LoginIT.ADMIN_USER_NAME;
import static com.biit.labstation.tests.LoginIT.ADMIN_USER_PASSWORD;

@SpringBootTest
//@Test(groups = "userManagerBasicUserPermissions", dependsOnGroups = "userManagerDefaultData")
@Test(groups = "userManagerBasicUserPermissions")
@Listeners(TestListener.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserManagerBasicUserPermissionsIT extends BaseTest implements ITestWithWebDriver {

    private static final String USER_NAME = "basicuser";
    private static final String USER_PASSWORD = "asd123";

    @Autowired
    private UserManager userManager;

    @Autowired
    private Popup popup;

    @Autowired
    private SnackBar snackBar;

    @BeforeClass
    public void setup() {
        userManager.access();
    }

    @Test()
    public void createBasicUser() {
        userManager.login(ADMIN_USER_NAME, ADMIN_USER_PASSWORD);
        try {
            userManager.selectUserOnMenu();
            userManager.addUser(USER_NAME, "basicuser@test.com", "Basic", "User", USER_PASSWORD);
            snackBar.checkMessage("regular", "User has been created successfully.");
            userManager.addUserToGroup(USER_NAME, "Employee");
        } finally {
            userManager.logout();
            snackBar.checkMessage("regular", "Your account was logged out successfully.");
        }
    }

    @Test(dependsOnMethods = "createBasicUser", expectedExceptions = org.openqa.selenium.ElementNotInteractableException.class)
    public void checkNoPermissions() {
        userManager.login(USER_NAME, USER_PASSWORD);
        snackBar.checkMessage("error", "Access denied.");
        try {
            userManager.selectOrganizationsOnMenu();
        } finally {
            userManager.logout();
            snackBar.checkMessage("error", "Your account was logged out successfully.");
        }
    }


    @AfterClass
    public void cleanup() {
        try {
            popup.close(null);
        } catch (Exception e) {
            //Ignore
        }
        try {
            userManager.logout();
        } catch (Exception e) {
            //Ignore
        }
        try {
            userManager.login(ADMIN_USER_NAME, ADMIN_USER_PASSWORD);
            userManager.deleteUser(USER_NAME);
            userManager.logout();
        } catch (Exception e) {
            //Ignore
        }
    }
}
