package com.chess.engine.board;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private final List<String> ALGEBRAIC_NOTATION = initializeAlgebraicNotation();
    private final Map<String, Integer> POSITION_TO_COORDINATE = initializePositionToCoordinateMap();

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

    public int getPositionAtCoordinate(final String position) {
        return POSITION_TO_COORDINATE.get(position);
    }

    public String getPositionAtCoordinate(final int coordinate) {
        return ALGEBRAIC_NOTATION.get(coordinate);
    }

    private static List<String> initializeAlgebraicNotation() {
        return ImmutableList.copyOf(new String[]{
                "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"
        });
    }

    private Map<String, Integer> initializePositionToCoordinateMap() {
        final Map<String, Integer> positionToCoordinate = new HashMap<>();
        for (int i = START_TILE_INDEX; i < NUM_TILES; i++) {
            positionToCoordinate.put(ALGEBRAIC_NOTATION.get(i), i);
        }
        return ImmutableMap.copyOf(positionToCoordinate);
    }
}
