package com.biit.labstation.tests.usermanager;

import com.biit.labstation.ToolTest;
import com.biit.labstation.components.Popup;
import com.biit.labstation.components.PopupId;
import com.biit.labstation.components.SnackBar;
import com.biit.labstation.components.Table;
import com.biit.labstation.components.TableId;
import com.biit.labstation.logger.ClassTestListener;
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

import static com.biit.labstation.tests.Priorities.ORGANIZATION_ADMIN_TESTS_PRIORITY;
import static com.biit.labstation.tests.usermanager.OrganizationsTests.ORGANIZATION_NAME;
import static com.biit.labstation.tests.usermanager.OrganizationsTests.TEAM_NAME;

@SpringBootTest
@Test(groups = "organizationAdmin", priority = ORGANIZATION_ADMIN_TESTS_PRIORITY)
@Listeners({TestListener.class, ClassTestListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class OrganizationAdminTests extends BaseTest implements ITestWithWebDriver {

    //Must be on lower case for the test.
    public static final String ORGANIZATION_ADMIN_USER = "orgadmin@test.com";
    public static final String ORGANIZATION_ADMIN_PASSWORD = "my-password";

    public static final String EMPLOYEE_USER_NAME = "orgemployee@test.com";
    public static final String EMPLOYEE_USER_PASSWORD = "my-password";

    private static final String SECOND_ORGANIZATION_NAME = "Wonka";
    private static final String SECOND_ORGANIZATION_DESCRIPTION = "Sweets and chocolate.";

    private static final String SECOND_TEAM_NAME = "Oompa Loompas";
    private static final String SECOND_TEAM_DESCRIPTION = "Hardest workers ever!";

    @Autowired
    private UserManager userManager;

    @Autowired
    private SnackBar snackBar;

    @Autowired
    private Popup popup;

    @Autowired
    private Table table;

    @Value("${admin.user}")
    private String adminUser;
    @Value("${admin.password}")
    private String adminPassword;

    @BeforeClass
    public void setup() {
        userManager.access();
    }


    @Test
    public void addOrganizationAdminBackendServiceRoles() {
        userManager.login(adminUser, adminPassword);

        //Appointment Center
//        userManager.addServiceRole("AppointmentCenter", "organization_admin");
//        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        //UserManagerSystem
        userManager.addServiceRole("UserManagerSystem", "organization_admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        //ProfileMatcher
        userManager.addServiceRole("ProfileMatcher", "organization_admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        //FactManager
        userManager.addServiceRole("FactManager", "organization_admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.logout();
    }


    @Test
    public void addRoles() {
        userManager.login(adminUser, adminPassword);

        userManager.addRole("Organization Admin", "Can manage organization user's and data.");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.logout();
    }


    @Test(dependsOnMethods = {"addOrganizationAdminBackendServiceRoles", "addRoles"})
    public void addApplicationsRoles() {
        userManager.login(adminUser, adminPassword);

        userManager.addApplicationRole("UserManagerSystem", "Organization Admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("UserManagerSystem", "Organization Admin", "UserManagerSystem", "organization_admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.addApplicationRole("FactsDashboard", "Organization Admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("FactsDashboard", "Organization Admin", "UserManagerSystem", "organization_admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("FactsDashboard", "Organization Admin", "FactManager", "organization_admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);


        userManager.addApplicationRole("ProfileMatcher", "Organization Admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("ProfileMatcher", "Organization Admin", "UserManagerSystem", "organization_admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.linkApplicationRoleWithServiceRole("ProfileMatcher", "Organization Admin", "ProfileMatcher", "organization_admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.logout();
    }


    @Test
    public void addGroups() {
        userManager.login(adminUser, adminPassword);

        userManager.addGroup("Organization Admins", "Works with organizations teams.");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.logout();
    }


    @Test(dependsOnMethods = {"addGroups", "addApplicationsRoles"})
    public void assignRolesToGroups() {
        userManager.login(adminUser, adminPassword);

        userManager.addGroupRole("Organization Admins", "UserManagerSystem", "Organization Admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.addGroupRole("Organization Admins", "FactsDashboard", "Organization Admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.addGroupRole("Organization Admins", "ProfileMatcher", "Organization Admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.logout();
    }


    @Test(dependsOnMethods = {"assignRolesToGroups"})
    public void createOrganizationAdminUser() {
        userManager.login(adminUser, adminPassword);

        userManager.addUser(ORGANIZATION_ADMIN_USER, ORGANIZATION_ADMIN_USER, "George", "Hammond", ORGANIZATION_ADMIN_PASSWORD);
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.USER_CREATED);
        userManager.addUserRoles(ORGANIZATION_ADMIN_USER, "UserManagerSystem", "Organization Admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addUserRoles(ORGANIZATION_ADMIN_USER, "FactsDashboard", "Organization Admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
        userManager.addUserRoles(ORGANIZATION_ADMIN_USER, "ProfileMatcher", "Organization Admin");
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

        userManager.logout();
    }

    @Test(dependsOnMethods = {"createOrganizationAdminUser"})
    public void organizationAdminUserCanLogIn() {
        userManager.login(ORGANIZATION_ADMIN_USER, ORGANIZATION_ADMIN_PASSWORD);
        ToolTest.waitComponent();
        //No user linked to the organization yet.
        Assert.assertEquals(table.getTotalNumberOfItems(TableId.USERS_TABLE), 0);
        userManager.logout();
    }

    @Test(dependsOnMethods = {"organizationAdminUserCanLogIn"})
    public void addingAdminUserToOrganizationAllowsToSeeUsers() {
        userManager.login(adminUser, adminPassword);
        userManager.addUserToTeam(ORGANIZATION_ADMIN_USER, TEAM_NAME, ORGANIZATION_NAME);
        userManager.logout();

        userManager.login(ORGANIZATION_ADMIN_USER, ORGANIZATION_ADMIN_PASSWORD);
        ToolTest.waitComponent();
        //No user linked to the organization yet.
        Assert.assertEquals(table.getTotalNumberOfItems(TableId.USERS_TABLE), 1);
        userManager.logout();
    }


    @Test(dependsOnMethods = {"addingAdminUserToOrganizationAllowsToSeeUsers"})
    public void orgAdminCannotDeleteSuperAdmin() {
        userManager.login(adminUser, adminPassword);
        userManager.addUserToTeam(adminUser, TEAM_NAME, ORGANIZATION_NAME);
        userManager.logout();

        userManager.login(ORGANIZATION_ADMIN_USER, ORGANIZATION_ADMIN_PASSWORD);
        ToolTest.waitComponent();
        //No user linked to the organization yet.
        Assert.assertEquals(table.getTotalNumberOfItems(TableId.USERS_TABLE), 2);

        userManager.deleteUser(adminUser);
        snackBar.checkMessage(SnackBar.Type.ERROR, SnackBar.ACTION_NOT_ALLOWED);
        userManager.logout();
    }

    @Test(dependsOnMethods = {"orgAdminCannotDeleteSuperAdmin"})
    public void orgAdminUsersAddedAreVisibleByOrgAdmin() {
        userManager.login(ORGANIZATION_ADMIN_USER, ORGANIZATION_ADMIN_PASSWORD);
        ToolTest.waitComponent();
        //No user linked to the organization yet.
        Assert.assertEquals(table.getTotalNumberOfItems(TableId.USERS_TABLE), 2);

        //Create new user by the Organization Admin.
        userManager.addUser(EMPLOYEE_USER_NAME, EMPLOYEE_USER_NAME, "Daniel", "Jackson", EMPLOYEE_USER_PASSWORD);
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.USER_CREATED);
        ToolTest.waitComponent();
        //Must be visible despite not being in any team.
        Assert.assertEquals(table.getTotalNumberOfItems(TableId.USERS_TABLE), 3);

        userManager.logout();
    }

    @Test(dependsOnMethods = {"orgAdminUsersAddedAreVisibleByOrgAdmin"})
    public void orgAdminUsersCanAssignTeamMembers() {
        userManager.login(ORGANIZATION_ADMIN_USER, ORGANIZATION_ADMIN_PASSWORD);
        userManager.addUserToTeamByOrgAdmin(EMPLOYEE_USER_NAME, TEAM_NAME);
        userManager.logout();
    }

    @Test(dependsOnMethods = {"orgAdminUsersAddedAreVisibleByOrgAdmin"})
    public void orgAdminCannotBeInTwoOrganizations() {
        //Create second org.
        userManager.login(adminUser, adminPassword);
        try {
            userManager.addOrganization(SECOND_ORGANIZATION_NAME, SECOND_ORGANIZATION_DESCRIPTION);
            snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);
            userManager.addTeamToOrganization(SECOND_ORGANIZATION_NAME, SECOND_TEAM_NAME, SECOND_TEAM_DESCRIPTION);
            snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.REQUEST_SUCCESSFUL);

            //Assign user to second org.
            try {
                userManager.selectOrganizationsOnMenu();
            } catch (Exception e) {
                //Already on this tab.
            }
            table.selectRow(TableId.ORGANIZATION_TABLE, SECOND_ORGANIZATION_NAME, 1);
            table.pressButton(TableId.ORGANIZATION_TABLE, "button-team");
            ToolTest.waitComponent();
            table.selectRow(TableId.TEAM_TABLE, SECOND_TEAM_NAME, 1);
            table.pressButton(TableId.TEAM_TABLE, "organization-team-button-user");
            table.selectRow(TableId.USERS_TABLE, ORGANIZATION_ADMIN_USER, 4);
            table.pressButton(TableId.USERS_TABLE, "assign-user");
            popup.findElement(PopupId.CONFIRMATION_ASSIGN, "confirm-assign-button").click();
            snackBar.checkMessage(SnackBar.Type.ERROR, SnackBar.DATA_CONFLICTS);
            snackBar.closeLatest();
            popup.close(PopupId.ASSIGN_USER_POPUP);
            popup.close(PopupId.TEAM);
        } finally {
            try {
                table.unselectRow(TableId.ORGANIZATION_TABLE, SECOND_ORGANIZATION_NAME, 1);
            } catch (Exception e) {
                //Ignore.
            }
            userManager.deleteOrganization(SECOND_ORGANIZATION_NAME);
            userManager.logout();
            snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.LOGGED_OUT);
        }
    }

    @AfterClass(alwaysRun = true)
    public void cleanup() {
        try {
            userManager.login(adminUser, adminPassword);
            userManager.deleteUser(EMPLOYEE_USER_NAME);
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
