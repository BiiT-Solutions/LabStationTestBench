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

public enum TableId {
    USERS_TABLE("users-table"),
    USERS_GROUP_TABLE("user-groups-table"),
    USERS_GROUP_ROLE_TABLE("user-group-role-table"),
    APPLICATION_TABLE("application-table"),
    APPLICATION_ROLE_TABLE("application-role-table"),
    ROLE_TABLE("role-table"),
    ORGANIZATION_TABLE("organization-table"),
    TEAM_TABLE("teams-table"),
    SERVICE_TABLE("service-table"),
    SERVICE_ROLE_TABLE("service-role-table"),
    PROFILES_TABLE("profiles-table"),
    PROJECTS_TABLE("projects-table"),
    CANDIDATES_TABLE("candidates-table"),
    ACTIVITY_TABLE("activity-table"),
    ATTENDANCE_TABLE("attendance-list-data");

    private final String id;

    TableId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
