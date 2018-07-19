package com.capgemini.chess.algorithms.implementation;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.MoveValidator;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.generated.Board;

public class RookMoveValidator implements MoveValidator {

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
		boolean changeIsHorizontal = false;

		if (columnsDelta != 0) {
			changeIsHorizontal = true;
			if (rowsDelta != 0) {
				return false;
			}
		}

		boolean changeIsOneStepStraight = (Math.abs(columnsDelta) == 1 || Math.abs(rowsDelta) == 1);
		if (!changeIsOneStepStraight) {

			int checkedColumn;
			int checkedRow;
			int columnSingleIterationChange = 0;
			int rowSingleIterationChange = 0;
			int iterator = 0;
			int loopLimit = 0;

			if (changeIsHorizontal) {
				checkedRow = figurePositionY;
				loopLimit = Math.abs(columnsDelta) - 1;

				if (columnsDelta > 0) {
					checkedColumn = figurePositionX + 1;
					columnSingleIterationChange = 1;
				} else {
					checkedColumn = figurePositionX - 1;
					columnSingleIterationChange = -1;
				}
			} else {
				checkedColumn = figurePositionX;
				loopLimit = Math.abs(rowsDelta) - 1;

				if (rowsDelta > 0) {
					checkedRow = figurePositionY + 1;
					rowSingleIterationChange = 1;
				} else {
					checkedRow = figurePositionY - 1;
					rowSingleIterationChange = -1;
				}

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
			
		} else{
			moveIsPossible=true;
		}

	
		Piece pieceStandingOnToCoordinate = currentBoard.getPieceAt(to);
		if (pieceStandingOnToCoordinate == null) {
			possibleMoveType = MoveType.ATTACK;
		} else {
			possibleMoveType = MoveType.CAPTURE;
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
