package com.biit.labstation.tests.cardgame;

import com.biit.labstation.ToolTest;
import com.biit.labstation.cardgame.Archetype;
import com.biit.labstation.cardgame.CardGame;
import com.biit.labstation.cardgame.Competence;
import com.biit.labstation.logger.ClassTestListener;
import com.biit.labstation.logger.TestListener;
import com.biit.labstation.tests.BaseTest;
import com.biit.labstation.tests.ITestWithWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@SpringBootTest
@Test(groups = "cardgame", priority = 200)
@Listeners({TestListener.class, ClassTestListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class CardGameTests extends BaseTest implements ITestWithWebDriver {
    public static final String ADMIN_USER_NAME = "admin@test.com";
    public static final String ADMIN_USER_PASSWORD = "asd123";

    @Autowired
    private CardGame cardGame;

    @BeforeClass
    public void setup() {
        cardGame.access();
    }

    public void cadtGame() {
        cardGame.login(ADMIN_USER_NAME, ADMIN_USER_PASSWORD);
        cardGame.closeWelcomePage();

        ToolTest.waitComponent(5000);
        cardGame.chooseArchetypesByDragAndDrop(Archetype.RECEPTIVE, Archetype.INNOVATOR, Archetype.STRATEGIST);
        cardGame.closeCompletionPage();
        ToolTest.waitComponent(5000);

        cardGame.chooseArchetypesByClick(Archetype.LEADER, Archetype.TRADESMAN, Archetype.SCIENTIST);
        cardGame.closeCompletionPage();
        ToolTest.waitComponent(5000);

        cardGame.chooseCompetences(Competence.COOPERATION, Competence.FUTURE, Competence.INNOVATION, Competence.PROBLEM_ANALYSIS, Competence.INITIATIVE,
                Competence.INTERPERSONAL_SENSITIVITY, Competence.DISCIPLINE, Competence.LEADERSHIP, Competence.FLEXIBILITY, Competence.ENGAGEMENT);


        cardGame.submitTest();
    }
}
