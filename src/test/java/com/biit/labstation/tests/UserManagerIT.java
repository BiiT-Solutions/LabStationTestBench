package com.biit.labstation.tests;

import com.biit.labstation.components.SnackBar;
import com.biit.labstation.usermanager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

import static org.awaitility.Awaitility.await;

@SpringBootTest
@Test(groups = "userCreation")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserManagerIT extends AbstractTestNGSpringContextTests {
    private static final String ADMIN_USER_NAME = "admin@test.com";
    private static final String ADMIN_USER_PASSWORD = "asd123";

    @Autowired
    private UserManager userManager;

    @Autowired
    private SnackBar snackBar;

    @BeforeClass
    public void setup() {
        userManager.access();
        //Creates admin user.
        userManager.login(ADMIN_USER_NAME, ADMIN_USER_PASSWORD);
        userManager.logout();
    }

    @Test
    public void loginIncorrect() {
        userManager.login(ADMIN_USER_NAME + "_Error", ADMIN_USER_PASSWORD);
        await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> Assert.assertEquals(snackBar.getMessageType(), "error"));
    }
}
