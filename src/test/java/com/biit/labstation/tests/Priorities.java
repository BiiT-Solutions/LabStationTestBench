package com.biit.labstation.tests;

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

    public static final int NCA_PRIORITY = 4000;
}
