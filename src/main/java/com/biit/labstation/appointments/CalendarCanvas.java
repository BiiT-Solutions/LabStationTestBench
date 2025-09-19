package com.biit.labstation.appointments;

import com.biit.labstation.CustomChromeDriver;
import com.biit.labstation.components.Dropdown;
import com.biit.labstation.components.Popup;
import com.biit.labstation.components.PopupId;
import com.biit.labstation.logger.LabStationLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class CalendarCanvas {

    private static final String CANVAS_ID = "calendar";

    private final CustomChromeDriver customChromeDriver;

    private final Popup popup;

    private final Dropdown dropdown;

    public CalendarCanvas(CustomChromeDriver customChromeDriver, Popup popup, Dropdown dropdown) {
        this.customChromeDriver = customChromeDriver;
        this.popup = popup;
        this.dropdown = dropdown;
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

    public void editAppointment(String title, String description, Collection<String> hosts, Integer cost) {
        LabStationLogger.debug(this.getClass().getName(), "@@ Adding workshop '{}'.", title);
        popup.findElement(PopupId.APPOINTMENT, "appointment-title").findElement(By.className("input-object")).clear();
        popup.findElement(PopupId.APPOINTMENT, "appointment-title").findElement(By.className("input-object")).sendKeys(title);
        popup.findElement(PopupId.APPOINTMENT, "appointment-description").findElement(By.className("input-object")).clear();
        popup.findElement(PopupId.APPOINTMENT, "appointment-description").findElement(By.className("input-object")).sendKeys(description);
        if (hosts != null) {
            for (String host : hosts) {
                dropdown.selectItem("appointment-speakers", host);
            }
        }

        popup.findElement(PopupId.APPOINTMENT, "appointment-cost").findElement(By.className("input-object")).clear();
        if (cost != null) {
            popup.findElement(PopupId.APPOINTMENT, "appointment-cost").findElement(By.className("input-object")).sendKeys(cost.toString());
        }
        popup.findElement(PopupId.APPOINTMENT, "appointment-button-save").click();
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
}
