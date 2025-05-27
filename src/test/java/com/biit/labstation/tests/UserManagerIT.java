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

import static com.biit.labstation.tests.LoginIT.ADMIN_USER_NAME;
import static com.biit.labstation.tests.LoginIT.ADMIN_USER_PASSWORD;

@SpringBootTest
@Test(groups = "userManager")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserManagerIT extends AbstractTestNGSpringContextTests {

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
    public void checkUserExists() {
        userManager.login(ADMIN_USER_NAME, ADMIN_USER_PASSWORD);
        Assert.assertEquals(userManager.getTableContent(0, 3), ADMIN_USER_NAME);
        //Assert.assertEquals(userManager.getTableContent(0, 6), LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    @Test(dependsOnMethods = "checkUserExists")
    public void addServices() {
        //Appointment Center
        userManager.addService("AppointmentCenter", "Tool for handling appointments");
        Assert.assertEquals(userManager.getTableContent(userManager.getTableSize() - 1, 1), "AppointmentCenter");
        userManager.addServiceRole(userManager.getTableSize() - 1, "admin");
        userManager.addServiceRole(userManager.getTableSize() - 1, "editor");
        userManager.addServiceRole(userManager.getTableSize() - 1, "manager");
        userManager.addServiceRole(userManager.getTableSize() - 1, "viewer");

        //BaseFormDroolsEngine
        userManager.addService("BaseFormDroolsEngine", "ABCD Rules runner");
        Assert.assertEquals(userManager.getTableContent(userManager.getTableSize() - 1, 1), "BaseFormDroolsEngine");
        userManager.addServiceRole(userManager.getTableSize() - 1, "admin");
        userManager.addServiceRole(userManager.getTableSize() - 1, "editor");
        userManager.addServiceRole(userManager.getTableSize() - 1, "viewer");

        //DataTide
        userManager.addService("DataTide", "Dummy data generator");
        Assert.assertEquals(userManager.getTableContent(userManager.getTableSize() - 1, 1), "DataTide");
        userManager.addServiceRole(userManager.getTableSize() - 1, "admin");

        //FactManager
        userManager.addService("FactManager", "Facts storage and search functionality");
        Assert.assertEquals(userManager.getTableContent(userManager.getTableSize() - 1, 1), "FactManager");
        userManager.addServiceRole(userManager.getTableSize() - 1, "admin");
        userManager.addServiceRole(userManager.getTableSize() - 1, "editor");
        userManager.addServiceRole(userManager.getTableSize() - 1, "viewer");

        //InfographicEngine
        userManager.addService("InfographicEngine", "Created beautiful SVGs");
        Assert.assertEquals(userManager.getTableContent(userManager.getTableSize() - 1, 1), "InfographicEngine");
        userManager.addServiceRole(userManager.getTableSize() - 1, "admin");
        userManager.addServiceRole(userManager.getTableSize() - 1, "editor");
        userManager.addServiceRole(userManager.getTableSize() - 1, "viewer");

        //KafkaProxy
        userManager.addService("KafkaProxy", "For sending Kafka events through a REST API");
        Assert.assertEquals(userManager.getTableContent(userManager.getTableSize() - 1, 1), "KafkaProxy");
        userManager.addServiceRole(userManager.getTableSize() - 1, "admin");
        userManager.addServiceRole(userManager.getTableSize() - 1, "editor");
        userManager.addServiceRole(userManager.getTableSize() - 1, "viewer");

        //KafkaProxy
        userManager.addService("KnowledgeSystem", "Storing Knowledge");
        Assert.assertEquals(userManager.getTableContent(userManager.getTableSize() - 1, 1), "KnowledgeSystem");
        userManager.addServiceRole(userManager.getTableSize() - 1, "admin");
        userManager.addServiceRole(userManager.getTableSize() - 1, "editor");
        userManager.addServiceRole(userManager.getTableSize() - 1, "viewer");

        //MetaViewerStructure
        userManager.addService("MetaViewerStructure", "Filtering elements");
        Assert.assertEquals(userManager.getTableContent(userManager.getTableSize() - 1, 1), "MetaViewerStructure");
        userManager.addServiceRole(userManager.getTableSize() - 1, "admin");
        userManager.addServiceRole(userManager.getTableSize() - 1, "editor");
        userManager.addServiceRole(userManager.getTableSize() - 1, "viewer");

        //ProfileMatcher
        userManager.addService("ProfileMatcher", "Search profiles for vacancies");
        Assert.assertEquals(userManager.getTableContent(userManager.getTableSize() - 1, 1), "ProfileMatcher");
        userManager.addServiceRole(userManager.getTableSize() - 1, "admin");
        userManager.addServiceRole(userManager.getTableSize() - 1, "editor");
        userManager.addServiceRole(userManager.getTableSize() - 1, "viewer");

        //UserManagerSystem
        userManager.addService("UserManagerSystem", "Itsumi UserManager");
        Assert.assertEquals(userManager.getTableContent(userManager.getTableSize() - 1, 1), "UserManagerSystem");
        userManager.addServiceRole(userManager.getTableSize() - 1, "admin");
        userManager.addServiceRole(userManager.getTableSize() - 1, "editor");
        userManager.addServiceRole(userManager.getTableSize() - 1, "viewer");

        //XForms
        userManager.addService("XForms", "Form Runner");
        Assert.assertEquals(userManager.getTableContent(userManager.getTableSize() - 1, 1), "XForms");
        userManager.addServiceRole(userManager.getTableSize() - 1, "user");
    }
}
