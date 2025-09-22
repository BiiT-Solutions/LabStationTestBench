package com.biit.labstation.components;

import com.biit.labstation.CustomChromeDriver;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Set;

@Component
public class PopupWindow {

    private final CustomChromeDriver customChromeDriver;
    private String parentWindowHandler;

    public PopupWindow(CustomChromeDriver customChromeDriver) {
        this.customChromeDriver = customChromeDriver;
    }


    public void saveParentWindowHandler() {
        this.parentWindowHandler = customChromeDriver.getDriver().getWindowHandle(); // Stor
    }

    public void switchToPopupWindow() {
        saveParentWindowHandler();
        final Set<String> handles = customChromeDriver.getDriver().getWindowHandles(); // get all window handles
        final Iterator<String> iterator = handles.iterator();
        String subWindowHandler = null;
        while (iterator.hasNext()) {
            subWindowHandler = iterator.next();
        }
        if (subWindowHandler != null) {
            customChromeDriver.getDriver().switchTo().window(subWindowHandler);
        }
    }

    public void restoreParentWindowHandler() {
        if (parentWindowHandler != null) {
            customChromeDriver.getDriver().switchTo().window(parentWindowHandler);
        }
    }
}
