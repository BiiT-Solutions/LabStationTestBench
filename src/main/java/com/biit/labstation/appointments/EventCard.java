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
