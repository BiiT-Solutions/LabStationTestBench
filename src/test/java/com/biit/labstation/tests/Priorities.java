package com.biit.labstation.tests;

public class Priorities {


    public static final int USER_MANAGER_PRIORITY = Integer.MIN_VALUE;
    public static final int LOGIN_PRIORITY = Integer.MIN_VALUE + 1;

    public static final int USER_TESTS_PRIORITY = -1000;
    public static final int USER_MANAGER_EDITOR_PRIORITY = USER_TESTS_PRIORITY + 1;
    public static final int BASIC_USER_PERMISSIONS_PRIORITY = USER_TESTS_PRIORITY + 2;
    public static final int SIGN_UP_PRIORITY = USER_TESTS_PRIORITY + 3;

    public static final int ORGANIZATION_TESTS_PRIORITY = 1;
    public static final int ORGANIZATION_ADMIN_TESTS_PRIORITY = ORGANIZATION_TESTS_PRIORITY + 1;

    public static final int PROFILES_TESTS_PRIORITY = 100;
    public static final int PROJECTS_TESTS_PRIORITY = PROFILES_TESTS_PRIORITY + 1;

    public static final int ORG_ADMIN_PROFILES_TESTS_PRIORITY = 150;

    public static final int CARD_GAME_PRIORITY = 1000;
    public static final int DASHBOARD_PRIORITY = CARD_GAME_PRIORITY + 1;
    public static final int ACTIVITY_HISTORIC_PRIORITY = DASHBOARD_PRIORITY + 1;
    public static final int ORGANIZATION_ADMIN_DASHBOARD_PRIORITY = ACTIVITY_HISTORIC_PRIORITY + 1;

    public static final int APPOINTMENT_CENTER_WORKSHOPS_PRIORITY = 2000;
    public static final int APPOINTMENT_MICROSOFT_PRIORITY = APPOINTMENT_CENTER_WORKSHOPS_PRIORITY + 1;
    public static final int APPOINTMENT_GOOGLE_PRIORITY = APPOINTMENT_MICROSOFT_PRIORITY + 1;
    public static final int SPEAKERS_PRIORITY = APPOINTMENT_GOOGLE_PRIORITY + 1;

    public static final int BOARDING_PASS_PRIORITY = 3000;

    public static final int NCA_PRIORITY = 3000;
}
