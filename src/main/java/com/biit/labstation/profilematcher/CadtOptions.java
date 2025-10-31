package com.biit.labstation.profilematcher;

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

public enum CadtOptions {
    RECEPTIVE("receptive"),
    INTERPERSONAL_SENSITIVITY("interpersonal-sensitivity"),
    MULTICULTURAL_SENSITIVITY("multicultural-sensitivity"),

    INNOVATOR("innovator"),
    COOPERATION("cooperation"),
    INNOVATION("innovation"),

    VISIONARY("visionary"),
    PERSUASIVENESS("persuasiveness"),
    FUTURE("future"),

    STRATEGIST("strategist"),
    DECISIVENESS("decisiveness"),
    JUDGEMENT("judgement"),

    DISCIPLINE("discipline"),
    GOAL_SETTING("goal-setting"),

    BUILDING_AND_MAINTAINING("building-and-maintaining"),
    FLEXIBILITY("flexibility"),

    CONSCIENTIOUSNESS("conscientiousness"),
    ENGAGEMENT("engagement"),

    DIRECTION("direction"),
    INITIATIVE("initiative"),

    BANKER("banker"),
    BUSINESS_MINDED("business-minded"),
    TENACITY("tenacity"),

    TRADESMAN("tradesman"),
    COMMUNICATION_SKILLS("communication-skills"),
    CLIENT_ORIENTED("client-oriented"),

    LEADER("leader"),
    LEADERSHIP("leadership"),
    INDEPENDENCE("independence"),

    SCIENTIST("scientist"),
    PROBLEM_ANALYSIS("problem-analysis"),
    PLANIFICATIOn("planification");


    private final String id;

    CadtOptions(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
