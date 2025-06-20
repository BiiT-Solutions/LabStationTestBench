package com.biit.labstation;

import com.biit.labstation.logger.LabStationLogger;

import java.time.Duration;

import static org.awaitility.Awaitility.await;

public abstract class ToolTest {
    public static final int WAITING_TIME = 250;
    protected static final int WAITING_TIME_SECONDS = 3;
    private static final int WAITING_TO_ACCESS_BROWSER = 2000;

    private final CustomChromeDriver customChromeDriver;

    protected ToolTest(CustomChromeDriver customChromeDriver) {
        this.customChromeDriver = customChromeDriver;
    }

    public CustomChromeDriver getCustomChromeDriver() {
        return customChromeDriver;
    }

    protected void waitAndExecute(Runnable operation) {
        await().atMost(Duration.ofSeconds(WAITING_TIME_SECONDS)).until(() -> {
            try {
                operation.run();
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }

    public static void waitComponent() {
        waitComponent(WAITING_TIME);
    }

    public static void waitComponent(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            //Ignore
            Thread.currentThread().interrupt();
        }
    }

    public abstract void access();

    public abstract void login(String username, String password);

    public abstract void logout();

    public void access(String serverDomain, String context) {
        try {
            final String requestedUrl = serverDomain + context;
            LabStationLogger.debug(this.getClass().getName(), "Accessing to URL '{}'.", requestedUrl);
            customChromeDriver.getDriver().get(requestedUrl);
            waitComponent(WAITING_TO_ACCESS_BROWSER);
            final String currentUrl = customChromeDriver.getDriver().getCurrentUrl();
            if (currentUrl == null || !currentUrl.startsWith(requestedUrl)) {
                LabStationLogger.info(this.getClass(), "Browser is not ready yet! Current URL is '{}'. Requested is '{}'. Waiting...",
                        currentUrl, requestedUrl);
                access(serverDomain, context);
            }
            LabStationLogger.info(this.getClass(), "Opened '{}'.", currentUrl);
        } catch (Exception e) {
            LabStationLogger.errorMessage(this.getClass(), e);
        }
    }
}
