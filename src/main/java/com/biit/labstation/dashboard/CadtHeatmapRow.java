package com.biit.labstation.dashboard;

public enum CadtHeatmapRow {
    VISIONARY(0),
    SCIENTIST(1),
    TRADESMAN(2),
    INNOVATOR(3),
    LEADER(4),
    STRATEGIST(5),
    BANKER(6),
    RECEPTIVE(7),
    STRUCTURE_INSPIRATION(8),
    ADAPTABILITY_ACTION(9);


    private int column;

    CadtHeatmapRow(int column) {
        this.column = column;
    }

    public int getColumn() {
        return column;
    }
}
