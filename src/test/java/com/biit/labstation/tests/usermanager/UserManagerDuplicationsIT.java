package com.biit.labstation.tests.usermanager;

import com.biit.labstation.components.Popup;
import com.biit.labstation.components.PopupId;
import com.biit.labstation.components.SnackBar;
import com.biit.labstation.logger.ClassTestListener;
import com.biit.labstation.logger.TestListener;
import com.biit.labstation.tests.BaseTest;
import com.biit.labstation.tests.ITestWithWebDriver;
import com.biit.labstation.usermanager.UserManager;
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
@Test(groups = "userManagerDuplications")
@Listeners({TestListener.class, ClassTestListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserManagerDuplicationsIT extends BaseTest implements ITestWithWebDriver {

    private static final String BASIC_USER_NAME = "newuser";
    private static final String USER_PASSWORD = "asd123";

    @Autowired
    private UserManager userManager;

    @Autowired
    private SnackBar snackBar;
    @Autowired
    private Popup popup;

    @BeforeClass
    public void setup() {
        userManager.access();
    }

    @Test()
    public void createExistingUser() {
        userManager.login(ADMIN_USER_NAME, ADMIN_USER_PASSWORD);
        userManager.selectUserOnMenu();
        userManager.addUser(BASIC_USER_NAME, BASIC_USER_NAME + "@test.com", "New", "User", USER_PASSWORD);
        userManager.addUser(BASIC_USER_NAME, BASIC_USER_NAME + "@test.com", "New", "User", USER_PASSWORD);
        snackBar.checkMessage("error", SnackBar.USER_ALREADY_EXISTS);
        popup.close(PopupId.USER);
        userManager.logout();
    }

    @Test(dependsOnMethods = "createExistingUser")
    public void createExistingEmail() {
        userManager.login(ADMIN_USER_NAME, ADMIN_USER_PASSWORD);
        userManager.selectUserOnMenu();
        userManager.addUser(BASIC_USER_NAME + "_2", BASIC_USER_NAME + "@test.com", "New", "User", USER_PASSWORD);
        snackBar.checkMessage("error", SnackBar.EMAIL_IN_USE);
        popup.close(PopupId.USER);
        userManager.logout();
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
