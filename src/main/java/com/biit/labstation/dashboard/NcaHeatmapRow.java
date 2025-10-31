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
