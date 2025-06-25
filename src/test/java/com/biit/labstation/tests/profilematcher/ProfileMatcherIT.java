package com.biit.labstation.tests.profilematcher;

import com.biit.labstation.ToolTest;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.biit.labstation.tests.LoginIT.ADMIN_USER_NAME;
import static com.biit.labstation.tests.LoginIT.ADMIN_USER_PASSWORD;

//@SpringBootTest
@Test(groups = "profiles")
@Listeners({TestListener.class, ClassTestListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ProfileMatcherIT extends BaseTest implements ITestWithWebDriver {

    private static final String OLD_PROFILE_NAME = "OldProfile";
    private static final String NEW_PROFILE_NAME = "NewProfile";

    //@Autowired
    private ProfileMatcher profileMatcher;

    //@Autowired
    private Table table;

    //@Autowired
    private SnackBar snackBar;

    @BeforeClass
    public void setup() {
        profileMatcher.access();
    }

    @Test
    public void createProfile() {
        profileMatcher.login(ADMIN_USER_NAME, ADMIN_USER_PASSWORD);
        profileMatcher.addProfile(OLD_PROFILE_NAME, null, null, null, CadtOptions.RECEPTIVE);
        ToolTest.waitComponent(1000);
        table.search(TableId.PROFILES_TABLE, OLD_PROFILE_NAME);
        Assert.assertEquals(table.getContent(TableId.PROFILES_TABLE, 0, 1), OLD_PROFILE_NAME);
        table.clearSearch(TableId.PROFILES_TABLE);
    }


    @Test(dependsOnMethods = "createProfile", expectedExceptions = ElementNotFoundAsExpectedException.class)
    public void editProfile() {
        profileMatcher.editProfile(OLD_PROFILE_NAME, NEW_PROFILE_NAME, null, null, null, CadtOptions.RECEPTIVE);
        ToolTest.waitComponent(1000);
        table.search(TableId.PROFILES_TABLE, NEW_PROFILE_NAME);
        Assert.assertEquals(table.getContent(TableId.PROFILES_TABLE, 0, 1), NEW_PROFILE_NAME);
        table.clearSearch(TableId.PROFILES_TABLE);

        try {
            table.selectRow(TableId.PROFILES_TABLE, OLD_PROFILE_NAME, 1);
        } catch (ConditionTimeoutException e) {
            throw new ElementNotFoundAsExpectedException();
        } finally {
            table.clearSearch(TableId.PROFILES_TABLE);
        }
    }

    @Test(dependsOnMethods = "editProfile")
    public void nothingToCompare() {
        profileMatcher.openProfileForMatching(NEW_PROFILE_NAME);
        snackBar.checkMessage("error", SnackBar.NO_ASSIGNED_PROFILES);
    }


    @Test(dependsOnMethods = "nothingToCompare")
    public void assignProfile() {
        profileMatcher.assignCandidate(NEW_PROFILE_NAME, ADMIN_USER_NAME);
    }

    @Test(dependsOnMethods = "assignProfile")
    public void canCompare() {
        profileMatcher.openProfileForMatching(NEW_PROFILE_NAME);
        snackBar.checkMessage("error", SnackBar.NO_ASSIGNED_PROFILES);
    }


    @Test(dependsOnMethods = "canCompare", alwaysRun = true, expectedExceptions = ElementNotFoundAsExpectedException.class)
    public void deleteProfile() {
        profileMatcher.deleteProfile(NEW_PROFILE_NAME);
        try {
            table.selectRow(TableId.PROFILES_TABLE, NEW_PROFILE_NAME, 1);
        } catch (ConditionTimeoutException e) {
            throw new ElementNotFoundAsExpectedException();
        } finally {
            table.clearSearch(TableId.PROFILES_TABLE);
        }
    }
}
