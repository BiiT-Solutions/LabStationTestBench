package com.biit.labstation.tests.profilematcher;

import com.biit.labstation.ScreenShooter;
import com.biit.labstation.ToolTest;
import com.biit.labstation.components.Popup;
import com.biit.labstation.components.PopupId;
import com.biit.labstation.components.SnackBar;
import com.biit.labstation.components.Table;
import com.biit.labstation.components.TableId;
import com.biit.labstation.exceptions.ElementNotFoundAsExpectedException;
import com.biit.labstation.logger.ClassTestListener;
import com.biit.labstation.logger.TestListener;
import com.biit.labstation.profilematcher.CadtOptions;
import com.biit.labstation.profilematcher.ProfileMatcher;
import com.biit.labstation.tests.BaseTest;
import com.biit.labstation.tests.ITestWithWebDriver;
import org.awaitility.core.ConditionTimeoutException;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.biit.labstation.tests.Priorities.PROFILES_TESTS_PRIORITY;

@SpringBootTest
@Test(groups = "profiles", priority = PROFILES_TESTS_PRIORITY)
@Listeners({TestListener.class, ClassTestListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ProfilesTests extends BaseTest implements ITestWithWebDriver {

    private static final String OLD_PROFILE_NAME = "OldProfile";
    private static final String NEW_PROFILE_NAME = "NewProfile";

    @Autowired
    private ProfileMatcher profileMatcher;

    @Autowired
    private Table table;

    @Autowired
    private SnackBar snackBar;

    @Autowired
    private Popup popup;

    @Autowired
    private ScreenShooter screenShooter;

    @Value("${admin.user}")
    private String adminUser;
    @Value("${admin.password}")
    private String adminPassword;

    @BeforeClass
    public void setup() {
        profileMatcher.access();
    }

    @Test
    public void createProfile() {
        profileMatcher.login(adminUser, adminPassword);
        profileMatcher.addProfile(OLD_PROFILE_NAME, null, null, null, CadtOptions.RECEPTIVE);
        ToolTest.waitComponent(1000);
        table.search(TableId.PROFILES_TABLE, OLD_PROFILE_NAME);
        Assert.assertEquals(table.getText(TableId.PROFILES_TABLE, 0, 1), OLD_PROFILE_NAME);
        table.clearSearch(TableId.PROFILES_TABLE);
        profileMatcher.logout();
    }


    @Test(dependsOnMethods = "createProfile", expectedExceptions = ElementNotFoundAsExpectedException.class)
    public void editProfile() {
        profileMatcher.login(adminUser, adminPassword);
        profileMatcher.editProfile(OLD_PROFILE_NAME, NEW_PROFILE_NAME, null, null, null, CadtOptions.BANKER);
        ToolTest.waitComponent(1000);
        table.search(TableId.PROFILES_TABLE, NEW_PROFILE_NAME);
        Assert.assertEquals(table.getText(TableId.PROFILES_TABLE, 0, 1), NEW_PROFILE_NAME);
        table.clearSearch(TableId.PROFILES_TABLE);

        try {
            table.selectRow(TableId.PROFILES_TABLE, OLD_PROFILE_NAME, 1);
        } catch (ConditionTimeoutException e) {
            throw new ElementNotFoundAsExpectedException();
        } finally {
            table.clearSearch(TableId.PROFILES_TABLE);
            profileMatcher.logout();
        }
    }

    @Test(dependsOnMethods = "editProfile")
    public void nothingToCompare() {
        profileMatcher.login(adminUser, adminPassword);
        profileMatcher.openProfileForMatching(NEW_PROFILE_NAME);
        snackBar.checkMessage(SnackBar.Type.WARNING, SnackBar.NO_ASSIGNED_PROFILES);
        profileMatcher.getCustomChromeDriver().findElement(By.id("profile-details")).findElement(By.id("cancel-button")).click();
        profileMatcher.logout();
    }


    @Test(dependsOnMethods = "nothingToCompare")
    public void assignProfile() {
        profileMatcher.login(adminUser, adminPassword);
        profileMatcher.assignCandidate(NEW_PROFILE_NAME, adminUser);
        profileMatcher.logout();
    }

    @Test(dependsOnMethods = "assignProfile", expectedExceptions = ElementNotFoundAsExpectedException.class)
    public void canCompare() {
        profileMatcher.login(adminUser, adminPassword);
        profileMatcher.openProfileForMatching(NEW_PROFILE_NAME);
        try {
            snackBar.checkMessage(SnackBar.Type.WARNING, SnackBar.NO_ASSIGNED_PROFILES);
        } catch (ConditionTimeoutException e) {
            throw new ElementNotFoundAsExpectedException();
        } finally {
            popup.close(PopupId.CANDIDATES_POPUP);
            profileMatcher.logout();
        }
    }


    @Test(dependsOnMethods = "canCompare", alwaysRun = true, expectedExceptions = ElementNotFoundAsExpectedException.class)
    public void deleteProfile() {
        profileMatcher.login(adminUser, adminPassword);
        profileMatcher.deleteProfile(NEW_PROFILE_NAME);
        try {
            table.selectRow(TableId.PROFILES_TABLE, NEW_PROFILE_NAME, 1);
        } catch (ConditionTimeoutException e) {
            throw new ElementNotFoundAsExpectedException();
        } finally {
            table.clearSearch(TableId.PROFILES_TABLE);
            profileMatcher.logout();
        }
    }


    @AfterClass(alwaysRun = true)
    public void cleanup() {
        try {
            popup.close(null);
        } catch (Exception e) {
            //Ignore
        }
        try {
            profileMatcher.logout();
        } catch (Exception e) {
            //Ignore
        }
    }

    @AfterClass(dependsOnMethods = "cleanup", alwaysRun = true)
    public void closeDriver() {
        profileMatcher.getCustomChromeDriver().closeDriver();
    }
}
