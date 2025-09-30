package com.biit.labstation.tests.nca;

import com.biit.labstation.biitsurveys.NCA;
import com.biit.labstation.logger.ClassTestListener;
import com.biit.labstation.logger.TestListener;
import com.biit.labstation.tests.BaseTest;
import com.biit.labstation.tests.ITestWithWebDriver;
import com.biit.labstation.tests.dashboard.OrganizationAdminDashboardTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.biit.labstation.tests.Priorities.NCA_PRIORITY;

@SpringBootTest
@Test(groups = "nca", priority = NCA_PRIORITY)
@Listeners({TestListener.class, ClassTestListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class NcaTests extends BaseTest implements ITestWithWebDriver {

    @Autowired
    private NCA nca;

    @Test
    public void fillUpNCA() {
        nca.login(OrganizationAdminDashboardTests.IN_ORG_USER_NAME, OrganizationAdminDashboardTests.IN_ORG_USER_PASSWORD);
        nca.fillUpNCA(0.8);
    }


    @AfterClass(alwaysRun = true)
    public void closeDriver() {
        nca.getCustomChromeDriver().closeDriver();
    }
}
