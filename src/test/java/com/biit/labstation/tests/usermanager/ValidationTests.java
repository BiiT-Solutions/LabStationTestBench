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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@SpringBootTest
@Test(groups = "validation")
@Listeners({TestListener.class, ClassTestListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ValidationTests extends BaseTest implements ITestWithWebDriver {

    private static final String USERNAME = "A";

    private static final String ORGANIZATION_NAME = "A";
    private static final String ORGANIZATION_DESCRIPTION = "A";


    @Autowired
    private UserManager userManager;

    @Autowired
    private SnackBar snackBar;

    @Autowired
    private Popup popup;

    @Value("${admin.user}")
    private String adminUser;
    @Value("${admin.password}")
    private String adminPassword;

    @BeforeClass
    public void setup() {
        userManager.access();
    }


    @Test
    public void checkUserValidation() {
        userManager.login(adminUser, adminPassword);
        try {
            userManager.selectUserOnMenu();
            userManager.addUser(USERNAME, USERNAME + "@test.com", "Basic", "User", "aaaa");
            snackBar.checkMessage(SnackBar.Type.ERROR, SnackBar.DATA_IS_INVALID);
            popup.close(PopupId.USER);
        } finally {
            userManager.logout();
            snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.LOGGED_OUT);
        }
    }


    @Test
    public void checkOrganizationValidation() {
        userManager.login(adminUser, adminPassword);
        try {
            userManager.selectOrganizationsOnMenu();
            userManager.addOrganization(ORGANIZATION_NAME, ORGANIZATION_DESCRIPTION);
            snackBar.checkMessage(SnackBar.Type.ERROR, SnackBar.DATA_IS_INVALID);
            popup.close(PopupId.ORGANIZATION);
        } finally {
            userManager.logout();
            snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.LOGGED_OUT);
        }
    }

    @AfterClass
    public void closeDriver() {
        userManager.getCustomChromeDriver().closeDriver();
    }
}
