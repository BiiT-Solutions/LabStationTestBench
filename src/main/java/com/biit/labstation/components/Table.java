package com.biit.labstation.components;

import com.biit.labstation.CustomChromeDriver;
import com.biit.labstation.logger.ComponentLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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

    public void search(TableId tableId, String text) {
        getSearchField(tableId).sendKeys(text);
        getSearchField(tableId).sendKeys(Keys.RETURN);
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
            ComponentLogger.debug(this.getClass().getName(), "Selecting Table '{}'. Row '{}'.", tableId, row);

        } catch (Exception e) {
            //Already selected
        }
    }

    public void selectRow(TableId tableId, String label, int column) {
        search(tableId, label);
        await().atMost(Duration.ofSeconds(WAITING_TIME_SECONDS)).until(() -> {
            for (int i = 0; i < countRows(tableId); i++) {
                if (Objects.equals(getContent(tableId, i, column), label)) {
                    selectRow(tableId, i);
                    ComponentLogger.debug(this.getClass().getName(), "Selecting Table '{}'. Row '{}'. Column '{}'.", tableId, label, column);
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
            ComponentLogger.debug(this.getClass().getName(), "Unselecting Table '{}'. Row '{}'.", tableId, row);
        } catch (Exception e) {
            //Already unselected
        }
    }

    public void unselectRow(TableId tableId, String label, int column) {
        await().atMost(Duration.ofSeconds(WAITING_TIME_SECONDS)).until(() -> {
            for (int i = 0; i < countRows(tableId); i++) {
                if (Objects.equals(getContent(tableId, i, column), label)) {
                    unselectRow(tableId, i);
                    ComponentLogger.debug(this.getClass().getName(), "Unselecting Table '{}'. Row '{}'. Column '{}'.", tableId, label, column);
                    return true;
                }
            }
            return false;
        });
    }

    public void selectLastRow(TableId tableId) {
        selectRow(tableId, countRows(tableId) - 1);
    }

    public String getCurrentPage(TableId tableId) {
        if (tableId == null) {
            return customChromeDriver.findElementWaiting(By.id("biit-table")).findElement(By.id("page-selector")).findElement(By.id("current-page")).getText();
        }
        final String text = customChromeDriver.findElementWaiting(By.id(tableId.getId())).findElement(By.id("biit-table")).findElement(By.id("page-selector"))
                .findElement(By.id("current-page")).getText();
        ComponentLogger.debug(this.getClass().getName(), "Table '{}'. Current Page '{}'", tableId, text);
        return text;
    }

    public String getTotalPages(TableId tableId) {
        if (tableId == null) {
            return customChromeDriver.findElementWaiting(By.id("biit-table")).findElement(By.id("page-selector")).findElement(By.id("total-pages")).getText();
        }
        final String text = customChromeDriver.findElementWaiting(By.id(tableId.getId())).findElement(By.id("biit-table")).findElement(By.id("page-selector"))
                .findElement(By.id("total-pages")).getText();
        ComponentLogger.debug(this.getClass().getName(), "Table '{}'. Total Pages '{}'", tableId, text);
        return text;
    }


    public void goFirstPage(TableId tableId) {
        if (tableId == null) {
            customChromeDriver.findElementWaiting(By.id("biit-table")).findElement(By.id("page-selector")).findElement(By.id("arrow-first")).click();
        } else {
            customChromeDriver.findElementWaiting(By.id(tableId.getId())).findElement(By.id("biit-table")).findElement(By.id("page-selector"))
                    .findElement(By.id("arrow-first")).click();
        }
        ComponentLogger.debug(this.getClass().getName(), "Table '{}' selects first page.", tableId);
    }

    public void goPreviousPage(TableId tableId) {
        if (tableId == null) {
            customChromeDriver.findElementWaiting(By.id("biit-table")).findElement(By.id("page-selector")).findElement(By.id("arrow-previous")).click();
        } else {
            customChromeDriver.findElementWaiting(By.id(tableId.getId())).findElement(By.id("biit-table")).findElement(By.id("page-selector"))
                    .findElement(By.id("arrow-previous")).click();
        }
        ComponentLogger.debug(this.getClass().getName(), "Table '{}' selects previous page.", tableId);
    }

    public void goNextPage(TableId tableId) {
        if (tableId == null) {
            customChromeDriver.findElementWaiting(By.id("biit-table")).findElement(By.id("page-selector")).findElement(By.id("arrow-next")).click();
        } else {
            customChromeDriver.findElementWaiting(By.id(tableId.getId())).findElement(By.id("biit-table"))
                    .findElement(By.id("page-selector")).findElement(By.id("arrow-next")).click();
        }
        ComponentLogger.debug(this.getClass().getName(), "Table '{}' selects next page.", tableId);
    }

    public void goLastPage(TableId tableId) {
        if (tableId == null) {
            customChromeDriver.findElementWaiting(By.id("biit-table")).findElement(By.id("page-selector")).findElement(By.id("arrow-last")).click();
        } else {
            customChromeDriver.findElementWaiting(By.id(tableId.getId())).findElement(By.id("biit-table"))
                    .findElement(By.id("page-selector")).findElement(By.id("arrow-last")).click();
        }
        ComponentLogger.debug(this.getClass().getName(), "Table '{}' selects last page.", tableId);
    }

    public String getTotalNumberOfItems(TableId tableId) {
        final String text;
        if (tableId == null) {
            text = customChromeDriver.findElementWaiting(By.id("biit-table")).findElement(By.id("datatable-footer"))
                    .findElement(By.id("total-number-of-items")).getText();
        } else {
            text = customChromeDriver.findElementWaiting(By.id(tableId.getId())).findElement(By.id("biit-table")).findElement(By.className("datatable-footer"))
                    .findElement(By.id("total-number-of-items")).getText();
        }
        ComponentLogger.debug(this.getClass().getName(), "Table '{}'. Total number of items '{}'.", text);
        return text;
    }

    public String getNumberOfItemsSelected(TableId tableId) {
        final String text;
        if (tableId == null) {
            text = customChromeDriver.findElementWaiting(By.id("biit-table")).findElement(By.id("datatable-footer"))
                    .findElement(By.id("number-of-items-selected")).getText();
        } else {
            text = customChromeDriver.findElementWaiting(By.id(tableId.getId())).findElement(By.id("biit-table")).findElement(By.className("datatable-footer"))
                    .findElement(By.id("number-of-items-selected")).getText();
        }
        ComponentLogger.debug(this.getClass().getName(), "Table '{}'. Total items selected '{}'.", text);
        return text;
    }
}
