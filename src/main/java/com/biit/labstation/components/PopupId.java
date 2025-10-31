package com.biit.labstation.components;

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

public enum PopupId {
    USER("user-popup"),
    APPLICATION("application-popup"),
    SERVICE("service-popup"),
    ORGANIZATION("organization-popup"),
    TEAM("teams-popup"),
    TEAM_FORM("team-form-popup"),
    ROLE("role-popup"),
    USER_ROLE("user-role-popup"),
    USER_GROUP("user-group-popup"),
    APPLICATION_ROLE_SERVICES("application-role-services-popup"),
    APPLICATION_ROLE("application-role-popup"),
    APPLICATION_ROLE_ASSIGN("application-role-assign-popup"),
    APPLICATION_ROLE_SELECTOR("application-role-selector-popup"),
    ASSIGN_APPLICATION_SELECTOR("assign-application-popup"),
    ASSIGN_USERS_TO_GROUP("assign-users-to-group"),
    SERVICE_ROLE("service-role-popup"),
    RESET_PASSWORD("reset-password-popup"),
    CONFIRMATION("confirmation-popup"),
    CONFIRMATION_ASSIGN("confirmation-assign-popup"),
    CONFIRMATION_UNASSIGN("confirmation-unassign-popup"),
    CONFIRMATION_DELETE("confirmation-delete-popup"),
    PROFILE("profile-popup"),
    PROJECT("project-popup"),
    ASSIGN_USER_POPUP("assign-user-popup"),
    ASSIGN_PROFILES_POPUP("profiles-popup"),
    USERS_PROFILES_POPUP("user-profiles-popup"),
    CANDIDATES_POPUP("candidates-popup"),
    LOGIN_WARNING("warning-popup"),
    WORKSHOP("workshop-popup"),
    APPOINTMENT("appointment-popup"),
    DELETE_WORKSHOP("delete-workshop-popup"),
    DELETE_APPOINTMENT("delete-event-popup"),
    EXTERNAL_CALENDAR("external-calendar-popup"),
    SYNCHRONIZE_CALENDAR("synchronize-calendar-popup");

    private final String id;

    PopupId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
