package com.biit.labstation.tests.usermanager;

import com.biit.labstation.ToolTest;
import com.biit.labstation.cardgame.CardGame;
import com.biit.labstation.components.Login;
import com.biit.labstation.components.SnackBar;
import com.biit.labstation.logger.ClassTestListener;
import com.biit.labstation.logger.TestListener;
import com.biit.labstation.tests.BaseTest;
import com.biit.labstation.tests.ITestWithWebDriver;
import com.biit.labstation.usermanager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.biit.labstation.tests.Priorities.SIGN_UP_PRIORITY;

@SpringBootTest
@Test(groups = "signUp", priority = SIGN_UP_PRIORITY)
@Listeners({TestListener.class, ClassTestListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class SignUpTests extends BaseTest implements ITestWithWebDriver {

    public static final String DEFAULT_GROUP = "Employee";
    public static final String NEW_USER_NAME = "Oma";
    public static final String NEW_USER_LASTNAME = "Desala";
    public static final String NEW_USER_PASSWORD = "my-password";
    public static final String NEW_USER_EMAIL = "signup@test.com";
    public static final String NEW_USER_USERNAME = NEW_USER_EMAIL;

    public static final String CADT_NEW_USER_NAME = "Hank";
    public static final String CADT_NEW_USER_LASTNAME = "Landry";
    public static final String CADT_NEW_USER_PASSWORD = "my-password";
    public static final String CADT_NEW_USER_EMAIL = "cadt@test.com";
    public static final String CADT_NEW_USERNAME = NEW_USER_USERNAME;

    @Autowired
    private UserManager userManager;

    @Autowired
    private CardGame cardGame;

    private String username;

    @Autowired
    private SnackBar snackBar;

    @Autowired
    private Login login;

    @Value("${admin.user}")
    private String adminUser;
    @Value("${admin.password}")
    private String adminPassword;

    @Test
    public void signUp() {
        cardGame.access();
        cardGame.signUp(NEW_USER_USERNAME, NEW_USER_PASSWORD, NEW_USER_NAME, NEW_USER_LASTNAME, NEW_USER_EMAIL);
    }

    @Test(dependsOnMethods = "signUp")
    public void checkUserIsCreated() {
        userManager.access();
        userManager.login(adminUser, adminPassword);
        username = userManager.checkUserExistsByEmail(NEW_USER_EMAIL);
        Assert.assertNotNull(username);
        userManager.logout();
    }

    @Test(dependsOnMethods = "checkUserIsCreated")
    public void checkUserIsAssignedToDefaultGroup() {
        userManager.access();
        userManager.login(adminUser, adminPassword);
        userManager.checkUserIsInGroup(username, DEFAULT_GROUP);
        userManager.logout();
    }

    @Test(dependsOnMethods = "checkUserIsCreated")
    public void signUpUsernameAlreadyExists() {
        cardGame.access();
        cardGame.signUp(CADT_NEW_USERNAME, CADT_NEW_USER_PASSWORD, CADT_NEW_USER_NAME, CADT_NEW_USER_LASTNAME, CADT_NEW_USER_EMAIL);
        ToolTest.waitComponent(3000);
        Assert.assertEquals(login.getUsernameOnSignUpPageError(), "Username already exists.");
        snackBar.checkMessage(SnackBar.Type.ERROR, SnackBar.USER_ALREADY_EXISTS);
    }

    @AfterClass(alwaysRun = true)
    public void cleanup() {
        try {
            userManager.access();
            userManager.login(adminUser, adminPassword);
            userManager.deleteUser(username);
        } catch (Exception e) {
            //Ignore
        }
        try {
            userManager.logout();
        } catch (Exception e) {
            //Ignore
        }
    }

    @AfterClass(dependsOnMethods = "cleanup", alwaysRun = true)
    public void closeDriver() {
        userManager.getCustomChromeDriver().closeDriver();
    }
}
