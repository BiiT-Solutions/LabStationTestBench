package com.biit.labstation;

import java.time.Duration;

import static org.awaitility.Awaitility.await;

public abstract class ToolTest {
    public static final int WAITING_TIME = 250;
    protected static final int WAITING_TIME_SECONDS = 3;

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
}
