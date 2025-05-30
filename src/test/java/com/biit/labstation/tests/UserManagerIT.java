package com.biit.labstation.tests;

import com.biit.labstation.components.Popup;
import com.biit.labstation.components.PopupId;
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

    @Autowired
    private Popup popup;

    @Value("${starts.from.clean.database}")
    private boolean startsFormCleanDatabase;



    @BeforeClass
    public void setup() throws InterruptedException {
        userManager.access();
        //Creates admin user.
        userManager.login(ADMIN_USER_NAME, ADMIN_USER_PASSWORD);
        //After a complete wipe out of the database, the first login is for creating user, the second one for accessing it.
        if (startsFormCleanDatabase) {
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
        userManager.addService("MetaViewerStructure", "Filtering elements.");
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
        //Takes some millis to refresh and appear the component.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            //Ignore
            Thread.currentThread().interrupt();
        }
        userManager.goNextPage(TableId.SERVICE_TABLE);
        userManager.addServiceRole("XForms", "user");

        //UserManagerSystem
        userManager.goPreviousPage(TableId.SERVICE_TABLE);
        userManager.addServiceRole("UserManagerSystem", "admin");
        userManager.addServiceRole("UserManagerSystem", "editor");
        userManager.addServiceRole("UserManagerSystem", "viewer");
    }

    @Test(dependsOnMethods = "checkUserExists")
    public void addRoles() {
        //Appointment Center
        userManager.addRole("CADT", "Fill up the CADT test.");
        userManager.addRole("Credibility", "Credibility form access.");
        userManager.addRole("Customer List", "Shows all the customers in the FactsDashboard.");
        userManager.addRole("Organization List", "Access to all the organizations in the FactsDashboard.");
        userManager.addRole("Happiness at Work", "HaW form.");
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
    public void addApplications() {
        //Appointment Center
        userManager.addApplication("AppointmentCenter", "Tool for handling appointments.");
        userManager.addApplication("BaseFormDroolsEngine", "Executing ABCD rules.");
        userManager.addApplication("BiitSurveys", "Online surveys.");
        userManager.addApplication("CardGame", "CADT.");
        userManager.addApplication("DataTide", "Create huge amount of dummy data.");
        userManager.addApplication("FactManager", "Handle Facts.");
        userManager.addApplication("FactsDashboard", "Show beautiful charts from facts.");
        userManager.addApplication("InfographicEngine", "Create SVGs on the fly.");
        userManager.addApplication("KafkaProxy", "For sending Kafka events through a REST API.");
        userManager.addApplication("KnowledgeSystem", "Work in Progress.");
        userManager.addApplication("MetaViewerStructure", "Filtering elements.");
        userManager.addApplication("ProfileMatcher", "For relations between candidates and vacancies.");
        userManager.addApplication("XForms", "Online form runner.");
    }


    @Test(dependsOnMethods = {"addApplications", "addRoles"})
    public void addApplicationsRoles() {
        userManager.addApplicationRole("AppointmentCenter", "admin");
        userManager.linkApplicationRoleWithServiceRole("AppointmentCenter", "admin", "AppointmentCenter", "admin");
        userManager.addApplicationRole("AppointmentCenter", "editor");
        userManager.linkApplicationRoleWithServiceRole("AppointmentCenter", "editor", "AppointmentCenter", "editor");
        userManager.addApplicationRole("AppointmentCenter", "manager");
        userManager.linkApplicationRoleWithServiceRole("AppointmentCenter", "manager", "AppointmentCenter", "editor");
        userManager.linkApplicationRoleWithServiceRole("AppointmentCenter", "manager", "AppointmentCenter", "viewer");
        userManager.addApplicationRole("AppointmentCenter", "speaker");
        userManager.linkApplicationRoleWithServiceRole("AppointmentCenter", "speaker", "AppointmentCenter", "editor");
        userManager.linkApplicationRoleWithServiceRole("AppointmentCenter", "speaker", "AppointmentCenter", "viewer");
        userManager.addApplicationRole("AppointmentCenter", "user");
        userManager.linkApplicationRoleWithServiceRole("AppointmentCenter", "user", "AppointmentCenter", "viewer");

        userManager.addApplicationRole("BaseFormDroolsEngine", "admin");
        userManager.linkApplicationRoleWithServiceRole("BaseFormDroolsEngine", "admin", "BaseFormDroolsEngine", "admin");
        userManager.addApplicationRole("BaseFormDroolsEngine", "user");
        userManager.linkApplicationRoleWithServiceRole("BaseFormDroolsEngine", "user", "BaseFormDroolsEngine", "viewer");

        userManager.addApplicationRole("BiitSurveys", "user");
        userManager.linkApplicationRoleWithServiceRole("BiitSurveys", "user", "FactManager", "editor");

        userManager.addApplicationRole("CardGame", "user");
        userManager.linkApplicationRoleWithServiceRole("CardGame", "user", "FactManager", "editor");

        userManager.addApplicationRole("DataTide", "admin");
        userManager.linkApplicationRoleWithServiceRole("DataTide", "admin", "DataTide", "admin");

        userManager.addApplicationRole("FactManager", "admin");
        userManager.linkApplicationRoleWithServiceRole("FactManager", "admin", "FactManager", "admin");
        userManager.addApplicationRole("FactManager", "user");
        userManager.linkApplicationRoleWithServiceRole("FactManager", "user", "FactManager", "viewer");

        //Facts dashboard CADT will be done later.

        userManager.addApplicationRole("FactsDashboard", "Credibility");
        userManager.linkApplicationRoleWithServiceRole("FactsDashboard", "Credibility", "FactManager", "viewer");
        userManager.linkApplicationRoleWithServiceRole("FactsDashboard", "Credibility", "InfographicEngine", "viewer");

        userManager.addApplicationRole("FactsDashboard", "Customer List");
        userManager.linkApplicationRoleWithServiceRole("FactsDashboard", "Customer List", "UserManagerSystem", "editor");

        userManager.addApplicationRole("FactsDashboard", "Happiness at Work");
        userManager.linkApplicationRoleWithServiceRole("FactsDashboard", "Happiness at Work", "FactManager", "viewer");

        userManager.addApplicationRole("FactsDashboard", "NCA");
        userManager.linkApplicationRoleWithServiceRole("FactsDashboard", "NCA", "FactManager", "viewer");
        userManager.linkApplicationRoleWithServiceRole("FactsDashboard", "NCA", "InfographicEngine", "viewer");

        userManager.addApplicationRole("FactsDashboard", "Organization List");
        userManager.linkApplicationRoleWithServiceRole("FactsDashboard", "Organization List", "UserManagerSystem", "editor");

        userManager.addApplicationRole("FactsDashboard", "ceo");
        userManager.linkApplicationRoleWithServiceRole("FactsDashboard", "ceo", "FactManager", "admin");
        userManager.linkApplicationRoleWithServiceRole("FactsDashboard", "ceo", "InfographicEngine", "viewer");

        userManager.addApplicationRole("FactsDashboard", "practitioner");
        userManager.linkApplicationRoleWithServiceRole("FactsDashboard", "practitioner", "UserManagerSystem", "editor");

        userManager.addApplicationRole("FactsDashboard", "teamleader");
        userManager.linkApplicationRoleWithServiceRole("FactsDashboard", "teamleader", "FactManager", "admin");
        userManager.linkApplicationRoleWithServiceRole("FactsDashboard", "teamleader", "InfographicEngine", "viewer");

        userManager.addApplicationRole("FactsDashboard", "xls access");
        userManager.linkApplicationRoleWithServiceRole("FactsDashboard", "xls access", "FactManager", "viewer");

        userManager.addApplicationRole("InfographicEngine", "admin");
        userManager.linkApplicationRoleWithServiceRole("InfographicEngine", "admin", "InfographicEngine", "admin");

        userManager.addApplicationRole("InfographicEngine", "practitioner");
        userManager.linkApplicationRoleWithServiceRole("InfographicEngine", "practitioner", "FactManager", "editor");
        userManager.linkApplicationRoleWithServiceRole("InfographicEngine", "practitioner", "InfographicEngine", "editor");

        userManager.addApplicationRole("InfographicEngine", "user");
        userManager.linkApplicationRoleWithServiceRole("InfographicEngine", "user", "InfographicEngine", "viewer");

        userManager.addApplicationRole("KafkaProxy", "admin");
        userManager.linkApplicationRoleWithServiceRole("KafkaProxy", "admin", "KafkaProxy", "admin");

        userManager.addApplicationRole("KafkaProxy", "user");
        userManager.linkApplicationRoleWithServiceRole("KafkaProxy", "user", "KafkaProxy", "editor");
        userManager.linkApplicationRoleWithServiceRole("KafkaProxy", "user", "KafkaProxy", "viewer");

        userManager.addApplicationRole("KnowledgeSystem", "admin");
        userManager.linkApplicationRoleWithServiceRole("KnowledgeSystem", "admin", "KnowledgeSystem", "admin");

        userManager.addApplicationRole("KnowledgeSystem", "user");
        userManager.linkApplicationRoleWithServiceRole("KnowledgeSystem", "user", "KnowledgeSystem", "viewer");

        userManager.goNextPage(TableId.APPLICATION_TABLE);

        userManager.addApplicationRole("MetaViewerStructure", "admin");
        userManager.linkApplicationRoleWithServiceRole("MetaViewerStructure", "admin", "MetaViewerStructure", "admin");

        userManager.addApplicationRole("MetaViewerStructure", "user");
        userManager.linkApplicationRoleWithServiceRole("MetaViewerStructure", "user", "MetaViewerStructure", "viewer");

        userManager.addApplicationRole("ProfileMatcher", "admin");
        userManager.linkApplicationRoleWithServiceRole("ProfileMatcher", "admin", "ProfileMatcher", "admin");

        userManager.addApplicationRole("ProfileMatcher", "manager");
        userManager.linkApplicationRoleWithServiceRole("ProfileMatcher", "manager", "ProfileMatcher", "editor");

        userManager.addApplicationRole("ProfileMatcher", "user");
        userManager.linkApplicationRoleWithServiceRole("ProfileMatcher", "user", "ProfileMatcher", "viewer");

        userManager.addApplicationRole("UserManagerSystem", "admin");
        userManager.linkApplicationRoleWithServiceRole("UserManagerSystem", "admin", "UserManagerSystem", "admin");

        userManager.addApplicationRole("UserManagerSystem", "editor");
        userManager.linkApplicationRoleWithServiceRole("UserManagerSystem", "editor", "UserManagerSystem", "editor");

        userManager.addApplicationRole("UserManagerSystem", "user");
        userManager.linkApplicationRoleWithServiceRole("UserManagerSystem", "user", "UserManagerSystem", "viewer");

        userManager.addApplicationRole("XForms", "user");
        userManager.linkApplicationRoleWithServiceRole("XForms", "user", "FactManager", "viewer");
        userManager.linkApplicationRoleWithServiceRole("XForms", "user", "XForms", "user");
    }


    /**
     * Same as 'addApplicationsRoles' test, but from roles view.
     */
    @Test(dependsOnMethods = {"addApplications", "addRoles"})
    public void linkRoles() {
        userManager.linkRoleWithApplication("CADT", "FactsDashboard");
        userManager.linkRoleWithApplicationService("CADT", "FactsDashboard", "FactManager", "viewer");
        userManager.linkRoleWithApplicationService("CADT", "FactsDashboard", "InfographicEngine", "viewer");

        //Check that role is inserted on the application.
        userManager.selectApplicationsOnMenu();
        userManager.selectTableRow(TableId.APPLICATION_TABLE, "FactsDashboard", 1);
        userManager.pressTableButton(TableId.APPLICATION_TABLE, "button-linkage");
        //No exception must be there.
        userManager.selectTableRow(TableId.ROLE_TABLE, "CADT", 1);
        userManager.pressTableButton(TableId.ROLE_TABLE, "popup-application-roles-button-linkage");
        Assert.assertEquals(userManager.getTableContent(TableId.APPLICATION_ROLE_TABLE, 0, 1), "FactManager");
        Assert.assertEquals(userManager.getTableContent(TableId.APPLICATION_ROLE_TABLE, 1, 1), "InfographicEngine");
        popup.close(PopupId.APPLICATION_ROLE_ASSIGN);
        popup.close(PopupId.APPLICATION_ROLE);
    }


    @Test(dependsOnMethods = "checkUserExists")
    public void addGroups() {
        userManager.addGroup("Admin", "Super Cow Powers.");
        userManager.addGroup("CEO", "Powers without cows.");
        userManager.addGroup("Editor", "Who can override data.");
        userManager.addGroup("Employee", "Work will make you free.");
        userManager.addGroup("HR", "Can fill up vacancies.");
        userManager.addGroup("Organization Leader", "Works with organizations teams.");
        userManager.addGroup("Practitioner", "Perform CADT tests.");
        userManager.addGroup("Speakers", "Can present a workshop.");
        userManager.addGroup("Team Leaders", "Captain of the ship.");
        Assert.assertEquals(userManager.getTotalNumberOfItems(TableId.USERS_GROUP_TABLE), 9);
    }

    @Test(dependsOnMethods = {"addGroups", "addApplicationsRoles"})
    public void assignRolesToGroups() {
        userManager.addGroupRole("Admin", "AppointmentCenter", "admin");
        userManager.addGroupRole("Admin", "BaseFormDroolsEngine", "admin");
        userManager.addGroupRole("Admin", "BiitSurveys", "user");
        userManager.addGroupRole("Admin", "CardGame", "user");
        userManager.addGroupRole("Admin", "DataTide", "admin");
        userManager.addGroupRole("Admin", "FactManager", "admin");
        userManager.addGroupRole("Admin", "FactsDashboard", "teamleader");
        userManager.addGroupRole("Admin", "FactsDashboard", "NCA");
        userManager.addGroupRole("Admin", "FactsDashboard", "xls access");
        userManager.addGroupRole("Admin", "FactsDashboard", "ceo");
        userManager.addGroupRole("Admin", "FactsDashboard", "practitioner");
        userManager.addGroupRole("Admin", "FactsDashboard", "Credibility");
        userManager.addGroupRole("Admin", "FactsDashboard", "Happiness at Work");
        userManager.addGroupRole("Admin", "FactsDashboard", "CADT");
        userManager.addGroupRole("Admin", "InfographicEngine", "admin");
        userManager.addGroupRole("Admin", "KafkaProxy", "admin");
        userManager.addGroupRole("Admin", "KnowledgeSystem", "admin");
        userManager.addGroupRole("Admin", "MetaViewerStructure", "admin");
        userManager.addGroupRole("Admin", "ProfileMatcher", "admin");
        userManager.addGroupRole("Admin", "UserManagerSystem", "admin");
        userManager.addGroupRole("Admin", "XForms", "user");

        Assert.assertEquals(userManager.getTotalRolesByGroup("Admin"), 21);

        userManager.addGroupRole("CEO", "BaseFormDroolsEngine", "user");
        userManager.addGroupRole("CEO", "FactManager", "user");
        userManager.addGroupRole("CEO", "FactsDashboard", "xls access");
        userManager.addGroupRole("CEO", "FactsDashboard", "ceo");
        userManager.addGroupRole("CEO", "KafkaProxy", "user");
        userManager.addGroupRole("CEO", "KnowledgeSystem", "user");
        userManager.addGroupRole("CEO", "UserManagerSystem", "admin");
        userManager.addGroupRole("CEO", "UserManagerSystem", "user");
        userManager.addGroupRole("CEO", "XForms", "user");

        Assert.assertEquals(userManager.getTotalRolesByGroup("CEO"), 9);

        userManager.addGroupRole("Editor", "AppointmentCenter", "editor");
        userManager.addGroupRole("Editor", "BaseFormDroolsEngine", "user");
        userManager.addGroupRole("Editor", "BiitSurveys", "user");
        userManager.addGroupRole("Editor", "CardGame", "user");
        userManager.addGroupRole("Editor", "FactManager", "user");
        userManager.addGroupRole("Editor", "FactsDashboard", "teamleader");
        userManager.addGroupRole("Editor", "FactsDashboard", "practitioner");
        userManager.addGroupRole("Editor", "FactsDashboard", "NCA");
        userManager.addGroupRole("Editor", "FactsDashboard", "xls access");
        userManager.addGroupRole("Editor", "FactsDashboard", "ceo");
        userManager.addGroupRole("Editor", "FactsDashboard", "Credibility");
        userManager.addGroupRole("Editor", "FactsDashboard", "CADT");
        userManager.addGroupRole("Editor", "FactsDashboard", "Happiness at Work");
        userManager.addGroupRole("Editor", "InfographicEngine", "user");
        userManager.addGroupRole("Editor", "KafkaProxy", "user");
        userManager.addGroupRole("Editor", "KnowledgeSystem", "user");
        userManager.addGroupRole("Editor", "MetaViewerStructure", "user");
        userManager.addGroupRole("Editor", "ProfileMatcher", "manager");
        userManager.addGroupRole("Editor", "UserManagerSystem", "editor");
        userManager.addGroupRole("Editor", "XForms", "user");

        Assert.assertEquals(userManager.getTotalRolesByGroup("Editor"), 20);

        userManager.addGroupRole("Employee", "AppointmentCenter", "user");
        userManager.addGroupRole("Employee", "BaseFormDroolsEngine", "user");
        userManager.addGroupRole("Employee", "BiitSurveys", "user");
        userManager.addGroupRole("Employee", "CardGame", "user");
        userManager.addGroupRole("Employee", "FactManager", "user");
        userManager.addGroupRole("Employee", "InfographicEngine", "user");
        userManager.addGroupRole("Employee", "KafkaProxy", "user");
        userManager.addGroupRole("Employee", "KnowledgeSystem", "user");
        userManager.addGroupRole("Employee", "MetaViewerStructure", "user");
        userManager.addGroupRole("Employee", "UserManagerSystem", "user");
        userManager.addGroupRole("Employee", "XForms", "user");

        Assert.assertEquals(userManager.getTotalRolesByGroup("Employee"), 11);

        userManager.addGroupRole("HR", "AppointmentCenter", "manager");
        userManager.addGroupRole("HR", "BaseFormDroolsEngine", "user");
        userManager.addGroupRole("HR", "FactManager", "user");
        userManager.addGroupRole("HR", "FactsDashboard", "Organization List");
        userManager.addGroupRole("HR", "FactsDashboard", "Credibility");
        userManager.addGroupRole("HR", "FactsDashboard", "CADT");
        userManager.addGroupRole("HR", "InfographicEngine", "user");
        userManager.addGroupRole("HR", "KafkaProxy", "user");
        userManager.addGroupRole("HR", "MetaViewerStructure", "admin");
        userManager.addGroupRole("HR", "ProfileMatcher", "manager");
        userManager.addGroupRole("HR", "UserManagerSystem", "user");
        userManager.addGroupRole("HR", "FactsDashboard", "xls access");

        Assert.assertEquals(userManager.getTotalRolesByGroup("HR"), 12);

        userManager.addGroupRole("Organization Leader", "FactManager", "user");
        userManager.addGroupRole("Organization Leader", "FactsDashboard", "ceo");
        userManager.addGroupRole("Organization Leader", "InfographicEngine", "user");

        Assert.assertEquals(userManager.getTotalRolesByGroup("HR"), 12);

        userManager.addGroupRole("Practitioner", "AppointmentCenter", "manager");
        userManager.addGroupRole("Practitioner", "FactsDashboard", "practitioner");
        userManager.addGroupRole("Practitioner", "InfographicEngine", "practitioner");

        Assert.assertEquals(userManager.getTotalRolesByGroup("Practitioner"), 3);

        userManager.addGroupRole("Speakers", "AppointmentCenter", "speaker");
        userManager.addGroupRole("Speakers", "UserManagerSystem", "user");

        Assert.assertEquals(userManager.getTotalRolesByGroup("Speakers"), 2);

        userManager.addGroupRole("Team Leaders", "FactManager", "user");
        userManager.addGroupRole("Team Leaders", "FactsDashboard", "teamleader");
        userManager.addGroupRole("Team Leaders", "FactsDashboard", "xls access");
        userManager.addGroupRole("Team Leaders", "InfographicEngine", "user");
        userManager.addGroupRole("Team Leaders", "MetaViewerStructure", "user");
        userManager.addGroupRole("Team Leaders", "UserManagerSystem", "user");
        userManager.addGroupRole("Team Leaders", "XForms", "user");

        Assert.assertEquals(userManager.getTotalRolesByGroup("Team Leaders"), 7);
    }

    @Test(dependsOnMethods = "assignRolesToGroups")
    public void assignUsersToGroups(){
        userManager.addUserToGroup("", "Admin");
    }


    @Test(dependsOnMethods = "assignUsersToGroups")
    public void createJwtUser() {
        userManager.addUser("jwt", "token@test.com", "System", "Token", "asd123");
        userManager.addUserRoles("jwt", "UserManagerSystem", "admin");
        userManager.addUserRoles("jwt", "FactManager", "admin");
    }


//    @AfterClass(enabled = false)
//    public void cleanup() {
//        while (userManager.getTableSize() > 1) {
//            userManager.selectTableRow("users-table", 0);
//            userManager.pressTableButton("users-table", "button-minus");
//        }
//    }
}
