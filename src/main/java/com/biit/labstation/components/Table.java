package com.biit.labstation.components;

import com.biit.labstation.CustomChromeDriver;
import com.biit.labstation.ScreenShooter;
import com.biit.labstation.ToolTest;
import com.biit.labstation.exceptions.ElementNotFoundAsExpectedException;
import com.biit.labstation.logger.ComponentLogger;
import com.biit.labstation.logger.LabStationLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static org.awaitility.Awaitility.await;

@Component
public class Table {
    private static final int WAITING_TIME_SECONDS = 3;
    public static final int CLEAR_WAIT = 20;
    private static final int SEARCH_WAIT = 1000;

    @Autowired
    private ScreenShooter screenShooter;

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
        ToolTest.waitComponent(SEARCH_WAIT);
        clearSearch(tableId);
        getSearchField(tableId).sendKeys(text);
        getSearchField(tableId).sendKeys(Keys.ENTER);
        final String content = getSearchField(tableId).getAttribute("value");
        LabStationLogger.debug(this.getClass().getName(), "Inserted search text content is '{}'.", content);
        if (!Objects.equals(text, content)) {
            LabStationLogger.warning(this.getClass().getName(), "Search text content '{}' does not match requested search '{}'.",
                    content, text);
            final String fileName = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) + "_search_failure";
            screenShooter.takeScreenshot(fileName);
        }
        ToolTest.waitComponent(SEARCH_WAIT);
    }

    public void clearSearch(TableId tableId) {
        final String content = getSearchField(tableId).getAttribute("value");
        if (content != null) {
            for (int i = 0; i < content.length(); i++) {
                getSearchField(tableId).sendKeys(Keys.BACK_SPACE);
                ToolTest.waitComponent(CLEAR_WAIT);
            }
        }
        getSearchField(tableId).sendKeys(Keys.ENTER);
    }


    public String getText(TableId tableId, int row, int column) {
        final String text = getCell(tableId, row, column).getText();
        LabStationLogger.debug(this.getClass().getName(), "Getting text '{}' from table ' {}' at row '{}' and column '{}'.", text, tableId, row, column);
        return text;
    }

    public WebElement getCell(TableId tableId, int row, int column) {
        return getRows(tableId).get(row).findElements(By.className("datatable-body-cell")).get(column);
    }

    /**
     * Search a cell content from a table, by the text value on a different column.
     *
     * @param tableId      table.
     * @param content      content to compare.
     * @param columContent column to compare.
     * @param columnToGet  content to retrieve.
     * @return
     */
    public WebElement getCell(TableId tableId, String content, int columContent, int columnToGet) {
        for (int i = 0; i < countRows(tableId); i++) {
            if (Objects.equals(getText(tableId, i, columContent), content)) {
                return getCell(tableId, i, columnToGet);
            }
        }
        throw new ElementNotFoundAsExpectedException();
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

    public void clickRow(TableId tableId, int row) {
        getCell(tableId, row, 0).click();
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
                if (Objects.equals(getText(tableId, i, column), label)) {
                    selectRow(tableId, i);
                    ComponentLogger.debug(this.getClass().getName(), "Selecting Table '{}'. Row '{}'. Column '{}'.", tableId, label, column);
                    return true;
                }
            }
            return false;
        });
    }

    public void selectRowWithoutCheckbox(TableId tableId, String label, int column) {
        search(tableId, label);
        await().atMost(Duration.ofSeconds(WAITING_TIME_SECONDS)).until(() -> {
            for (int i = 0; i < countRows(tableId); i++) {
                if (Objects.equals(getText(tableId, i, column), label)) {
                    clickRow(tableId, i);
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
        try {
            await().atMost(Duration.ofSeconds(WAITING_TIME_SECONDS)).until(() -> {
                for (int i = 0; i < countRows(tableId); i++) {
                    if (Objects.equals(getText(tableId, i, column), label)) {
                        unselectRow(tableId, i);
                        ComponentLogger.debug(this.getClass().getName(), "Unselecting Table '{}'. Row '{}'. Column '{}'.", tableId, label, column);
                        return true;
                    }
                }
                return false;
            });
        } catch (Exception e) {
            //Already unselected.
        }
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

    public int getTotalNumberOfItems(TableId tableId) {
        ToolTest.waitComponent();
        final String text;
        if (tableId == null) {
            text = customChromeDriver.findElementWaiting(By.id("biit-table")).findElement(By.id("datatable-footer"))
                    .findElement(By.id("total-number-of-items")).getText();
        } else {
            text = customChromeDriver.findElementWaiting(By.id(tableId.getId())).findElement(By.id("biit-table")).findElement(By.className("datatable-footer"))
                    .findElement(By.id("total-number-of-items")).getText();
        }
        ComponentLogger.debug(this.getClass().getName(), "Table '{}'. Total number of items '{}'.", text);
        return Integer.parseInt(text);
    }

    public int getNumberOfItemsSelected(TableId tableId) {
        ToolTest.waitComponent();
        final String text;
        if (tableId == null) {
            text = customChromeDriver.findElementWaiting(By.id("biit-table")).findElement(By.id("datatable-footer"))
                    .findElement(By.id("number-of-items-selected")).getText();
        } else {
            text = customChromeDriver.findElementWaiting(By.id(tableId.getId())).findElement(By.id("biit-table")).findElement(By.className("datatable-footer"))
                    .findElement(By.id("number-of-items-selected")).getText();
        }
        ComponentLogger.debug(this.getClass().getName(), "Table '{}'. Total items selected '{}'.", text);
        return Integer.parseInt(text);
    }

    /**
     * Basic testes for a table.
     *
     * @param tableId
     */
    public void completeTestTable(TableId tableId) {
        LabStationLogger.debug(this.getClass().getName(), "@@ Testing table '{}'.", tableId);
        if (getTotalNumberOfItems(tableId) > 1) {
            //Seelct second row
            final String itemToSearch = getText(tableId, 1, 1);
            search(tableId, itemToSearch);
            //After filtering, the element must be on the first row.
            if (!Objects.equals(itemToSearch, getText(tableId, 0, 1))) {
                throw new ElementNotFoundAsExpectedException();
            }
            //Again on the second row.
            clearSearch(tableId);
            if (!Objects.equals(itemToSearch, getText(tableId, 1, 1))) {
                throw new ElementNotFoundAsExpectedException();
            }
        }
    }

    public void pressButton(TableId tableId, String id) {
        LabStationLogger.debug(this.getClass().getName(), "Pressing '{}' button on table '{}'.", id, tableId);
        getMenuItem(tableId, id).click();
        ToolTest.waitComponent();
    }


    public void selectColumnOption(TableId tableId, String column) {
        customChromeDriver.findElementWaiting(By.id(tableId.getId())).findElement(By.id("column-selector")).click();

        try {
            customChromeDriver.findElementWaiting(By.id(tableId.getId())).findElement(By.id("options"))
                    .findElement(By.id("option-" + column)).findElement(By.id("unchecked"));
            customChromeDriver.findElementWaiting(By.id(tableId.getId())).findElement(By.id("options"))
                    .findElement(By.id("option-" + column)).click();
            ComponentLogger.debug(this.getClass().getName(), "Selecting Column '{}'.", column);
        } catch (Exception e) {
            //Already selected
        }
        customChromeDriver.findElementWaiting(By.id(tableId.getId())).findElement(By.id("column-selector")).click();
    }


    public void unselectColumnOption(TableId tableId, String column) {
        customChromeDriver.findElementWaiting(By.id(tableId.getId())).findElement(By.className("column-selector")).click();

        try {
            customChromeDriver.findElementWaiting(By.id(tableId.getId())).findElement(By.className("options"))
                    .findElement(By.className("option-" + column)).findElement(By.id("checked"));
            customChromeDriver.findElementWaiting(By.id(tableId.getId())).findElement(By.className("options"))
                    .findElement(By.className("option-" + column)).click();
            ComponentLogger.debug(this.getClass().getName(), "Selecting Column '{}'.", column);
        } catch (Exception e) {
            //Already unselected
        }
    }
}
