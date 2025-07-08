package com.biit.labstation.tests.usermanager;

import com.biit.labstation.components.Popup;
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
@Test(groups = "organization")
@Listeners({TestListener.class, ClassTestListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class OrganizationsTests extends BaseTest implements ITestWithWebDriver {

    private static final String ORGANIZATION_NAME = "Umbrella";
    private static final String ORGANIZATION_DESCRIPTION = "Developing safe medical supplies";

    private static final String TEAM_NAME = "U.S.S.";
    private static final String TEAM_DESCRIPTION = "Umbrella Security Service";


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


    @Test
    public void createOrganization() {
        userManager.login(ADMIN_USER_NAME, ADMIN_USER_PASSWORD);
        try {
            userManager.selectOrganizationsOnMenu();
            userManager.addOrganization(ORGANIZATION_NAME, ORGANIZATION_DESCRIPTION);
            snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        } finally {
            userManager.logout();
            snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.LOGGED_OUT);
        }
    }


    @Test(dependsOnMethods = "createOrganization")
    public void addTeamToOrganization() {
        userManager.login(ADMIN_USER_NAME, ADMIN_USER_PASSWORD);
        userManager.addTeamToOrganization(ORGANIZATION_NAME, TEAM_NAME, TEAM_DESCRIPTION);
        userManager.logout();
    }


    @Test(dependsOnMethods = "addTeamToOrganization")
    public void addUserToTeam() {
        userManager.login(ADMIN_USER_NAME, ADMIN_USER_PASSWORD);
        userManager.addUserToTeam(ADMIN_USER_NAME, TEAM_NAME, ORGANIZATION_NAME);
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
            userManager.deleteOrganization(ORGANIZATION_NAME);
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
