package com.biit.labstation.cardgame;

public enum Competence {
    DISCIPLINE("discipline"),
    CLIENT_ORIENTED("client-oriented"),
    ENGAGEMENT("engagement"),
    COOPERATION("cooperation"),
    LEADERSHIP("leadership"),
    BUILDING_AND_MAINTAINING("building-and-maintaining"),
    DIRECTION("direction"),
    MULTICULTURAL_SENSITIVITY("multicultural-sensitivity"),
    JUDGEMENT("judgement"),
    INDEPENDENCE("independence"),
    INITIATIVE("initiative"),
    GOAL_SETTING("goal-setting"),
    DECISIVENESS("decisiveness"),
    FUTURE("future"),
    COMMUNICATION_SKILLS("communication-skills"),
    BUSINESS_MINDED("business-minded"),
    TENACITY("tenacity"),
    CONSCIENTIOUSNESS("conscientiousness"),
    INTERPERSONAL_SENSITIVITY("interpersonal-sensitivity"),
    FLEXIBILITY("flexibility"),
    PERSUASIVENESS("persuasiveness"),
    INNOVATION("innovation"),
    PROBLEM_ANALYSIS("problem-analysis"),
    PLANIFICATION("planification");

    private final String tag;

    Competence(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}
