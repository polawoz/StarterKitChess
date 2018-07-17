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
	Move lastMove;
	int figurePositionX;
	int figurePositionY;
	int destinationPositionX;
	int destinationPositionY;
	Piece pieceStandingOnToCoordinate;

	@Override
	public boolean isMovePossible(Coordinate from, Coordinate to) {

		List<Move> moveHistory = currentBoard.getMoveHistory();

		if (!moveHistory.isEmpty()) {
			// tutaj przekazanie ostatniego ruchu

			lastMove = moveHistory.get(moveHistory.size() - 1);

		}

		boolean isMovePossible = false;
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

			boolean attemptedVerticallMoveIsPossible = checkIfAttemptedVerticallMoveIsPossible(from, to);
			if (attemptedVerticallMoveIsPossible) {
				isMovePossible = true;
				possibleMoveType = MoveType.ATTACK;
			} else {
				return false;
			}
		}

		if (figurePositionX != destinationPositionX) {

			boolean attemptedMoveIsOneStepForwardDiagonall = checkIfAttemptedMoveIsOneStepForwardDiagonall(from, to);
			if (attemptedMoveIsOneStepForwardDiagonall) {

				if (pieceStandingOnToCoordinate != null) {
					isMovePossible = true;
					possibleMoveType = MoveType.CAPTURE;

				} else {

					boolean attemptedMoveIsEnPassant = checkIfAttemptedMoveIsEnPassant(from);
					if (attemptedMoveIsEnPassant) {
						isMovePossible = true;
						possibleMoveType = MoveType.EN_PASSANT;
					} else {
						return false;
					}
				}
			} else {
				return false;
			}
		}

		return isMovePossible;
	}

	private boolean checkIfAttemptedVerticallMoveIsPossible(Coordinate from, Coordinate to) {

		if (pieceStandingOnToCoordinate != null) {
			return false;
		}

		boolean attemptedMoveIsBiggerThanOneStep = (figurePositionY - destinationPositionY) > 1;
		if (attemptedMoveIsBiggerThanOneStep) {
			// pozwalam na bycie bigger than one step tylko jesli (wykonuje
			// sprawdzenie):

			if (figurePositionY == 6 && destinationPositionY == 4) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	private boolean checkIfAttemptedMoveIsEnPassant(Coordinate from) {

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

	private boolean checkIfAttemptedMoveIsOneStepForwardDiagonall(Coordinate from, Coordinate to) {

		if ((destinationPositionX == (figurePositionX + 1) && destinationPositionY == (figurePositionY - 1))
				|| (destinationPositionX == (figurePositionX - 1) && destinationPositionY == (figurePositionY - 1))) {
			return true;
		}

		return false;
	}

	public void setLastMove(Move lastMove) {
		this.lastMove = lastMove;

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
