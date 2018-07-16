package com.capgemini.chess.algorithms.data.enums;

/**
 * Chess piece types
 * 
 * @author Michal Bejm
 *
 */
public enum PieceType {
	KING,
	//
	// KING (krol)
	// Ruch: jedno pole w dowolnym kierunku (na ukos na boki przod tyl) +
	// nigdy nie moze sie przeniesc w szach
	// Atak:
	QUEEN,
	//
	// QUEEN (krolowa)
	// Ruch: dowolna liczba pol w dowolnym kierunku
	// Atak: capturuje pierwsza figure przeciwnika na ktora wpadnie
	BISHOP,
	//
	// BISHOP (goniec)
	// Ruch: moga przechodzic dowolna liczbe pol na ukos
	// Atak: atakuja (capturuja) pierwsza figure na ktora wpadna po drodze
	KNIGHT,
	//
	// KNIGHT (skoczek)
	// Ruch: w ksztalcie L - najpierw 2 pola w bok lub do przodu (nie na
	// ukos), a potem jedno pole o 90 stopni,
	// skoczek przeskakuje kazda figure po drodze
	// Atak: przeskakuje figury po drodze a atakuje(capture) figure
	// przeciwnika na ostatnim polu z wykonanego ruchu
	ROOK,
	//
	// ROOK (wieza)
	// Ruch: nie moga na ukos, ale moga dowolna liczbe pol do przodu tylu i
	// na boki
	// Atak: nie moga przeskakiwac figur ale zamiast tego atakuja(capture)
	// pierwsza figure na ktora wpadna
	PAWN;
	//
	// PAWN (pionek)
	// Ruch: jezeli pierwszy ruch danego pionka w rozgrywce to moze 2 do
	// przodu (albo 1)
	// pozniej tylko jeden do przodu
	// Atak (Capture): jedno pole na ukos przed nim - nie moze atakowac
	// pionkow bezposrednio przed soba (wiec nie moga sie ruszac do przodu)
	// jezeli pionek dotrze do konca planszy to moze zostac przemieniony na
	// dowolna inna figure (promocja)
	// przy pionku trzeba bedzie zmieniac status z atak na bicie
}
