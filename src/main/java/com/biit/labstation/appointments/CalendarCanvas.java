package com.biit.labstation.appointments;

import com.biit.labstation.CustomChromeDriver;
import com.biit.labstation.ToolTest;
import com.biit.labstation.components.Dropdown;
import com.biit.labstation.components.Mouse;
import com.biit.labstation.components.Popup;
import com.biit.labstation.components.PopupId;
import com.biit.labstation.logger.LabStationLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

@Component
public class CalendarCanvas {

    private static final String CANVAS_ID = "calendar";
    private static final String DATE_FORMAT = "ddMMyyyy";
    private static final String TIME_FORMAT = "hhmm";

    private final CustomChromeDriver customChromeDriver;

    private final Popup popup;

    private final Dropdown dropdown;

    private final Mouse mouse;

    public CalendarCanvas(CustomChromeDriver customChromeDriver, Popup popup, Dropdown dropdown,
                          Mouse mouse) {
        this.customChromeDriver = customChromeDriver;
        this.popup = popup;
        this.dropdown = dropdown;
        this.mouse = mouse;
    }

    public WebElement getScheduleSlot(int dayOfWeek, int hour) {
        final List<WebElement> days = customChromeDriver.findElementWaiting(By.id(CANVAS_ID)).findElement(By.className("cal-day-columns"))
                .findElements(By.className("cal-day-column"));
        if (dayOfWeek > days.size() || dayOfWeek < 0) {
            return null;
        }
        final List<WebElement> hours = days.get(dayOfWeek).findElements(By.className("cal-hour"));
        return hours.get(hour);
    }

    public WebElement getAddAppointmentButton() {
        return customChromeDriver.findElementWaiting(By.id("canvas")).findElement(By.className("main")).findElement(By.id("add-appointment"));
    }

    public WebElement getConnectAppointmentButton() {
        return customChromeDriver.findElementWaiting(By.id("canvas")).findElement(By.className("main")).findElement(By.id("link-calendar"));
    }

    public void editAppointment(String title, String description, Collection<String> hosts, Integer cost, LocalDateTime startingTime,
                                LocalDateTime endingTime) {
        LabStationLogger.debug(this.getClass().getName(), "@@ Adding workshop '{}'.", title);
        if (title != null) {
            popup.findElement(PopupId.APPOINTMENT, "appointment-title").findElement(By.className("input-object")).clear();
            popup.findElement(PopupId.APPOINTMENT, "appointment-title").findElement(By.className("input-object")).sendKeys(title);
        }
        if (description != null) {
            popup.findElement(PopupId.APPOINTMENT, "appointment-description").findElement(By.className("input-object")).clear();
            popup.findElement(PopupId.APPOINTMENT, "appointment-description").findElement(By.className("input-object")).sendKeys(description);
        }
        if (hosts != null) {
            for (String host : hosts) {
                dropdown.selectItem("appointment-speakers", host);
            }
        }

        popup.findElement(PopupId.APPOINTMENT, "appointment-cost").findElement(By.className("input-object")).clear();
        if (cost != null) {
            popup.findElement(PopupId.APPOINTMENT, "appointment-cost").findElement(By.className("input-object")).sendKeys(cost.toString());
        }
        if (startingTime != null) {
            popup.findElement(PopupId.APPOINTMENT, "appointment-starting-time").findElement(By.className("input-object")).clear();
            popup.findElement(PopupId.APPOINTMENT, "appointment-starting-time").findElement(By.className("input-object")).sendKeys(
                    startingTime.format(DateTimeFormatter.ofPattern(DATE_FORMAT))
                            + Keys.TAB
                            + startingTime.format(DateTimeFormatter.ofPattern(TIME_FORMAT))
            );
        }
        if (endingTime != null) {
            popup.findElement(PopupId.APPOINTMENT, "appointment-ending-time").findElement(By.className("input-object")).clear();
            popup.findElement(PopupId.APPOINTMENT, "appointment-ending-time").findElement(By.className("input-object")).sendKeys(
                    endingTime.format(DateTimeFormatter.ofPattern(DATE_FORMAT))
                            + Keys.TAB
                            + endingTime.format(DateTimeFormatter.ofPattern(TIME_FORMAT))
            );
        }
        popup.findElement(PopupId.APPOINTMENT, "appointment-button-save").click();
    }

    public void deleteAppointment(String title) {
        selectContextMenuOnAppointment(title, "Delete Appointment");
        popup.findElement(PopupId.DELETE_APPOINTMENT, "confirm-event-delete-button").click();
        ToolTest.waitComponent();
    }

    public void selectContextMenuOnAppointment(String title, String option) {
        final WebElement appointment = getAppointment(title);
        if (appointment != null) {
            new Actions(customChromeDriver.getDriver()).scrollToElement(appointment).perform();
            mouse.selectContextMenu(appointment, option);
        }
    }

    public int countAppointments() {
        return customChromeDriver.findElementWaiting(By.id(CANVAS_ID)).findElements(By.className("cal-event")).size();
    }

    public WebElement getAppointment(String title) {
        final List<WebElement> events = customChromeDriver.findElementWaiting(By.id(CANVAS_ID)).findElements(By.className("cal-event"));
        for (WebElement element : events) {
            if (element.getText().contains("\n" + title.toUpperCase())) {
                return element;
            }
        }
        return null;
    }

    public List<String> getAttendees() {
        return dropdown.getSelectedItems("appointment-attendees");
    }
}
