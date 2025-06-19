package com.biit.labstation.tests.usermanager;

import com.biit.labstation.ToolTest;
import com.biit.labstation.components.Popup;
import com.biit.labstation.components.SnackBar;
import com.biit.labstation.components.Table;
import com.biit.labstation.exceptions.ElementNotFoundAsExpectedException;
import com.biit.labstation.logger.ClassTestListener;
import com.biit.labstation.logger.TestListener;
import com.biit.labstation.tests.BaseTest;
import com.biit.labstation.tests.ITestWithWebDriver;
import com.biit.labstation.usermanager.UserManager;
import org.openqa.selenium.ElementNotInteractableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.biit.labstation.tests.LoginIT.ADMIN_USER_NAME;
import static com.biit.labstation.tests.LoginIT.ADMIN_USER_PASSWORD;

@SpringBootTest
@Test(groups = "userManagerBasicUserPermissions", dependsOnGroups = "userManagerDefaultData")
@Listeners({TestListener.class, ClassTestListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserManagerBasicUserPermissionsIT extends BaseTest implements ITestWithWebDriver {

    private static final String BASIC_USER_NAME = "basicuser";
    private static final String USER_PASSWORD = "asd123";

    @Autowired
    private UserManager userManager;

    @Autowired
    private Popup popup;

    @Autowired
    private SnackBar snackBar;

    @Autowired
    private Table table;

    @BeforeClass
    public void setup() {
        userManager.access();
    }

    @Test()
    public void createBasicUser() {
        userManager.login(ADMIN_USER_NAME, ADMIN_USER_PASSWORD);
        try {
            userManager.selectUserOnMenu();
            userManager.addUser(BASIC_USER_NAME, BASIC_USER_NAME + "@test.com", "Basic", "User", USER_PASSWORD);
            snackBar.checkMessage("regular", SnackBar.USER_CREATED);
            userManager.addUserToGroup(BASIC_USER_NAME, "Employee");
        } finally {
            userManager.logout();
            snackBar.checkMessage("regular", SnackBar.LOGGED_OUT);
        }
    }

    @Test(dependsOnMethods = "createBasicUser", expectedExceptions = ElementNotFoundAsExpectedException.class)
    public void checkBasicNoPermissions() {
        userManager.login(BASIC_USER_NAME, USER_PASSWORD);
        snackBar.checkMessage("error", SnackBar.ACCESS_DENIED);
        try {
            userManager.selectOrganizationsOnMenu();
        } catch (ElementNotInteractableException e) {
            throw new ElementNotFoundAsExpectedException();
        } finally {
            userManager.logout();
            snackBar.checkMessage("regular", SnackBar.LOGGED_OUT);
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
            userManager.deleteUser(BASIC_USER_NAME);
        } catch (Exception e) {
            //Ignore
        }
        try {
            userManager.logout();
        } catch (Exception e) {
            //Ignore
        }
    }

    @AfterClass(dependsOnMethods = "cleanup")
    public void closeDriver() {
        userManager.getCustomChromeDriver().closeDriver();
    }
}
