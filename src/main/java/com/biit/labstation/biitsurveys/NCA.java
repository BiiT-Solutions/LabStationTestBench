package com.biit.labstation.biitsurveys;

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

import com.biit.labstation.CustomChromeDriver;
import com.biit.labstation.ToolTest;
import com.biit.labstation.components.Login;
import com.biit.labstation.components.Popup;
import com.biit.labstation.logger.LabStationLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

@Component
public class NCA extends ToolTest {

    private final Random random = new SecureRandom();

    @Value("${testing.server.domain}")
    private String serverDomain;

    @Value("${nca.context}")
    private String context;

    protected NCA(CustomChromeDriver customChromeDriver, Login login, Popup popup) {
        super(customChromeDriver, login, popup);
    }

    @Override
    public void access() {
        access(serverDomain, context, true);
    }

    @Override
    public void logout() {
        //Not existing.
    }

    public void fillUpNCA(double yesNoRate) {
        LabStationLogger.info(this.getClass(), "Filling up NCA");
        int questionNumber = 0;
        while (true) {
            final List<WebElement> elements = getCustomChromeDriver().findElements(By.className("question"));
            final String question = elements.get(questionNumber % 2).getText();
            questionNumber++;
            try {
                if (random.nextDouble() < yesNoRate) {
                    getCustomChromeDriver().findElement(By.id("yes")).click();
                    LabStationLogger.debug(this.getClass(), "Selecting 'yes' on '{}' number '{}'.", question, questionNumber);
                } else {
                    getCustomChromeDriver().findElement(By.id("no")).click();
                    LabStationLogger.debug(this.getClass(), "Selecting 'no' on '{}' number '{}'.", question, questionNumber);
                }
            } catch (Exception e) {
                //It is not a yes/no but a 1/2/3/4/5 question
                final String selection = "value" + (random.nextInt(5) + 1);
                getCustomChromeDriver().findElement(By.id(selection)).click();
                LabStationLogger.debug(this.getClass(), "Selecting '{}' on '{}' number '{}'.", selection, question, questionNumber);
            }
            try {
                getCustomChromeDriver().findElement(By.id("submitted-message"));
                LabStationLogger.debug(this.getClass(), "NCA Finished!");
                break;
            } catch (Exception e) {
                //Ignore, the form is not finished.
            }
        }
    }


}
