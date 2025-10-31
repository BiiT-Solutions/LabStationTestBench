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

import com.biit.labstation.appointments.AppointmentCenter;
import com.biit.labstation.logger.ClassTestListener;
import com.biit.labstation.logger.TestListener;
import com.biit.labstation.tests.BaseTest;
import com.biit.labstation.tests.ITestWithWebDriver;
import com.biit.labstation.usermanager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.Collections;

import static com.biit.labstation.tests.Priorities.SPEAKERS_PRIORITY;

@SpringBootTest
@Test(groups = "speakers", priority = SPEAKERS_PRIORITY)
@Listeners({TestListener.class, ClassTestListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class SpeakerTests extends BaseTest implements ITestWithWebDriver {

    private static final String SPEAKER = "nonorg@test.com";
    private static final String SPEAKER_NAME = "Imhotep Ptah";
    private static final String SPEAKER_GROUP = "Speakers";

    private static final String APPOINTMENT = "Speakers Appointment";

    @Value("${admin.user}")
    private String adminUser;
    @Value("${admin.password}")
    private String adminPassword;

    @Autowired
    private AppointmentCenter appointmentCenter;

    @Autowired
    private UserManager userManager;

    @BeforeClass
    public void setupUserManager() {
        userManager.login(adminUser, adminPassword);
        userManager.addUserToGroup(SPEAKER, SPEAKER_GROUP);
        userManager.logout();
    }

    @BeforeClass(dependsOnMethods = "setupUserManager")
    public void setupAppointmentCenter() {
        appointmentCenter.access();
    }

    @Test
    public void addSpeakerAppointment() {
        appointmentCenter.login(adminUser, adminPassword);
        appointmentCenter.createAppointment(APPOINTMENT, null, Collections.singletonList(SPEAKER_NAME), 23,
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1));
        appointmentCenter.logout();
    }

    @AfterClass
    public void deleteSpeakerAppointment() {
        try {
            appointmentCenter.access();
            appointmentCenter.login(adminUser, adminPassword);
            appointmentCenter.deleteAppointment(APPOINTMENT);
            appointmentCenter.logout();
        } catch (Exception e) {
            //Ignore
        }
    }


    @AfterClass(alwaysRun = true)
    public void removeGroup() {
        try {
            userManager.access();
            userManager.login(adminUser, adminPassword);
            userManager.removeUserFromGroup(SPEAKER, SPEAKER_GROUP);
            userManager.logout();
        } catch (Exception e) {
            //Ignore
        }
    }

    @AfterClass(dependsOnMethods = {"removeGroup", "deleteSpeakerAppointment"}, alwaysRun = true)
    public void closeDriver() {
        userManager.getCustomChromeDriver().closeDriver();
    }

}
