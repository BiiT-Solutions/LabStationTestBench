package com.biit.labstation.tests.cadt;


import com.biit.labstation.cardgame.Archetype;
import com.biit.labstation.cardgame.CardGame;
import com.biit.labstation.cardgame.Competence;
import com.biit.labstation.logger.ClassTestListener;
import com.biit.labstation.logger.LabStationLogger;
import com.biit.labstation.logger.TestListener;
import com.biit.labstation.tests.BaseTest;
import com.biit.labstation.tests.ITestWithWebDriver;
import com.biit.labstation.usermanager.UserManager;
import graphql.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.biit.labstation.tests.Priorities.CARD_GAME_PRIORITY;

@SpringBootTest
@Test(groups = "cadt", priority = CARD_GAME_PRIORITY)
@Listeners({TestListener.class, ClassTestListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class CadtTests extends BaseTest implements ITestWithWebDriver {

    public static final String NEW_USER_NAME = "CADT";
    public static final String NEW_USER_LASTNAME = "User";
    public static final String NEW_USER_PASSWORD = "asd123";
    public static final String NEW_USER_EMAIL = "cadt@test.com";
    public static final String NEW_USERNAME = NEW_USER_EMAIL;

    @Autowired
    private CardGame cardGame;

    @Autowired
    private UserManager userManager;

    @Value("${admin.user}")
    private String adminUser;
    @Value("${admin.password}")
    private String adminPassword;

    private String username;


    @Test
    public void signUpOnCardGame() {
        cardGame.access();
        cardGame.signUp(NEW_USERNAME, NEW_USER_PASSWORD, NEW_USER_NAME, NEW_USER_LASTNAME, NEW_USER_EMAIL);

        cardGame.closeWelcomePage();

        LabStationLogger.info(this.getClass().getName(), "Selecting feminine archetypes.");
        cardGame.chooseArchetypesByDragAndDrop(Archetype.INNOVATOR, Archetype.RECEPTIVE, Archetype.VISIONARY);

        LabStationLogger.info(this.getClass().getName(), "Selecting masculine cards.");
        cardGame.chooseArchetypesByClick(Archetype.SCIENTIST, Archetype.BANKER, Archetype.LEADER);

        LabStationLogger.info(this.getClass().getName(), "Selecting competence cards.");
        cardGame.chooseCompetences(Competence.COOPERATION, Competence.INNOVATION, Competence.PERSUASIVENESS, Competence.JUDGEMENT, Competence.INITIATIVE,
                Competence.PLANIFICATION, Competence.PROBLEM_ANALYSIS, Competence.INDEPENDENCE, Competence.TENACITY, Competence.GOAL_SETTING);

        Assert.assertTrue(cardGame.canSubmitTest());
        cardGame.submitTest();
    }

    @Test(dependsOnMethods = "signUpOnCardGame")
    public void getUsername() {
        userManager.access();
        userManager.login(adminUser, adminPassword);
        username = userManager.checkUserExistsByEmail(NEW_USER_EMAIL);
        org.testng.Assert.assertNotNull(username);
        userManager.logout();
    }


    @AfterClass(alwaysRun = true)
    public void cleanup() {
        try {
            userManager.login(adminUser, adminPassword);
            userManager.deleteUser(username);
        } catch (Exception e) {
            //Ignore
        }
        try {
            userManager.logout();
        } catch (Exception e) {
            //Ignore
        }
    }

    @AfterClass(dependsOnMethods = "cleanup", alwaysRun = true)
    public void closeDriver() {
        userManager.getCustomChromeDriver().closeDriver();
    }

}
