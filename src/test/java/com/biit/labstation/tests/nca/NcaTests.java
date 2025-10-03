package com.biit.labstation.tests.nca;

import com.biit.labstation.ToolTest;
import com.biit.labstation.biitsurveys.NCA;
import com.biit.labstation.dashboard.Dashboard;
import com.biit.labstation.dashboard.NcaHeatmapRow;
import com.biit.labstation.logger.ClassTestListener;
import com.biit.labstation.logger.LabStationLogger;
import com.biit.labstation.logger.TestListener;
import com.biit.labstation.tests.BaseTest;
import com.biit.labstation.tests.ITestWithWebDriver;
import com.biit.labstation.tests.dashboard.OrganizationAdminDashboardTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.biit.labstation.tests.Priorities.NCA_PRIORITY;

@SpringBootTest
@Test(groups = "nca", priority = NCA_PRIORITY)
@Listeners({TestListener.class, ClassTestListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class NcaTests extends BaseTest implements ITestWithWebDriver {

    public static final int ADMIN_USER_COLUMN = 0;

    @Autowired
    private NCA nca;

    @Autowired
    private Dashboard dashboard;

    @Value("${admin.user}")
    private String adminUser;
    @Value("${admin.password}")
    private String adminPassword;

    @Test
    public void fillUpNCA() {
        nca.login(OrganizationAdminDashboardTests.IN_ORG_USER_NAME, OrganizationAdminDashboardTests.IN_ORG_USER_PASSWORD);
        nca.fillUpNCA(0.8);
        nca.logout();
    }


    @Test(dependsOnMethods = "fillUpNCA")
    public void checkNCA() {
        //Wait Drools.
        ToolTest.waitComponent(30000);
        dashboard.login(adminUser, adminPassword);
        dashboard.selectNcaOverviewOnMenu();

        LabStationLogger.info(this.getClass(), "@@ Checking NCA overview");
        ToolTest.waitComponent();

        Assert.assertFalse(dashboard.getHeatMapValue(ADMIN_USER_COLUMN, NcaHeatmapRow.ADAPTABILITY).isBlank());
        Assert.assertFalse(dashboard.getHeatMapValue(ADMIN_USER_COLUMN, NcaHeatmapRow.PROACTIVITY).isBlank());
        Assert.assertFalse(dashboard.getHeatMapValue(ADMIN_USER_COLUMN, NcaHeatmapRow.ANALYSIS).isBlank());
        Assert.assertFalse(dashboard.getHeatMapValue(ADMIN_USER_COLUMN, NcaHeatmapRow.INSPIRATION).isBlank());
        Assert.assertFalse(dashboard.getHeatMapValue(ADMIN_USER_COLUMN, NcaHeatmapRow.BONDING).isBlank());
        Assert.assertFalse(dashboard.getHeatMapValue(ADMIN_USER_COLUMN, NcaHeatmapRow.COMMUNICATION).isBlank());
        Assert.assertFalse(dashboard.getHeatMapValue(ADMIN_USER_COLUMN, NcaHeatmapRow.STRENGTH).isBlank());
        Assert.assertFalse(dashboard.getHeatMapValue(ADMIN_USER_COLUMN, NcaHeatmapRow.SOCIALIZATION).isBlank());
        Assert.assertFalse(dashboard.getHeatMapValue(ADMIN_USER_COLUMN, NcaHeatmapRow.PROCESS_DESIGN).isBlank());
        Assert.assertFalse(dashboard.getHeatMapValue(ADMIN_USER_COLUMN, NcaHeatmapRow.UNIVERSAL).isBlank());
        Assert.assertFalse(dashboard.getHeatMapValue(ADMIN_USER_COLUMN, NcaHeatmapRow.RESPONSIBILITY).isBlank());
        Assert.assertFalse(dashboard.getHeatMapValue(ADMIN_USER_COLUMN, NcaHeatmapRow.VISION).isBlank());

        dashboard.logout();
    }


    @AfterClass(alwaysRun = true)
    public void closeDriver() {
        nca.getCustomChromeDriver().closeDriver();
    }
}
