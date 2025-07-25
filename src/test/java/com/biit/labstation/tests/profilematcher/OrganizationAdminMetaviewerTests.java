package com.biit.labstation.tests.profilematcher;


import com.biit.labstation.ToolTest;
import com.biit.labstation.logger.ClassTestListener;
import com.biit.labstation.logger.TestListener;
import com.biit.labstation.profilematcher.ProfileMatcher;
import com.biit.labstation.tests.BaseTest;
import com.biit.labstation.tests.ITestWithWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.biit.labstation.tests.Priorities.ORG_ADMIN_PROFILES_TESTS_PRIORITY;
import static com.biit.labstation.tests.usermanager.OrganizationAdminTests.ORGANIZATION_adminPassword;
import static com.biit.labstation.tests.usermanager.OrganizationAdminTests.ORGANIZATION_adminUser;

@SpringBootTest
@Test(groups = "orgAdminMetaviewer", priority = ORG_ADMIN_PROFILES_TESTS_PRIORITY)
@Listeners({TestListener.class, ClassTestListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class OrganizationAdminMetaviewerTests extends BaseTest implements ITestWithWebDriver {

    @Autowired
    private ProfileMatcher profileMatcher;

    @Value("${admin.user}")
    private String adminUser;
    @Value("${admin.password}")
    private String adminPassword;

    @BeforeClass
    public void access() {
        profileMatcher.access();
    }


    @Test
    public void adminCanSee3ElementsOnMetaviewer() {
        profileMatcher.login(adminUser, adminPassword);

        profileMatcher.selectTriageOnMenu();
        ToolTest.waitComponent();
        Assert.assertEquals(profileMatcher.countMetaviewerElements(), 3);
        profileMatcher.logout();
    }


    @Test
    public void orgAdminCanSee2ElementsOnMetaviewer() {
        profileMatcher.login(ORGANIZATION_adminUser, ORGANIZATION_adminPassword);

        profileMatcher.selectTriageOnMenu();
        ToolTest.waitComponent();
        Assert.assertEquals(profileMatcher.countMetaviewerElements(), 2);
        profileMatcher.logout();
    }
}
