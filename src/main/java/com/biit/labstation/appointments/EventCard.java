package com.biit.labstation.appointments;

import com.biit.labstation.CustomChromeDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

@Component
public class EventCard {

    private static final String ID = "event-card";

    private final CustomChromeDriver customChromeDriver;

    public EventCard(CustomChromeDriver customChromeDriver) {
        this.customChromeDriver = customChromeDriver;
    }

    public WebElement subscribeButton() {
        return customChromeDriver.findElementWaiting(By.id(ID)).findElement(By.id("subscribe-button"));
    }

    public WebElement unsubscribeButton() {
        return customChromeDriver.findElementWaiting(By.id(ID)).findElement(By.id("unsubscribe-button"));
    }

    public WebElement editButton() {
        return customChromeDriver.findElementWaiting(By.id(ID)).findElement(By.id("edit-button"));
    }
}
