package com.biit.labstation.tests.dashboard;

import com.biit.labstation.ToolTest;
import com.biit.labstation.cardgame.Archetype;
import com.biit.labstation.cardgame.CardGame;
import com.biit.labstation.cardgame.Competence;
import com.biit.labstation.components.Table;
import com.biit.labstation.components.TableId;
import com.biit.labstation.dashboard.CadtHeatmapRow;
import com.biit.labstation.dashboard.Dashboard;
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
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.biit.labstation.tests.Priorities.ORGANIZATION_ADMIN_DASHBOARD_PRIORITY;
import static com.biit.labstation.tests.usermanager.OrganizationsTests.ORGANIZATION_NAME;
import static com.biit.labstation.tests.usermanager.OrganizationsTests.TEAM_NAME;

@SpringBootTest
@Test(groups = "organizationAdminDashboard", priority = ORGANIZATION_ADMIN_DASHBOARD_PRIORITY)
@Listeners({TestListener.class, ClassTestListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class OrganizationAdminDashboardTests extends BaseTest implements ITestWithWebDriver {

    public static final int CADT_WAITING_TIME = 90000;

    public static final String ORGANIZATION_ADMIN_USER = "orgadmin@test.com";
    public static final String ORGANIZATION_ADMIN_PASSWORD = "asd123";

    public static final String NON_ORG_USER_NAME = "nonorg@test.com";
    public static final String NON_ORG_USER_PASSWORD = "asd123";

    public static final String IN_ORG_USER_NAME = "inorg@test.com";
    public static final String IN_ORG_USER_PASSWORD = "asd123";

    public static final int ADMIN_USER_COLUMN = 0;
    public static final int NON_ORG_USER_COLUMN = 1;
    public static final int IN_ORG_USER_COLUMN = 2;

    @Autowired
    private Dashboard dashboard;

    @Autowired
    private UserManager userManager;

    @Autowired
    private CardGame cardGame;

    @Autowired
    private Table table;

    @Value("${admin.user}")
    private String adminUser;
    @Value("${admin.password}")
    private String adminPassword;

    @Test
    public void createTestUsers() {
        userManager.access();
        userManager.login(adminUser, adminPassword);
        userManager.addUser(NON_ORG_USER_NAME, NON_ORG_USER_NAME, "Imhotep", "Ptah", NON_ORG_USER_PASSWORD);

        userManager.addUser(IN_ORG_USER_NAME, IN_ORG_USER_NAME, "Jack", "O'Neill", IN_ORG_USER_PASSWORD);

        userManager.addUserToTeam(IN_ORG_USER_NAME, TEAM_NAME, ORGANIZATION_NAME);
        userManager.logout();
    }

    @Test(dependsOnMethods = "createTestUsers")
    public void fillUpCadtForNonOrgUser() {
        cardGame.access();
        cardGame.login(NON_ORG_USER_NAME, NON_ORG_USER_PASSWORD);
        cardGame.closeWelcomePage();

        LabStationLogger.info(this.getClass().getName(), "Selecting feminine archetypes for Non Org User.");
        cardGame.chooseArchetypesByDragAndDrop(Archetype.RECEPTIVE, Archetype.INNOVATOR, Archetype.STRATEGIST);

        LabStationLogger.info(this.getClass().getName(), "Selecting masculine cards for Non Org User.");
        cardGame.chooseArchetypesByClick(Archetype.TRADESMAN, Archetype.SCIENTIST, Archetype.BANKER);

        LabStationLogger.info(this.getClass().getName(), "Selecting competence cards for Non Org User.");
        cardGame.chooseCompetences(Competence.INTERPERSONAL_SENSITIVITY, Competence.DECISIVENESS, Competence.MULTICULTURAL_SENSITIVITY,
                Competence.CLIENT_ORIENTED, Competence.DISCIPLINE, Competence.BUILDING_AND_MAINTAINING,
                Competence.PERSUASIVENESS, Competence.PLANIFICATION, Competence.BUSINESS_MINDED, Competence.GOAL_SETTING);

        Assert.assertTrue(cardGame.canSubmitTest());
        cardGame.submitTest();
        cardGame.logout();
    }


    @Test(dependsOnMethods = "fillUpCadtForNonOrgUser")
    public void fillUpCadtForInOrgUser() {
        cardGame.access();
        cardGame.login(IN_ORG_USER_NAME, IN_ORG_USER_PASSWORD);

        cardGame.closeWelcomePage();

        LabStationLogger.info(this.getClass().getName(), "Selecting feminine archetypes for In Org User.");
        cardGame.chooseArchetypesByDragAndDrop(Archetype.VISIONARY, Archetype.RECEPTIVE, Archetype.STRATEGIST);

        LabStationLogger.info(this.getClass().getName(), "Selecting masculine cards for Non In User.");
        cardGame.chooseArchetypesByClick(Archetype.LEADER, Archetype.TRADESMAN, Archetype.SCIENTIST);

        LabStationLogger.info(this.getClass().getName(), "Selecting competence cards for Non In User.");
        cardGame.chooseCompetences(Competence.TENACITY, Competence.DISCIPLINE, Competence.PROBLEM_ANALYSIS,
                Competence.FUTURE, Competence.LEADERSHIP, Competence.DECISIVENESS,
                Competence.PLANIFICATION, Competence.GOAL_SETTING, Competence.BUILDING_AND_MAINTAINING, Competence.DIRECTION);

        Assert.assertTrue(cardGame.canSubmitTest());
        cardGame.submitTest();
        cardGame.logout();
    }


    @Test(dependsOnMethods = {"fillUpCadtForNonOrgUser", "fillUpCadtForInOrgUser"})
    public void waitForCadt() {
        //Cadt takes time to be processed. We need to wait until it is ready
        try {
            LabStationLogger.info(this.getClass(), "Waiting for cadt to be ready...");
            Thread.sleep(CADT_WAITING_TIME);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Test(dependsOnMethods = {"waitForCadt"})
    public void checkDashboardByAdminCanSee3Users() {
        dashboard.access();
        dashboard.login(adminUser, adminPassword);

        dashboard.selectCadtOverviewOnMenu();

        LabStationLogger.info(this.getClass(), "@@ Checking CADT overview");

        ToolTest.waitComponent();
        Assert.assertEquals(dashboard.getHeatMapValue(ADMIN_USER_COLUMN, CadtHeatmapRow.VISIONARY), "-1");
        Assert.assertEquals(dashboard.getHeatMapValue(ADMIN_USER_COLUMN, CadtHeatmapRow.SCIENTIST), "1");
        Assert.assertEquals(dashboard.getHeatMapValue(ADMIN_USER_COLUMN, CadtHeatmapRow.TRADESMAN), "0");
        Assert.assertEquals(dashboard.getHeatMapValue(ADMIN_USER_COLUMN, CadtHeatmapRow.INNOVATOR), "-2");
        Assert.assertEquals(dashboard.getHeatMapValue(ADMIN_USER_COLUMN, CadtHeatmapRow.LEADER), "1");
        Assert.assertEquals(dashboard.getHeatMapValue(ADMIN_USER_COLUMN, CadtHeatmapRow.STRATEGIST), "1");
        Assert.assertEquals(dashboard.getHeatMapValue(ADMIN_USER_COLUMN, CadtHeatmapRow.BANKER), "0");
        Assert.assertEquals(dashboard.getHeatMapValue(ADMIN_USER_COLUMN, CadtHeatmapRow.RECEPTIVE), "1");
        Assert.assertEquals(dashboard.getHeatMapValue(ADMIN_USER_COLUMN, CadtHeatmapRow.STRUCTURE_INSPIRATION), "-1");
        Assert.assertEquals(dashboard.getHeatMapValue(ADMIN_USER_COLUMN, CadtHeatmapRow.ADAPTABILITY_ACTION), "1");

        Assert.assertEquals(dashboard.getHeatMapValue(NON_ORG_USER_COLUMN, CadtHeatmapRow.VISIONARY), "-1");
        Assert.assertEquals(dashboard.getHeatMapValue(NON_ORG_USER_COLUMN, CadtHeatmapRow.SCIENTIST), "-1");
        Assert.assertEquals(dashboard.getHeatMapValue(NON_ORG_USER_COLUMN, CadtHeatmapRow.TRADESMAN), "1");
        Assert.assertEquals(dashboard.getHeatMapValue(NON_ORG_USER_COLUMN, CadtHeatmapRow.INNOVATOR), "0");
        Assert.assertEquals(dashboard.getHeatMapValue(NON_ORG_USER_COLUMN, CadtHeatmapRow.LEADER), "0");
        Assert.assertEquals(dashboard.getHeatMapValue(NON_ORG_USER_COLUMN, CadtHeatmapRow.STRATEGIST), "1");
        Assert.assertEquals(dashboard.getHeatMapValue(NON_ORG_USER_COLUMN, CadtHeatmapRow.BANKER), "1");
        Assert.assertEquals(dashboard.getHeatMapValue(NON_ORG_USER_COLUMN, CadtHeatmapRow.RECEPTIVE), "2");
        Assert.assertEquals(dashboard.getHeatMapValue(NON_ORG_USER_COLUMN, CadtHeatmapRow.STRUCTURE_INSPIRATION), "-1");
        Assert.assertEquals(dashboard.getHeatMapValue(NON_ORG_USER_COLUMN, CadtHeatmapRow.ADAPTABILITY_ACTION), "-1");

        Assert.assertEquals(dashboard.getHeatMapValue(IN_ORG_USER_COLUMN, CadtHeatmapRow.VISIONARY), "1");
        Assert.assertEquals(dashboard.getHeatMapValue(IN_ORG_USER_COLUMN, CadtHeatmapRow.SCIENTIST), "2");
        Assert.assertEquals(dashboard.getHeatMapValue(IN_ORG_USER_COLUMN, CadtHeatmapRow.TRADESMAN), "0");
        Assert.assertEquals(dashboard.getHeatMapValue(IN_ORG_USER_COLUMN, CadtHeatmapRow.INNOVATOR), "0");
        Assert.assertEquals(dashboard.getHeatMapValue(IN_ORG_USER_COLUMN, CadtHeatmapRow.LEADER), "1");
        Assert.assertEquals(dashboard.getHeatMapValue(IN_ORG_USER_COLUMN, CadtHeatmapRow.STRATEGIST), "1");
        Assert.assertEquals(dashboard.getHeatMapValue(IN_ORG_USER_COLUMN, CadtHeatmapRow.BANKER), "-1");
        Assert.assertEquals(dashboard.getHeatMapValue(IN_ORG_USER_COLUMN, CadtHeatmapRow.RECEPTIVE), "0");
        Assert.assertEquals(dashboard.getHeatMapValue(IN_ORG_USER_COLUMN, CadtHeatmapRow.STRUCTURE_INSPIRATION), "-1");
        Assert.assertEquals(dashboard.getHeatMapValue(IN_ORG_USER_COLUMN, CadtHeatmapRow.ADAPTABILITY_ACTION), "1");
        dashboard.logout();
    }

    @Test(dependsOnMethods = {"checkDashboardByAdminCanSee3Users"})
    public void checkDashboardByOrgAdminCanOnlySee2Users() {
        dashboard.access();
        dashboard.login(ORGANIZATION_ADMIN_USER, ORGANIZATION_ADMIN_PASSWORD);

        dashboard.selectCadtOverviewOnMenu();

        Assert.assertEquals(dashboard.getHeatMapValue(ADMIN_USER_COLUMN, CadtHeatmapRow.VISIONARY), "-1");
        Assert.assertEquals(dashboard.getHeatMapValue(ADMIN_USER_COLUMN, CadtHeatmapRow.SCIENTIST), "1");
        Assert.assertEquals(dashboard.getHeatMapValue(ADMIN_USER_COLUMN, CadtHeatmapRow.TRADESMAN), "0");
        Assert.assertEquals(dashboard.getHeatMapValue(ADMIN_USER_COLUMN, CadtHeatmapRow.INNOVATOR), "-2");
        Assert.assertEquals(dashboard.getHeatMapValue(ADMIN_USER_COLUMN, CadtHeatmapRow.LEADER), "1");
        Assert.assertEquals(dashboard.getHeatMapValue(ADMIN_USER_COLUMN, CadtHeatmapRow.STRATEGIST), "1");
        Assert.assertEquals(dashboard.getHeatMapValue(ADMIN_USER_COLUMN, CadtHeatmapRow.BANKER), "0");
        Assert.assertEquals(dashboard.getHeatMapValue(ADMIN_USER_COLUMN, CadtHeatmapRow.RECEPTIVE), "1");
        Assert.assertEquals(dashboard.getHeatMapValue(ADMIN_USER_COLUMN, CadtHeatmapRow.STRUCTURE_INSPIRATION), "-1");
        Assert.assertEquals(dashboard.getHeatMapValue(ADMIN_USER_COLUMN, CadtHeatmapRow.ADAPTABILITY_ACTION), "1");

        Assert.assertEquals(dashboard.getHeatMapValue(IN_ORG_USER_COLUMN - 1, CadtHeatmapRow.VISIONARY), "1");
        Assert.assertEquals(dashboard.getHeatMapValue(IN_ORG_USER_COLUMN - 1, CadtHeatmapRow.SCIENTIST), "2");
        Assert.assertEquals(dashboard.getHeatMapValue(IN_ORG_USER_COLUMN - 1, CadtHeatmapRow.TRADESMAN), "0");
        Assert.assertEquals(dashboard.getHeatMapValue(IN_ORG_USER_COLUMN - 1, CadtHeatmapRow.INNOVATOR), "0");
        Assert.assertEquals(dashboard.getHeatMapValue(IN_ORG_USER_COLUMN - 1, CadtHeatmapRow.LEADER), "1");
        Assert.assertEquals(dashboard.getHeatMapValue(IN_ORG_USER_COLUMN - 1, CadtHeatmapRow.STRATEGIST), "1");
        Assert.assertEquals(dashboard.getHeatMapValue(IN_ORG_USER_COLUMN - 1, CadtHeatmapRow.BANKER), "-1");
        Assert.assertEquals(dashboard.getHeatMapValue(IN_ORG_USER_COLUMN - 1, CadtHeatmapRow.RECEPTIVE), "0");
        Assert.assertEquals(dashboard.getHeatMapValue(IN_ORG_USER_COLUMN - 1, CadtHeatmapRow.STRUCTURE_INSPIRATION), "-1");
        Assert.assertEquals(dashboard.getHeatMapValue(IN_ORG_USER_COLUMN - 1, CadtHeatmapRow.ADAPTABILITY_ACTION), "1");

        //No more users are visible

        dashboard.logout();
    }


    @Test(dependsOnMethods = {"createTestUsers"})
    public void orgAdminOnlyCanListOrganizationUsersOnDashboard() {
        dashboard.access();

        //OrgAdmin can only see two users.
        dashboard.login(ORGANIZATION_ADMIN_USER, ORGANIZATION_ADMIN_PASSWORD);
        ToolTest.waitComponent();
        dashboard.selectCustomerListOnMenu();
        ToolTest.waitComponent();

        //Admin, Jack and Ptah has createdOn the user organization. + OrgAdmin.
        Assert.assertEquals(table.countRows(TableId.USERS_TABLE), 4);

        dashboard.logout();
    }


    @Test(dependsOnMethods = {"createTestUsers"})
    public void adminCanListAllUsersOnDashboard() {
        dashboard.access();

        //OrgAdmin can only see two users.
        dashboard.login(adminUser, adminPassword);
        ToolTest.waitComponent();
        dashboard.selectCustomerListOnMenu();
        ToolTest.waitComponent();

        Assert.assertEquals(table.countRows(TableId.USERS_TABLE), 5);

        dashboard.logout();
    }


    @AfterClass(alwaysRun = true)
    public void cleanup() {
        try {
            userManager.login(adminUser, adminPassword);
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
