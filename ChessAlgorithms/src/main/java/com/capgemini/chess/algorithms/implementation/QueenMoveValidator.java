package com.capgemini.chess.algorithms.implementation;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.MoveValidator;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.generated.Board;

public class QueenMoveValidator implements MoveValidator {

	MoveType possibleMoveType;
	Board currentBoard;

	@Override
	public boolean isMovePossible(Coordinate from, Coordinate to) {
		// TODO Auto-generated method stub

		boolean isMovePossible = false;

		BishopMoveValidator queenDiagonallMoveValidator = new BishopMoveValidator();
		RookMoveValidator queenStraightMoveValidator = new RookMoveValidator();

		queenDiagonallMoveValidator.setCurrentBoard(this.currentBoard);
		boolean possibleMoveIsDiagonall = queenDiagonallMoveValidator.isMovePossible(from, to);
		boolean possibleMoveIsStraight = false;

		if (possibleMoveIsDiagonall) {
			isMovePossible = true;
			possibleMoveType = queenDiagonallMoveValidator.getTypeOfTheValidatedMove();
		} else {
			queenStraightMoveValidator.setCurrentBoard(this.currentBoard);
			possibleMoveIsStraight = queenStraightMoveValidator.isMovePossible(from, to);
		}

		if (possibleMoveIsStraight) {
			isMovePossible = true;
			possibleMoveType = queenStraightMoveValidator.getTypeOfTheValidatedMove();
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
