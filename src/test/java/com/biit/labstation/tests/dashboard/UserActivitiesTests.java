package com.biit.labstation.tests.dashboard;

import com.biit.labstation.ToolTest;
import com.biit.labstation.components.Table;
import com.biit.labstation.components.TableId;
import com.biit.labstation.dashboard.Dashboard;
import com.biit.labstation.dashboard.cadt.CadtCustomerPage1;
import com.biit.labstation.logger.ClassTestListener;
import com.biit.labstation.logger.TestListener;
import com.biit.labstation.profilematcher.Metaviewer;
import com.biit.labstation.tests.BaseTest;
import com.biit.labstation.tests.ITestWithWebDriver;
import com.biit.labstation.tests.usermanager.OrganizationsTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.biit.labstation.tests.Priorities.ACTIVITY_HISTORIC_PRIORITY;

@SpringBootTest
@Test(groups = "activities", priority = ACTIVITY_HISTORIC_PRIORITY)
@Listeners({TestListener.class, ClassTestListener.class})
public class UserActivitiesTests extends BaseTest implements ITestWithWebDriver {

    private static final String CADT_PAGE_1 = "CADT_Customer_1";

    @Autowired
    private Dashboard dashboard;

    @Value("${admin.user}")
    private String adminUser;
    @Value("${admin.password}")
    private String adminPassword;

    @Autowired
    private Table table;

    @Autowired
    private Metaviewer metaviewer;


    @BeforeClass
    public void setup() {
        dashboard.access();
    }

    @Test
    public void checkActivities() {
        dashboard.login(adminUser, adminPassword);
        dashboard.selectActivities();
        ToolTest.waitComponent(2000);
        Assert.assertEquals(table.countRows(TableId.ACTIVITY_TABLE), 7);

        table.search(TableId.ACTIVITY_TABLE, CADT_PAGE_1);
        table.selectRowWithoutCheckbox(TableId.ACTIVITY_TABLE, CADT_PAGE_1, 0);
        ToolTest.waitComponent(1000);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage1.SVG_ID, "text", CadtCustomerPage1.PRIMARY_POWER_TEXT).getText(), "I am a global citizen.");

        dashboard.logout();
    }

    @Test
    public void checkHistorical() {
        dashboard.login(adminUser, adminPassword);
        dashboard.selectHistoric();
        ToolTest.waitComponent(2000);

        Assert.assertEquals(metaviewer.countMetaviewerElements(), 1);

        metaviewer.openElement(0);
        ToolTest.waitComponent();
        Assert.assertEquals(metaviewer.getMetaviewerElementHeader(), "CADT");
        Assert.assertEquals(metaviewer.getMetaviewerElementData(3), OrganizationsTests.ORGANIZATION_NAME);
        metaviewer.metaviewerElementClose();

        dashboard.logout();
    }

    @AfterClass()
    public void closeDriver() {
        dashboard.getCustomChromeDriver().closeDriver();
    }
}
