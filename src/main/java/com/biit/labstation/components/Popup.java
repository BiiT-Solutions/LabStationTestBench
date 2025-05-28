package com.biit.labstation.components;

import com.biit.labstation.CustomChromeDriver;
import com.biit.labstation.logger.LabStationLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

@Component
public class Popup {

    private final CustomChromeDriver customChromeDriver;
    private final Table table;

    public Popup(CustomChromeDriver customChromeDriver, Table table) {
        this.customChromeDriver = customChromeDriver;
        this.table = table;
    }

    public WebElement findElement(String id) {
        return customChromeDriver.findElementWaiting(By.id("biit-popup")).findElement(By.id("content")).findElement(By.id(id));
    }

    public void selectLastRow() {
        table.selectLastRow();
    }

    public void selectRow(int rowIndex) {
        table.selectRow(rowIndex);
    }

    public void close() {
        try {
            customChromeDriver.findElementWaiting(By.id("biit-popup")).findElement(By.id("header")).findElement(By.id("popup-x-button")).click();
        } catch (Exception e) {
            //Cannot close.
            LabStationLogger.errorMessage(this.getClass(), e);
        }
    }

    public void selectTableRow(String label, int column) {
        table.selectRow(label, column);
    }
}
