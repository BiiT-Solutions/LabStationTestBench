package com.biit.labstation.tests.usermanager;

import com.biit.labstation.components.Popup;
import com.biit.labstation.components.PopupId;
import com.biit.labstation.components.SnackBar;
import com.biit.labstation.logger.TestListener;
import com.biit.labstation.tests.BaseTest;
import com.biit.labstation.tests.ITestWithWebDriver;
import com.biit.labstation.usermanager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.biit.labstation.tests.LoginIT.ADMIN_USER_NAME;
import static com.biit.labstation.tests.LoginIT.ADMIN_USER_PASSWORD;

@SpringBootTest
//@Test(groups = "userManagerDuplications", dependsOnGroups = "userManagerDefaultData")
@Test(groups = "userManagerDuplications")
@Listeners(TestListener.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserManagerDuplicationsIT extends BaseTest implements ITestWithWebDriver {

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
        try {
            userManager.selectUserOnMenu();
            userManager.addUser("Employee", "randomEmail@test.com", "Basic", "User", "asd123");
            Assert.assertEquals(snackBar.getMessageType(), "error");
            Assert.assertEquals(snackBar.getMessage(), "The user already exists.");
            snackBar.close();
            popup.close(PopupId.USER);
        } finally {
            userManager.logout();
        }
    }

    @Test()
    public void createExistingEmail() {
        userManager.login(ADMIN_USER_NAME, ADMIN_USER_PASSWORD);
        try {
            userManager.selectUserOnMenu();
            userManager.addUser("RandomUser", "employee@test.com", "Basic", "User", "asd123");
            Assert.assertEquals(snackBar.getMessageType(), "error");
            Assert.assertEquals(snackBar.getMessage(), "The email is already in use.");
            snackBar.close();
            popup.close(PopupId.USER);
        } finally {
            userManager.logout();
        }
    }
}
