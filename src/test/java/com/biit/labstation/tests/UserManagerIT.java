package com.biit.labstation.tests;

import com.biit.labstation.components.SnackBar;
import com.biit.labstation.components.TableId;
import com.biit.labstation.usermanager.UserManager;
import org.awaitility.Awaitility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
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

    @Value("${retry.first.login}")
    private boolean retryFirstLogin;

    @BeforeClass
    public void setup() throws InterruptedException {
        userManager.access();
        //Creates admin user.
        userManager.login(ADMIN_USER_NAME, ADMIN_USER_PASSWORD);
        //After a complete wipe out of the database, the first login is for creating user, the second one for accessing it.
        if (retryFirstLogin) {
            Thread.sleep(2000);
            userManager.login(ADMIN_USER_NAME, ADMIN_USER_PASSWORD);
        }
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
        Assert.assertEquals(userManager.getTableContent(TableId.USERS_TABLE, 0, 3), ADMIN_USER_NAME);
        //Assert.assertEquals(userManager.getTableContent(0, 6), LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }


    @Test(dependsOnMethods = "checkUserExists")
    public void addBackendServices() {
        //Appointment Center
        userManager.addService("AppointmentCenter", "Tool for handling appointments");
        userManager.addServiceRole("AppointmentCenter", "admin");
        userManager.addServiceRole("AppointmentCenter", "editor");
        userManager.addServiceRole("AppointmentCenter", "manager");
        userManager.addServiceRole("AppointmentCenter", "viewer");

        //BaseFormDroolsEngine
        userManager.addService("BaseFormDroolsEngine", "ABCD Rules runner");
        userManager.addServiceRole("BaseFormDroolsEngine", "admin");
        userManager.addServiceRole("BaseFormDroolsEngine", "editor");
        userManager.addServiceRole("BaseFormDroolsEngine", "viewer");

        //DataTide
        userManager.addService("DataTide", "Dummy data generator");
        userManager.addServiceRole("DataTide", "admin");

        //FactManager
        userManager.addService("FactManager", "Facts storage and search functionality");
        userManager.addServiceRole("FactManager", "admin");
        userManager.addServiceRole("FactManager", "editor");
        userManager.addServiceRole("FactManager", "viewer");

        //InfographicEngine
        userManager.addService("InfographicEngine", "Created beautiful SVGs");
        userManager.addServiceRole("InfographicEngine", "admin");
        userManager.addServiceRole("InfographicEngine", "editor");
        userManager.addServiceRole("InfographicEngine", "viewer");

        //KafkaProxy
        userManager.addService("KafkaProxy", "For sending Kafka events through a REST API");
        userManager.addServiceRole("KafkaProxy", "admin");
        userManager.addServiceRole("KafkaProxy", "editor");
        userManager.addServiceRole("KafkaProxy", "viewer");

        //KafkaProxy
        userManager.addService("KnowledgeSystem", "Storing Knowledge");
        userManager.addServiceRole("KnowledgeSystem", "admin");
        userManager.addServiceRole("KnowledgeSystem", "editor");
        userManager.addServiceRole("KnowledgeSystem", "viewer");

        //MetaViewerStructure
        userManager.addService("MetaViewerStructure", "Filtering elements");
        userManager.addServiceRole("MetaViewerStructure", "admin");
        userManager.addServiceRole("MetaViewerStructure", "editor");
        userManager.addServiceRole("MetaViewerStructure", "viewer");

        //ProfileMatcher
        userManager.addService("ProfileMatcher", "Search profiles for vacancies");
        userManager.addServiceRole("ProfileMatcher", "admin");
        userManager.addServiceRole("ProfileMatcher", "editor");
        userManager.addServiceRole("ProfileMatcher", "viewer");

        //XForms
        userManager.addService("XForms", "Form Runner");
        userManager.goNextPage(TableId.SERVICE_TABLE);
        userManager.addServiceRole("XForms", "user");
    }

    @Test(dependsOnMethods = "checkUserExists")
    public void addRoles() {
        //Appointment Center
        userManager.addRole("CADT", "Fill up the CADT test.");
        userManager.addRole("Credibility", "Credibility form access.");
        userManager.addRole("Customer List", "Shows all the customers in the FactDashboard.");
        userManager.addRole("Happiness At Work", "HaW form.");
        userManager.addRole("NCA", "NCA form.");
        userManager.addRole("admin", "Other admins.");
        userManager.addRole("ceo", null);
        userManager.addRole("editor", "Can see and change data.");
        userManager.addRole("manager", "Can see and change data.");
        userManager.addRole("practitioner", "Performs CADT test.");
        userManager.addRole("speaker", "Organize workshops.");
        userManager.addRole("teamleader", null);
        userManager.addRole("user", null);
        userManager.addRole("xls access", "Can download data as XLS");
    }


    @Test(dependsOnMethods = {"addBackendServices"})
    //@Test(dependsOnMethods = "checkUserExists")
    public void addApplications() {
        //Appointment Center
        userManager.addApplication("AppointmentCenter", "Tool for handling appointments");
    }


    @Test(dependsOnMethods = {"addApplications", "addRoles"})
    public void addApplicationsRoles() {
        userManager.addApplicationRole("AppointmentCenter", "admin");
        userManager.linkApplicationRoleWithServiceRole("AppointmentCenter", "AppointmentCenter", "admin", "admin");
        userManager.addApplicationRole("AppointmentCenter", "editor");
        userManager.linkApplicationRoleWithServiceRole("AppointmentCenter", "AppointmentCenter", "editor", "editor");
        userManager.addApplicationRole("AppointmentCenter", "manager");
        userManager.linkApplicationRoleWithServiceRole("AppointmentCenter", "AppointmentCenter", "manager", "editor");
        userManager.linkApplicationRoleWithServiceRole("AppointmentCenter", "AppointmentCenter", "manager", "viewer");
        userManager.addApplicationRole("AppointmentCenter", "speaker");
        userManager.linkApplicationRoleWithServiceRole("AppointmentCenter", "AppointmentCenter", "speaker", "editor");
        userManager.linkApplicationRoleWithServiceRole("AppointmentCenter", "AppointmentCenter", "speaker", "viewer");
        userManager.addApplicationRole("AppointmentCenter", "user");
    }

    /**
     * Same as 'addApplicationsRoles' test, but from roles view.
     */
    @Test(dependsOnMethods = {"addApplications", "addRoles"})
    public void linkRoles() {
        userManager.linkRoleWithApplication("CADT", "FactsDashboard", "FactManager", "viewer");
        userManager.linkRoleWithApplication("CADT", "FactsDashboard", "InfographicEngine", "viewer");
    }


//    @AfterClass(enabled = false)
//    public void cleanup() {
//        while (userManager.getTableSize() > 1) {
//            userManager.selectTableRow("users-table", 0);
//            userManager.pressTableButton("users-table", "button-minus");
//        }
//    }
}
