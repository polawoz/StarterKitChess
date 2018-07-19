package com.capgemini.chess.algorithms.implementation;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.MoveValidator;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.generated.Board;

public class RookMoveValidator implements MoveValidator {

	MoveType possibleMoveType;
	Board currentBoard;

	@Override
	public boolean isMovePossible(Coordinate from, Coordinate to) {
		// TODO Auto-generated method stub

		boolean isMovePossible = false;

		int figurePositionX = from.getX();
		int figurePositionY = from.getY();

		int destinationPositionX = to.getX();
		int destinationPositionY = to.getY();

		// sprawdzenie czy koordynat 'to' jest zgodny ze sposobem poruszania
		// Rook
		int columnsDelta = destinationPositionX - figurePositionX;
		int rowsDelta = destinationPositionY - figurePositionY;
		boolean changeIsHorizontal = false;

		boolean changeIsOneStep = (Math.abs(columnsDelta) == 1 || Math.abs(rowsDelta) == 1);

		if (columnsDelta != 0) {
			changeIsHorizontal = true;
			if (rowsDelta != 0) {
				return false;
			}
		}

		if (!changeIsOneStep) {

			// sprawdzenie czy po drodze nie stoi jakas figura - przygotowanie
			// parametrow do petli
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
					isMovePossible = true;
				}
				checkedColumn = checkedColumn + columnSingleIterationChange;
				checkedRow = checkedRow + rowSingleIterationChange;
				iterator++;

			}
			
			
		} else{
			isMovePossible=true;
		}

		// sprawdzenie czy na polu docelowym stoi figura przeciwnika (capture)
		// lub czy jest puste (attack)
		Piece pieceStandingOnToCoordinate = currentBoard.getPieceAt(to);
		if (pieceStandingOnToCoordinate == null) {
			possibleMoveType = MoveType.ATTACK;
		} else {
			possibleMoveType = MoveType.CAPTURE;
		}

		return isMovePossible;
	}

	@Override
	public MoveType getTypeOfTheValidatedMove() {
		// TODO Auto-generated method stub
		return possibleMoveType;
	}

	@Override
	public void setCurrentBoard(Board currentBoard) {
		// TODO Auto-generated method stub
		this.currentBoard = currentBoard;
	}

}
