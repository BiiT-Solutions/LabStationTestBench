package com.biit.labstation.dashboard;

public enum NcaHeatmapRow implements HeatmapRow {
    ADAPTABILITY(0),
    PROACTIVITY(1),
    ANALYSIS(2),
    INSPIRATION(3),
    BONDING(4),
    COMMUNICATION(5),
    STRENGTH(6),
    SOCIALIZATION(7),
    PROCESS_DESIGN(8),
    UNIVERSAL(9),
    RESPONSIBILITY(10),
    VISION(11);


    private int column;

    NcaHeatmapRow(int column) {
        this.column = column;
    }

    public int getColumn() {
        return column;
    }
}
