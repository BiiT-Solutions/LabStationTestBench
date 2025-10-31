package com.biit.labstation.appointments;

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

import com.biit.labstation.CustomChromeDriver;
import com.biit.labstation.ToolTest;
import com.biit.labstation.components.Login;
import com.biit.labstation.components.NavBar;
import com.biit.labstation.components.Popup;
import com.biit.labstation.components.PopupId;
import com.biit.labstation.components.PopupWindow;
import com.biit.labstation.components.SnackBar;
import com.biit.labstation.components.Tab;
import com.biit.labstation.components.TabId;
import com.biit.labstation.logger.LabStationLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static com.biit.labstation.components.SnackBar.EXTERNAL_CALENDAR_PERMISSIONS_SUBSCRIBED;
import static com.biit.labstation.components.SnackBar.EXTERNAL_CALENDAR_PERMISSIONS_UNSUBSCRIBED;

@Component
public class AppointmentCenter extends ToolTest {

    private static final int EXTERNAL_PROVIDER_WINDOW_WAIT = 8000;
    private static final String MS_USER = "biit-test@outlook.es";
    private static final String MS_PASSWORD = "my-password";
    private static final String GOOGLE_USER = "biit-test@gmail.com";

    @Value("${testing.server.domain}")
    private String serverDomain;

    @Value("${appointment-center.context}")
    private String context;

    private final NavBar navBar;
    private final SnackBar snackBar;
    private final Popup popup;
    private final Tab tab;
    private final PopupWindow popupWindow;

    private final WorkshopCanvas workshopCanvas;
    private final CalendarCanvas calendarCanvas;
    private final EventCard eventCard;

    public AppointmentCenter(CustomChromeDriver customChromeDriver, Login login, Popup popup, SnackBar snackBar, WorkshopCanvas workshopCanvas, NavBar navBar,
                             CalendarCanvas calendarCanvas, EventCard eventCard, Tab tab, PopupWindow popupWindow) {
        super(customChromeDriver, login, popup);
        this.snackBar = snackBar;
        this.popup = popup;
        this.navBar = navBar;
        this.workshopCanvas = workshopCanvas;
        this.calendarCanvas = calendarCanvas;
        this.eventCard = eventCard;
        this.tab = tab;
        this.popupWindow = popupWindow;
    }

    @Override
    public void access() {
        access(serverDomain, context);
    }

    @Override
    public void logout() {
        getCustomChromeDriver().findElementWaiting(By.id("appointmentcenter-menu")).click();
        getCustomChromeDriver().findElementWaiting(By.id("appointmentcenter-menu-logout")).click();
    }

    public void selectAppointmentsOnMenu() {
        if (navBar.goTo("nav-item-appointments")) {
            ToolTest.waitComponent();
        }
    }

    public int getNumberOfWorkshops() {
        return workshopCanvas.countWorkshops();
    }

    public int getNumberOfAppointments() {
        return calendarCanvas.countAppointments();
    }

    public void createWorkshop(String title, String description, Collection<String> speakers, int duration, Integer cost, AppointmentTheme theme) {
        try {
            selectAppointmentsOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        ToolTest.waitComponent();
        workshopCanvas.addWorkshopButton().click();
        ToolTest.waitComponent();
        workshopCanvas.addWorkshop(title, description, speakers, duration, cost, theme);
        ToolTest.waitComponent();
    }

    public void editWorkshop(String sourceTitle, String title, String description, Collection<String> speakers,
                             int duration, Integer cost, AppointmentTheme theme) {
        try {
            selectAppointmentsOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        ToolTest.waitComponent();
        workshopCanvas.selectContextMenuOnWorkshop(sourceTitle, "Edit Workshop");
        workshopCanvas.editWorkshop(title, description, speakers, duration, cost, theme);
        ToolTest.waitComponent();
    }


    public void deleteWorkshop(String title) {
        try {
            selectAppointmentsOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        ToolTest.waitComponent();
        workshopCanvas.deleteWorkshop(title);
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.WORKSHOP_DELETED);
    }

    public void createAppointment(String title, String description, Collection<String> hosts, Integer cost,
                                  LocalDateTime startingTime, LocalDateTime endingTime) {
        try {
            selectAppointmentsOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        ToolTest.waitComponent();
        calendarCanvas.getAddAppointmentButton().click();
        calendarCanvas.editAppointment(title, description, hosts, cost, startingTime, endingTime);
    }

    public void deleteAppointment(String title) {
        try {
            selectAppointmentsOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        ToolTest.waitComponentOneSecond();
        calendarCanvas.deleteAppointment(title);
    }


    public void createAppointmentFromWorkshop(String sourceTitle, String title, String description, Collection<String> hosts, Integer cost,
                                              DayOfWeek day, int hour) {
        try {
            selectAppointmentsOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        ToolTest.waitComponent();

        final Actions actions = new Actions(getCustomChromeDriver().getDriver());

        final WebElement workshop = workshopCanvas.getWorkshop(sourceTitle);
        final WebElement calendarHour = calendarCanvas.getScheduleSlot(day.getValue() - 1, hour);

        actions.clickAndHold(workshop).moveToElement(calendarHour).release(calendarHour).build().perform();
        ToolTest.waitComponent();
        calendarCanvas.editAppointment(title, description, hosts, cost, null, null);
    }

    public void subscribeToAppointment(String workshopTitle, String appointmentTitle) {
        try {
            selectAppointmentsOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        ToolTest.waitComponentOneSecond();
        //Show possible workshops.
        if (workshopTitle != null) {
            workshopCanvas.getWorkshop(workshopTitle).findElement(By.className("eye")).click();
            ToolTest.waitComponentOneSecond();
        }
        final WebElement appointment = calendarCanvas.getAppointment(appointmentTitle);
        //Scroll to element.
        new Actions(getCustomChromeDriver().getDriver()).scrollToElement(appointment).perform();
        //Open Menu
        appointment.click();
        ToolTest.waitComponent();

        eventCard.subscribeButton().click();
        ToolTest.waitComponentOneSecond();
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.APPOINTMENT_SUBSCRIBED);
    }

    public List<String> getAttendees(String appointmentTitle) {
        try {
            selectAppointmentsOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        ToolTest.waitComponentOneSecond();
        final WebElement appointment = calendarCanvas.getAppointment(appointmentTitle);
        //Scroll to element.
        new Actions(getCustomChromeDriver().getDriver()).scrollToElement(appointment).perform();
        //Open Menu
        appointment.click();
        ToolTest.waitComponent();

        eventCard.editButton().click();
        ToolTest.waitComponent();

        return calendarCanvas.getAttendees();
    }


    public void unsubscribeFromAppointment(String appointmentTitle) {
        try {
            selectAppointmentsOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        ToolTest.waitComponentOneSecond();
        final WebElement appointment = calendarCanvas.getAppointment(appointmentTitle);
        //Scroll to element.
        new Actions(getCustomChromeDriver().getDriver()).scrollToElement(appointment).perform();
        //Open Menu
        appointment.click();
        ToolTest.waitComponent();

        eventCard.unsubscribeButton().click();
        ToolTest.waitComponentOneSecond();
        snackBar.checkMessage(SnackBar.Type.REGULAR, SnackBar.APPOINTMENT_UNSUBSCRIBED);
    }

    private void openLinkOptions() {
        try {
            selectAppointmentsOnMenu();
        } catch (Exception e) {
            //Already on this tab.
        }
        ToolTest.waitComponent();
        calendarCanvas.getConnectAppointmentButton().click();
    }

    public void connectToMicrosoft() {
        openLinkOptions();
        tab.selectTab(TabId.EXTERNAL_PROVIDERS, "tab-Microsoft");
        ToolTest.waitComponentOneSecond();
        try {
            getCustomChromeDriver().findElementWaiting(By.id("ms-login-button")).click();
        } catch (Exception e) {
            getCustomChromeDriver().findElementWaiting(By.id("ms-disconnect-button")).click();
            throw e;
        }

        popupWindow.switchToPopupWindow();
        ToolTest.waitComponent(EXTERNAL_PROVIDER_WINDOW_WAIT);

        //Microsoft login form.
        LabStationLogger.debug(this.getClass().getName(), "Sending MS user '{}' to Microsoft popup.", MS_USER);
        getCustomChromeDriver().findElement(By.className("form-control")).sendKeys(MS_USER);
        ToolTest.waitComponent();
        getCustomChromeDriver().findElement(By.className("win-button")).click();
        ToolTest.waitComponentFiveSecond();

        try {
            getCustomChromeDriver().findElement(By.xpath("//span[text()='Use your password']")).click();
            ToolTest.waitComponentOneSecond();
        } catch (Exception e) {
            //Maybe this options is not available.
        }

        getCustomChromeDriver().findElementWaiting(By.id("view")).findElement(By.name("passwd")).sendKeys(MS_PASSWORD);
        getCustomChromeDriver().findElement(By.xpath("//button[@type='submit']")).click();
        ToolTest.waitComponentOneSecond();

        //Stay signed in?
        getCustomChromeDriver().findElement(By.xpath("//button[@data-testId='primaryButton']")).click();
        ToolTest.waitComponentOneSecond();

        //Accept permissions
        try {
            getCustomChromeDriver().findElement(By.xpath("//button[@data-testId='appConsentPrimaryButton']")).click();
            ToolTest.waitComponent(EXTERNAL_PROVIDER_WINDOW_WAIT);
        } catch (Exception e) {
            //No always are asked. As remember the application.
        }

        popupWindow.restoreParentWindowHandler();
        snackBar.checkMessage(SnackBar.Type.REGULAR, EXTERNAL_CALENDAR_PERMISSIONS_SUBSCRIBED);
        popup.close(PopupId.SYNCHRONIZE_CALENDAR);
    }

    public void disconnectFromMicrosoft() {
        openLinkOptions();
        tab.selectTab(TabId.EXTERNAL_PROVIDERS, "tab-Microsoft");
        ToolTest.waitComponentOneSecond();
        LabStationLogger.debug(this.getClass().getName(), "Disconnecting from Microsoft.");
        getCustomChromeDriver().findElementWaiting(By.id("ms-disconnect-button")).click();
        ToolTest.waitComponent();
        LabStationLogger.debug(this.getClass().getName(), "Disconnected!");
        snackBar.checkMessage(SnackBar.Type.REGULAR, EXTERNAL_CALENDAR_PERMISSIONS_UNSUBSCRIBED);
        popup.close(PopupId.SYNCHRONIZE_CALENDAR);
    }

    public void connectToGoogle() {
        openLinkOptions();
        tab.selectTab(TabId.EXTERNAL_PROVIDERS, "tab-Google");
        ToolTest.waitComponentOneSecond();
        try {
            getCustomChromeDriver().findElementWaiting(By.id("google-login-button")).click();
        } catch (Exception e) {
            getCustomChromeDriver().findElementWaiting(By.id("google-disconnect-button")).click();
            throw e;
        }

        popupWindow.switchToPopupWindow();
        ToolTest.waitComponent(EXTERNAL_PROVIDER_WINDOW_WAIT);

        //Google login form.
        getCustomChromeDriver().findElement(By.xpath("//input[@type='email']")).sendKeys(GOOGLE_USER);
        getCustomChromeDriver().findElement(By.xpath("//button[@type='button']")).click();
        ToolTest.waitComponentOneSecond();

        //Warning about redirect
        getCustomChromeDriver().findElement(By.xpath("//span[text()='Got it']")).click();
        getCustomChromeDriver().findElement(By.xpath("//span[text()='Next']")).click();

        //Now is failing as Google detects Selenium!
        //https://stackoverflow.com/questions/63521491/this-browser-or-app-may-not-be-secure-error-has-appeared-when-i-am-trying-to-si

        popupWindow.restoreParentWindowHandler();
        snackBar.checkMessage(SnackBar.Type.REGULAR, EXTERNAL_CALENDAR_PERMISSIONS_SUBSCRIBED);
        popup.close(PopupId.SYNCHRONIZE_CALENDAR);
    }

    public void disconnectFromGoogle() {
        openLinkOptions();
        tab.selectTab(TabId.EXTERNAL_PROVIDERS, "tab-Google");
        ToolTest.waitComponentOneSecond();
        getCustomChromeDriver().findElementWaiting(By.id("google-disconnect-button")).click();
        ToolTest.waitComponent();
        snackBar.checkMessage(SnackBar.Type.REGULAR, EXTERNAL_CALENDAR_PERMISSIONS_UNSUBSCRIBED);
        popup.close(PopupId.SYNCHRONIZE_CALENDAR);
    }
}
