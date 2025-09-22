package com.biit.labstation.components;

public enum TabId {
    USERS("user-tabs"),
    PROFILES("profile-tabs"),
    LOGIN("login-tabs"),
    EXTERNAL_PROVIDERS("external-providers-tabs");

    private final String id;

    TabId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
