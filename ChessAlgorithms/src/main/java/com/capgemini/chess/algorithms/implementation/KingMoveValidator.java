package com.capgemini.chess.algorithms.implementation;

import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.MoveValidator;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;

public class KingMoveValidator implements MoveValidator {

	MoveType possibleMoveType;
	Board currentBoard;
	
	@Override
	public boolean isMovePossible(Coordinate from, Coordinate to) {
		// TODO Auto-generated method stub
		
		
		boolean isMovePossible = false;
		boolean castlingIsPossible=false;

		int figurePositionX = from.getX();
		int figurePositionY = from.getY();

		int destinationPositionX = to.getX();
		int destinationPositionY = to.getY();
		
		int columnsDelta = destinationPositionX - figurePositionX; 
		int rowsDelta = destinationPositionY - figurePositionY;
		
		
		
		//ruchy pionowe i poziome
		if(Math.abs(columnsDelta)==1 && Math.abs(rowsDelta)==0){
			isMovePossible=true;
		}
		else if(Math.abs(columnsDelta)==0 && Math.abs(rowsDelta)==1){
			isMovePossible=true;
		}//ruchy na ukos
		else if(Math.abs(columnsDelta)==1 && Math.abs(rowsDelta)==1){
			isMovePossible=true;
		}
		else if(Math.abs(columnsDelta)>1){//ruchy w poziomie dalsze niz jeden
			castlingIsPossible = checkIfCastlingIsPossible(from, to);
			if(castlingIsPossible){
				isMovePossible=true;
			}
			else{
				isMovePossible=false;
			}
		}
		
	
		//sprawdzenie jaki typ ruchu
		if(isMovePossible){
			if(castlingIsPossible){
				possibleMoveType=MoveType.CASTLING;
			}
			else if(currentBoard.getPieceAt(to)==null){
				possibleMoveType=MoveType.ATTACK;
			}
			else if(currentBoard.getPieceAt(to)!=null){
				possibleMoveType=MoveType.CAPTURE;
			}
			
		}
		
		return isMovePossible;
	}

	private boolean checkIfCastlingIsPossible(Coordinate from, Coordinate to) {
		
		List<Move> moveHistory= currentBoard.getMoveHistory();
		
		PieceType typeOfPieceStandingOnFromCoordinate = currentBoard.getPieceAt(from).getType();
		Color colorOfPieceStandingOnFromCoordinate = currentBoard.getPieceAt(from).getColor();
		
		//sprawdzenie czy wieza lub krol nie byly juz wczesniej ruszane
		if (!moveHistory.isEmpty()) {
			
			for(Move move : moveHistory){
				if(move.getMovedPiece().getType().equals(typeOfPieceStandingOnFromCoordinate)
				|| (move.getMovedPiece().getType().equals(PieceType.ROOK)&& move.getMovedPiece().getColor().equals(colorOfPieceStandingOnFromCoordinate))){
					return false;
				}
			
			}

		}
		
		//sprawdzenie czy cos nie stoi pomiedzy krolem a wieza
		
		//sprawdzenie czy krol jest w szachu lub przechodzi przez szach
		
		
		return false;
		
		
	}

	@Override
	public MoveType getTypeOfTheValidatedMove() {
		
		return possibleMoveType;
	}

	@Override
	public void setCurrentBoard(Board currentBoard) {
		
		this.currentBoard=currentBoard;
	}
	
	
	


}
