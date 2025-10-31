package com.biit.labstation.boardingpass;

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
import com.biit.labstation.components.Login;
import com.biit.labstation.components.Popup;
import com.biit.labstation.components.Table;
import com.biit.labstation.components.TableId;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BoardingPass extends ToolTest {

    @Value("${testing.server.domain}")
    private String serverDomain;

    @Value("${boarding-pass.context}")
    private String context;

    private final Table table;

    protected BoardingPass(CustomChromeDriver customChromeDriver, Login login, Popup popup, Table table) {
        super(customChromeDriver, login, popup);
        this.table = table;
    }

    @Override
    public void access() {
        access(serverDomain, context);
    }

    @Override
    public void logout() {
        getCustomChromeDriver().findElementWaiting(By.id("boarding-pass-menu")).click();
        getCustomChromeDriver().findElementWaiting(By.id("boarding-pass-menu-logout")).click();
    }

    public String getOrganizationName() {
        return getCustomChromeDriver().findElementWaiting(By.id("info")).findElement(By.id("user-organization")).findElement(By.className("data")).getText();
    }

    public String getUserName() {
        return getCustomChromeDriver().findElementWaiting(By.id("info")).findElement(By.id("user-name")).findElement(By.className("data")).getText();
    }

    public String getUserDate() {
        return getCustomChromeDriver().findElementWaiting(By.id("info")).findElement(By.id("user-date")).findElement(By.className("data")).getText();
    }

    public String getUserAddress() {
        return getCustomChromeDriver().findElementWaiting(By.id("info")).findElement(By.id("user-address")).findElement(By.className("data")).getText();
    }

    public WebElement getQrCode() {
        return getCustomChromeDriver().findElementWaiting(By.id("qr"));
    }

    public WebElement getPreviousAppointmentButton() {
        return getCustomChromeDriver().findElementWaiting(By.id("left-button"));
    }

    public WebElement getNextAppointmentButton() {
        return getCustomChromeDriver().findElementWaiting(By.id("right-button"));
    }

    public WebElement getJoinAppointmentButton() {
        return getCustomChromeDriver().findElementWaiting(By.id("join-button"));
    }

    public List<WebElement> getEvents() {
        return getCustomChromeDriver().findElementWaiting(By.id("canvas")).findElement(By.id("events")).findElements(By.className("event-item"));
    }

    public void enterEvent(String appointmentTitle) {
        final List<WebElement> events = getEvents();
        for (WebElement event : events) {
            if (event.getText().contains(appointmentTitle.toUpperCase() + "\n")) {
                event.findElement(By.id("open-scanner")).click();
            }
        }
    }

    public void checkNoEventsAvailable() {
        getCustomChromeDriver().findElementWaiting(By.id("canvas")).findElement(By.className("no-activities-wrapper"))
                .findElements(By.className("title"));
    }

    public String getAttendanceNumber() {
        return getCustomChromeDriver().findElementWaiting(By.id("bottom-info")).findElement(By.id("attendance-number")).getText();
    }

    public void goToAttendanceList() {
        getCustomChromeDriver().findElementWaiting(By.id("bottom-menu")).findElement(By.id("list-button")).click();
        ToolTest.waitComponentOneSecond();
    }

    public boolean isAttending(String user) {
        //ABSENT
        return table.getCell(TableId.ATTENDANCE_TABLE, user, 0, 1).getText().contains("CHECKED IN");
    }

}
