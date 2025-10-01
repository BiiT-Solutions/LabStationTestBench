package com.biit.labstation.dashboard;

public enum CadtHeatmapRow implements HeatmapRow {
    ADAPTABILITY_ACTION(0),
    BANKER(1),
    INNOVATOR(2),
    LEADER(3),
    RECEPTIVE(4),
    SCIENTIST(5),
    STRATEGIST(6),
    STRUCTURE_INSPIRATION(7),
    TRADESMAN(8),
    VISIONARY(9);


    private int column;

    CadtHeatmapRow(int column) {
        this.column = column;
    }

    public int getColumn() {
        return column;
    }
}
