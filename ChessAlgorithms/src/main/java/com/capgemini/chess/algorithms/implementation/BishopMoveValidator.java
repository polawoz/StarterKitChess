package com.capgemini.chess.algorithms.implementation;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.MoveValidator;
import com.capgemini.chess.algorithms.data.enums.BoardState;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.generated.Board;

public class BishopMoveValidator implements MoveValidator {

	MoveType possibleMoveType;
	Board currentBoard;

	@Override
	public boolean isMovePossible(Coordinate from, Coordinate to) {


		boolean isMovePossible = false;

		int figurePositionX = from.getX();
		int figurePositionY = from.getY();

		int destinationPositionX = to.getX();
		int destinationPositionY = to.getY();

		// sprawdzenie czy koordynat 'to' jest zgodny ze sposobem poruszania
		// Bishopa
		int columnsDelta = destinationPositionX - figurePositionX;
		int rowsDelta = destinationPositionY - figurePositionY;

		if (figurePositionX == destinationPositionX || figurePositionY == destinationPositionY) {
			return false;
		}

		if ((figurePositionX < destinationPositionX && figurePositionY < destinationPositionY)
				|| (figurePositionX > destinationPositionX && figurePositionY > destinationPositionY)) {
			if (columnsDelta != rowsDelta) {
				return false;
			}
		} else {
			if (columnsDelta != -rowsDelta) {
				return false;
			}
		}

		// sprawdzenie czy po drodze nie stoi jakas figura - przygotowanie
		// parametrow do petli
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

		// nie bedzie IndexOutOfBounds, bo chcemy zeby sprawdzilo zawartosc pol
		// wszystkich po drodze oprocz pola finalnego

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
	
	
	public void kingInCheckValidation(){
		
		if(currentBoard.getState().equals(BoardState.CHECK)){
			
			
		}
		
	}




}
