package com.biit.labstation.tests.appointments;

import com.biit.labstation.appointments.AppointmentCenter;
import com.biit.labstation.components.Popup;
import com.biit.labstation.components.Tab;
import com.biit.labstation.logger.ClassTestListener;
import com.biit.labstation.logger.TestListener;
import com.biit.labstation.tests.BaseTest;
import com.biit.labstation.tests.ITestWithWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
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
    private Popup popup;

    @BeforeClass
    public void setup() {
        appointmentCenter.access();
    }

    @Test
    public void connectToMicrosoft() {
        appointmentCenter.login(adminUser, adminPassword);
        appointmentCenter.connectToMicrosoft();
        appointmentCenter.logout();
    }

    @Test(dependsOnMethods = "connectToMicrosoft", alwaysRun = true)
    public void disconnectFromMicrosoft() {
        appointmentCenter.login(adminUser, adminPassword);
        appointmentCenter.disconnectFromMicrosoft();
        appointmentCenter.logout();
    }
}
