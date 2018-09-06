package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MajorMove;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Rook extends Piece{
    private final static int[] CANDIDATE_MOVE_COORDINATES = { -8, -1, 1, 8 };

    public Rook(final Alliance alliance,
                final int piecePosition) {
        super(PieceType.ROOK, alliance, piecePosition, true);
    }
    public Rook(final Alliance alliance,
                final int piecePosition,
                final boolean isFirstMove) {
        super(PieceType.BISHOP, alliance, piecePosition, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
            int candidateDestinationCoordinate = this.piecePosition;
            while (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                if (isColumnExclusion(currentCandidateOffset, candidateDestinationCoordinate)) {
                    break;
                }
                candidateDestinationCoordinate += currentCandidateOffset;
                if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                    final Piece pieceAtDestination = board.getPiece(candidateDestinationCoordinate);
                    if (pieceAtDestination == null) {
                        legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                    } else {
                        final Alliance pieceAtDestinationAllegiance = pieceAtDestination.getPieceAllegiance();
                        if (this.pieceAlliance != pieceAtDestinationAllegiance) {
                            legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate,
                                    pieceAtDestination));
                        }
                        break;
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Rook movePiece(Move move) {
        return new Rook(move.getMovedPiece().getPieceAllegiance(), move.getDestinationCoordinate());
    }

    private static boolean isColumnExclusion(final int currentCandidate,
                                             final int candidateDestinationCoordinate) {
        return (BoardUtils.INSTANCE.FIRST_FILE.get(candidateDestinationCoordinate) && (currentCandidate == -1)) ||
                (BoardUtils.INSTANCE.EIGHTH_FILE.get(candidateDestinationCoordinate) && (currentCandidate == 1));
    }

    @Override
    public String toString() {
        return PieceType.ROOK.toString();
    }
}
