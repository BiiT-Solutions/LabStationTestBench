package com.biit.labstation.tests.appointments;


import com.biit.labstation.ToolTest;
import com.biit.labstation.appointments.AppointmentCenter;
import com.biit.labstation.components.Popup;
import com.biit.labstation.logger.ClassTestListener;
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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

import static com.biit.labstation.tests.Priorities.APPOINTMENT_CENTER_WORKSHOPS_PRIORITY;

@SpringBootTest
@Test(groups = "workshops", priority = APPOINTMENT_CENTER_WORKSHOPS_PRIORITY)
@Listeners({TestListener.class, ClassTestListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AppointmentTests extends BaseTest implements ITestWithWebDriver {

    private static final String WORKSHOP_TITLE = "Workshop1";
    private static final String WORKSHOP_DESCRIPTION = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam fringilla ultrices urna in commodo. Mauris posuere, elit in vulputate egestas, nisl mauris tempor nulla, ut euismod nibh leo viverra tellus.";
    private static final Integer WORKSHOP_COST = 100;
    private static final Integer WORKSHOP_DURATION = 60;

    private static final String WORKSHOP_NEW_TITLE = "Workshop2";
    private static final String WORKSHOP_NEW_DESCRIPTION = "";
    private static final Integer WORKSHOP_NEW_COST = 50;
    private static final Integer WORKSHOP_NEW_DURATION = 120;

    private static final String APPOINTMENT_NEW_TITLE = "Appointment1";
    private static final String APPOINTMENT_NEW_DESCRIPTION = "This is an appointment.";
    private static final Integer APPOINTMENT_NEW_COST = 75;


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
    public void createSimpleWorkshop() {
        appointmentCenter.login(adminUser, adminPassword);
        appointmentCenter.createWorkshop(WORKSHOP_TITLE, WORKSHOP_DESCRIPTION, null, WORKSHOP_DURATION, WORKSHOP_COST, null);
        ToolTest.waitComponentOneSecond();
        Assert.assertEquals(appointmentCenter.getNumberOfWorkshops(), 1);
        appointmentCenter.logout();
    }


    @Test(dependsOnMethods = "createSimpleWorkshop")
    public void createAppointmentFromWorkshop() {
        appointmentCenter.login(adminUser, adminPassword);
        Assert.assertEquals(appointmentCenter.getNumberOfAppointments(), 0);
        appointmentCenter.createAppointmentFromWorkshop(WORKSHOP_TITLE, APPOINTMENT_NEW_TITLE, APPOINTMENT_NEW_DESCRIPTION, null, APPOINTMENT_NEW_COST,
                DayOfWeek.MONDAY, 12);
        ToolTest.waitComponentOneSecond();
        Assert.assertEquals(appointmentCenter.getNumberOfAppointments(), 1);
        appointmentCenter.logout();
    }


    @Test(dependsOnMethods = "createAppointmentFromWorkshop")
    public void subscribeToAppointment() {
        appointmentCenter.login(OrganizationAdminDashboardTests.IN_ORG_USER_NAME, OrganizationAdminDashboardTests.IN_ORG_USER_PASSWORD);
        appointmentCenter.subscribeToAppointment(WORKSHOP_TITLE, APPOINTMENT_NEW_TITLE);
        appointmentCenter.logout();
    }


    @Test(dependsOnMethods = "subscribeToAppointment")
    public void checkPeopleSubscribed() {
        appointmentCenter.login(adminUser, adminPassword);
        Assert.assertEquals(appointmentCenter.getAttendees(APPOINTMENT_NEW_TITLE).size(), 1);
        popup.close(null);
        appointmentCenter.logout();
    }


    @Test(dependsOnMethods = "subscribeToAppointment")
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
        Assert.assertEquals(appointmentCenter.getNumberOfAppointments(), 1);
        appointmentCenter.logout();
    }

    @Test(dependsOnMethods = "deleteWorkshop")
    public void unsubscribeFromAppointment() {
        appointmentCenter.login(OrganizationAdminDashboardTests.IN_ORG_USER_NAME, OrganizationAdminDashboardTests.IN_ORG_USER_PASSWORD);
        appointmentCenter.unsubscribeFromAppointment(APPOINTMENT_NEW_TITLE);
        appointmentCenter.logout();
    }

    @Test(dependsOnMethods = "unsubscribeFromAppointment")
    public void deleteWorkshopAppointment() {
        appointmentCenter.login(adminUser, adminPassword);
        appointmentCenter.deleteAppointment(APPOINTMENT_NEW_TITLE);
        appointmentCenter.logout();
    }

    @Test(dependsOnMethods = "deleteWorkshopAppointment")
    public void addManualAppointment() {
        appointmentCenter.login(adminUser, adminPassword);
        appointmentCenter.createAppointment("TestAppointment", null, null, 15, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        appointmentCenter.logout();
    }

    @Test(dependsOnMethods = "addManualAppointment")
    public void deleteManualAppointment() {
        appointmentCenter.login(adminUser, adminPassword);
        appointmentCenter.deleteAppointment("TestAppointment");
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
