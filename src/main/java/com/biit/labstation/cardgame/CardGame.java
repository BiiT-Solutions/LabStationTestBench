package com.biit.labstation.cardgame;

import com.biit.labstation.CustomChromeDriver;
import com.biit.labstation.ToolTest;
import com.biit.labstation.components.Login;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

import static org.awaitility.Awaitility.await;


@Component
public class CardGame extends ToolTest {

    private static final int WAITING_CARD_SELECTION = 5000;
    private static final int WAITING_PAGE_TRANSITION = 1000;

    @Value("${testing.server.domain}")
    private String serverDomain;

    @Value("${cargame.context}")
    private String context;


    public CardGame(CustomChromeDriver customChromeDriver, Login login) {
        super(customChromeDriver, login);
    }

    @Override
    public void access() {
        access(serverDomain, context);
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
    }

    public void submitTest() {
        await().atMost(Duration.ofSeconds(LONG_WAITING_TIME_SECONDS)).until(() -> {
            try {
                getCustomChromeDriver().findElement(By.id("button-submit")).click();
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }

    public void chooseArchetypesByClick(Archetype archetypes1, Archetype archetypes2, Archetype archetypes3) {
        getCustomChromeDriver().findElementWaiting(By.id("card-rows")).findElement(By.id(archetypes1.getTag())).click();
        ToolTest.waitComponent(WAITING_CARD_SELECTION);
        getCustomChromeDriver().findElementWaiting(By.id("card-rows")).findElement(By.id(archetypes2.getTag())).click();
        ToolTest.waitComponent(WAITING_CARD_SELECTION);
        getCustomChromeDriver().findElementWaiting(By.id("card-rows")).findElement(By.id(archetypes3.getTag())).click();
        ToolTest.waitComponent(WAITING_CARD_SELECTION);
    }

    public void chooseArchetypesByDragAndDrop(Archetype archetypes1, Archetype archetypes2, Archetype archetypes3) {
        final Actions actions = new Actions(getCustomChromeDriver().getDriver());

        actions.clickAndHold(getCustomChromeDriver().findElement(By.id("card-rows")).findElement(By.id(archetypes1.getTag())))
                .moveToElement(getCustomChromeDriver().findElement(By.id("envelope")).findElement(By.className("card-drop-place")))
                .release(getCustomChromeDriver().findElement(By.id("envelope")).findElement(By.className("card-drop-place")))
                .build().perform();
        ToolTest.waitComponent(WAITING_CARD_SELECTION);
        actions.clickAndHold(getCustomChromeDriver().findElement(By.id("card-rows")).findElement(By.id(archetypes2.getTag())))
                .moveToElement(getCustomChromeDriver().findElement(By.id("envelope")).findElement(By.className("card-drop-place")))
                .release(getCustomChromeDriver().findElement(By.id("envelope")).findElement(By.className("card-drop-place")))
                .build().perform();
        ToolTest.waitComponent(WAITING_CARD_SELECTION);
        actions.clickAndHold(getCustomChromeDriver().findElement(By.id("card-rows")).findElement(By.id(archetypes3.getTag())))
                .moveToElement(getCustomChromeDriver().findElement(By.id("envelope")).findElement(By.className("card-drop-place")))
                .release(getCustomChromeDriver().findElement(By.id("envelope")).findElement(By.className("card-drop-place")))
                .build().perform();
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


    public void chooseCompetences(Competence... competences) {
        for (Competence competence : competences) {
            final Actions actions = new Actions(getCustomChromeDriver().getDriver());
            actions.clickAndHold(getCustomChromeDriver().findElement(By.id("card-mat")).findElement(By.id(competence.getTag())))
                    .moveToElement(getCustomChromeDriver().findElement(By.id("selected-items")).findElement(By.id("card-drop-place")))
                    .release(getCustomChromeDriver().findElement(By.id("selected-items")).findElement(By.id("card-drop-place")))
                    .build().perform();

            ToolTest.waitComponent();
        }
    }


    public void discardCompetences(Competence... competences) {
        for (Competence competence : competences) {
            final Actions actions = new Actions(getCustomChromeDriver().getDriver());
            actions.clickAndHold(getCustomChromeDriver().findElement(By.id("card-mat")).findElement(By.id(competence.getTag())))
                    .moveToElement(getCustomChromeDriver().findElement(By.id("dismissed-items")).findElement(By.id("card-drop-place")))
                    .release(getCustomChromeDriver().findElement(By.id("dismissed-items")).findElement(By.id("card-drop-place")))
                    .build().perform();
            ToolTest.waitComponent();
        }
    }

}
