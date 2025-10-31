package com.biit.labstation.tests.profilematcher;

/*-
 * #%L
 * Lab Station Test Bench
 * %%
 * Copyright (C) 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */


import com.biit.labstation.ToolTest;
import com.biit.labstation.logger.ClassTestListener;
import com.biit.labstation.logger.TestListener;
import com.biit.labstation.profilematcher.Metaviewer;
import com.biit.labstation.profilematcher.ProfileMatcher;
import com.biit.labstation.tests.BaseTest;
import com.biit.labstation.tests.ITestWithWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.biit.labstation.tests.Priorities.ORG_ADMIN_PROFILES_TESTS_PRIORITY;
import static com.biit.labstation.tests.usermanager.OrganizationAdminTests.ORGANIZATION_ADMIN_PASSWORD;
import static com.biit.labstation.tests.usermanager.OrganizationAdminTests.ORGANIZATION_ADMIN_USER;

@SpringBootTest
@Test(groups = "orgAdminMetaviewer", priority = ORG_ADMIN_PROFILES_TESTS_PRIORITY)
@Listeners({TestListener.class, ClassTestListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class OrganizationAdminMetaviewerTests extends BaseTest implements ITestWithWebDriver {

    @Autowired
    private ProfileMatcher profileMatcher;

    @Autowired
    private Metaviewer metaviewer;

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
        Assert.assertEquals(metaviewer.countMetaviewerElements(), 3);

        metaviewer.openElement(0);
        Assert.assertEquals(metaviewer.getMetaviewerElementHeader(), "admin@test.com");
        //Admin is receptive
        Assert.assertEquals(metaviewer.getMetaviewerElementData(1), "true");
        metaviewer.metaviewerElementClose();

        metaviewer.openElement(1);
        Assert.assertEquals(metaviewer.getMetaviewerElementHeader(), "nonorg@test.com");
        //nonorg is not receptive
        Assert.assertEquals(metaviewer.getMetaviewerElementData(1), "true");
        metaviewer.metaviewerElementClose();

        metaviewer.openElement(2);
        Assert.assertEquals(metaviewer.getMetaviewerElementHeader(), "inorg@test.com");
        //inorg is not receptive
        Assert.assertEquals(metaviewer.getMetaviewerElementData(1), "false");
        metaviewer.metaviewerElementClose();

        profileMatcher.logout();
    }


    @Test
    public void orgAdminCanSee2ElementsOnMetaviewer() {
        profileMatcher.login(ORGANIZATION_ADMIN_USER, ORGANIZATION_ADMIN_PASSWORD);

        profileMatcher.selectTriageOnMenu();
        ToolTest.waitComponent();
        Assert.assertEquals(metaviewer.countMetaviewerElements(), 2);

        metaviewer.openElement(0);
        Assert.assertEquals(metaviewer.getMetaviewerElementHeader(), "admin@test.com");
        //Admin is receptive
        Assert.assertEquals(metaviewer.getMetaviewerElementData(1), "true");
        metaviewer.metaviewerElementClose();

        metaviewer.openElement(1);
        Assert.assertEquals(metaviewer.getMetaviewerElementHeader(), "inorg@test.com");
        //inorg is not receptive
        Assert.assertEquals(metaviewer.getMetaviewerElementData(1), "false");
        metaviewer.metaviewerElementClose();

        profileMatcher.logout();
    }


    @AfterClass(alwaysRun = true)
    public void cleanup() {
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
