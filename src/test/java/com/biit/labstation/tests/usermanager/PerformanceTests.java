package com.biit.labstation.tests.usermanager;

/*-
 * #%L
 * Lab Station Test Bench
 * %%
 * Copyright (C) 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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
