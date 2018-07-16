package com.capgemini.chess.algorithms.data.enums;

/**
 * Types of moves
 * 
 * @author Michal Bejm
 *
 */
public enum MoveType {
	ATTACK,
	//An aggressive action on a part of the chessboard, or to threaten the capture of a piece or pawn
	
	CAPTURE,
	//A move by a pawn or piece that removes from the board the opponent’s pawn or piece. 
	//The capturing piece then occupies the square of the captured piece (except in the case of a capture that is done en passant).
	
	CASTLING,  //roszada
	//Polega na tym ze w trakcie gry gracz moze przesunac swojego krola 2 pola w jedna strone,
	//a potem przesuwa ROOK (wieza) z tego kierunku (rogu) na druga strone bezposrednio obok krola
	//Musza byc jednak spelnione warunki:
	//to musi byc pierwszy ruch krola
	//to musi byc pierwszy ruch wiezy
	//nie moga byc zadne figury miedzy krolem a wieza
	//krol nie moze byc w szachu ani przechodzic przez szacha
	
	
	
	EN_PASSANT;
	// Jezeli pionek przesuwa sie dwa pola w trakcie swojego pierwszego ruchu i czyniac to laduje bezposrednio obok pionka przeciwnika
	//ktory normalnie nie moze go zbic (bo na ukos jedno pole przed soba moze zbijac) ALE
	//pionek przeciwnika moze zbic tego pionka ktory sie poruszyl "as it passes by"
	//ale musi to zrobic od razu (inaczej traci szanse) po tym ruchu przeciwnika (tego o 2 pola) stajac na ukos bezposrednio za tym innym pionkiem
	//The pawn can be taken as if it had advanced only one square
}
