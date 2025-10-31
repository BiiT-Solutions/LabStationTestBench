package com.biit.labstation.tests.cadt;

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
