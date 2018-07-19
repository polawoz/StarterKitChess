package com.capgemini.chess.algorithms.implementation;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.MoveValidator;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.generated.Board;

public class QueenMoveValidator implements MoveValidator {

	MoveType possibleMoveType;
	Board currentBoard;

	@Override
	public boolean isMovePossible(Coordinate from, Coordinate to) {


		BishopMoveValidator queenDiagonallMoveValidator = new BishopMoveValidator();
		RookMoveValidator queenStraightMoveValidator = new RookMoveValidator();

		queenDiagonallMoveValidator.setCurrentBoard(this.currentBoard);
		boolean possibleMoveIsDiagonall = queenDiagonallMoveValidator.isMovePossible(from, to);
		boolean possibleMoveIsStraight = false;

		if (possibleMoveIsDiagonall) {
			possibleMoveType = queenDiagonallMoveValidator.getTypeOfTheValidatedMove();
			return true;
		} else {
			queenStraightMoveValidator.setCurrentBoard(this.currentBoard);
			possibleMoveIsStraight = queenStraightMoveValidator.isMovePossible(from, to);
		}

		if (possibleMoveIsStraight) {
			possibleMoveType = queenStraightMoveValidator.getTypeOfTheValidatedMove();
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
