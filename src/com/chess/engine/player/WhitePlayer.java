package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.KingSideCastleMove;
import com.chess.engine.board.Move.QueenSideCastleMove;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WhitePlayer extends Player {
    public WhitePlayer(final Board board,
                       final Collection<Move> whiteStandardLegalMoves,
                       final Collection<Move> blackStandardLegalMoves){
        super(board, whiteStandardLegalMoves, blackStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.blackPlayer();
    }

    /*
    * The king and the chosen rook are on the player's first rank.
    * Neither the king nor the chosen rook has previously moved.
    * There are no pieces between the king and the chosen rook.
    * The king is not currently in check.
    * The king does not pass through a square that is attacked by an enemy piece.
    * The king does not end up in check. (True of any legal move.)
    */
    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals,
                                                    final Collection<Move> opponentsLegals) {
        List<Move> kingCastles = new ArrayList<>();
        if (this.playerKing.isFirstMove() && !this.isInCheck()){
            //Whites king side castle
            if (!this.board.getTile(61).isOccupied() &&
                !this.board.getTile(62).isOccupied()){

                final Tile rookTile = this.board.getTile(63);
                if (rookTile.isOccupied() && rookTile.getPiece().isFirstMove()){
                    if (Player.calculateAttacksOnTile(61, opponentsLegals).isEmpty() &&
                        Player.calculateAttacksOnTile(62, opponentsLegals).isEmpty() &&
                        rookTile.getPiece().getPieceType().isRook()){
                            kingCastles.add(new KingSideCastleMove(this.board,
                                                                   this.playerKing,
                                                                  62,
                                                                   (Rook)rookTile.getPiece(),
                                                                    rookTile.getTileCoordinate(),
                                                                   61));
                    }
                }
            }
            //Whites queen side castle
            if (!this.board.getTile(59).isOccupied() &&
                !this.board.getTile(58).isOccupied() &&
                !this.board.getTile(57).isOccupied()){

                final Tile rookTile = this.board.getTile(56);
                if (rookTile.isOccupied() && rookTile.getPiece().isFirstMove()){
                    if (Player.calculateAttacksOnTile(59, opponentsLegals).isEmpty() &&
                        Player.calculateAttacksOnTile(58, opponentsLegals).isEmpty() &&
                        Player.calculateAttacksOnTile(57, opponentsLegals).isEmpty() &&
                        rookTile.getPiece().getPieceType().isRook()){
                            kingCastles.add(new QueenSideCastleMove(this.board,
                                                                    this.playerKing,
                                                                   58,
                                                                    (Rook)rookTile.getPiece(),
                                                                    rookTile.getTileCoordinate(),
                                                                   59));
                    }
                }
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }
}
