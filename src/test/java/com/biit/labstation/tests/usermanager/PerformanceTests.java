package com.biit.labstation.tests.usermanager;

import com.biit.labstation.logger.ClassTestListener;
import com.biit.labstation.logger.ConsoleLogger;
import com.biit.labstation.logger.TestListener;
import com.biit.labstation.tests.BaseTest;
import com.biit.labstation.tests.ITestWithWebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.Date;

@SpringBootTest
@Test(groups = "organizationAdmin", priority = Integer.MAX_VALUE)
@Listeners({TestListener.class, ClassTestListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class PerformanceTests extends BaseTest implements ITestWithWebDriver {

    @Test
    public void analyzePerformanceTests() {
        final LogEntries logEntries = getDriver().getDriver().manage().logs().get(LogType.PERFORMANCE);
        ConsoleLogger.info(this.getClass(), "[[[[[[  Starting performance logs  ]]]]]]");
        for (LogEntry entry : logEntries) {
            ConsoleLogger.info(this.getClass(), new Date(entry.getTimestamp()) + " " + entry.getLevel() + " " + entry.getMessage());
        }
        ConsoleLogger.info(this.getClass(), "[[[[[[   Ending performance logs   ]]]]]]");
    }

}
