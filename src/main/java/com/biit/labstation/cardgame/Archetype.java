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

import java.util.Arrays;
import java.util.List;

public enum Archetype {
    RECEPTIVE("receptive", Competence.INTERPERSONAL_SENSITIVITY, Competence.MULTICULTURAL_SENSITIVITY),
    INNOVATOR("innovator", Competence.COOPERATION, Competence.INNOVATION),
    VISIONARY("visionary", Competence.PERSUASIVENESS, Competence.FUTURE),
    STRATEGIST("strategist", Competence.JUDGEMENT, Competence.DECISIVENESS),
    BANKER("banker", Competence.BUSINESS_MINDED, Competence.TENACITY),
    TRADESMAN("tradesman", Competence.COMMUNICATION_SKILLS, Competence.CLIENT_ORIENTED),
    LEADER("leader", Competence.LEADERSHIP, Competence.INDEPENDENCE),
    SCIENTIST("scientist", Competence.PLANIFICATION, Competence.PROBLEM_ANALYSIS);

    private final String tag;

    private final List<Competence> competences;

    Archetype(String tag, Competence... competences) {
        this.tag = tag;
        this.competences = Arrays.asList(competences);
    }

    public List<Competence> getCompetences() {
        return competences;
    }

    public String getTag() {
        return tag;
    }
}
