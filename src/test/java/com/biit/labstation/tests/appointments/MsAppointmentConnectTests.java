package com.biit.labstation.tests.appointments;

import com.biit.labstation.ToolTest;
import com.biit.labstation.appointments.AppointmentCenter;
import com.biit.labstation.appointments.CalendarCanvas;
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

import static com.biit.labstation.tests.Priorities.APPOINTMENT_MICROSOFT_PRIORITY;

@SpringBootTest
@Test(groups = "connectToMs", priority = APPOINTMENT_MICROSOFT_PRIORITY)
@Listeners({TestListener.class, ClassTestListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class MsAppointmentConnectTests extends BaseTest implements ITestWithWebDriver {

    @Value("${admin.user}")
    private String adminUser;
    @Value("${admin.password}")
    private String adminPassword;

    @Autowired
    private AppointmentCenter appointmentCenter;

    @Autowired
    private CalendarCanvas calendarCanvas;

    @BeforeClass
    public void setup() {
        appointmentCenter.access();
    }

    @Test
    public void connectToMicrosoft() {
        appointmentCenter.login(adminUser, adminPassword);
        ToolTest.waitComponentThreeSecond();
        int previousAppointments = calendarCanvas.countAppointments();
        appointmentCenter.connectToMicrosoft();
        ToolTest.waitComponentFiveSecond();
        Assert.assertEquals(calendarCanvas.countAppointments(), previousAppointments + 7);
        appointmentCenter.logout();
    }


    @Test(dependsOnMethods = "connectToMicrosoft", alwaysRun = true)
    public void disconnectFromMicrosoft() {
        appointmentCenter.login(adminUser, adminPassword);
        ToolTest.waitComponentFiveSecond();
        int previousAppointments = calendarCanvas.countAppointments();
        appointmentCenter.disconnectFromMicrosoft();
        ToolTest.waitComponentFiveSecond();
        Assert.assertEquals(calendarCanvas.countAppointments(), previousAppointments - 7);
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
