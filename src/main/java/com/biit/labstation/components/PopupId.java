package com.biit.labstation.components;

public enum PopupId {
    USER("user-popup"),
    APPLICATION("application-popup"),
    SERVICE("service-popup"),
    ORGANIZATION("organization-popup"),
    TEAM("team-popup"),
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
    PROFILE("profile-popup");

    private final String id;

    PopupId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
