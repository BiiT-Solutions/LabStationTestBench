package com.biit.labstation.dashboard;

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
