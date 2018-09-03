package com.chess.engine.board;

import com.google.common.collect.ImmutableList;
import java.util.List;

public enum BoardUtils {
    INSTANCE;

    public final List<Boolean> FIRST_FILE = initColumn(0);
    public final List<Boolean> SECOND_FILE = initColumn(1);
    public final List<Boolean> THIRD_FILE = initColumn(2);
    public final List<Boolean> FOURTH_FILE = initColumn(3);
    public final List<Boolean> FIFTH_FILE = initColumn(4);
    public final List<Boolean> SIXTH_FILE = initColumn(5);
    public final List<Boolean> SEVENTH_FILE = initColumn(6);
    public final List<Boolean> EIGHTH_FILE = initColumn(7);

    public final List<Boolean> EIGHTH_RANK = initRow(0);
    public final List<Boolean> SEVENTH_RANK = initRow(8);
    public final List<Boolean> SIXTH_RANK = initRow(16);
    public final List<Boolean> FIFTH_RANK = initRow(24);
    public final List<Boolean> FOURTH_RANK = initRow(32);
    public final List<Boolean> THIRD_RANK = initRow(40);
    public final List<Boolean> SECOND_RANK = initRow(48);
    public final List<Boolean> FIRST_RANK = initRow(56);
    public static final int START_TILE_INDEX = 0;
    public static final int NUM_TILES_PER_ROW = 8;
    public static final int NUM_TILES = 64;

    private static List<Boolean> initColumn(int columnNumber) {
        final Boolean[] column = new Boolean[NUM_TILES];
        for(int i = 0; i < column.length; i++) {
            column[i] = false;
        }
        do {
            column[columnNumber] = true;
            columnNumber += NUM_TILES_PER_ROW;
        } while(columnNumber < NUM_TILES);
        return ImmutableList.copyOf(column);
    }

    private static List<Boolean> initRow(int rowNumber) {
        final Boolean[] row = new Boolean[NUM_TILES];
        for(int i = 0; i < row.length; i++) {
            row[i] = false;
        }
        do {
            row[rowNumber] = true;
            rowNumber++;
        } while(rowNumber % NUM_TILES_PER_ROW != 0);
        return ImmutableList.copyOf(row);
    }

    public static boolean isValidTileCoordinate(final int coordinate) {
        return coordinate >= START_TILE_INDEX && coordinate < NUM_TILES;
    }
}
