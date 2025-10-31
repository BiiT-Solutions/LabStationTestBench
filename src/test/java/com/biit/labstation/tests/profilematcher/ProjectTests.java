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
import com.biit.labstation.components.Popup;
import com.biit.labstation.components.SnackBar;
import com.biit.labstation.components.Table;
import com.biit.labstation.components.TableId;
import com.biit.labstation.logger.ClassTestListener;
import com.biit.labstation.logger.TestListener;
import com.biit.labstation.profilematcher.CadtOptions;
import com.biit.labstation.profilematcher.ProfileMatcher;
import com.biit.labstation.tests.BaseTest;
import com.biit.labstation.tests.ITestWithWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.biit.labstation.tests.Priorities.PROJECTS_TESTS_PRIORITY;

@SpringBootTest
@Test(groups = "projects", priority = PROJECTS_TESTS_PRIORITY)
@Listeners({TestListener.class, ClassTestListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ProjectTests extends BaseTest implements ITestWithWebDriver {

    private static final String PROFILE_NAME_1 = "Profile1";
    private static final String PROFILE_NAME_2 = "Profile2";
    private static final String PROJECT_NAME_1 = "Project1";
    private static final String PROJECT_NAME_2 = "Project2";

    @Autowired
    private ProfileMatcher profileMatcher;

    @Autowired
    private Table table;

    @Autowired
    private Popup popup;

    @Value("${admin.user}")
    private String adminUser;
    @Value("${admin.password}")
    private String adminPassword;

    @BeforeClass
    public void setup() {
        profileMatcher.access();
    }

    @Test
    public void createProfiles() {
        profileMatcher.login(adminUser, adminPassword);
        profileMatcher.addProfile(PROFILE_NAME_1, null, null, null, CadtOptions.RECEPTIVE);
        ToolTest.waitComponent(1000);
        profileMatcher.addProfile(PROFILE_NAME_2, null, null, null, CadtOptions.INNOVATION);
        ToolTest.waitComponent(1000);
        profileMatcher.logout();
    }

    @Test
    public void createProjects() {
        profileMatcher.login(adminUser, adminPassword);

        profileMatcher.addProject(PROJECT_NAME_1, null);
        ToolTest.waitComponent(1000);
        profileMatcher.addProject(PROJECT_NAME_2, null);
        ToolTest.waitComponent(1000);

        profileMatcher.logout();
    }

    @Test(dependsOnMethods = {"createProfiles", "createProjects"})
    public void assignProfiles() {
        profileMatcher.login(adminUser, adminPassword);

        profileMatcher.assignProfile(PROJECT_NAME_1, PROFILE_NAME_1);
        profileMatcher.assignProfile(PROJECT_NAME_1, PROFILE_NAME_2);
        table.unselectRow(TableId.PROJECTS_TABLE, PROJECT_NAME_1, 1);
        profileMatcher.assignProfile(PROJECT_NAME_2, PROFILE_NAME_2);
        table.unselectRow(TableId.PROJECTS_TABLE, PROJECT_NAME_2, 1);

        profileMatcher.logout();
    }


    @Test(dependsOnMethods = "assignProfiles")
    public void assignUsersToProfile() {
        profileMatcher.login(adminUser, adminPassword);

        profileMatcher.assignUserToProfile(PROJECT_NAME_1, PROFILE_NAME_1, adminUser);
        profileMatcher.assignUserToProfile(PROJECT_NAME_1, PROFILE_NAME_2, adminUser);
        table.unselectRow(TableId.PROJECTS_TABLE, PROJECT_NAME_1, 1);
        profileMatcher.assignUserToProfile(PROJECT_NAME_2, PROFILE_NAME_2, adminUser);
        table.unselectRow(TableId.PROJECTS_TABLE, PROJECT_NAME_2, 1);

        profileMatcher.logout();
    }

    @Test(dependsOnMethods = "assignUsersToProfile")
    public void canUnassignProfileWithUser() {
        profileMatcher.login(adminUser, adminPassword);
        profileMatcher.unassignProfile(PROJECT_NAME_1, PROFILE_NAME_2);
        profileMatcher.logout();
    }


    @Test(dependsOnMethods = "canUnassignProfileWithUser")
    public void canRemoveProfileWithUser() {
        profileMatcher.login(adminUser, adminPassword);
        profileMatcher.deleteProfile(PROFILE_NAME_1);
        profileMatcher.logout();
    }


   @Test(dependsOnMethods = "canUnassignProfileWithUser")
    public void canRemoveProjectWithUser(){
        profileMatcher.login(adminUser, adminPassword);
        profileMatcher.deleteProject(PROJECT_NAME_2);
        profileMatcher.deleteProject(PROJECT_NAME_1);
        profileMatcher.logout();
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
