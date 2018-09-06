package com.chess.engine.board;

import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;

public abstract class Tile {
    protected final int tileCoordinate;

    private Tile(final int tileCoordinate){
        this.tileCoordinate = tileCoordinate;
    }
    public abstract boolean isOccupied();
    public int getTileCoordinate() {
        return this.tileCoordinate;
    }
    public abstract Piece getPiece();
    private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();

    private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {
        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();
        for (int i = 0; i < BoardUtils.NUM_TILES; i++){
            emptyTileMap.put(i, new EmptyTile(i));
        }
        return ImmutableMap.copyOf(emptyTileMap);
    }
    public static Tile createTile(final int tileCoordinate, final Piece piece){
        return piece != null ? new OccupiedTile(tileCoordinate, piece) : EMPTY_TILES_CACHE.get(tileCoordinate);
    }

    public static final class OccupiedTile extends Tile{
        private final Piece pieceOnTile;
        OccupiedTile(final int tileCoordinate, final Piece pieceOnTile){
            super(tileCoordinate);
            this.pieceOnTile = pieceOnTile;
        }

        @Override
        public boolean isOccupied() {
            return true;
        }

        @Override
        public Piece getPiece() {
            return this.pieceOnTile;
        }

        @Override
        public String toString(){
            return getPiece().getPieceAllegiance().isBlack() ?
                    getPiece().toString().toLowerCase() : getPiece().toString();
        }
    }

    public static final class EmptyTile extends Tile{

        EmptyTile(final int tileCoordinate){
            super(tileCoordinate);
        }
        @Override
        public boolean isOccupied() {
            return false;
        }

        @Override
        public Piece getPiece() {
            return null;
        }

        @Override
        public String toString(){
            return "-";
        }
    }

}
