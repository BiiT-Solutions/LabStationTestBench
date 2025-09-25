package com.biit.labstation.appointments;


import com.biit.labstation.CustomChromeDriver;
import com.biit.labstation.ToolTest;
import com.biit.labstation.components.Dropdown;
import com.biit.labstation.components.Mouse;
import com.biit.labstation.components.Popup;
import com.biit.labstation.components.PopupId;
import com.biit.labstation.logger.LabStationLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class WorkshopCanvas {

    private static final String CANVAS_ID = "canvas";

    private final CustomChromeDriver customChromeDriver;

    private final Popup popup;

    private final Dropdown dropdown;

    private final Mouse mouse;

    public WorkshopCanvas(CustomChromeDriver customChromeDriver, Popup popup, Dropdown dropdown, Mouse mouse) {
        this.customChromeDriver = customChromeDriver;
        this.popup = popup;
        this.dropdown = dropdown;
        this.mouse = mouse;
    }

    public WebElement addWorkshopButton() {
        return customChromeDriver.findElementWaiting(By.id(CANVAS_ID)).findElement(By.id("add-workshop-button"));
    }

    public WebElement getSearchField() {
        return customChromeDriver.findElementWaiting(By.id(CANVAS_ID)).findElement(By.id("search")).findElement(By.className("input-object"));
    }

    public void addWorkshop(String title, String description, Collection<String> speakers, int duration, Integer cost, AppointmentTheme theme) {
        LabStationLogger.debug(this.getClass().getName(), "@@ Adding workshop '{}'.", title);
        popup.findElement(PopupId.WORKSHOP, "event-title").findElement(By.className("input-object")).sendKeys(title);
        popup.findElement(PopupId.WORKSHOP, "event-description").findElement(By.className("input-object")).sendKeys(description);
        if (speakers != null) {
            for (String speaker : speakers) {
                dropdown.selectItem("event-speakers", speaker);
            }
        }
        popup.findElement(PopupId.WORKSHOP, "event-duration").findElement(By.className("input-object")).sendKeys(String.valueOf(duration));
        if (cost != null) {
            popup.findElement(PopupId.WORKSHOP, "event-cost").findElement(By.className("input-object")).sendKeys(cost.toString());
        }
        if (theme != null) {
            dropdown.selectItem("event-theme", theme.getIndex().toString());
        }
        popup.findElement(PopupId.WORKSHOP, "event-button-save").click();
    }

    public void deleteWorkshop(String title) {
        selectContextMenuOnWorkshop(title, "Delete Workshop");
        popup.findElement(PopupId.DELETE_WORKSHOP, "confirm-delete-button").click();
        ToolTest.waitComponent();
    }

    public void editWorkshop(String title, String description, Collection<String> speakers, int duration, Integer cost, AppointmentTheme theme) {
        LabStationLogger.debug(this.getClass().getName(), "@@ Adding workshop '{}'.", title);
        popup.findElement(PopupId.WORKSHOP, "event-title").findElement(By.className("input-object")).clear();
        popup.findElement(PopupId.WORKSHOP, "event-title").findElement(By.className("input-object")).sendKeys(title);
        popup.findElement(PopupId.WORKSHOP, "event-description").findElement(By.className("input-object")).clear();
        popup.findElement(PopupId.WORKSHOP, "event-description").findElement(By.className("input-object")).sendKeys(description);
        if (speakers != null) {
            for (String speaker : speakers) {
                dropdown.selectItem("event-speakers", speaker);
            }
        }
        popup.findElement(PopupId.WORKSHOP, "event-duration").findElement(By.className("input-object")).clear();
        popup.findElement(PopupId.WORKSHOP, "event-duration").findElement(By.className("input-object")).sendKeys(String.valueOf(duration));

        popup.findElement(PopupId.WORKSHOP, "event-cost").findElement(By.className("input-object")).clear();
        if (cost != null) {
            popup.findElement(PopupId.WORKSHOP, "event-cost").findElement(By.className("input-object")).sendKeys(cost.toString());
        }
        if (theme != null) {
            dropdown.selectItem("event-theme", theme.getIndex().toString());
        }
        popup.findElement(PopupId.WORKSHOP, "event-button-save").click();
    }


    public void selectContextMenuOnWorkshop(String title, String option) {
        final WebElement workshop = getWorkshop(title);
        if (workshop != null) {
            mouse.selectContextMenu(workshop, option);
        }
    }

    public WebElement getWorkshop(String title) {
        final List<WebElement> elements = customChromeDriver.findElementWaiting(By.id("workshops")).findElements(By.className("workshop"));
        for (WebElement element : elements) {
            if (element.getText().contains("\n" + title.toUpperCase())) {
                return element;
            }
        }
        return null;
    }

    public int countWorkshops() {
        return customChromeDriver.findElementWaiting(By.id("workshops")).findElements(By.className("workshop")).size();
    }
}
