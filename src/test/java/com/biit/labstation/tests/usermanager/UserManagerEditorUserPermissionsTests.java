package com.biit.labstation.tests.usermanager;

import com.biit.labstation.components.Popup;
import com.biit.labstation.components.SnackBar;
import com.biit.labstation.components.Table;
import com.biit.labstation.components.TableId;
import com.biit.labstation.exceptions.ElementNotFoundAsExpectedException;
import com.biit.labstation.logger.ClassTestListener;
import com.biit.labstation.logger.TestListener;
import com.biit.labstation.tests.BaseTest;
import com.biit.labstation.tests.ITestWithWebDriver;
import com.biit.labstation.usermanager.UserManager;
import org.openqa.selenium.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.biit.labstation.tests.Priorities.USER_MANAGER_EDITOR_PRIORITY;

@SpringBootTest
@Test(groups = "userManagerEditorUserPermissions", priority = USER_MANAGER_EDITOR_PRIORITY)
@Listeners({TestListener.class, ClassTestListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserManagerEditorUserPermissionsTests extends BaseTest implements ITestWithWebDriver {

    private static final String EDITOR_USER_NAME = "editoruser";
    private static final String DUMMYUSER_NAME = "dummyuser";
    private static final String USER_PASSWORD = "my-password";

    @Autowired
    private UserManager userManager;

    @Autowired
    private Popup popup;

    @Autowired
    private SnackBar snackBar;

    @Autowired
    private Table table;

    @Value("${admin.user}")
    private String adminUser;
    @Value("${admin.password}")
    private String adminPassword;

    @BeforeClass
    public void setup() {
        userManager.access();
    }

    @Test()
    public void createEditorUser() {
        userManager.login(adminUser, adminPassword);
        try {
            userManager.selectUserOnMenu();
            userManager.addUser(EDITOR_USER_NAME, EDITOR_USER_NAME + "@test.com", "Editor", "User", USER_PASSWORD);
            snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.USER_CREATED);
            userManager.addUserToGroup(EDITOR_USER_NAME, "Editor");
        } finally {
            userManager.logout();
            snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.LOGGED_OUT);
        }
    }

    @Test(dependsOnMethods = "createEditorUser")
    public void checkEditorCanCreateUsers() {
        userManager.login(EDITOR_USER_NAME, USER_PASSWORD);
        userManager.selectUserOnMenu();
        userManager.addUser(DUMMYUSER_NAME, DUMMYUSER_NAME + "@test.com", "Dummy", "User", USER_PASSWORD);
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.USER_CREATED);
        userManager.logout();
    }


    @Test(dependsOnMethods = "createEditorUser", expectedExceptions = ElementNotFoundAsExpectedException.class)
    public void checkEditorCannotChangeUserRoles() {
        userManager.login(EDITOR_USER_NAME, USER_PASSWORD);
        userManager.selectUserOnMenu();
        table.selectRow(TableId.USERS_TABLE, 0);
        try {
            table.pressButton(TableId.USERS_TABLE, "button-linkage");
        } catch (NoSuchElementException e) {
            throw new ElementNotFoundAsExpectedException();
        } finally {
            userManager.logout();
        }
    }

    @Test(dependsOnMethods = {"checkEditorCanCreateUsers"})
    public void checkEditorCanDeleteBasicUsers() {
        userManager.login(EDITOR_USER_NAME, USER_PASSWORD);
        userManager.selectUserOnMenu();
        userManager.deleteUser(DUMMYUSER_NAME);
        userManager.logout();
    }

    @Test(dependsOnMethods = "createEditorUser")
    public void checkEditorCannotDeleteAdminUsers() {
        userManager.login(EDITOR_USER_NAME, USER_PASSWORD);
        userManager.selectUserOnMenu();
        userManager.deleteUser(adminUser);
        snackBar.checkMessage(SnackBar.Type.ERROR, SnackBar.ACTION_NOT_ALLOWED);
        userManager.logout();
    }


    @AfterClass(alwaysRun = true)
    public void cleanup() {
        try {
            popup.close(null);
        } catch (Exception e) {
            //Ignore
        }
        try {
            userManager.access();
            userManager.logout();
        } catch (Exception e) {
            //Ignore
        }

        try {
            userManager.login(adminUser, adminPassword);
            userManager.deleteUser(EDITOR_USER_NAME);
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
