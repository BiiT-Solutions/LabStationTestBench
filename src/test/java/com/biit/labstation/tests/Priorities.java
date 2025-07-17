package com.biit.labstation.tests;

public class Priorities {


    public static final int USER_MANAGER_PRIORITY = Integer.MIN_VALUE;

    public static final int USER_TESTS_PRIORITY = -1000;
    public static final int USER_MANAGER_EDITOR_PRIORITY = USER_TESTS_PRIORITY + 1;
    public static final int BASIC_USER_PERMISSIONS_PRIORITY = USER_TESTS_PRIORITY + 2;
    public static final int SIGN_UP_PRIORITY = USER_TESTS_PRIORITY + 3;

    public static final int ORGANIZATION_TESTS_PRIORITY = 10;
    public static final int ORGANIZATION_ADMIN_TESTS_PRIORITY = ORGANIZATION_TESTS_PRIORITY + 1;

    public static final int PROFILES_TESTS_PRIORITY = 100;

    public static final int CARD_GAME_PRIORITY = 1000;
    public static final int DASHBOARD_PRIORITY = CARD_GAME_PRIORITY + 1;
}
