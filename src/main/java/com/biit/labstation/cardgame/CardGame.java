package com.biit.labstation.cardgame;

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
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

import static org.awaitility.Awaitility.await;


@Component
public class CardGame extends ToolTest {

    private static final int WAITING_CARD_SELECTION = 5000;

    @Value("${testing.server.domain}")
    private String serverDomain;

    @Value("${cargame.context}")
    private String context;


    public CardGame(CustomChromeDriver customChromeDriver, Login login, Popup popup) {
        super(customChromeDriver, login, popup);
    }

    @Override
    public void access() {
        access(serverDomain, context);
    }

    @Override
    public void logout() {
        //Only when test is finished.
        getCustomChromeDriver().findElementWaiting(By.id("cardgame-menu-logout")).click();
    }

    public void closeWelcomePage() {
        await().atMost(Duration.ofSeconds(LONG_WAITING_TIME_SECONDS)).until(() -> {
            try {
                getCustomChromeDriver().findElementWaiting(By.id("start-button")).click();
                return true;
            } catch (Exception e) {
                return false;
            }
        });
        ToolTest.waitComponent(WAITING_CARD_SELECTION);
    }


    public void submitTest() {
        await().atMost(Duration.ofSeconds(LONG_WAITING_TIME_SECONDS)).until(() -> {
            try {
                getCustomChromeDriver().findElement(By.id("button-submit")).click();
                LabStationLogger.info(this.getClass(), "Submitting CADT game.");
                return true;
            } catch (Exception e) {
                return false;
            }
        });
        ToolTest.waitComponent();
    }

    public boolean canSubmitTest() {
        return getCustomChromeDriver().findElement(By.id("button-submit")).isEnabled();
    }


    public void chooseArchetypesByClick(Archetype archetypes1, Archetype archetypes2, Archetype archetypes3) {
        getCustomChromeDriver().findElementWaiting(By.id("card-rows")).findElement(By.id(archetypes1.getTag())).click();
        LabStationLogger.info(this.getClass(), "Selecting card '{}' as first option.", archetypes1);
        ToolTest.waitComponent(WAITING_CARD_SELECTION);
        getCustomChromeDriver().findElementWaiting(By.id("card-rows")).findElement(By.id(archetypes2.getTag())).click();
        LabStationLogger.info(this.getClass(), "Selecting card '{}' as second option.", archetypes2);
        ToolTest.waitComponent(WAITING_CARD_SELECTION);
        getCustomChromeDriver().findElementWaiting(By.id("card-rows")).findElement(By.id(archetypes3.getTag())).click();
        ToolTest.waitComponent(WAITING_CARD_SELECTION);
        LabStationLogger.info(this.getClass(), "Selecting card '{}' as third option.", archetypes3);
        closeCompletionPage();
        ToolTest.waitComponent(WAITING_CARD_SELECTION);
    }


    public void chooseArchetypesByDragAndDrop(Archetype archetypes1, Archetype archetypes2, Archetype archetypes3) {
        final Actions actions = new Actions(getCustomChromeDriver().getDriver());

        actions.clickAndHold(getCustomChromeDriver().findElement(By.id("card-rows")).findElement(By.id(archetypes1.getTag())))
                .moveToElement(getCustomChromeDriver().findElement(By.id("envelope")).findElement(By.className("card-drop-place")))
                .release(getCustomChromeDriver().findElement(By.id("envelope")).findElement(By.className("card-drop-place")))
                .build().perform();
        LabStationLogger.info(this.getClass(), "Selecting card '{}' as first option.", archetypes1);
        ToolTest.waitComponent(WAITING_CARD_SELECTION);
        actions.clickAndHold(getCustomChromeDriver().findElement(By.id("card-rows")).findElement(By.id(archetypes2.getTag())))
                .moveToElement(getCustomChromeDriver().findElement(By.id("envelope")).findElement(By.className("card-drop-place")))
                .release(getCustomChromeDriver().findElement(By.id("envelope")).findElement(By.className("card-drop-place")))
                .build().perform();
        LabStationLogger.info(this.getClass(), "Selecting card '{}' as second option.", archetypes2);
        ToolTest.waitComponent(WAITING_CARD_SELECTION);
        actions.clickAndHold(getCustomChromeDriver().findElement(By.id("card-rows")).findElement(By.id(archetypes3.getTag())))
                .moveToElement(getCustomChromeDriver().findElement(By.id("envelope")).findElement(By.className("card-drop-place")))
                .release(getCustomChromeDriver().findElement(By.id("envelope")).findElement(By.className("card-drop-place")))
                .build().perform();
        LabStationLogger.info(this.getClass(), "Selecting card '{}' as third option.", archetypes3);
        ToolTest.waitComponent(WAITING_CARD_SELECTION);

        closeCompletionPage();
        ToolTest.waitComponent(WAITING_CARD_SELECTION);
    }

    public void closeCompletionPage() {
        await().atMost(Duration.ofSeconds(LONG_WAITING_TIME_SECONDS)).until(() -> {
            try {
                getCustomChromeDriver().findElementWaiting(By.id("button-close")).click();
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }

    public void closeTestFinishedPage() {
        await().atMost(Duration.ofSeconds(LONG_WAITING_TIME_SECONDS)).until(() -> {
            try {
                getCustomChromeDriver().findElementWaiting(By.id("cardgame-menu-logout")).click();
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }


    public void chooseCompetences(Competence... competences) {
        for (Competence competence : competences) {
            final Actions actions = new Actions(getCustomChromeDriver().getDriver());
            actions.clickAndHold(getCustomChromeDriver().findElement(By.id("card-mat")).findElement(By.id(competence.getTag())))
                    .moveToElement(getCustomChromeDriver().findElement(By.id("selected-items")).findElement(By.id("card-drop-place")))
                    .release(getCustomChromeDriver().findElement(By.id("selected-items")).findElement(By.id("card-drop-place")))
                    .build().perform();
            LabStationLogger.info(this.getClass(), "Selecting competence '{}'.", competence);

            ToolTest.waitComponentOneSecond();
        }
    }

    public void undoChooseCompetences(Competence... competences) {
        for (Competence competence : competences) {
            final Actions actions = new Actions(getCustomChromeDriver().getDriver());
            actions.clickAndHold(getCustomChromeDriver().findElement(By.id("selected-items"))
                            .findElement(By.id("card-drop-place")).findElement(By.id(competence.getTag())))
                    .moveToElement(getCustomChromeDriver().findElement(By.id("card-mat")))
                    .release(getCustomChromeDriver().findElement(By.id("card-mat")))
                    .build().perform();
            LabStationLogger.info(this.getClass(), "Unselecting competence '{}'.", competence);

            ToolTest.waitComponentOneSecond();
        }
    }


    public void discardCompetences(Competence... competences) {
        for (Competence competence : competences) {
            final Actions actions = new Actions(getCustomChromeDriver().getDriver());
            actions.clickAndHold(getCustomChromeDriver().findElement(By.id("card-mat")).findElement(By.id(competence.getTag())))
                    .moveToElement(getCustomChromeDriver().findElement(By.id("dismissed-items")).findElement(By.id("card-drop-place")))
                    .release(getCustomChromeDriver().findElement(By.id("dismissed-items")).findElement(By.id("card-drop-place")))
                    .build().perform();
            LabStationLogger.info(this.getClass(), "Discarding competence '{}'.", competence);
            ToolTest.waitComponentOneSecond();
        }
    }

    public void undoDiscardCompetences(Competence... competences) {
        for (Competence competence : competences) {
            final Actions actions = new Actions(getCustomChromeDriver().getDriver());
            actions.clickAndHold(getCustomChromeDriver().findElement(By.id("dismissed-items")).findElement(By.id("card-drop-place"))
                            .findElement(By.id(competence.getTag())))
                    .moveToElement(getCustomChromeDriver().findElement(By.id("card-mat")))
                    .release(getCustomChromeDriver().findElement(By.id("card-mat")))
                    .build().perform();
            LabStationLogger.info(this.getClass(), "Discarding competence '{}'.", competence);
            ToolTest.waitComponentOneSecond();
        }
    }

}
