package com.biit.labstation.profilematcher;

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
