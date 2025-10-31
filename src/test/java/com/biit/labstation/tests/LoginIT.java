package com.biit.labstation.tests;

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

import com.biit.labstation.components.SnackBar;
import com.biit.labstation.usermanager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

import static com.biit.labstation.tests.Priorities.LOGIN_PRIORITY;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@Test(groups = "login", priority = LOGIN_PRIORITY)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class LoginIT extends AbstractTestNGSpringContextTests {

    @Autowired
    private UserManager userManager;

    @Autowired
    private SnackBar snackBar;

    @Value("${admin.user}")
    private String adminUser;
    @Value("${admin.password}")
    private String adminPassword;

    @BeforeClass
    public void setup() {
        userManager.access();
        //Creates admin user.
        userManager.login(adminUser, adminPassword);
        userManager.logout();
    }

    @Test
    public void loginIncorrect() {
        userManager.login(adminUser + "_Error", adminPassword);
        await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> Assert.assertEquals(snackBar.getMessageType(), "error"));
    }
}
