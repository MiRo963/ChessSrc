package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.board.Move.*;

public class Pawn extends Piece {
    private final static int[] CANDIDATE_MOVE_COORDINATES = {8, 16, 7, 9};

    public Pawn(final Alliance allegiance,
                final int piecePosition) {
        super(PieceType.PAWN, allegiance, piecePosition);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
            int candidateDestinationCoordinate = this.piecePosition + (this.getPieceAllegiance().getDirection() * currentCandidateOffset); //see Alliance.getDirection()
            if (!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                continue;
            }
            //Normal one step move
            if (currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isOccupied()) {
                //TODO deal with promotions
                legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
            }
            //Pawn jump:
            else if (currentCandidateOffset == 16 && this.isFirstMove() &&
                    (BoardUtils.INSTANCE.SEVENTH_RANK.get(this.piecePosition) && this.getPieceAllegiance().isBlack()) ||
                    (BoardUtils.INSTANCE.SECOND_RANK.get(this.piecePosition) && this.getPieceAllegiance().isWhite())) {
                final int behindCandidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * 8);
                //Making sure that there is no other piece standing between the pawn and the tail
                if (!board.getTile(behindCandidateDestinationCoordinate).isOccupied() &&
                        !board.getTile(candidateDestinationCoordinate).isOccupied()) {
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                }
            }
            //Attack move:
            else if(currentCandidateOffset == 7 &&
                    !((BoardUtils.INSTANCE.EIGHTH_RANK.get(this.piecePosition) && this.pieceAlliance.isWhite()) ||
                     (BoardUtils.INSTANCE.FIRST_RANK.get(this.piecePosition) && this.pieceAlliance.isBlack()))) {
                if (board.getPiece(candidateDestinationCoordinate) != null) {
                    final Piece pieceOnCandidate = board.getPiece(candidateDestinationCoordinate);
                        if (this.pieceAlliance != pieceOnCandidate.pieceAlliance) {
                            legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                }
            }
            else if (currentCandidateOffset == 9 && //Exceptions for the capturing move of a pawn
                    !((BoardUtils.INSTANCE.FIRST_FILE.get(this.piecePosition) && this.pieceAlliance.isWhite()) ||
                            (BoardUtils.INSTANCE.EIGHTH_FILE.get(this.piecePosition) && this.pieceAlliance.isBlack()))) {
                if (board.getPiece(candidateDestinationCoordinate) != null) {
                    final Piece pieceOnCandidate = board.getPiece(candidateDestinationCoordinate);
                    if (this.pieceAlliance != pieceOnCandidate.getPieceAllegiance()) {
                        legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Pawn movePiece(Move move) {
        return new Pawn(move.getMovedPiece().getPieceAllegiance(), move.getDestinationCoordinate());
    }

    @Override
    public String toString() {
        return PieceType.PAWN.toString();
    }
}
