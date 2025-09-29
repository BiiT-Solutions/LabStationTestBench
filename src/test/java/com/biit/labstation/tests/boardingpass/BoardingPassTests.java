package com.biit.labstation.tests.boardingpass;

import com.biit.appointment.core.models.AppointmentDTO;
import com.biit.appointment.core.models.QrCodeDTO;
import com.biit.appointment.rest.client.AppointmentCenterClient;
import com.biit.labstation.ToolTest;
import com.biit.labstation.appointments.AppointmentCenter;
import com.biit.labstation.boardingpass.BoardingPass;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.biit.labstation.tests.Priorities.BOARDING_PASS_PRIORITY;

@SpringBootTest
@Test(groups = "boardingPass", priority = BOARDING_PASS_PRIORITY)
@Listeners({TestListener.class, ClassTestListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class BoardingPassTests extends BaseTest implements ITestWithWebDriver {

    private static final String APPOINTMENT_TITLE = "BoardingPassAppointment";

    @Value("${admin.user}")
    private String adminUser;
    @Value("${admin.password}")
    private String adminPassword;

    @Autowired
    private AppointmentCenter appointmentCenter;

    @Autowired
    private BoardingPass boardingPass;

    @Autowired
    private AppointmentCenterClient appointmentCenterClient;


    @BeforeClass
    public void createAppointmentFromWorkshop() {
        appointmentCenter.login(adminUser, adminPassword);
        appointmentCenter.createWorkshop(APPOINTMENT_TITLE, "", null, 90, 10, null);
        appointmentCenter.getCustomChromeDriver().refresh();
        ToolTest.waitComponentOneSecond();
        Assert.assertEquals(appointmentCenter.getNumberOfWorkshops(), 1);

        final int previousAppointments = appointmentCenter.getNumberOfAppointments();
        appointmentCenter.createAppointmentFromWorkshop(APPOINTMENT_TITLE, APPOINTMENT_TITLE, "", null, 10,
                LocalDateTime.now().getDayOfWeek(), LocalDateTime.now().getHour() - 1);
        ToolTest.waitComponentOneSecond();
        Assert.assertEquals(appointmentCenter.getNumberOfAppointments(), previousAppointments + 1);
        appointmentCenter.logout();
    }


    @Test
    public void checkAppointmentContentFromOrganizer() {
        boardingPass.login(adminUser, adminPassword);
        ToolTest.waitComponentOneSecond();
        Assert.assertEquals(boardingPass.getEvents().size(), 1);
        boardingPass.enterEvent(APPOINTMENT_TITLE);
        boardingPass.logout();
    }


    @Test(dependsOnMethods = {"checkAppointmentContentFromOrganizer"})
    public void noAppointmentToSelect() {
        boardingPass.login(OrganizationAdminDashboardTests.IN_ORG_USER_NAME, OrganizationAdminDashboardTests.IN_ORG_USER_PASSWORD);
        ToolTest.waitComponentOneSecond();
        boardingPass.checkNoEventsAvailable();
        boardingPass.logout();
    }

    @Test(dependsOnMethods = "noAppointmentToSelect")
    public void checkNobodyIsAttending() {
        boardingPass.login(adminUser, adminPassword);
        ToolTest.waitComponentOneSecond();
        boardingPass.enterEvent(APPOINTMENT_TITLE);
        ToolTest.waitComponentOneSecond();
        Assert.assertEquals(boardingPass.getAttendanceNumber(), "0 / 0");
        boardingPass.logout();
    }


    @Test(dependsOnMethods = "checkNobodyIsAttending")
    public void subscribeToAppointment() {
        appointmentCenter.login(OrganizationAdminDashboardTests.IN_ORG_USER_NAME, OrganizationAdminDashboardTests.IN_ORG_USER_PASSWORD);
        appointmentCenter.subscribeToAppointment(null, APPOINTMENT_TITLE);
        appointmentCenter.logout();
    }


    @Test(dependsOnMethods = "subscribeToAppointment")
    public void checkAppointmentContentFromAttendee() {
        boardingPass.login(OrganizationAdminDashboardTests.IN_ORG_USER_NAME, OrganizationAdminDashboardTests.IN_ORG_USER_PASSWORD);
        ToolTest.waitComponentFiveSecond();
        Assert.assertEquals(boardingPass.getUserName(), "Jack O'Neill");
        Assert.assertEquals(boardingPass.getOrganizationName(), "Umbrella");
        Assert.assertTrue(boardingPass.getUserDate().length() > 10);
        Assert.assertFalse(boardingPass.getUserAddress().isEmpty());
        Assert.assertNotNull(boardingPass.getQrCode());
        boardingPass.logout();
    }


    @Test(dependsOnMethods = "checkAppointmentContentFromAttendee")
    public void checkAttendeeHasNoArrived() {
        boardingPass.login(adminUser, adminPassword);
        ToolTest.waitComponentOneSecond();
        boardingPass.enterEvent(APPOINTMENT_TITLE);
        ToolTest.waitComponentOneSecond();
        Assert.assertEquals(boardingPass.getAttendanceNumber(), "0 / 1");
        boardingPass.logout();
    }


    @Test(dependsOnMethods = "checkAppointmentContentFromAttendee")
    public void checkListOfAttendance() {
        boardingPass.login(adminUser, adminPassword);
        ToolTest.waitComponentOneSecond();
        boardingPass.enterEvent(APPOINTMENT_TITLE);
        ToolTest.waitComponentOneSecond();
        boardingPass.goToAttendanceList();
        Assert.assertFalse(boardingPass.isAttending("Jack O'Neill"));
        boardingPass.logout();
    }


    @Test(dependsOnMethods = {"checkAttendeeHasNoArrived", "checkListOfAttendance"})
    public void attendAppointment() {
        final List<AppointmentDTO> appointmentDTOs = appointmentCenterClient.findAll();
        final Optional<QrCodeDTO> qrCode = appointmentCenterClient.getQrCode(appointmentDTOs.get(0).getId(),
                appointmentDTOs.get(0).getAttendees().iterator().next());
        Assert.assertTrue(qrCode.isPresent());
        appointmentCenterClient.attendByQrCode(appointmentDTOs.get(0).getId(), qrCode.get());
    }


    @Test(dependsOnMethods = {"attendAppointment"})
    public void checkAttendeeIsAccepted() {
        boardingPass.login(adminUser, adminPassword);
        ToolTest.waitComponentOneSecond();
        boardingPass.enterEvent(APPOINTMENT_TITLE);
        ToolTest.waitComponentOneSecond();
        Assert.assertEquals(boardingPass.getAttendanceNumber(), "1 / 1");
        boardingPass.goToAttendanceList();
        Assert.assertTrue(boardingPass.isAttending("Jack O'Neill"));
        boardingPass.logout();
    }


    @Test(dependsOnMethods = "checkAttendeeIsAccepted")
    public void checkAppointmentContentFromAttendeeWhenAttending() {
//        boardingPass.access();
        boardingPass.login(OrganizationAdminDashboardTests.IN_ORG_USER_NAME, OrganizationAdminDashboardTests.IN_ORG_USER_PASSWORD);
        ToolTest.waitComponentFiveSecond();
        Assert.assertEquals(boardingPass.getUserName(), "Jack O'Neill");
        Assert.assertEquals(boardingPass.getOrganizationName(), "Umbrella");
        Assert.assertTrue(boardingPass.getUserDate().length() > 10);
        Assert.assertFalse(boardingPass.getUserAddress().isEmpty());
        Assert.assertNotNull(boardingPass.getQrCode());
        boardingPass.join(APPOINTMENT_TITLE);
        ToolTest.waitComponentOneSecond();
        boardingPass.logout();
    }


    @AfterClass
    public void deleteAppointment() {
        appointmentCenter.login(adminUser, adminPassword);
        appointmentCenter.deleteAppointment(APPOINTMENT_TITLE);
        appointmentCenter.logout();
    }


    @AfterClass
    public void deleteWorkshop() {
        appointmentCenter.login(adminUser, adminPassword);
        appointmentCenter.deleteWorkshop(APPOINTMENT_TITLE);
        appointmentCenter.logout();
    }

    @AfterClass(dependsOnMethods = {"deleteWorkshop", "deleteAppointment"}, alwaysRun = true)
    public void closeDriver() {
        appointmentCenter.getCustomChromeDriver().closeDriver();
    }
}
