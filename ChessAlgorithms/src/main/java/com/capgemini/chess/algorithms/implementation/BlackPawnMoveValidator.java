package com.capgemini.chess.algorithms.implementation;

import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.MoveValidator;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.generated.Board;

public class BlackPawnMoveValidator implements MoveValidator {

	MoveType possibleMoveType;
	Board currentBoard;
	int figurePositionX;
	int figurePositionY;
	int destinationPositionX;
	int destinationPositionY;
	Piece pieceStandingOnToCoordinate;

	@Override
	public boolean isMovePossible(Coordinate from, Coordinate to) {

		pieceStandingOnToCoordinate = currentBoard.getPieceAt(to);
		figurePositionX = from.getX();
		figurePositionY = from.getY();
		destinationPositionX = to.getX();
		destinationPositionY = to.getY();

		boolean attemptedMoveIsBackwards = (figurePositionY - destinationPositionY) < 0;
		if (attemptedMoveIsBackwards) {
			return false;
		}

		if (figurePositionX == destinationPositionX) {

			boolean attemptedVerticallMoveIsPossible = checkIfAttemptedVerticallMoveIsPossible();
			if (attemptedVerticallMoveIsPossible) {
				possibleMoveType = MoveType.ATTACK;
				return true;
			} else {
				return false;
			}
		}

		if (figurePositionX != destinationPositionX) {

			boolean attemptedMoveIsOneStepForwardDiagonall = checkIfAttemptedMoveIsOneStepForwardDiagonall();
			if (attemptedMoveIsOneStepForwardDiagonall) {

				if (pieceStandingOnToCoordinate != null) {
					possibleMoveType = MoveType.CAPTURE;
					return true;

				} else {

					boolean attemptedMoveIsEnPassant = checkIfAttemptedMoveIsEnPassant();
					if (attemptedMoveIsEnPassant) {
						possibleMoveType = MoveType.EN_PASSANT;
						return true;
					} else {
						return false;
					}
				}
			} else {
				return false;
			}
		}

		return false;
	}

	private boolean checkIfAttemptedVerticallMoveIsPossible() {

		if (pieceStandingOnToCoordinate != null) {
			return false;
		}

		boolean attemptedMoveIsBiggerThanOneStep = (figurePositionY - destinationPositionY) > 1;
		if (attemptedMoveIsBiggerThanOneStep) {

			if (figurePositionY == 6 && destinationPositionY == 4) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	private boolean checkIfAttemptedMoveIsEnPassant() {

		List<Move> moveHistory = currentBoard.getMoveHistory();

		Move lastMove = null;
		if (!moveHistory.isEmpty()) {
			lastMove = moveHistory.get(moveHistory.size() - 1);

		}

		if (lastMove != null) {
			int lastMoveFromY = lastMove.getFrom().getY();
			int lastMoveToY = lastMove.getTo().getY();
			int lastMoveFromX = lastMove.getFrom().getX();
			int lastMoveToX = lastMove.getTo().getX();

			Piece lastMovedPiece = lastMove.getMovedPiece();

			if (lastMovedPiece.equals(Piece.WHITE_PAWN) && lastMoveFromY == 1 && lastMoveToY == 3
					&& ((lastMoveFromX == figurePositionX - 1 && lastMoveToX == figurePositionX - 1)
							|| (lastMoveFromX == figurePositionX + 1 && lastMoveToX == figurePositionX + 1))) {

				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	private boolean checkIfAttemptedMoveIsOneStepForwardDiagonall() {

		if ((destinationPositionX == (figurePositionX + 1) && destinationPositionY == (figurePositionY - 1))
				|| (destinationPositionX == (figurePositionX - 1) && destinationPositionY == (figurePositionY - 1))) {
			return true;
		}

		return false;
	}

	@Override
	public MoveType getTypeOfTheValidatedMove() {

		return possibleMoveType;
	}

	@Override
	public void setCurrentBoard(Board currentBoard) {

		this.currentBoard = currentBoard;

	}

}
