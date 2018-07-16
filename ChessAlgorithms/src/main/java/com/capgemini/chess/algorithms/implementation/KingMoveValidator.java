package com.capgemini.chess.algorithms.implementation;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.MoveValidator;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.generated.Board;

public class KingMoveValidator implements MoveValidator {

	MoveType possibleMoveType;
	Board currentBoard;
	
	@Override
	public boolean isMovePossible(Coordinate from, Coordinate to) {
		// TODO Auto-generated method stub
		
		boolean moveIsAttack=false;
		boolean moveIsCapture=true;
		boolean moveIsCastling=false;
		
		boolean isMovePossible=true;
		
		if(isMovePossible){
			if(moveIsAttack){
				possibleMoveType=MoveType.ATTACK;
			}
			
			else if(moveIsCapture){
				possibleMoveType=MoveType.CAPTURE;
			}
			
			else if(moveIsCastling){
				possibleMoveType=MoveType.CASTLING;
			}
				
			
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
		this.currentBoard=currentBoard;
	}
	
	
	


}
