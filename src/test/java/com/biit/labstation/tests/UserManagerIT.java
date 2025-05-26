package com.biit.labstation.tests;

import com.biit.labstation.usermanager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@SpringBootTest
@Test(groups = "userCreation")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserManagerIT extends AbstractTestNGSpringContextTests {
    private static final String USER_NAME = "admin@test.com";
    private static final String USER_PASSWORD = "asd123";

    @Autowired
    private UserManager userManager;

    @BeforeClass
    public void setup() {
        userManager.access();
    }

    @Test
    public void login() {
        userManager.access();
        userManager.login(USER_NAME, USER_PASSWORD);
    }
}
