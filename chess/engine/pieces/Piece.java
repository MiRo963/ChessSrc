package com.chess.engine.pieces;
import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import java.util.Collection;

public abstract class Piece {
    protected final PieceType pieceType;
    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;
    private final int cachedHashCode;

    Piece(final PieceType pieceType,
          final Alliance pieceAlliance,
          final int piecePosition,
          final boolean isFirstMove){
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        this.isFirstMove = isFirstMove;
        this.pieceType = pieceType;
        this.cachedHashCode = computeHashCode();
    }

    private int computeHashCode() {
        int result = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + (isFirstMove ? 1 : 0);
        return result;
    }

    public int getPiecePosition(){
        return this.piecePosition;
    }

    public PieceType getPieceType(){
        return this.pieceType;
    }

    public abstract Collection<Move> calculateLegalMoves(final Board board);

    public boolean isFirstMove(){
        return this.isFirstMove;
    }
    public  Alliance getPieceAllegiance(){
        return this.pieceAlliance;
    }
    public abstract Piece movePiece(Move move); //will return a new piece with a new coordinate, because the pieces are immutable

    @Override
    public boolean equals(final Object other) {
        if (this == other){
            return true;
        }
        if (!(other instanceof Piece)){
            return false;
        }
        final Piece otherPiece = (Piece) other;
        return piecePosition == otherPiece.piecePosition && pieceType == otherPiece.pieceType &&
               pieceAlliance == otherPiece.pieceAlliance && isFirstMove == otherPiece.isFirstMove;
    }

    @Override
    public int hashCode() {
        return this.cachedHashCode;
    }

    public enum PieceType{
        PAWN("P"){
            @Override
            public boolean isKing(){
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KNIGHT("N"){
            @Override
            public boolean isKing(){
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        BISHOP ("B"){
            @Override
            public boolean isKing(){
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK("R"){
            @Override
            public boolean isKing(){
                return false;
            }

            @Override
            public boolean isRook() {
                return true;
            }
        },
        QUEEN("Q") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KING("K") {
            @Override
            public boolean isKing() {
                return true;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        };

        private String pieceName;
        PieceType(final String pieceName){
            this.pieceName = pieceName;
        }

        @Override
        public String toString(){
            return this.pieceName;
        }
        public abstract boolean isKing();
        public abstract boolean isRook();
    }
}
