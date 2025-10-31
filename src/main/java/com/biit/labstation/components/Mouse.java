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
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Mouse {

    private final CustomChromeDriver customChromeDriver;

    public Mouse(CustomChromeDriver customChromeDriver) {
        this.customChromeDriver = customChromeDriver;
    }

    public void rightClick(WebElement webElement) {
        final Actions actions = new Actions(customChromeDriver.getDriver());
        actions.contextClick(webElement).perform();
    }

    public void selectContextMenu(WebElement element, String menuItem) {
        rightClick(element);
        ToolTest.waitComponent();
        final List<WebElement> options = customChromeDriver.findElementWaiting(By.className("ngx-contextmenu"))
                .findElements(By.className("ngx-contextmenu--item-content"));
        for (WebElement option : options) {
            if (option.getText().contains(menuItem)) {
                option.click();
                break;
            }
        }
    }
}
