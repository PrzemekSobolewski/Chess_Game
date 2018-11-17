package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.board.Move.*;

public class Queen extends Piece {
    private static final int[] CANDIDATE_MOVE_VECTOR_COORDINATES ={-9,-8,-7,-1,1,7,8,9};

    public Queen(final Alliance pieceAlliance, final int piecePosition) {
        super(PieceType.QUEEN, piecePosition, pieceAlliance,true);
    }
    public Queen(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove) {
        super(PieceType.QUEEN, piecePosition, pieceAlliance, isFirstMove);
    }
    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();
        for (final int candidateCoordinateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES) {
            int candidateDestinationCoordiante = this.piecePosition;
            while (BoardUtils.isValidTileCoordinate(candidateDestinationCoordiante)) {
                if (isFirstCoolumnExlusion(candidateDestinationCoordiante, candidateCoordinateOffset) || isEightCoolumnExlusion(candidateDestinationCoordiante, candidateCoordinateOffset)) {
                    break;
                }
                candidateDestinationCoordiante += candidateCoordinateOffset;
                if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordiante)) {
                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordiante);

                    if (!candidateDestinationTile.isTileOccupied()) {
                        legalMoves.add(new MajorMove(board, this, candidateDestinationCoordiante));
                    } else {
                        final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                        final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                        if (this.pieceAlliance != pieceAlliance) {
                            legalMoves.add(new MajorAttackMove(board, this, candidateDestinationCoordiante, pieceAtDestination));
                        }
                        break;
                    }

                }
            }

        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Piece movePiece(Move move) {
        return new Queen(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    @Override
    public String toString(){
        return PieceType.QUEEN.toString();
    }

    private static boolean isFirstCoolumnExlusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == 7||candidateOffset==-1);
    }

    private static boolean isEightCoolumnExlusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == 9 || candidateOffset == -7||candidateOffset==1);
    }

}
