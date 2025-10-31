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
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Set;

@Component
public class PopupWindow {

    private final CustomChromeDriver customChromeDriver;
    private String parentWindowHandler;

    public PopupWindow(CustomChromeDriver customChromeDriver) {
        this.customChromeDriver = customChromeDriver;
    }


    public void saveParentWindowHandler() {
        this.parentWindowHandler = customChromeDriver.getDriver().getWindowHandle(); // Stor
    }

    public void switchToPopupWindow() {
        saveParentWindowHandler();
        final Set<String> handles = customChromeDriver.getDriver().getWindowHandles(); // get all window handles
        final Iterator<String> iterator = handles.iterator();
        String subWindowHandler = null;
        while (iterator.hasNext()) {
            subWindowHandler = iterator.next();
        }
        if (subWindowHandler != null) {
            customChromeDriver.getDriver().switchTo().window(subWindowHandler);
        }
    }

    public void restoreParentWindowHandler() {
        if (parentWindowHandler != null) {
            customChromeDriver.getDriver().switchTo().window(parentWindowHandler);
        }
    }
}
