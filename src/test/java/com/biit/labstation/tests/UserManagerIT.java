package com.biit.labstation.tests;

import com.biit.labstation.components.SnackBar;
import com.biit.labstation.usermanager.UserManager;
import org.awaitility.Awaitility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

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

    @BeforeClass
    public void configureAwait() {
        Awaitility.setDefaultPollInterval(10, TimeUnit.MILLISECONDS);
        Awaitility.setDefaultPollDelay(Duration.ZERO);
        Awaitility.setDefaultTimeout(Duration.ofSeconds(3));
    }

    @Test
    public void checkUserExists() {
        userManager.login(ADMIN_USER_NAME, ADMIN_USER_PASSWORD);
        Assert.assertEquals(userManager.getTableContent(0, 3), ADMIN_USER_NAME);
        //Assert.assertEquals(userManager.getTableContent(0, 6), LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }


//    @Test(dependsOnMethods = "checkUserExists")
//    public void addBackendServices() {
//        //Appointment Center
//        userManager.addService("AppointmentCenter", "Tool for handling appointments");
//        userManager.addServiceRole("AppointmentCenter", "admin");
//        userManager.addServiceRole("AppointmentCenter", "editor");
//        userManager.addServiceRole("AppointmentCenter", "manager");
//        userManager.addServiceRole("AppointmentCenter", "viewer");
//
//        //BaseFormDroolsEngine
//        userManager.addService("BaseFormDroolsEngine", "ABCD Rules runner");
//        userManager.addServiceRole("BaseFormDroolsEngine", "admin");
//        userManager.addServiceRole("BaseFormDroolsEngine", "editor");
//        userManager.addServiceRole("BaseFormDroolsEngine", "viewer");
//
//        //DataTide
//        userManager.addService("DataTide", "Dummy data generator");
//        userManager.addServiceRole("DataTide", "admin");
//
//        //FactManager
//        userManager.addService("FactManager", "Facts storage and search functionality");
//        userManager.addServiceRole("FactManager", "admin");
//        userManager.addServiceRole("FactManager", "editor");
//        userManager.addServiceRole("FactManager", "viewer");
//
//        //InfographicEngine
//        userManager.addService("InfographicEngine", "Created beautiful SVGs");
//        userManager.addServiceRole("InfographicEngine", "admin");
//        userManager.addServiceRole("InfographicEngine", "editor");
//        userManager.addServiceRole("InfographicEngine", "viewer");
//
//        //KafkaProxy
//        userManager.addService("KafkaProxy", "For sending Kafka events through a REST API");
//        userManager.addServiceRole("KafkaProxy", "admin");
//        userManager.addServiceRole("KafkaProxy", "editor");
//        userManager.addServiceRole("KafkaProxy", "viewer");
//
//        //KafkaProxy
//        userManager.addService("KnowledgeSystem", "Storing Knowledge");
//        userManager.addServiceRole("KnowledgeSystem", "admin");
//        userManager.addServiceRole("KnowledgeSystem", "editor");
//        userManager.addServiceRole("KnowledgeSystem", "viewer");
//
//        //MetaViewerStructure
//        userManager.addService("MetaViewerStructure", "Filtering elements");
//        userManager.addServiceRole("MetaViewerStructure", "admin");
//        userManager.addServiceRole("MetaViewerStructure", "editor");
//        userManager.addServiceRole("MetaViewerStructure", "viewer");
//
//        //ProfileMatcher
//        userManager.addService("ProfileMatcher", "Search profiles for vacancies");
//        userManager.addServiceRole("ProfileMatcher", "admin");
//        userManager.addServiceRole("ProfileMatcher", "editor");
//        userManager.addServiceRole("ProfileMatcher", "viewer");
//

    /// /        //UserManagerSystem
    /// /        userManager.addService("UserManagerSystem", "Itsumi UserManager");
    /// /        userManager.addServiceRole("UserManagerSystem", "admin");
    /// /        userManager.addServiceRole("UserManagerSystem", "editor");
    /// /        userManager.addServiceRole("UserManagerSystem", "viewer");
//
//        //XForms
//        userManager.goNextPage();
//        userManager.addService("XForms", "Form Runner");
//        userManager.addServiceRole("XForms", "user");
//    }


//    @Test(dependsOnMethods = "addBackendServices")
    @Test(dependsOnMethods = "checkUserExists")
    public void addApplications() {
        //Appointment Center
        userManager.addApplication("AppointmentCenter", "Tool for handling appointments");
        userManager.addApplicationRole("AppointmentCenter", "ADMIN");
        userManager.linkApplicationRoleWithServiceRole("AppointmentCenter", "ADMIN", "admin");
        userManager.addServiceRole("AppointmentCenter", "editor");
        userManager.linkApplicationRoleWithServiceRole("AppointmentCenter", "editor", "editor");
        userManager.addServiceRole("AppointmentCenter", "manager");
        userManager.linkApplicationRoleWithServiceRole("AppointmentCenter", "manager", "editor");
        userManager.linkApplicationRoleWithServiceRole("AppointmentCenter", "manager", "viewer");
        userManager.addServiceRole("AppointmentCenter", "speaker");
        userManager.linkApplicationRoleWithServiceRole("AppointmentCenter", "speaker", "editor");
        userManager.linkApplicationRoleWithServiceRole("AppointmentCenter", "speaker", "viewer");
        userManager.addServiceRole("AppointmentCenter", "user");
    }

    @Test(dependsOnMethods = "addApplications")
    public void addRoles() {
        //Appointment Center
        userManager.addRole("CADT", "Fill up the CADT test.");
        userManager.linkRoleWithApplication("CADT", "FactsDashboard",  "FactManager", "viewer");
        userManager.linkRoleWithApplication("CADT", "FactsDashboard",  "InfographicEngine", "viewer");
    }


    @AfterClass(enabled = false)
    public void cleanup() {
        while (userManager.getTableSize() > 1) {
            userManager.selectTableRow(0);
            userManager.pressTableButton("button-minus");
        }
    }
}
