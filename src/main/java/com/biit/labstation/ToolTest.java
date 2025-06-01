package com.biit.labstation;

import java.time.Duration;

import static org.awaitility.Awaitility.await;

public abstract class ToolTest {
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
}
