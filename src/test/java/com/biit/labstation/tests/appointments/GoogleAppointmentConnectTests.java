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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.biit.labstation.tests.Priorities.APPOINTMENT_MICROSOFT_PRIORITY;

@SpringBootTest
@Test(groups = "connectToGoogle", priority = APPOINTMENT_MICROSOFT_PRIORITY)
@Listeners({TestListener.class, ClassTestListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class GoogleAppointmentConnectTests extends BaseTest implements ITestWithWebDriver {

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
    public void connectToGoogle() {
        appointmentCenter.login(adminUser, adminPassword);
        appointmentCenter.connectToGoogle();
        ToolTest.waitComponentFiveSecond();
        Assert.assertEquals(calendarCanvas.countAppointments(), 7);
        appointmentCenter.logout();
    }

    @Test(dependsOnMethods = "connectToGoogle", alwaysRun = true)
    public void disconnectFromGoogle() {
        appointmentCenter.login(adminUser, adminPassword);
        ToolTest.waitComponentOneSecond();
        Assert.assertEquals(calendarCanvas.countAppointments(), 7);
        appointmentCenter.disconnectFromGoogle();
        ToolTest.waitComponentFiveSecond();
        Assert.assertEquals(calendarCanvas.countAppointments(), 0);
        appointmentCenter.logout();
    }
}
