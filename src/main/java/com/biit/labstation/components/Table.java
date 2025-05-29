package com.biit.labstation.components;

import com.biit.labstation.CustomChromeDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.time.Duration;
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

    public WebElement getMenuItem(TableId tableId, String id) {
        return customChromeDriver.findElementWaiting(By.id(tableId.getId())).findElement(By.className("action-bar")).findElement(By.id(id));
    }

    public WebElement getSearchField(TableId tableId) {
        return customChromeDriver.findElementWaiting(By.id(tableId.getId())).findElement(By.id("biit-table"))
                .findElement(By.id("options")).findElement(By.id("search")).findElement(By.id("input"));
    }

    public String getContent(TableId tableId, int row, int column) {
        return getCell(tableId, row, column).findElement(By.xpath(".//span[contains(@class, 'ng-star-inserted')]")).getText();
    }

    public WebElement getCell(TableId tableId, int row, int column) {
        return getRows(tableId).get(row).findElements(By.className("datatable-body-cell")).get(column);
    }

    public WebElement getRow(TableId tableId, int row) {
        return getRows(tableId).get(row);
    }

    public int countRows(TableId tableId) {
        return getRows(tableId).size();
    }

    public List<WebElement> getRows(TableId tableId) {
        final WebElement table = customChromeDriver.findElementWaiting(By.id(tableId.getId())).findElement(By.id("biit-table"));
        return table.findElements(By.className("datatable-body-row"));
    }

    public void selectRow(TableId tableId, int row) {
        try {
            getCell(tableId, row, 0).findElement(By.id("biit-checkbox")).findElement(By.id("unchecked"));
            getCell(tableId, row, 0).findElement(By.id("biit-checkbox")).click();
        } catch (Exception e) {
            //Already selected
        }
    }

    public void selectRow(TableId tableId, String label, int column) {
        await().atMost(Duration.ofSeconds(WAITING_TIME_SECONDS)).until(() -> {
            for (int i = 0; i < countRows(tableId); i++) {
                if (Objects.equals(getContent(tableId, i, column), label)) {
                    selectRow(tableId, i);
                    return true;
                }
            }
            return false;
        });
    }

    public void unselectRow(TableId tableId, int row) {
        try {
            getCell(tableId, row, 0).findElement(By.id("biit-checkbox")).findElement(By.id("checked"));
            getCell(tableId, row, 0).findElement(By.id("biit-checkbox")).click();
        } catch (Exception e) {
            //Already unselected
        }
    }

    public void selectLastRow(TableId tableId) {
        selectRow(tableId, countRows(tableId) - 1);
    }

    public String getCurrentPage(TableId tableId) {
        if (tableId == null) {
            return customChromeDriver.findElementWaiting(By.id("biit-table")).findElement(By.id("page-selector")).findElement(By.id("current-page")).getText();
        }
        return customChromeDriver.findElementWaiting(By.id(tableId.getId())).findElement(By.id("biit-table")).findElement(By.id("page-selector"))
                .findElement(By.id("current-page")).getText();
    }

    public String getTotalPages(TableId tableId) {
        if (tableId == null) {
            return customChromeDriver.findElementWaiting(By.id("biit-table")).findElement(By.id("page-selector")).findElement(By.id("total-pages")).getText();
        }
        return customChromeDriver.findElementWaiting(By.id(tableId.getId())).findElement(By.id("biit-table")).findElement(By.id("page-selector"))
                .findElement(By.id("total-pages")).getText();
    }


    public void goFirstPage(TableId tableId) {
        if (tableId == null) {
            customChromeDriver.findElementWaiting(By.id("biit-table")).findElement(By.id("page-selector")).findElement(By.id("arrow-first")).click();
        } else {
            customChromeDriver.findElementWaiting(By.id(tableId.getId())).findElement(By.id("biit-table")).findElement(By.id("page-selector"))
                    .findElement(By.id("arrow-first")).click();
        }
    }

    public void goPreviousPage(TableId tableId) {
        if (tableId == null) {
            customChromeDriver.findElementWaiting(By.id("biit-table")).findElement(By.id("page-selector")).findElement(By.id("arrow-previous")).click();
        } else {
            customChromeDriver.findElementWaiting(By.id(tableId.getId())).findElement(By.id("biit-table")).findElement(By.id("page-selector"))
                    .findElement(By.id("arrow-previous")).click();
        }
    }

    public void goNextPage(TableId tableId) {
        if (tableId == null) {
            customChromeDriver.findElementWaiting(By.id("biit-table")).findElement(By.id("page-selector")).findElement(By.id("arrow-next")).click();
        } else {
            customChromeDriver.findElementWaiting(By.id(tableId.getId())).findElement(By.id("biit-table")).findElement(By.id("page-selector")).findElement(By.id("arrow-next")).click();
        }

    }

    public void goLastPage(TableId tableId) {
        if (tableId == null) {
            customChromeDriver.findElementWaiting(By.id("biit-table")).findElement(By.id("page-selector")).findElement(By.id("arrow-last")).click();
        } else {
            customChromeDriver.findElementWaiting(By.id(tableId.getId())).findElement(By.id("biit-table")).findElement(By.id("page-selector")).findElement(By.id("arrow-last")).click();
        }
    }
}
