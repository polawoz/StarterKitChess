package com.capgemini.chess.algorithms.implementation;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.MoveValidator;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.generated.Board;

public class BishopMoveValidator implements MoveValidator {

	MoveType possibleMoveType;
	Board currentBoard;

	@Override
	public boolean isMovePossible(Coordinate from, Coordinate to) {

		boolean moveIsPossible = false;
		int figurePositionX = from.getX();
		int figurePositionY = from.getY();
		int destinationPositionX = to.getX();
		int destinationPositionY = to.getY();

		int columnsDelta = destinationPositionX - figurePositionX;
		int rowsDelta = destinationPositionY - figurePositionY;

		if (figurePositionX == destinationPositionX || figurePositionY == destinationPositionY) {
			return false;
		}

		if (Math.abs(rowsDelta) != Math.abs(columnsDelta)) {
			return false;
		}

		if (Math.abs(rowsDelta) != 1) {

			int checkedColumn;
			int checkedRow;
			int columnSingleIterationChange;
			int rowSingleIterationChange;
			int iterator = 0;
			int loopLimit = Math.abs(columnsDelta) - 1;

			if (figurePositionX < destinationPositionX) {
				checkedColumn = figurePositionX + 1;
				columnSingleIterationChange = 1;
			} else {
				checkedColumn = figurePositionX - 1;
				columnSingleIterationChange = -1;
			}

			if (figurePositionY < destinationPositionY) {
				checkedRow = figurePositionY + 1;
				rowSingleIterationChange = 1;
			} else {
				checkedRow = figurePositionY - 1;
				rowSingleIterationChange = -1;
			}

			while (iterator < loopLimit) {

				Coordinate currentlyCheckedCoordinate = new Coordinate(checkedColumn, checkedRow);
				Piece pieceStandingOnCurrentlyCheckedCoordinate = currentBoard.getPieceAt(currentlyCheckedCoordinate);

				if (pieceStandingOnCurrentlyCheckedCoordinate != null) {

					return false;
				} else {

					moveIsPossible = true;
				}
				checkedColumn = checkedColumn + columnSingleIterationChange;
				checkedRow = checkedRow + rowSingleIterationChange;
				iterator++;

			}
		} else {
			moveIsPossible = true;
		}

		if (moveIsPossible) {
			Piece pieceStandingOnToCoordinate = currentBoard.getPieceAt(to);
			if (pieceStandingOnToCoordinate == null) {
				possibleMoveType = MoveType.ATTACK;
			} else {
				possibleMoveType = MoveType.CAPTURE;
			}
		}

		return moveIsPossible;
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
