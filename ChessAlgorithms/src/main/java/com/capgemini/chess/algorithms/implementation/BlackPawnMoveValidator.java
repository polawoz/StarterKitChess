package com.capgemini.chess.algorithms.implementation;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.MoveValidator;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.generated.Board;

public class BlackPawnMoveValidator implements MoveValidator {

	MoveType possibleMoveType;
	Board currentBoard;
	
	
	
	@Override
	public boolean isMovePossible(Coordinate from, Coordinate to) {
	
		boolean isMovePossible = false;
		Piece pieceStandingOnToCoordinate = currentBoard.getPieceAt(to);
	
		
		int figurePositionX = from.getX();
		int figurePositionY = from.getY();

		int destinationPositionX = to.getX();
		int destinationPositionY = to.getY();
		
		//sprawdzenie czy jest to pole na ukos pionka na ktorym musi dojsc do bicia
		if(figurePositionX!=destinationPositionX){
			if((destinationPositionX==(figurePositionX-1) && destinationPositionY==(figurePositionY+1)) || 
					(destinationPositionX==(figurePositionX+1)&& destinationPositionY==(figurePositionY+1))){
				
				if (pieceStandingOnToCoordinate == null) {
					return false;
				} else {
					isMovePossible=true;
					possibleMoveType = MoveType.CAPTURE;
				}
			}
			else{
				return false;
			}
		}
		
		
		
		//tutaj wiem juz, ze kolumna sie nie zmienia, wiec sprawdzam czy nie jest to ruch o 2 pola
		//(mozliwy tylko, gdy pionek stoi na wierszu nr 1)
		//oraz czy 
		if((destinationPositionY-figurePositionY)>1){
			
			if(figurePositionY>1){
				return false;
			}
			else if((destinationPositionY-figurePositionY)!=2){
				return false;
			}
			else if(pieceStandingOnToCoordinate!=null){
				return false;
			}
			else{
				isMovePossible=true;
				possibleMoveType=MoveType.ATTACK;
			}	
		}

		
		
		
		return isMovePossible;
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
