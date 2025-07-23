package com.biit.labstation.dashboard;

public enum CadtHeatmapRow {
    VISIONARY(9),
    SCIENTIST(5),
    TRADESMAN(8),
    INNOVATOR(2),
    LEADER(3),
    STRATEGIST(6),
    BANKER(1),
    RECEPTIVE(4),
    STRUCTURE_INSPIRATION(7),
    ADAPTABILITY_ACTION(0);


    private int column;

    CadtHeatmapRow(int column) {
        this.column = column;
    }

    public int getColumn() {
        return column;
    }
}
