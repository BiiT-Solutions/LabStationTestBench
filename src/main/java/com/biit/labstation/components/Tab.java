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
import org.springframework.stereotype.Component;

@Component
public class Tab {

    private final CustomChromeDriver customChromeDriver;


    public Tab(CustomChromeDriver customChromeDriver) {
        this.customChromeDriver = customChromeDriver;
    }

    public void selectTab(TabId tabId, String element) {
        ComponentLogger.debug(this.getClass().getName(), "Selecting tab '{}' on '{}'.", element, tabId);
        customChromeDriver.findElementWaiting(By.id(tabId.getId())).findElement(By.id(element)).click();

    }
}
