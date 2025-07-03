package com.biit.labstation.tests.cadt;


import com.biit.labstation.ToolTest;
import com.biit.labstation.cardgame.Archetype;
import com.biit.labstation.cardgame.CardGame;
import com.biit.labstation.cardgame.Competence;
import com.biit.labstation.components.SnackBar;
import com.biit.labstation.logger.ClassTestListener;
import com.biit.labstation.logger.LabStationLogger;
import com.biit.labstation.logger.TestListener;
import com.biit.labstation.tests.BaseTest;
import com.biit.labstation.tests.ITestWithWebDriver;
import com.biit.labstation.usermanager.UserManager;
import graphql.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.biit.labstation.tests.LoginIT.ADMIN_USER_NAME;
import static com.biit.labstation.tests.LoginIT.ADMIN_USER_PASSWORD;

@SpringBootTest
@Test(groups = "cadt", priority = Integer.MAX_VALUE)
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
    private SnackBar snackBar;

    @Autowired
    private UserManager userManager;

    private String username;


    @Test
    public void signUpOnCardGame() {
        cardGame.access();
        cardGame.signUp(NEW_USERNAME, NEW_USER_PASSWORD, NEW_USER_NAME, NEW_USER_LASTNAME, NEW_USER_EMAIL);

        cardGame.closeWelcomePage();

        ToolTest.waitComponent(5000);
        LabStationLogger.info(this.getClass().getName(), "Selecting feminine archetypes.");
        cardGame.chooseArchetypesByDragAndDrop(Archetype.INNOVATOR, Archetype.RECEPTIVE, Archetype.VISIONARY);
        cardGame.closeCompletionPage();
        ToolTest.waitComponent(5000);

        LabStationLogger.info(this.getClass().getName(), "Selecting masculine cards.");
        cardGame.chooseArchetypesByClick(Archetype.SCIENTIST, Archetype.BANKER, Archetype.LEADER);
        cardGame.closeCompletionPage();
        ToolTest.waitComponent(5000);

        LabStationLogger.info(this.getClass().getName(), "Selecting competence cards.");

        cardGame.chooseCompetences(Competence.COOPERATION, Competence.INNOVATION, Competence.PERSUASIVENESS, Competence.JUDGEMENT, Competence.INITIATIVE,
                Competence.PLANIFICATION, Competence.PROBLEM_ANALYSIS, Competence.INDEPENDENCE, Competence.TENACITY, Competence.GOAL_SETTING);

        Assert.assertTrue(cardGame.canSubmitTest());
        cardGame.submitTest();
    }

    @Test(dependsOnMethods = "signUpOnCardGame")
    public void getUsername() {
        userManager.access();
        userManager.login(ADMIN_USER_NAME, ADMIN_USER_PASSWORD);
        username = userManager.checkUserExistsByEmail(NEW_USER_EMAIL);
        org.testng.Assert.assertNotNull(username);
        userManager.logout();
    }


    @AfterClass
    public void cleanup() {
        try {
            userManager.login(ADMIN_USER_NAME, ADMIN_USER_PASSWORD);
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

    @AfterClass(dependsOnMethods = "cleanup")
    public void closeDriver() {
        userManager.getCustomChromeDriver().closeDriver();
    }

}
