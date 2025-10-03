package com.biit.labstation.dashboard;

import com.biit.labstation.CustomChromeDriver;
import com.biit.labstation.ToolTest;
import com.biit.labstation.components.Login;
import com.biit.labstation.components.NavBar;
import com.biit.labstation.components.Popup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Dashboard extends ToolTest {

    @Value("${testing.server.domain}")
    private String serverDomain;

    @Value("${dashboards.context}")
    private String context;

    private final NavBar navBar;

    public Dashboard(CustomChromeDriver customChromeDriver, Login login, Popup popup, NavBar navBar) {
        super(customChromeDriver, login, popup);
        this.navBar = navBar;
    }

    @Override
    public void access() {
        access(serverDomain, context);
    }

    @Override
    public void logout() {
        getCustomChromeDriver().findElementWaiting(By.id("dashboard-menu-user")).click();
        getCustomChromeDriver().findElementWaiting(By.id("dashboard-menu-logout")).click();
    }

    public void selectActivities() {
        getCustomChromeDriver().findElementWaiting(By.id("dashboard-menu-user")).click();
        getCustomChromeDriver().findElementWaiting(By.id("dashboard-menu-activity")).click();
    }

    public void selectHistoric() {
        getCustomChromeDriver().findElementWaiting(By.id("dashboard-menu-user")).click();
        getCustomChromeDriver().findElementWaiting(By.id("dashboard-menu-historical")).click();
    }

    public void selectPersonalCadtOnMenu() {
        if (navBar.goTo("nav-item-cadt", "nav-subitem-personal")) {
            ToolTest.waitComponent();
        }
    }

    public void selectCadtOverviewOnMenu() {
        if (navBar.goTo("nav-item-cadt", "nav-subitem-overview")) {
            ToolTest.waitComponent();
        }
    }

    public void selectNcaPersonalOnMenu() {
        if (navBar.goTo("nav-item-nca", "nav-subitem-personal")) {
            ToolTest.waitComponent();
        }
    }

    public void selectNcaOverviewOnMenu() {
        if (navBar.goTo("nav-item-nca", "nav-subitem-overview")) {
            ToolTest.waitComponent();
        }
    }


    public void selectCustomerListOnMenu() {
        if (navBar.goTo("nav-item-customer-list")) {
            ToolTest.waitComponent();
        }
    }


    private WebElement findSvg(String id) {
        return getCustomChromeDriver().findElementWaiting(By.xpath("//*[name() = 'svg' and @id='" + id + "']"));
    }


    public WebElement getSvgElement(String svgId, String node, String id) {
        return getCustomChromeDriver().findElementWaiting(By.xpath("//*[name() = 'svg' and @id='" + svgId + "']"
                + "//*[local-name()='" + node + "' and @id='" + id + "']"));
    }

    public String getHeatMapValue(int column, HeatmapRow row) {
        return getCustomChromeDriver().findElementWaiting(By.id("chart")).findElement(By.className("apexcharts-inner"))
                .findElements(By.className("apexcharts-heatmap-series")).get(row.getColumn())
                .findElements(By.className("apexcharts-data-labels")).get(column)
                .getText();
    }
}
