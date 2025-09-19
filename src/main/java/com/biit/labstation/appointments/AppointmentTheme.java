package com.biit.labstation.appointments;

public enum AppointmentTheme {
    RED(0),
    GREEN(1),
    YELLOW(2),
    BLUE(3),
    PURPLE(4);

    private final Integer index;

    AppointmentTheme(Integer index) {
        this.index = index;
    }

    public Integer getIndex() {
        return index;
    }
}
