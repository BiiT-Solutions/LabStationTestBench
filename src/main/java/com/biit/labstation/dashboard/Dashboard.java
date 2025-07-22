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

    public static final int CADT_WAIT_TIME = 5000;

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
        getCustomChromeDriver().findElementWaiting(By.id("dashboard-menu")).click();
        getCustomChromeDriver().findElementWaiting(By.id("dashboard-menu-logout")).click();
    }

    public void selectPersonalCadtOnMenu() {
        navBar.goTo("nav-item-CADT", "nav-subitem-Personal");
        ToolTest.waitComponent(CADT_WAIT_TIME);
    }


    public void selectCadtOverviewOnMenu() {
        navBar.goTo("nav-item-CADT", "nav-subitem-Overview");
        ToolTest.waitComponent(CADT_WAIT_TIME);
    }

    private WebElement findSvg(String id) {
        return getCustomChromeDriver().findElementWaiting(By.xpath("//*[name() = 'svg' and @id='" + id + "']"));
    }

    public WebElement getSvgElement(String svgId, String node, String id) {
        return getCustomChromeDriver().findElementWaiting(By.xpath("//*[name() = 'svg' and @id='" + svgId + "']"
                + "//*[local-name()='" + node + "' and @id='" + id + "']"));
    }

    public String getCadtHeatMapValue(int column, CadtHeatmapRow row) {
        return getCustomChromeDriver().findElementWaiting(By.id("chart")).findElement(By.className("apexcharts-inner"))
                .findElements(By.className("apexcharts-heatmap-series")).get(row.getColumn())
                .findElements(By.className("apexcharts-data-labels")).get(column)
                .getText();
    }
}
