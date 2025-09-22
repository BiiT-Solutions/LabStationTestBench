package com.biit.labstation.components;

import com.biit.labstation.CustomChromeDriver;
import com.biit.labstation.ToolTest;
import com.biit.labstation.logger.ComponentLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Multiselect {

    private final CustomChromeDriver customChromeDriver;

    public Multiselect(CustomChromeDriver customChromeDriver) {
        this.customChromeDriver = customChromeDriver;
    }

    public void selectItem(String parent, String item) {
        ComponentLogger.debug(this.getClass().getName(), "Selecting item '{}' on parent '{}'.", item, parent);
        customChromeDriver.findElementWaiting(By.id(parent)).findElement(By.className("input-object")).click();
        final WebElement element = customChromeDriver.findElementWaiting(By.id(parent)).findElement(By.id("options"))
                .findElement(By.xpath(".//a[contains(text(), '" + item + "')]"));
        new Actions(customChromeDriver.getDriver()).scrollToElement(element).perform();
        element.click();
    }

    public void selectItem(String parent, String selector, String item) {
        ComponentLogger.debug(this.getClass().getName(), "Selecting item '{}' on dropdown '{}' in '{}'.", item, selector, parent);
        customChromeDriver.findElementWaiting(By.id(parent)).findElement(By.id(selector)).findElement(By.className("input-object")).click();
        ToolTest.waitComponent();
        final WebElement element = customChromeDriver.findElementWaiting(By.id(parent)).findElement(By.id(selector))
                .findElement(By.id("dropdown")).findElement(By.xpath(".//div[@id='content']/a[contains(text(), '" + item + "')]"));
        new Actions(customChromeDriver.getDriver()).scrollToElement(element).perform();
        element.click();
    }

    public List<String> getItems(String parent) {
        final List<WebElement> options = customChromeDriver.findElementWaiting(By.id(parent)).findElement(By.id("options"))
                .findElements(By.className("biit-checkbox"));
        return options.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public List<String> getSelectedItems(String parent) {
        //Click on the arrow.
        customChromeDriver.findElementWaiting(By.id(parent)).click();
        ToolTest.waitComponent();
        final List<WebElement> options = customChromeDriver.findElementWaiting(By.id(parent)).findElement(By.id("options"))
                .findElements(By.className("biit-checkbox"));
        final List<String> selected = new ArrayList<>();
        options.forEach(option -> {
            try {
                option.findElement(By.id("checked"));
                selected.add(option.getText().trim());
            } catch (Exception ignore) {
                //Not selected.
            }
        });
        return selected;
    }
}
