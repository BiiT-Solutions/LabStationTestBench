package com.biit.labstation.tests.appointments;

/*-
 * #%L
 * Lab Station Test Bench
 * %%
 * Copyright (C) 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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
