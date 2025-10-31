package com.biit.labstation.components;

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
import com.biit.labstation.logger.ComponentLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

@Component
public class Popup {

    private final CustomChromeDriver customChromeDriver;

    public Popup(CustomChromeDriver customChromeDriver) {
        this.customChromeDriver = customChromeDriver;
    }

    public WebElement findElement(PopupId popupId, String id) {
        if (popupId != null) {
            return customChromeDriver.findElementWaiting(By.id(popupId.getId())).findElement(By.id("biit-popup"))
                    .findElement(By.id("content")).findElement(By.id(id));
        } else {
            return customChromeDriver.findElementWaiting(By.id("biit-popup")).findElement(By.id("content")).findElement(By.id(id));
        }
    }

    public void close(PopupId popupId) {
        try {
            if (popupId != null) {
                customChromeDriver.findElementWaiting(By.id(popupId.getId())).findElement(By.id("biit-popup"))
                        .findElement(By.id("header")).findElement(By.id("popup-x-button")).click();
            } else {
                customChromeDriver.findElementWaiting(By.id("biit-popup")).findElement(By.id("header")).findElement(By.id("popup-x-button")).click();
            }
            ComponentLogger.debug(this.getClass().getName(), "Closing popup '{}'.", popupId);
        } catch (Exception e) {
            //Ignore
        }
    }
}
