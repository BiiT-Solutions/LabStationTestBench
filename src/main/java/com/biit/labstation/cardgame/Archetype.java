package com.biit.labstation.cardgame;

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
