package com.biit.labstation.tests;

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

import static org.awaitility.Awaitility.await;

@SpringBootTest
@Test(groups = "login")
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
