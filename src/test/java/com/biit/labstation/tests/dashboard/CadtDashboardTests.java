package com.biit.labstation.tests.dashboard;

import com.biit.labstation.dashboard.Dashboard;
import com.biit.labstation.dashboard.cadt.CadtColors;
import com.biit.labstation.dashboard.cadt.CadtCustomerPage1;
import com.biit.labstation.dashboard.cadt.CadtCustomerPage2;
import com.biit.labstation.dashboard.cadt.CadtCustomerPage3;
import com.biit.labstation.dashboard.cadt.CadtCustomerPage4;
import com.biit.labstation.logger.ClassTestListener;
import com.biit.labstation.logger.LabStationLogger;
import com.biit.labstation.logger.TestListener;
import com.biit.labstation.tests.BaseTest;
import com.biit.labstation.tests.ITestWithWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.biit.labstation.tests.Priorities.DASHBOARD_PRIORITY;

@SpringBootTest
@Test(groups = "cadtDashboard", priority = DASHBOARD_PRIORITY)
@Listeners({TestListener.class, ClassTestListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class CadtDashboardTests extends BaseTest implements ITestWithWebDriver {

    public static final int CADT_WAITING_TIME = 90000;

    public static final String ADMIN_USER_NAME = "admin@test.com";
    public static final String ADMIN_USER_PASSWORD = "asd123";

    @Autowired
    private Dashboard dashboard;

    @Value("${headless.mode}")
    private boolean headLessMode;

    @BeforeClass
    public void waitForCadt() {
        //Cadt takes time to be processed. We need to wait until it is ready
        if (headLessMode) {
            try {
                LabStationLogger.info(this.getClass(), "Waiting for cadt to be ready...");
                Thread.sleep(CADT_WAITING_TIME);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @BeforeClass
    public void setup() {
        dashboard.access();
    }

    @Test
    public void checkFirstCadtSelectionInfographic() {
        dashboard.login(ADMIN_USER_NAME, ADMIN_USER_PASSWORD);
        dashboard.selectPersonalCadtOnMenu();

        LabStationLogger.info(this.getClass(), "@@ Checking Page 1 of the report");

        //Checking texts
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage1.SVG_ID, "text", CadtCustomerPage1.PRIMARY_POWER_TEXT).getText(), "I am a global citizen.");

        //Checking colors
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage1.SVG_ID, "circle", CadtCustomerPage1.FIRST_FEMININE_ARCHETYPE).getAttribute("fill"), CadtColors.RECEPTIVE);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage1.SVG_ID, "circle", CadtCustomerPage1.FIRST_FEMININE_ARCHETYPE_FIRST_COMPETENCE).getAttribute("fill"), CadtColors.RECEPTIVE);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage1.SVG_ID, "circle", CadtCustomerPage1.FIRST_FEMININE_ARCHETYPE_SECOND_COMPETENCE).getAttribute("fill"), CadtColors.COLOR_WHITE);

        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage1.SVG_ID, "rect", CadtCustomerPage1.FIRST_MASCULINE_ARCHETYPE).getAttribute("fill"), CadtColors.LEADER);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage1.SVG_ID, "rect", CadtCustomerPage1.FIRST_MASCULINE_ARCHETYPE_FIRST_COMPETENCE).getAttribute("fill"), CadtColors.LEADER);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage1.SVG_ID, "rect", CadtCustomerPage1.FIRST_MASCULINE_ARCHETYPE_SECOND_COMPETENCE).getAttribute("fill"), CadtColors.COLOR_WHITE);

        //Checking texts
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage1.SVG_ID, "text", CadtCustomerPage1.SECONDARY_POWER_TEXT).getText(), "I am aware of my power.");

        //Checking colors
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage1.SVG_ID, "circle", CadtCustomerPage1.SECOND_FEMININE_ARCHETYPE).getAttribute("fill"), CadtColors.STRATEGIST);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage1.SVG_ID, "circle", CadtCustomerPage1.SECOND_FEMININE_ARCHETYPE_FIRST_COMPETENCE).getAttribute("fill"), CadtColors.COLOR_WHITE);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage1.SVG_ID, "circle", CadtCustomerPage1.SECOND_FEMININE_ARCHETYPE_SECOND_COMPETENCE).getAttribute("fill"), CadtColors.STRATEGIST);

        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage1.SVG_ID, "rect", CadtCustomerPage1.SECOND_MASCULINE_ARCHETYPE).getAttribute("fill"), CadtColors.SCIENTIST);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage1.SVG_ID, "rect", CadtCustomerPage1.SECOND_MASCULINE_ARCHETYPE_FIRST_COMPETENCE).getAttribute("fill"), CadtColors.SCIENTIST);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage1.SVG_ID, "rect", CadtCustomerPage1.SECOND_MASCULINE_ARCHETYPE_SECOND_COMPETENCE).getAttribute("fill"), CadtColors.COLOR_WHITE);

        LabStationLogger.info(this.getClass(), "@@ Checking Page 2 of the report");
        //Checking texts
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage2.SVG_ID, "text", CadtCustomerPage2.PRIMARY_VULNERABLE_POWER_TEXT).getText(), "Everyone rejects me.");

        //Checking colors
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage2.SVG_ID, "circle", CadtCustomerPage2.THIRD_FEMININE_ARCHETYPE).getAttribute("fill"), CadtColors.COLOR_WHITE);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage2.SVG_ID, "circle", CadtCustomerPage2.THIRD_FEMININE_ARCHETYPE_FIRST_COMPETENCE).getAttribute("fill"), CadtColors.COLOR_GREY);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage2.SVG_ID, "circle", CadtCustomerPage2.THIRD_FEMININE_ARCHETYPE_SECOND_COMPETENCE).getAttribute("fill"), CadtColors.COLOR_GREY);

        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage2.SVG_ID, "rect", CadtCustomerPage2.THIRD_MASCULINE_ARCHETYPE).getAttribute("fill"), CadtColors.COLOR_WHITE);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage2.SVG_ID, "rect", CadtCustomerPage2.THIRD_MASCULINE_ARCHETYPE_FIRST_COMPETENCE).getAttribute("fill"), CadtColors.COLOR_WHITE);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage2.SVG_ID, "rect", CadtCustomerPage2.THIRD_MASCULINE_ARCHETYPE_SECOND_COMPETENCE).getAttribute("fill"), CadtColors.COLOR_WHITE);

        //Checking texts
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage2.SVG_ID, "text", CadtCustomerPage2.SECONDARY_VULNERABLE_POWER_TEXT).getText(), "I keep distance in bonding.");

        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage2.SVG_ID, "circle", CadtCustomerPage2.FOURTH_FEMININE_ARCHETYPE).getAttribute("fill"), CadtColors.COLOR_WHITE);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage2.SVG_ID, "circle", CadtCustomerPage2.FOURTH_FEMININE_ARCHETYPE_FIRST_COMPETENCE).getAttribute("fill"), CadtColors.COLOR_WHITE);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage2.SVG_ID, "circle", CadtCustomerPage2.FOURTH_FEMININE_ARCHETYPE_SECOND_COMPETENCE).getAttribute("fill"), CadtColors.COLOR_GREY);

        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage2.SVG_ID, "rect", CadtCustomerPage2.FOURTH_MASCULINE_ARCHETYPE).getAttribute("fill"), CadtColors.COLOR_WHITE);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage2.SVG_ID, "rect", CadtCustomerPage2.FOURTH_MASCULINE_ARCHETYPE_FIRST_COMPETENCE).getAttribute("fill"), CadtColors.COLOR_WHITE);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage2.SVG_ID, "rect", CadtCustomerPage2.FOURTH_MASCULINE_ARCHETYPE_SECOND_COMPETENCE).getAttribute("fill"), CadtColors.COLOR_WHITE);

        LabStationLogger.info(this.getClass(), "@@ Checking Page 3 of the report");
        //Checking colors
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage3.SVG_ID, "rect", CadtCustomerPage3.STRUCTURE_FIRST_COMPETENCE).getAttribute("fill"), CadtColors.STRUCTURE);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage3.SVG_ID, "rect", CadtCustomerPage3.STRUCTURE_SECOND_COMPETENCE).getAttribute("fill"), CadtColors.COLOR_WHITE);

        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage3.SVG_ID, "circle", CadtCustomerPage3.INSPIRATION_FIRST_COMPETENCE).getAttribute("fill"), CadtColors.COLOR_WHITE);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage3.SVG_ID, "circle", CadtCustomerPage3.INSPIRATION_SECOND_COMPETENCE).getAttribute("fill"), CadtColors.COLOR_WHITE);

        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage3.SVG_ID, "text", CadtCustomerPage3.STRUCTURE_INSPIRATION_BALANCED_TEXT).getText(), "UNBALANCED");

        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage3.SVG_ID, "circle", CadtCustomerPage3.ADAPTABILITY_FIRST_COMPETENCE).getAttribute("fill"), CadtColors.COLOR_WHITE);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage3.SVG_ID, "circle", CadtCustomerPage3.ADAPTABILITY_SECOND_COMPETENCE).getAttribute("fill"), CadtColors.ADAPTABILITY);

        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage3.SVG_ID, "rect", CadtCustomerPage3.ACTION_FIRST_COMPETENCE).getAttribute("fill"), CadtColors.COLOR_WHITE);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage3.SVG_ID, "rect", CadtCustomerPage3.ACTION_SECOND_COMPETENCE).getAttribute("fill"), CadtColors.ACTION);

        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage3.SVG_ID, "text", CadtCustomerPage3.ADAPTABILITY_ACTION_BALANCED_TEXT).getText(), "BALANCED");


        LabStationLogger.info(this.getClass(), "@@ Checking Page 4 of the report");
        //Page 4
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage4.SVG_ID, "circle", CadtCustomerPage4.UNIVERSAL_ARCHETYPE).getAttribute("fill"), CadtColors.RECEPTIVE);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage4.SVG_ID, "circle", CadtCustomerPage4.UNIVERSAL_FIRST_COMPETENCE).getAttribute("fill"), CadtColors.RECEPTIVE);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage4.SVG_ID, "circle", CadtCustomerPage4.UNIVERSAL_SECOND_COMPETENCE).getAttribute("fill"), CadtColors.COLOR_WHITE);

        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage4.SVG_ID, "circle", CadtCustomerPage4.SOCIETY_ARCHETYPE).getAttribute("fill"), CadtColors.COLOR_WHITE);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage4.SVG_ID, "circle", CadtCustomerPage4.SOCIETY_FIRST_COMPETENCE).getAttribute("fill"), CadtColors.COLOR_GREY);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage4.SVG_ID, "circle", CadtCustomerPage4.SOCIETY_SECOND_COMPETENCE).getAttribute("fill"), CadtColors.COLOR_GREY);

        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage4.SVG_ID, "circle", CadtCustomerPage4.VISION_ARCHETYPE).getAttribute("fill"), CadtColors.COLOR_WHITE);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage4.SVG_ID, "circle", CadtCustomerPage4.VISION_FIRST_COMPETENCE).getAttribute("fill"), CadtColors.COLOR_WHITE);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage4.SVG_ID, "circle", CadtCustomerPage4.VISION_SECOND_COMPETENCE).getAttribute("fill"), CadtColors.COLOR_GREY);

        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage4.SVG_ID, "circle", CadtCustomerPage4.STRENGTH_ARCHETYPE).getAttribute("fill"), CadtColors.STRATEGIST);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage4.SVG_ID, "circle", CadtCustomerPage4.STRENGTH_FIRST_COMPETENCE).getAttribute("fill"), CadtColors.STRATEGIST);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage4.SVG_ID, "circle", CadtCustomerPage4.STRENGTH_SECOND_COMPETENCE).getAttribute("fill"), CadtColors.COLOR_WHITE);

        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage4.SVG_ID, "circle", CadtCustomerPage4.STRENGTH_ARCHETYPE).getAttribute("fill"), CadtColors.STRATEGIST);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage4.SVG_ID, "circle", CadtCustomerPage4.STRENGTH_FIRST_COMPETENCE).getAttribute("fill"), CadtColors.STRATEGIST);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage4.SVG_ID, "circle", CadtCustomerPage4.STRENGTH_SECOND_COMPETENCE).getAttribute("fill"), CadtColors.COLOR_WHITE);

        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage4.SVG_ID, "rect", CadtCustomerPage4.STRUCTURE_FIRST_COMPETENCE).getAttribute("fill"), CadtColors.STRUCTURE);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage4.SVG_ID, "rect", CadtCustomerPage4.STRUCTURE_SECOND_COMPETENCE).getAttribute("fill"), CadtColors.COLOR_WHITE);

        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage4.SVG_ID, "circle", CadtCustomerPage4.INSPIRATION_FIRST_COMPETENCE).getAttribute("fill"), CadtColors.COLOR_WHITE);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage4.SVG_ID, "circle", CadtCustomerPage4.INSPIRATION_SECOND_COMPETENCE).getAttribute("fill"), CadtColors.COLOR_WHITE);

        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage4.SVG_ID, "circle", CadtCustomerPage4.ADAPTABILITY_FIRST_COMPETENCE).getAttribute("fill"), CadtColors.COLOR_WHITE);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage4.SVG_ID, "circle", CadtCustomerPage4.ADAPTABILITY_SECOND_COMPETENCE).getAttribute("fill"), CadtColors.ADAPTABILITY);

        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage4.SVG_ID, "rect", CadtCustomerPage4.ACTION_FIRST_COMPETENCE).getAttribute("fill"), CadtColors.COLOR_WHITE);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage4.SVG_ID, "rect", CadtCustomerPage4.ACTION_SECOND_COMPETENCE).getAttribute("fill"), CadtColors.ACTION);

        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage4.SVG_ID, "rect", CadtCustomerPage4.MATERIAL_ATTACHMENT_ARCHETYPE).getAttribute("fill"), CadtColors.COLOR_WHITE);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage4.SVG_ID, "rect", CadtCustomerPage4.MATERIAL_ATTACHMENT_FIRST_COMPETENCE).getAttribute("fill"), CadtColors.COLOR_WHITE);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage4.SVG_ID, "rect", CadtCustomerPage4.MATERIAL_ATTACHMENT_SECOND_COMPETENCE).getAttribute("fill"), CadtColors.COLOR_WHITE);

        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage4.SVG_ID, "rect", CadtCustomerPage4.COMMUNICATION_ARCHETYPE).getAttribute("fill"), CadtColors.COLOR_WHITE);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage4.SVG_ID, "rect", CadtCustomerPage4.COMMUNICATION_FIRST_COMPETENCE).getAttribute("fill"), CadtColors.COLOR_WHITE);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage4.SVG_ID, "rect", CadtCustomerPage4.COMMUNICATION_SECOND_COMPETENCE).getAttribute("fill"), CadtColors.COLOR_WHITE);

        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage4.SVG_ID, "rect", CadtCustomerPage4.SELF_AWARE_ARCHETYPE).getAttribute("fill"), CadtColors.LEADER);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage4.SVG_ID, "rect", CadtCustomerPage4.SELF_AWARE_FIRST_COMPETENCE).getAttribute("fill"), CadtColors.LEADER);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage4.SVG_ID, "rect", CadtCustomerPage4.SELF_AWARE_SECOND_COMPETENCE).getAttribute("fill"), CadtColors.COLOR_WHITE);

        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage4.SVG_ID, "rect", CadtCustomerPage4.ANALYSIS_ARCHETYPE).getAttribute("fill"), CadtColors.SCIENTIST);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage4.SVG_ID, "rect", CadtCustomerPage4.ANALYSIS_FIRST_COMPETENCE).getAttribute("fill"), CadtColors.SCIENTIST);
        Assert.assertEquals(dashboard.getSvgElement(CadtCustomerPage4.SVG_ID, "rect", CadtCustomerPage4.ANALYSIS_SECOND_COMPETENCE).getAttribute("fill"), CadtColors.COLOR_WHITE);

        dashboard.logout();
    }

    @AfterClass()
    public void closeDriver() {
        dashboard.getCustomChromeDriver().closeDriver();
    }
}
