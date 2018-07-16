package com.capgemini.chess.algorithms.implementation;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.MoveValidator;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.generated.Board;

public class KnightMoveValidator implements MoveValidator {

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
		// Knight

		if (destinationPositionX > figurePositionX && destinationPositionY > figurePositionY) {

			if (destinationPositionX == figurePositionX + 2 && destinationPositionY == figurePositionY + 1) {
				isMovePossible = true;
			} else if (destinationPositionX == figurePositionX + 1 && destinationPositionY == figurePositionY + 2) {
				isMovePossible = true;
			} else
				return false;
		}

		if (destinationPositionX < figurePositionX && destinationPositionY < figurePositionY) {

			if (destinationPositionX == figurePositionX - 2 && destinationPositionY == figurePositionY - 1) {
				isMovePossible = true;
			} else if (destinationPositionX == figurePositionX - 1 && destinationPositionY == figurePositionY - 2) {
				isMovePossible = true;
			} else
				return false;
		}

		if (destinationPositionX > figurePositionX && destinationPositionY < figurePositionY) {

			if (destinationPositionX == figurePositionX + 2 && destinationPositionY == figurePositionY - 1) {
				isMovePossible = true;
			} else if (destinationPositionX == figurePositionX + 1 && destinationPositionY == figurePositionY - 2) {
				isMovePossible = true;
			} else
				return false;
		}
		
		
		if (destinationPositionX < figurePositionX && destinationPositionY > figurePositionY) {

			if (destinationPositionX == figurePositionX - 2 && destinationPositionY == figurePositionY + 1) {
				isMovePossible = true;
			} else if (destinationPositionX == figurePositionX - 1 && destinationPositionY == figurePositionY + 2) {
				isMovePossible = true;
			} else
				return false;
		}
	
		
		
		//sprawdzanie czy atak czy zbicie
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
