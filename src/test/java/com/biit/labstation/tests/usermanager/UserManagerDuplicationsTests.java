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

import static com.biit.labstation.tests.Priorities.USER_TESTS_PRIORITY;

@SpringBootTest
@Test(groups = "userManagerDuplications", priority = USER_TESTS_PRIORITY)
@Listeners({TestListener.class, ClassTestListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserManagerDuplicationsTests extends BaseTest implements ITestWithWebDriver {

    private static final String BASIC_USER_NAME = "newuser";
    private static final String USER_PASSWORD = "my-password";

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

    @Test()
    public void createExistingUser() {
        userManager.login(adminUser, adminPassword);
        userManager.selectUserOnMenu();
        userManager.addUser(BASIC_USER_NAME, BASIC_USER_NAME + "@test.com", "New", "User", USER_PASSWORD);
        userManager.addUser(BASIC_USER_NAME, BASIC_USER_NAME + "@test.com", "New", "User", USER_PASSWORD);
        snackBar.checkMessage(SnackBar.Type.ERROR, SnackBar.USER_ALREADY_EXISTS);
        popup.close(PopupId.USER);
        userManager.logout();
    }

    @Test(dependsOnMethods = "createExistingUser")
    public void createExistingEmail() {
        userManager.login(adminUser, adminPassword);
        userManager.selectUserOnMenu();
        userManager.addUser(BASIC_USER_NAME + "_2", BASIC_USER_NAME + "@test.com", "New", "User", USER_PASSWORD);
        snackBar.checkMessage(SnackBar.Type.ERROR, SnackBar.EMAIL_IN_USE);
        popup.close(PopupId.USER);
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
            userManager.deleteUser(BASIC_USER_NAME);
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
