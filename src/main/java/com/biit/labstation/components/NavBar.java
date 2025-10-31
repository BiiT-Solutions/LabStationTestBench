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
import com.biit.labstation.ToolTest;
import com.biit.labstation.logger.ComponentLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

@Component
public class NavBar {

    private final CustomChromeDriver customChromeDriver;

    public NavBar(CustomChromeDriver customChromeDriver) {
        this.customChromeDriver = customChromeDriver;
    }

    public boolean goTo(String id) {
        final String classAttribute = getMenuItem(id).getAttribute("class");
        if (classAttribute == null || !classAttribute.contains("active")) {
            ComponentLogger.debug(this.getClass().getName(), "Pressing '{}' on navigation menu.", id);
            getMenuItem(id).click();
            return true;
        } else {
            ComponentLogger.debug(this.getClass().getName(), "Menu item '{}' already selected.", id);
        }
        return false;
    }

    public boolean goTo(String menuId, String submenuId) {
        ComponentLogger.debug(this.getClass().getName(), "Pressing '{}' and '{}' on navigation menu.", menuId, submenuId);
        final WebElement menuItem = getSubmenuItem(menuId, submenuId);
        if (menuItem != null) {
            menuItem.click();
            return true;
        } else {
            ComponentLogger.debug(this.getClass().getName(), "Menu item '{}' not found", submenuId);
        }
        return false;
    }

    public WebElement getMenuItem(String id) {
        return customChromeDriver.findElementWaiting(By.id("nav-menu")).findElement(By.id(id));
    }

    public WebElement getSubmenuItem(String menuId, String submenuId) {
        customChromeDriver.findElementWaiting(By.id("nav-menu")).findElement(By.id(menuId)).click();
        ToolTest.waitComponent();
        return customChromeDriver.findElementWaiting(By.id(submenuId));
    }
}
