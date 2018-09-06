package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Player {
    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;

    Player(final Board board,
           final Collection<Move> legalMoves,
           final Collection<Move> opponentMoves){
        this.board = board;
        this.playerKing = establishKing();
        this.legalMoves = ImmutableList.copyOf(Iterables.concat(calculateKingCastles(legalMoves, opponentMoves), legalMoves));
        //if the list is empty (isEmpty() == true) >>> there is no check (isInCheck == false)
        this.isInCheck = !Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentMoves).isEmpty();
    }
    /*
    * @piecePosition: the king's position
    * @opponentMoves: list of all legal moves the opponent has.
    * This method checks if any legal move for the opponent attacks the king's position and return a list with all attack on the given position.
    */
    protected static Collection<Move> calculateAttacksOnTile(final int piecePosition, final Collection<Move> opponentMoves) {
        final List<Move> attackMoves = new ArrayList<>();
        for (final Move move : opponentMoves){
            if (move.getDestinationCoordinate() == piecePosition){
                attackMoves.add(move);
            }
        }
        return ImmutableList.copyOf(attackMoves);
    }

    /*Will get the king piece for the player*/
    private King establishKing(){
        for(final Piece piece : this.getActivePieces()){
            if (piece.getPieceType().isKing()){
                return (King) piece;
            }
        }
        throw new RuntimeException("Not a valid board!!");
    }
    public King getPlayerKing() {
        return this.playerKing;
    }

    public Collection<Move> getLegalMoves() {
        return this.legalMoves;
    }

    public boolean isInCheck(){
        return this.isInCheck;
    }

    public boolean isInCheckMate(){
        //if the king is in check and he has no escape moves then it's a checkmate!
        //if hasEscapeMoves == false >>> king is in checkmate( == true).
        return this.isInCheck && !hasEscapeMoves();
    }

    public boolean isInStaleMate(){
        //the player is not in check but can't do any move without leaving his king in a check (Has no legal moves left).
        return !isInCheck && !hasEscapeMoves();
    }

    public boolean isCastled(){
        return false;
    }

    public boolean isMoveLegal(final Move move) {
        return this.legalMoves.contains(move);
    }
    /*
    * here we will go through every legal move available for the player, and we will do each move of them on an "imaginary board".
    * if none of those moves were able to be done (because they leave the king in check for example), then the king has no way to escape th check.
    * if any one of these moves was able to be done (it ends the check "by blocking it or by capturing the attacking piece"), then the king has an escape.
    * if the player is able to make ANY legal move and after that move the king is no longer in check >>> hasEscapeMoves == true.
     */
    protected boolean hasEscapeMoves(){
        for (final Move move : legalMoves){
            final MoveTransition transition = makeMove(move);
            if (transition.getMoveStatus().isDone()){
                return true;
            }
        }
        return false;
    }
    /*
    * the idea of making a move is establishing a new board with the new move made on it, because the board is immutable.
    * And then returning the new board.
    */
    public MoveTransition makeMove(final Move move){
        if(!isMoveLegal(move)){
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
        }
        final Board transitionBoard = move.execute(); //the new board with the move made on it
        final Collection<Move> kingAttacks =
                Player.calculateAttacksOnTile(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
                transitionBoard.currentPlayer().getLegalMoves());
        //if the new move leaves the player in check:
        if (!kingAttacks.isEmpty()){
            return new MoveTransition(transitionBoard, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }
        return new MoveTransition(transitionBoard, move, MoveStatus.DONE); //It's a legal move.
    }

    public abstract Collection<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();
    protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentsLegals);
}
