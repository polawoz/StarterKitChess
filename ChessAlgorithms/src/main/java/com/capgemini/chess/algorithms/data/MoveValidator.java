package com.capgemini.chess.algorithms.data;

import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.generated.Board;

public interface MoveValidator {

	boolean isMovePossible(Coordinate from, Coordinate to);

	MoveType getTypeOfTheValidatedMove();

	void setCurrentBoard(Board currentBoard);
	
	

}
