package com.capgemini.chess.algorithms.data;

import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.generated.Board;

public interface MoveValidator {

	boolean isMovePossible(Coordinate from, Coordinate to);

	MoveType getTypeOfTheValidatedMove();

	void setCurrentBoard(Board currentBoard);
	
	//ta metoda juz niepotrzebna
	default boolean isAnyMovePossible(Coordinate from) {

		boolean thereIsAPossibleMove;

		int figurePositionX = from.getX(); // X to kolumna
		int figurePositionY = from.getY();// Y to wiersz

		for (int column = 0; column < Board.SIZE; column++) {
			for (int row = 0; row < Board.SIZE; row++) {
				if (figurePositionX == column && figurePositionY == row) {
					continue;
				}

				Coordinate to = new Coordinate(row, column);
				thereIsAPossibleMove = isMovePossible(from, to);

				if (thereIsAPossibleMove) {
					return true;
				}
			}

		}

		return false;

	};

}
