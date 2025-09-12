package com.biit.labstation.tests.usermanager;

import com.biit.labstation.ScreenShooter;
import com.biit.labstation.components.Popup;
import com.biit.labstation.components.PopupId;
import com.biit.labstation.components.SnackBar;
import com.biit.labstation.components.Table;
import com.biit.labstation.components.TableId;
import com.biit.labstation.logger.ClassTestListener;
import com.biit.labstation.logger.LabStationLogger;
import com.biit.labstation.logger.TestListener;
import com.biit.labstation.tests.BaseTest;
import com.biit.labstation.tests.ITestWithWebDriver;
import com.biit.labstation.usermanager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.biit.labstation.tests.Priorities.USER_MANAGER_PRIORITY;

@SpringBootTest
@Test(groups = "userManagerDefaultData", priority = USER_MANAGER_PRIORITY)
@Listeners({TestListener.class, ClassTestListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserManagerTests extends BaseTest implements ITestWithWebDriver {

    private static final String ADMIN_NAME = "Angus";
    private static final String ADMIN_LASTNAME = "MacGyver";

    @Autowired
    private UserManager userManager;

    @Autowired
    private Popup popup;

    @Autowired
    private SnackBar snackBar;

    @Autowired
    private Table table;

    @Value("${starts.from.clean.database}")
    private boolean startsFormCleanDatabase;

    @Value("${admin.user}")
    private String adminUser;
    @Value("${admin.password}")
    private String adminPassword;

    @Value("${jwt.user}")
    private String jwtUser;
    @Value("${jwt.password}")
    private String jwtPassword;
    @Autowired
    private ScreenShooter screenShooter;

    @BeforeClass
    public void setup() throws InterruptedException {
        userManager.access();
        //Creates admin user.
        LabStationLogger.info(this.getClass(), "First access try...");
        waitUntilReady(userManager);
        LabStationLogger.info(this.getClass(), "System is ready!");
        //After a complete wipe out of the database, the first login is for creating user, the second one for accessing it.
        if (startsFormCleanDatabase) {
            Thread.sleep(2000);
            LabStationLogger.info(this.getClass(), "Checking admin user!");
            userManager.login(adminUser, adminPassword);
            userManager.logout();
        }
    }

    @Test
    public void checkUserExists() {
        userManager.login(adminUser, adminPassword);

        Assert.assertEquals(table.getText(TableId.USERS_TABLE, 0, 3), adminUser);
        Assert.assertEquals(table.getText(TableId.USERS_TABLE, 0, 6), LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        userManager.logout();
    }

    @Test(dependsOnMethods = "checkUserExists", priority = -1)
    public void editUser() {
        userManager.login(adminUser, adminPassword);

        userManager.editUser(adminUser, adminUser, ADMIN_NAME, ADMIN_LASTNAME);
        table.search(TableId.USERS_TABLE, adminUser);
        Assert.assertEquals(table.getText(TableId.USERS_TABLE, 0, 1), ADMIN_NAME);
        Assert.assertEquals(table.getText(TableId.USERS_TABLE, 0, 2), ADMIN_LASTNAME);
        Assert.assertEquals(table.getText(TableId.USERS_TABLE, 0, 3), adminUser);
        Assert.assertEquals(table.getText(TableId.USERS_TABLE, 0, 4), adminUser);
        Assert.assertEquals(table.getText(TableId.USERS_TABLE, 0, 6), LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        userManager.logout();
    }


    @Test(dependsOnMethods = "checkUserExists")
    public void addBackendServices() {
        userManager.login(adminUser, adminPassword);

        //Appointment Center
        userManager.addService("AppointmentCenter", "Tool for handling appointments");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addServiceRole("AppointmentCenter", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addServiceRole("AppointmentCenter", "editor");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addServiceRole("AppointmentCenter", "manager");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addServiceRole("AppointmentCenter", "viewer");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        //BaseFormDroolsEngine
        userManager.addService("BaseFormDroolsEngine", "ABCD Rules runner");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addServiceRole("BaseFormDroolsEngine", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addServiceRole("BaseFormDroolsEngine", "editor");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addServiceRole("BaseFormDroolsEngine", "viewer");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        //DataTide
        userManager.addService("DataTide", "Dummy data generator");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addServiceRole("DataTide", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        //FactManager
        userManager.addService("FactManager", "Facts storage and search functionality");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addServiceRole("FactManager", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addServiceRole("FactManager", "editor");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addServiceRole("FactManager", "viewer");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        //InfographicEngine
        userManager.addService("InfographicEngine", "Created beautiful SVGs");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addServiceRole("InfographicEngine", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addServiceRole("InfographicEngine", "editor");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addServiceRole("InfographicEngine", "viewer");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        //KafkaProxy
        userManager.addService("KafkaProxy", "For sending Kafka events through a REST API");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addServiceRole("KafkaProxy", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addServiceRole("KafkaProxy", "editor");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addServiceRole("KafkaProxy", "viewer");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        //KafkaProxy
        userManager.addService("KnowledgeSystem", "Storing Knowledge");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addServiceRole("KnowledgeSystem", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addServiceRole("KnowledgeSystem", "editor");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addServiceRole("KnowledgeSystem", "viewer");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        //MetaViewerStructure
        userManager.addService("MetaViewerStructure", "Filtering elements.");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addServiceRole("MetaViewerStructure", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addServiceRole("MetaViewerStructure", "editor");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addServiceRole("MetaViewerStructure", "viewer");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        //ProfileMatcher
        userManager.addService("ProfileMatcher", "Search profiles for vacancies");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addServiceRole("ProfileMatcher", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addServiceRole("ProfileMatcher", "editor");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addServiceRole("ProfileMatcher", "viewer");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);


        //XForms
        userManager.addService("XForms", "Form Runner");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addServiceRole("XForms", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        //UserManagerSystem
        userManager.addServiceRole("UserManagerSystem", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addServiceRole("UserManagerSystem", "editor");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addServiceRole("UserManagerSystem", "viewer");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        table.completeTestTable(TableId.SERVICE_TABLE);

        userManager.logout();
    }

    @Test(dependsOnMethods = "checkUserExists")
    public void addRoles() {
        userManager.login(adminUser, adminPassword);

        //Appointment Center
        userManager.addRole("CADT", "Fill up the CADT test.");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addRole("Credibility", "Credibility form access.");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addRole("Customer List", "Shows all the customers in the FactsDashboard.");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addRole("Organization List", "Access to all the organizations in the FactsDashboard.");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addRole("Happiness at Work", "HaW form.");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addRole("NCA", "NCA form.");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addRole("admin", "Other admins.");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addRole("ceo", null);
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addRole("editor", "Can see and change data.");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addRole("manager", "Can see and change data.");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addRole("practitioner", "Performs CADT test.");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addRole("speaker", "Organize workshops.");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addRole("teamleader", null);
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addRole("user", null);
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addRole("xls access", "Can download data as XLS");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        table.completeTestTable(TableId.ROLE_TABLE);

        userManager.logout();
    }


    @Test(dependsOnMethods = {"addBackendServices"})
    public void addApplications() {
        userManager.login(adminUser, adminPassword);

        //Appointment Center
        userManager.addApplication("AppointmentCenter", "Tool for handling appointments.");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addApplication("BaseFormDroolsEngine", "Executing ABCD rules.");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addApplication("BiitSurveys", "Online surveys.");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addApplication("CardGame", "CADT.");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addApplication("DataTide", "Create huge amount of dummy data.");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addApplication("FactManager", "Handle Facts.");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addApplication("FactsDashboard", "Show beautiful charts from facts.");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addApplication("InfographicEngine", "Create SVGs on the fly.");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addApplication("KafkaProxy", "For sending Kafka events through a REST API.");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addApplication("KnowledgeSystem", "Work in Progress.");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addApplication("MetaViewerStructure", "Filtering elements.");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addApplication("ProfileMatcher", "For relations between candidates and vacancies.");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addApplication("XForms", "Online form runner.");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        table.completeTestTable(TableId.APPLICATION_TABLE);

        userManager.logout();
    }


    @Test(dependsOnMethods = {"addApplications", "addRoles"})
    public void addApplicationsRoles() {
        userManager.login(adminUser, adminPassword);

        userManager.addApplicationRole("AppointmentCenter", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("AppointmentCenter", "admin", "AppointmentCenter", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("AppointmentCenter", "admin", "UserManagerSystem", "editor");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addApplicationRole("AppointmentCenter", "editor");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("AppointmentCenter", "editor", "AppointmentCenter", "editor");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("AppointmentCenter", "editor", "UserManagerSystem", "editor");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addApplicationRole("AppointmentCenter", "manager");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("AppointmentCenter", "manager", "AppointmentCenter", "editor");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("AppointmentCenter", "manager", "UserManagerSystem", "editor");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addApplicationRole("AppointmentCenter", "speaker");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("AppointmentCenter", "speaker", "AppointmentCenter", "editor");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("AppointmentCenter", "speaker", "UserManagerSystem", "viewer");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addApplicationRole("AppointmentCenter", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("AppointmentCenter", "user", "AppointmentCenter", "viewer");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("AppointmentCenter", "user", "UserManagerSystem", "viewer");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.addApplicationRole("BaseFormDroolsEngine", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("BaseFormDroolsEngine", "admin", "BaseFormDroolsEngine", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addApplicationRole("BaseFormDroolsEngine", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("BaseFormDroolsEngine", "user", "BaseFormDroolsEngine", "viewer");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.addApplicationRole("BiitSurveys", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("BiitSurveys", "user", "KafkaProxy", "editor");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.addApplicationRole("CardGame", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("CardGame", "user", "KafkaProxy", "editor");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.addApplicationRole("DataTide", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("DataTide", "admin", "DataTide", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.addApplicationRole("FactManager", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("FactManager", "admin", "FactManager", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addApplicationRole("FactManager", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("FactManager", "user", "FactManager", "viewer");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        //Facts dashboard CADT will be done later.

        userManager.addApplicationRole("FactsDashboard", "Credibility");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("FactsDashboard", "Credibility", "FactManager", "viewer");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("FactsDashboard", "Credibility", "InfographicEngine", "viewer");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.addApplicationRole("FactsDashboard", "Customer List");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("FactsDashboard", "Customer List", "UserManagerSystem", "editor");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.addApplicationRole("FactsDashboard", "Happiness at Work");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("FactsDashboard", "Happiness at Work", "FactManager", "viewer");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.addApplicationRole("FactsDashboard", "NCA");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("FactsDashboard", "NCA", "FactManager", "viewer");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("FactsDashboard", "NCA", "InfographicEngine", "viewer");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.addApplicationRole("FactsDashboard", "Organization List");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("FactsDashboard", "Organization List", "UserManagerSystem", "editor");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.addApplicationRole("FactsDashboard", "ceo");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("FactsDashboard", "ceo", "FactManager", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("FactsDashboard", "ceo", "InfographicEngine", "viewer");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.addApplicationRole("FactsDashboard", "practitioner");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("FactsDashboard", "practitioner", "UserManagerSystem", "editor");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.addApplicationRole("FactsDashboard", "teamleader");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("FactsDashboard", "teamleader", "FactManager", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("FactsDashboard", "teamleader", "InfographicEngine", "viewer");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.addApplicationRole("FactsDashboard", "xls access");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("FactsDashboard", "xls access", "FactManager", "viewer");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.addApplicationRole("InfographicEngine", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("InfographicEngine", "admin", "InfographicEngine", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.addApplicationRole("InfographicEngine", "practitioner");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("InfographicEngine", "practitioner", "FactManager", "editor");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("InfographicEngine", "practitioner", "InfographicEngine", "editor");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.addApplicationRole("InfographicEngine", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("InfographicEngine", "user", "InfographicEngine", "viewer");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.addApplicationRole("KafkaProxy", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("KafkaProxy", "admin", "KafkaProxy", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.addApplicationRole("KafkaProxy", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("KafkaProxy", "user", "KafkaProxy", "editor");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("KafkaProxy", "user", "KafkaProxy", "viewer");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.addApplicationRole("KnowledgeSystem", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("KnowledgeSystem", "admin", "KnowledgeSystem", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.addApplicationRole("KnowledgeSystem", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("KnowledgeSystem", "user", "KnowledgeSystem", "viewer");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.addApplicationRole("MetaViewerStructure", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("MetaViewerStructure", "admin", "MetaViewerStructure", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.addApplicationRole("MetaViewerStructure", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("MetaViewerStructure", "user", "MetaViewerStructure", "viewer");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.addApplicationRole("ProfileMatcher", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("ProfileMatcher", "admin", "ProfileMatcher", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.addApplicationRole("ProfileMatcher", "manager");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("ProfileMatcher", "manager", "ProfileMatcher", "editor");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.addApplicationRole("ProfileMatcher", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("ProfileMatcher", "user", "ProfileMatcher", "viewer");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.addApplicationRole("UserManagerSystem", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("UserManagerSystem", "admin", "UserManagerSystem", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.addApplicationRole("UserManagerSystem", "editor");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("UserManagerSystem", "editor", "UserManagerSystem", "editor");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.addApplicationRole("UserManagerSystem", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("UserManagerSystem", "user", "UserManagerSystem", "viewer");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.addApplicationRole("XForms", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("XForms", "user", "FactManager", "viewer");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("XForms", "user", "XForms", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.logout();
    }


    /**
     * Same as 'addApplicationsRoles' test, but from roles view.
     */
    @Test(dependsOnMethods = {"addApplications", "addRoles"})
    public void linkRoles() {
        userManager.login(adminUser, adminPassword);

        userManager.linkRoleWithApplication("CADT", "FactsDashboard");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkRoleWithApplicationService("CADT", "FactsDashboard", "FactManager", "viewer");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkRoleWithApplicationService("CADT", "FactsDashboard", "InfographicEngine", "viewer");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        //Check that role is inserted on the application.
        userManager.selectApplicationsOnMenu();
        table.selectRow(TableId.APPLICATION_TABLE, "FactsDashboard", 1);
        table.pressButton(TableId.APPLICATION_TABLE, "button-linkage");
        //No exception must be there.
        table.selectRow(TableId.ROLE_TABLE, "CADT", 1);
        table.pressButton(TableId.ROLE_TABLE, "popup-application-roles-button-linkage");
        Assert.assertEquals(table.getText(TableId.APPLICATION_ROLE_TABLE, 0, 1), "FactManager");
        Assert.assertEquals(table.getText(TableId.APPLICATION_ROLE_TABLE, 1, 1), "InfographicEngine");
        popup.close(PopupId.APPLICATION_ROLE_ASSIGN);
        popup.close(PopupId.APPLICATION_ROLE);

        userManager.logout();
    }


    @Test(dependsOnMethods = "checkUserExists")
    public void addGroups() {
        userManager.login(adminUser, adminPassword);

        userManager.addGroup("Admin", "With great power comes great responsibility");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroup("CEO", "Powers without cows.");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroup("Editor", "Who can override data.");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroup("Employee", "Work will make you free.");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroup("HR", "Can fill up vacancies.");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroup("Organization Leader", "Works with organizations teams.");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroup("Practitioner", "Perform CADT tests.");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroup("Speakers", "Can present a workshop.");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroup("Team Leaders", "Captain of the ship.");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        Assert.assertEquals(table.getTotalNumberOfItems(TableId.USERS_GROUP_TABLE), 9);

        table.completeTestTable(TableId.USERS_GROUP_TABLE);

        userManager.logout();
    }

    @Test(dependsOnMethods = {"addGroups", "addApplicationsRoles", "linkRoles"})
    public void assignRolesToGroups() {
        userManager.login(adminUser, adminPassword);

        userManager.addGroupRole("Admin", "AppointmentCenter", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Admin", "BaseFormDroolsEngine", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Admin", "BiitSurveys", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Admin", "CardGame", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Admin", "DataTide", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Admin", "FactManager", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Admin", "FactsDashboard", "teamleader");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Admin", "FactsDashboard", "NCA");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Admin", "FactsDashboard", "xls access");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Admin", "FactsDashboard", "ceo");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Admin", "FactsDashboard", "practitioner");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Admin", "FactsDashboard", "Credibility");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Admin", "FactsDashboard", "Happiness at Work");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Admin", "FactsDashboard", "CADT");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Admin", "InfographicEngine", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Admin", "KafkaProxy", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Admin", "KnowledgeSystem", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Admin", "MetaViewerStructure", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Admin", "ProfileMatcher", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Admin", "UserManagerSystem", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Admin", "XForms", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        Assert.assertEquals(userManager.getTotalRolesByGroup("Admin"), 21);

        userManager.addGroupRole("CEO", "BaseFormDroolsEngine", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("CEO", "FactManager", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("CEO", "FactsDashboard", "xls access");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("CEO", "FactsDashboard", "ceo");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("CEO", "KafkaProxy", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("CEO", "KnowledgeSystem", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("CEO", "UserManagerSystem", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("CEO", "UserManagerSystem", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("CEO", "XForms", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        Assert.assertEquals(userManager.getTotalRolesByGroup("CEO"), 9);

        userManager.addGroupRole("Editor", "AppointmentCenter", "editor");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Editor", "BaseFormDroolsEngine", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Editor", "BiitSurveys", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Editor", "CardGame", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Editor", "FactManager", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Editor", "FactsDashboard", "teamleader");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Editor", "FactsDashboard", "practitioner");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Editor", "FactsDashboard", "NCA");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Editor", "FactsDashboard", "xls access");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Editor", "FactsDashboard", "ceo");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Editor", "FactsDashboard", "Credibility");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Editor", "FactsDashboard", "CADT");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Editor", "FactsDashboard", "Happiness at Work");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Editor", "InfographicEngine", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Editor", "KafkaProxy", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Editor", "KnowledgeSystem", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.addGroupRole("Editor", "MetaViewerStructure", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Editor", "ProfileMatcher", "manager");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Editor", "UserManagerSystem", "editor");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Editor", "XForms", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        Assert.assertEquals(userManager.getTotalRolesByGroup("Editor"), 20);

        userManager.addGroupRole("Employee", "AppointmentCenter", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Employee", "BaseFormDroolsEngine", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Employee", "BiitSurveys", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Employee", "CardGame", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Employee", "FactManager", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Employee", "InfographicEngine", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Employee", "KafkaProxy", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Employee", "KnowledgeSystem", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Employee", "MetaViewerStructure", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Employee", "UserManagerSystem", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Employee", "XForms", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Employee", "FactsDashboard", "CADT");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        Assert.assertEquals(userManager.getTotalRolesByGroup("Employee"), 12);

        userManager.addGroupRole("HR", "AppointmentCenter", "manager");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("HR", "BaseFormDroolsEngine", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("HR", "FactManager", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("HR", "FactsDashboard", "Organization List");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("HR", "FactsDashboard", "Credibility");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("HR", "FactsDashboard", "CADT");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("HR", "InfographicEngine", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("HR", "KafkaProxy", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("HR", "MetaViewerStructure", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("HR", "ProfileMatcher", "manager");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("HR", "UserManagerSystem", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("HR", "FactsDashboard", "xls access");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        Assert.assertEquals(userManager.getTotalRolesByGroup("HR"), 12);

        userManager.addGroupRole("Organization Leader", "FactManager", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Organization Leader", "FactsDashboard", "ceo");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Organization Leader", "InfographicEngine", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        Assert.assertEquals(userManager.getTotalRolesByGroup("HR"), 12);

        userManager.addGroupRole("Practitioner", "AppointmentCenter", "manager");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Practitioner", "FactsDashboard", "practitioner");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Practitioner", "InfographicEngine", "practitioner");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        Assert.assertEquals(userManager.getTotalRolesByGroup("Practitioner"), 3);

        userManager.addGroupRole("Speakers", "AppointmentCenter", "speaker");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Speakers", "UserManagerSystem", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        Assert.assertEquals(userManager.getTotalRolesByGroup("Speakers"), 2);

        userManager.addGroupRole("Team Leaders", "FactManager", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Team Leaders", "FactsDashboard", "teamleader");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Team Leaders", "FactsDashboard", "xls access");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Team Leaders", "InfographicEngine", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Team Leaders", "MetaViewerStructure", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Team Leaders", "UserManagerSystem", "user");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addGroupRole("Team Leaders", "XForms", "user");

        Assert.assertEquals(userManager.getTotalRolesByGroup("Team Leaders"), 7);

        userManager.logout();
    }

    @Test(dependsOnMethods = "assignRolesToGroups")
    public void assignUsersToGroups() {
        userManager.login(adminUser, adminPassword);

        userManager.addUserToGroup(adminUser, "Admin");

        userManager.logout();
    }


    @Test
//    @Test(dependsOnMethods = "assignRolesToGroups")
    public void createJwtUser() {
        userManager.login(adminUser, adminPassword);

        userManager.addUser(jwtUser, "token@test.com", "System", "Token", jwtPassword);
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.USER_CREATED);
        screenShooter.takeScreenshot("before_adding_roles");
        userManager.addUserRoles(jwtUser, "UserManagerSystem", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addUserRoles(jwtUser, "FactManager", "admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.logout();
    }

    @AfterClass
    public void closeDriver() {
        userManager.getCustomChromeDriver().closeDriver();
    }
}
