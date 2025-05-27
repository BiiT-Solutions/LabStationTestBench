package com.biit.labstation.components;

import com.biit.labstation.CustomChromeDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Table {

    private final CustomChromeDriver customChromeDriver;

    public Table(CustomChromeDriver customChromeDriver) {
        this.customChromeDriver = customChromeDriver;
    }

    public WebElement getMenuItem(String id) {
        return customChromeDriver.findElementWaiting(By.className("action-bar")).findElement(By.id(id));
    }

    public WebElement getSearchField() {
        return customChromeDriver.findElementWaiting(By.id("biit-table")).findElement(By.id("options")).findElement(By.id("search")).findElement(By.id("input"));
    }

    public String getContent(int row, int column) {
        return getCell(row, column).findElement(By.xpath(".//span[contains(@class, 'ng-star-inserted')]")).getText();
    }

    public WebElement getCell(int row, int column) {
        return getRows().get(row).findElements(By.className("datatable-body-cell")).get(column);
    }

    public WebElement getRow(int row) {
        return getRows().get(row);
    }

    public int countRows() {
        return getRows().size();
    }

    public List<WebElement> getRows() {
        final WebElement table = customChromeDriver.findElementWaiting(By.id("biit-table"));
        if (table == null) {
            return new ArrayList<>();
        }
        return table.findElements(By.className("datatable-body-row"));
    }

    public void selectRow(int row) {
        if (!getCell(row, 0).findElement(By.id("biit-checkbox")).isSelected()) {
            getCell(row, 0).findElement(By.id("biit-checkbox")).click();
        }
    }

    public void unselectRow(int row) {
        if (getCell(row, 0).findElement(By.id("biit-checkbox")).isSelected()) {
            getCell(row, 0).findElement(By.id("biit-checkbox")).click();
        }
    }
}
