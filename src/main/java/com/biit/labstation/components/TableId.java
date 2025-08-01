package com.biit.labstation.components;

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
    CANDIDATES_TABLE("candidates-table");

    private final String id;

    TableId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
