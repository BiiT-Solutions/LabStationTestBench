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
import com.biit.labstation.exceptions.ElementNotFoundAsExpectedException;
import com.biit.labstation.logger.ClassTestListener;
import com.biit.labstation.logger.TestListener;
import com.biit.labstation.tests.BaseTest;
import com.biit.labstation.tests.ITestWithWebDriver;
import com.biit.labstation.usermanager.UserManager;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.biit.labstation.tests.Priorities.BASIC_USER_PERMISSIONS_PRIORITY;

@SpringBootTest
@Test(groups = "userManagerBasicUserPermissions", priority = BASIC_USER_PERMISSIONS_PRIORITY)
@Listeners({TestListener.class, ClassTestListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserManagerBasicUserPermissionsTests extends BaseTest implements ITestWithWebDriver {

    private static final String BASIC_USER_NAME = "basicuser";
    private static final String USER_PASSWORD = "my-password";

    @Autowired
    private UserManager userManager;

    @Autowired
    private Popup popup;

    @Autowired
    private SnackBar snackBar;

    @Value("${admin.user}")
    private String adminUser;
    @Value("${admin.password}")
    private String adminPassword;

    @BeforeClass
    public void setup() {
        userManager.access();
    }

    @Test()
    public void createBasicUser() {
        userManager.login(adminUser, adminPassword);
        try {
            userManager.selectUserOnMenu();
            userManager.addUser(BASIC_USER_NAME, BASIC_USER_NAME + "@test.com", "Basic", "User", USER_PASSWORD);
            snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.USER_CREATED);
            userManager.addUserToGroup(BASIC_USER_NAME, "Employee");
        } finally {
            userManager.logout();
            snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.LOGGED_OUT);
        }
    }

    @Test(dependsOnMethods = "createBasicUser", expectedExceptions = ElementNotFoundAsExpectedException.class)
    public void checkBasicNoPermissions() {
        userManager.login(BASIC_USER_NAME, USER_PASSWORD);
        //Now can see its own profile.
        Assert.assertEquals(getDriver().findElementWaiting(By.id("username")).findElement(By.className("input-object")).getAttribute("value"), BASIC_USER_NAME);
        try {
            userManager.selectOrganizationsOnMenu();
        } catch (ElementNotInteractableException e) {
            throw new ElementNotFoundAsExpectedException();
        } finally {
            userManager.logout();
        }
    }

    @Test(dependsOnMethods = "checkBasicNoPermissions")
    public void addUserToGroup() {
        userManager.login(adminUser, adminPassword);
        userManager.addUserToGroup(BASIC_USER_NAME, "Admin");
        userManager.logout();
    }

    @Test(dependsOnMethods = "addUserToGroup")
    public void checkUserNowHasPermissions() {
        userManager.login(BASIC_USER_NAME, USER_PASSWORD);
        userManager.selectOrganizationsOnMenu();
        userManager.logout();
    }

    @Test(dependsOnMethods = "checkUserNowHasPermissions")
    public void removeUserFromGroup() {
        userManager.login(adminUser, adminPassword);
        userManager.removeUserFromGroup(BASIC_USER_NAME, "Admin");
        userManager.logout();
    }

    @Test(dependsOnMethods = "removeUserFromGroup", expectedExceptions = ElementNotFoundAsExpectedException.class)
    public void checkBasicNoPermissionsAgain() {
        userManager.login(BASIC_USER_NAME, USER_PASSWORD);
        //Now can see its own profile.
        Assert.assertEquals(getDriver().findElementWaiting(By.id("username")).findElement(By.className("input-object")).getAttribute("value"), BASIC_USER_NAME);
        try {
            userManager.selectOrganizationsOnMenu();
        } catch (ElementNotInteractableException e) {
            throw new ElementNotFoundAsExpectedException();
        } finally {
            userManager.logout();
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
