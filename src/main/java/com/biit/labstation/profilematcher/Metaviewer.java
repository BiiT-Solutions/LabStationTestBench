package com.biit.labstation.profilematcher;

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
import org.springframework.stereotype.Component;

@Component
public class Metaviewer {

    private final CustomChromeDriver customChromeDriver;

    public Metaviewer(CustomChromeDriver customChromeDriver) {
        this.customChromeDriver = customChromeDriver;
    }

    public int countMetaviewerElements() {
        return customChromeDriver.findElementWaiting(By.id("metaviewer-board")).findElements(By.className("metaviewer-element"))
                .size();
    }

    public String getMetaviewerColor(int bulletIndex) {
        return customChromeDriver.findElementWaiting(By.id("metaviewer-board")).findElements(By.className("metaviewer-element"))
                .get(bulletIndex).getCssValue("background-color");
    }

    public void openElement(int bulletIndex) {
        customChromeDriver.findElementWaiting(By.id("metaviewer-board")).findElements(By.className("metaviewer-element"))
                .get(bulletIndex).click();
        ToolTest.waitComponent();
    }

    public String getMetaviewerElementHeader() {
        return customChromeDriver.findElementWaiting(By.id("metadata-viewer")).findElement(By.id("metaviewer-data"))
                .findElement(By.id("metaviewer-header")).findElement(By.id("title")).getText();
    }

    public String getMetaviewerElementData(int index) {
        return customChromeDriver.findElementWaiting(By.id("metadata-viewer")).findElement(By.id("metaviewer-data")).findElement(By.id("metaviewer-info"))
                .findElements(By.id("metaviewer-info-row")).get(index).findElement(By.id("field")).findElement(By.className("input-object"))
                .getAttribute("value");
    }

    public void metaviewerElementClose() {
        customChromeDriver.findElementWaiting(By.id("metadata-viewer")).findElement(By.id("metaviewer-data")).findElement(By.id("close-button")).click();
    }

    public WebElement getMetaviewerFilter(int index) {
        return customChromeDriver.findElementWaiting(By.id("metadata-filter")).findElement(By.id("metaviewer-data")).findElement(By.id("metaviewer-info"))
                .findElements(By.id("metaviewer-info-row")).get(index).findElement(By.id("field")).findElement(By.className("input-object"));
    }
}
