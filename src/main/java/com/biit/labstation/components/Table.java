package com.biit.labstation.components;

import com.biit.labstation.CustomChromeDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.awaitility.Awaitility.await;

@Component
public class Table {
    private static final int WAITING_TIME_SECONDS = 3;

    private final CustomChromeDriver customChromeDriver;

    public Table(CustomChromeDriver customChromeDriver) {
        this.customChromeDriver = customChromeDriver;
    }

    public WebElement getMenuItem(String id) {
        return customChromeDriver.findElementWaiting(By.className("action-bar")).findElement(By.id(id));
    }

    public WebElement getSearchField() {
        return customChromeDriver.findElementWaiting(By.id("biit-table")).findElement(By.id("options")).findElement(By.id("search"))
                .findElement(By.id("input"));
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
        try {
            getCell(row, 0).findElement(By.id("biit-checkbox")).findElement(By.id("unchecked"));
            getCell(row, 0).findElement(By.id("biit-checkbox")).click();
        } catch (Exception e) {
            //Already selected
        }
    }

    public void selectRow(String label, int column) {
        await().atMost(Duration.ofSeconds(WAITING_TIME_SECONDS)).until(() -> {
            for (int i = 0; i < countRows(); i++) {
                if (Objects.equals(getContent(i, column), label)) {
                    selectRow(i);
                    return true;
                }
            }
            return false;
        });
    }

    public void unselectRow(int row) {
        try {
            getCell(row, 0).findElement(By.id("biit-checkbox")).findElement(By.id("checked"));
            getCell(row, 0).findElement(By.id("biit-checkbox")).click();
        } catch (Exception e) {
            //Already unselected
        }
    }

    public void selectLastRow() {
        selectRow(countRows() - 1);
    }

    public String getCurrentPage() {
        return customChromeDriver.findElementWaiting(By.id("biit-table")).findElement(By.id("page-selector")).findElement(By.id("current-page")).getText();
    }

    public String getTotalPages() {
        return customChromeDriver.findElementWaiting(By.id("biit-table")).findElement(By.id("page-selector")).findElement(By.id("total-pages")).getText();
    }


    public void goFirstPage() {
        customChromeDriver.findElementWaiting(By.id("biit-table")).findElement(By.id("page-selector")).findElement(By.id("arrow-first")).click();
    }

    public void goPreviousPage() {
        customChromeDriver.findElementWaiting(By.id("biit-table")).findElement(By.id("page-selector")).findElement(By.id("arrow-previous")).click();
    }

    public void goNextPage() {
        customChromeDriver.findElementWaiting(By.id("biit-table")).findElement(By.id("page-selector")).findElement(By.id("arrow-next")).click();
    }

    public void goLastPage() {
        customChromeDriver.findElementWaiting(By.id("biit-table")).findElement(By.id("page-selector")).findElement(By.id("arrow-last")).click();
    }
}
