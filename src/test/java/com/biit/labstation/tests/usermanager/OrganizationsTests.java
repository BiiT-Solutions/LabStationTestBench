package com.biit.labstation.tests.usermanager;

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

import com.biit.labstation.components.Popup;
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

import static com.biit.labstation.tests.Priorities.ORGANIZATION_TESTS_PRIORITY;

@SpringBootTest
@Test(groups = "organization", priority = ORGANIZATION_TESTS_PRIORITY)
@Listeners({TestListener.class, ClassTestListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class OrganizationsTests extends BaseTest implements ITestWithWebDriver {

    public static final String ORGANIZATION_NAME = "Umbrella";
    private static final String ORGANIZATION_DESCRIPTION = "Developing safe medical supplies";

    public static final String TEAM_NAME = "U.S.S.";
    private static final String TEAM_DESCRIPTION = "Umbrella Security Service";


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
    public void createOrganization() {
        userManager.login(adminUser, adminPassword);
        try {
            userManager.addOrganization(ORGANIZATION_NAME, ORGANIZATION_DESCRIPTION);
            snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        } finally {
            userManager.logout();
            snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.LOGGED_OUT);
        }
    }


    @Test(dependsOnMethods = "createOrganization")
    public void addTeamToOrganization() {
        userManager.login(adminUser, adminPassword);
        userManager.addTeamToOrganization(ORGANIZATION_NAME, TEAM_NAME, TEAM_DESCRIPTION);
        userManager.logout();
    }


    @Test(dependsOnMethods = "addTeamToOrganization")
    public void addAndRemoveUserToTeam() {
        userManager.login(adminUser, adminPassword);
        userManager.addUserToTeam(adminUser, TEAM_NAME, ORGANIZATION_NAME);
        userManager.removeUserFromTeam(adminUser, TEAM_NAME, ORGANIZATION_NAME);
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
    }

    @AfterClass(dependsOnMethods = "cleanup", alwaysRun = true)
    public void closeDriver() {
        userManager.getCustomChromeDriver().closeDriver();
    }
}
