package com.biit.labstation.tests.appointments;


import com.biit.labstation.ToolTest;
import com.biit.labstation.appointments.AppointmentCenter;
import com.biit.labstation.logger.ClassTestListener;
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

import static com.biit.labstation.tests.Priorities.APPOINTMENT_CENTER_WORKSHOPS_PRIORITY;

@SpringBootTest
@Test(groups = "workshops", priority = APPOINTMENT_CENTER_WORKSHOPS_PRIORITY)
@Listeners({TestListener.class, ClassTestListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class WorkshopsTests extends BaseTest implements ITestWithWebDriver {

    private static final String WORKSHOP_TITLE = "Workshop1";
    private static final String WORKSHOP_DESCRIPTION = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam fringilla ultrices urna in commodo. Mauris posuere, elit in vulputate egestas, nisl mauris tempor nulla, ut euismod nibh leo viverra tellus.";
    private static final Integer WORKSHOP_COST = 100;
    private static final Integer WORKSHOP_DURATION = 60;

    private static final String WORKSHOP_NEW_TITLE = "Workshop2";
    private static final String WORKSHOP_NEW_DESCRIPTION = "";
    private static final Integer WORKSHOP_NEW_COST = 50;
    private static final Integer WORKSHOP_NEW_DURATION = 120;


    @Value("${admin.user}")
    private String adminUser;
    @Value("${admin.password}")
    private String adminPassword;

    @Autowired
    private AppointmentCenter appointmentCenter;

    @BeforeClass
    public void setup() {
        appointmentCenter.access();
    }

    @Test
    public void createSimpleWorkshop() {
        appointmentCenter.login(adminUser, adminPassword);
        appointmentCenter.createWorkshop(WORKSHOP_TITLE, WORKSHOP_DESCRIPTION, null, WORKSHOP_DURATION, WORKSHOP_COST, null);
        ToolTest.waitComponent();
        Assert.assertEquals(appointmentCenter.getNumberOfWorkshops(), 1);
        appointmentCenter.logout();
    }

    @Test(dependsOnMethods = "createSimpleWorkshop")
    public void editWorkshop() {
        appointmentCenter.login(adminUser, adminPassword);
        appointmentCenter.editWorkshop(WORKSHOP_TITLE, WORKSHOP_NEW_TITLE, WORKSHOP_NEW_DESCRIPTION, null, WORKSHOP_NEW_DURATION, WORKSHOP_NEW_COST, null);
        ToolTest.waitComponent();
        Assert.assertEquals(appointmentCenter.getNumberOfWorkshops(), 1);
        appointmentCenter.logout();
    }


    @Test(dependsOnMethods = "editWorkshop")
    public void deleteWorkshop() {
        appointmentCenter.login(adminUser, adminPassword);
        appointmentCenter.deleteWorkshop(WORKSHOP_NEW_TITLE);
        ToolTest.waitComponent();
        Assert.assertEquals(appointmentCenter.getNumberOfWorkshops(), 0);
        appointmentCenter.logout();
    }


    @AfterClass(alwaysRun = true)
    public void cleanup() {
        try {
            appointmentCenter.logout();
        } catch (Exception e) {
            //Ignore
        }
    }


    @AfterClass(dependsOnMethods = "cleanup", alwaysRun = true)
    public void closeDriver() {
        appointmentCenter.getCustomChromeDriver().closeDriver();
    }

}
