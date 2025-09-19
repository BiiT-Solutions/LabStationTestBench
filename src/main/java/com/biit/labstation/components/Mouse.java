package com.biit.labstation.components;

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
