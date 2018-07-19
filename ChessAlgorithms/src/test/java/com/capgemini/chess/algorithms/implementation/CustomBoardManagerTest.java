package com.capgemini.chess.algorithms.implementation;

import static org.junit.Assert.*;

import org.junit.Test;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.MoveValidator;
import com.capgemini.chess.algorithms.data.enums.BoardState;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;
import com.capgemini.chess.algorithms.implementation.exceptions.KingInCheckException;

public class CustomBoardManagerTest {

	// ----------------------------------------------------------------------------------------
	// BISHOP
	@Test
	public void testShouldReturnTrueWhenCheckingIfThreeStepsDiagonallMoveIsPossibleForBishop() {

		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(7, 0));
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(1, 7));
		board.setPieceAt(Piece.WHITE_BISHOP, new Coordinate(3, 3));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(1, 1));

		// when
		MoveValidator moveValidator = new BishopMoveValidator();
		moveValidator.setCurrentBoard(board);
		boolean result = moveValidator.isMovePossible(new Coordinate(3, 3), new Coordinate(5, 1));
		MoveType checkedMoveType = moveValidator.getTypeOfTheValidatedMove();

		// then
		assertEquals(true, result);
		assertEquals(MoveType.ATTACK, checkedMoveType);

	}

	@Test
	public void testShouldPerformThreeStepsDiagonallMoveWithBishop() throws InvalidMoveException {

		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(7, 0));
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(1, 7));
		board.setPieceAt(Piece.WHITE_BISHOP, new Coordinate(3, 3));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(1, 1));

		// when
		BoardManager boardManager = new BoardManager(board);
		Move move = boardManager.performMove(new Coordinate(3, 3), new Coordinate(6, 0));

		// then
		assertEquals(MoveType.ATTACK, move.getType());
		assertEquals(Piece.WHITE_BISHOP, move.getMovedPiece());
	}

	@Test
	public void testShouldPerformThreeStepsDiagonallCaptureWithBishop() throws InvalidMoveException {

		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(7, 0));
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(1, 7));
		board.setPieceAt(Piece.WHITE_BISHOP, new Coordinate(3, 3));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(0, 0));

		// when
		BoardManager boardManager = new BoardManager(board);
		Move move = boardManager.performMove(new Coordinate(3, 3), new Coordinate(0, 0));

		// then
		assertEquals(MoveType.CAPTURE, move.getType());
		assertEquals(Piece.WHITE_BISHOP, move.getMovedPiece());
	}

	@Test
	public void testShouldPerformTwoStepsDiagonallCaptureWithBishop() throws InvalidMoveException {

		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(7, 0));
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(1, 7));
		board.setPieceAt(Piece.WHITE_BISHOP, new Coordinate(3, 3));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(1, 1));

		// when
		BoardManager boardManager = new BoardManager(board);
		Move move = boardManager.performMove(new Coordinate(3, 3), new Coordinate(1, 1));

		// then
		assertEquals(MoveType.CAPTURE, move.getType());
		assertEquals(Piece.WHITE_BISHOP, move.getMovedPiece());
	}

	@Test
	public void testShouldThrowInvalidMoveExceptionWhenPerformingBishopLeapOver() throws InvalidMoveException {

		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(7, 0));
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(1, 7));
		board.setPieceAt(Piece.WHITE_BISHOP, new Coordinate(3, 3));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(1, 1));

		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(3, 3), new Coordinate(0, 0));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		// then
		assertTrue(exceptionThrown);

	}

	@Test
	public void testShouldPerformMoveBishopCaptureOneSpotFromBishop() throws InvalidMoveException {

		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(2, 0));
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(0, 7));
		board.setPieceAt(Piece.WHITE_BISHOP, new Coordinate(4, 4));
		board.setPieceAt(Piece.BLACK_PAWN, new Coordinate(7, 1));
		board.setPieceAt(Piece.WHITE_PAWN, new Coordinate(6, 6));
		board.setPieceAt(Piece.BLACK_PAWN, new Coordinate(5, 3));

		// when
		BoardManager boardManager = new BoardManager(board);
		Move move = boardManager.performMove(new Coordinate(4, 4), new Coordinate(5, 3));

		// then
		assertEquals(MoveType.CAPTURE, move.getType());
		assertEquals(Piece.WHITE_BISHOP, move.getMovedPiece());

	}

	@Test
	public void testShouldPerformMoveBishopOneStepCapture() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(2, 0));
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(0, 7));
		board.setPieceAt(Piece.WHITE_BISHOP, new Coordinate(4, 4));
		board.setPieceAt(Piece.BLACK_PAWN, new Coordinate(7, 1));
		board.setPieceAt(Piece.WHITE_PAWN, new Coordinate(6, 6));
		board.setPieceAt(Piece.BLACK_PAWN, new Coordinate(5, 3));

		// when
		BoardManager boardManager = new BoardManager(board);
		Move move = boardManager.performMove(new Coordinate(4, 4), new Coordinate(5, 3));

		// then
		assertEquals(MoveType.CAPTURE, move.getType());
		assertEquals(Piece.WHITE_BISHOP, move.getMovedPiece());
	}

	@Test
	public void testShouldPerformMoveBishopOneStepAttack() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(2, 0));
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(0, 7));
		board.setPieceAt(Piece.WHITE_BISHOP, new Coordinate(4, 4));
		board.setPieceAt(Piece.BLACK_PAWN, new Coordinate(7, 1));
		board.setPieceAt(Piece.WHITE_PAWN, new Coordinate(6, 6));
		board.setPieceAt(Piece.BLACK_PAWN, new Coordinate(5, 3));

		// when
		BoardManager boardManager = new BoardManager(board);
		Move move = boardManager.performMove(new Coordinate(4, 4), new Coordinate(3, 3));

		// then
		assertEquals(MoveType.ATTACK, move.getType());
		assertEquals(Piece.WHITE_BISHOP, move.getMovedPiece());
	}

	@Test
	public void testShouldPerformBishopCapture() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_BISHOP, new Coordinate(5, 0));
		board.setPieceAt(Piece.BLACK_PAWN, new Coordinate(7, 2));

		// when
		BoardManager boardManager = new BoardManager(board);
		Move move = boardManager.performMove(new Coordinate(5, 0), new Coordinate(7, 2));

		// then
		assertEquals(MoveType.CAPTURE, move.getType());
		assertEquals(Piece.WHITE_BISHOP, move.getMovedPiece());
	}

	// ----------------------------------------------------------------------------------------
	// ROOK

	@Test
	public void testShouldReturnTrueWhenCheckingIfThreeStepsStraightMoveIsPossibleForRook() {

		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(7, 0));
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(1, 7));
		board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(3, 3));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(1, 1));

		// when

		MoveValidator moveValidator = new RookMoveValidator();
		moveValidator.setCurrentBoard(board);
		boolean result = moveValidator.isMovePossible(new Coordinate(3, 3), new Coordinate(3, 0));
		MoveType checkedMoveType = moveValidator.getTypeOfTheValidatedMove();

		// then
		assertEquals(true, result);
		assertEquals(MoveType.ATTACK, checkedMoveType);

	}

	@Test
	public void testShouldReturnTrueWhenCheckingIfOneStepsStraightCaptureIsPossibleForRook() {

		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(7, 0));
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(1, 7));
		board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(3, 3));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(3, 2));

		// when

		MoveValidator moveValidator = new RookMoveValidator();
		moveValidator.setCurrentBoard(board);
		boolean result = moveValidator.isMovePossible(new Coordinate(3, 3), new Coordinate(3, 2));
		MoveType checkedMoveType = moveValidator.getTypeOfTheValidatedMove();

		// then
		assertEquals(true, result);
		assertEquals(MoveType.CAPTURE, checkedMoveType);

	}

	@Test
	public void testShouldReturnTrueWhenCheckingIfTwoStepsStraightCaptureIsPossibleForRook()
			throws InvalidMoveException {

		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(0, 0));
		board.setPieceAt(Piece.BLACK_PAWN, new Coordinate(0, 3));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(0, 4));
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(0, 2));
		board.setPieceAt(Piece.BLACK_PAWN, new Coordinate(1, 4));
		board.setPieceAt(Piece.WHITE_KNIGHT, new Coordinate(1, 6));
		board.setPieceAt(Piece.WHITE_PAWN, new Coordinate(3, 1));
		board.setPieceAt(Piece.WHITE_BISHOP, new Coordinate(3, 4));
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(7, 0));
		BoardManager boardManager = new BoardManager(board);

		// when

		Move move = boardManager.performMove(new Coordinate(0, 0), new Coordinate(0, 2));

		// then
		assertEquals(MoveType.CAPTURE, move.getType());
		assertEquals(Piece.WHITE_ROOK, move.getMovedPiece());

	}

	@Test
	public void testShouldReturnFalseWhenCheckingIfThreeStepsStraightLeapOverMoveIsPossibleForRook() {

		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(7, 0));
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(1, 7));
		board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(3, 3));
		board.setPieceAt(Piece.BLACK_KNIGHT, new Coordinate(3, 1));

		// when

		MoveValidator moveValidator = new RookMoveValidator();
		moveValidator.setCurrentBoard(board);
		boolean result = moveValidator.isMovePossible(new Coordinate(3, 3), new Coordinate(3, 0));

		// then
		assertEquals(false, result);

	}

	@Test
	public void testPerformMoveRookAttack() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.getMoveHistory().add(createDummyMove(board));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(1, 4));

		// when
		BoardManager boardManager = new BoardManager(board);
		Move move = boardManager.performMove(new Coordinate(1, 4), new Coordinate(5, 4));

		// then
		assertEquals(MoveType.ATTACK, move.getType());
		assertEquals(Piece.BLACK_ROOK, move.getMovedPiece());
	}

	// ----------------------------------------------------------------------------------------
	// QUEEN

	@Test
	public void testShouldReturnTrueWhenCheckingIfThreeStepsStraightMoveIsPossibleForQueen() {

		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(7, 0));
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(1, 7));
		board.setPieceAt(Piece.WHITE_QUEEN, new Coordinate(3, 3));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(1, 1));

		// when

		MoveValidator moveValidator = new QueenMoveValidator();
		moveValidator.setCurrentBoard(board);
		boolean result = moveValidator.isMovePossible(new Coordinate(3, 3), new Coordinate(3, 0));
		MoveType checkedMoveType = moveValidator.getTypeOfTheValidatedMove();

		// then
		assertEquals(true, result);
		assertEquals(MoveType.ATTACK, checkedMoveType);

	}

	@Test
	public void testShouldReturnTrueWhenCheckingIfOneStepsStraightCaptureIsPossibleForQueen() {

		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(7, 0));
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(1, 7));
		board.setPieceAt(Piece.WHITE_QUEEN, new Coordinate(3, 3));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(3, 2));

		// when

		MoveValidator moveValidator = new QueenMoveValidator();
		moveValidator.setCurrentBoard(board);
		boolean result = moveValidator.isMovePossible(new Coordinate(3, 3), new Coordinate(3, 2));
		MoveType checkedMoveType = moveValidator.getTypeOfTheValidatedMove();

		// then
		assertEquals(true, result);
		assertEquals(MoveType.CAPTURE, checkedMoveType);

	}

	// ----------------------------------------------------------------------------------------
	// KNIGHT

	@Test
	public void testShouldPerformCaptureWithKnight() throws InvalidMoveException {

		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(0, 0));
		board.setPieceAt(Piece.BLACK_PAWN, new Coordinate(0, 3));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(0, 4));
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(2, 4));
		board.setPieceAt(Piece.BLACK_PAWN, new Coordinate(1, 4));
		board.setPieceAt(Piece.WHITE_KNIGHT, new Coordinate(1, 6));
		board.setPieceAt(Piece.WHITE_PAWN, new Coordinate(3, 1));
		board.setPieceAt(Piece.WHITE_BISHOP, new Coordinate(3, 4));
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(7, 0));
		BoardManager boardManager = new BoardManager(board);

		// when

		Move move = boardManager.performMove(new Coordinate(1, 6), new Coordinate(2, 4));

		// then
		assertEquals(MoveType.CAPTURE, move.getType());
		assertEquals(Piece.WHITE_KNIGHT, move.getMovedPiece());

	}

	@Test
	public void testShouldReturnTrueCaptureWhenCheckingIfMoveIsPossibleForKnight() {

		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(7, 0));
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(1, 7));
		board.setPieceAt(Piece.WHITE_KNIGHT, new Coordinate(2, 4));
		board.setPieceAt(Piece.BLACK_QUEEN, new Coordinate(2, 3));
		board.setPieceAt(Piece.BLACK_PAWN, new Coordinate(3, 2));

		// when

		MoveValidator moveValidator = new KnightMoveValidator();
		moveValidator.setCurrentBoard(board);
		boolean result = moveValidator.isMovePossible(new Coordinate(2, 4), new Coordinate(3, 2));
		MoveType checkedMoveType = moveValidator.getTypeOfTheValidatedMove();

		// then
		assertEquals(true, result);
		assertEquals(MoveType.CAPTURE, checkedMoveType);

	}

	@Test
	public void testShouldReturnTrueAttackWhenCheckingIfMoveIsPossibleForKnight() {

		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(7, 0));
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(1, 7));
		board.setPieceAt(Piece.WHITE_KNIGHT, new Coordinate(2, 4));
		board.setPieceAt(Piece.BLACK_QUEEN, new Coordinate(2, 3));

		// when

		MoveValidator moveValidator = new KnightMoveValidator();
		moveValidator.setCurrentBoard(board);
		boolean result = moveValidator.isMovePossible(new Coordinate(2, 4), new Coordinate(3, 2));
		MoveType checkedMoveType = moveValidator.getTypeOfTheValidatedMove();

		// then
		assertEquals(true, result);
		assertEquals(MoveType.ATTACK, checkedMoveType);

	}

	@Test
	public void testShouldReturnFalseWhenCheckingIfMoveIsPossibleForKnight() {

		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(7, 0));
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(1, 7));
		board.setPieceAt(Piece.WHITE_KNIGHT, new Coordinate(2, 4));
		board.setPieceAt(Piece.BLACK_QUEEN, new Coordinate(2, 3));
		board.setPieceAt(Piece.BLACK_PAWN, new Coordinate(3, 2));

		// when

		MoveValidator moveValidator = new KnightMoveValidator();
		moveValidator.setCurrentBoard(board);
		boolean result = moveValidator.isMovePossible(new Coordinate(2, 4), new Coordinate(1, 3));

		// then
		assertEquals(false, result);

	}

	// ----------------------------------------------------------------------------------------
	// PAWN

	@Test
	public void testShouldReturnFalseWhenCheckingIfMoveIsPossibleForPawn() {

		// given
		Board board = new Board();
		board.getMoveHistory().add(createDummyMove(board)); // kolej czarnego
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(7, 0));
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(1, 7));
		board.setPieceAt(Piece.WHITE_KNIGHT, new Coordinate(2, 4));
		board.setPieceAt(Piece.BLACK_PAWN, new Coordinate(3, 3));
		board.setPieceAt(Piece.WHITE_PAWN, new Coordinate(3, 2));

		// when

		MoveValidator moveValidator = new BlackPawnMoveValidator();
		moveValidator.setCurrentBoard(board);
		boolean result = moveValidator.isMovePossible(new Coordinate(3, 3), new Coordinate(3, 4));

		// then
		assertEquals(false, result);

	}

	@Test
	public void testShouldReturnFalseWhenCheckingIfTwoStepForwardMoveIsPossibleForPawn() {

		// given
		Board board = new Board();
		board.getMoveHistory().add(createDummyMove(board)); // kolej czarnego
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(7, 0));
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(1, 7));
		board.setPieceAt(Piece.WHITE_KNIGHT, new Coordinate(2, 4));
		board.setPieceAt(Piece.BLACK_PAWN, new Coordinate(3, 3));
		board.setPieceAt(Piece.WHITE_PAWN, new Coordinate(3, 2));

		// when

		MoveValidator moveValidator = new BlackPawnMoveValidator();
		moveValidator.setCurrentBoard(board);
		boolean result = moveValidator.isMovePossible(new Coordinate(3, 3), new Coordinate(3, 1));

		// then
		assertEquals(false, result);

	}

	@Test
	public void testShouldReturnFalseWhenCheckingIfDiagonallAttackIsPossibleForPawn() {

		// given
		Board board = new Board();
		board.getMoveHistory().add(createDummyMove(board));
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(7, 0));
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(1, 7));
		board.setPieceAt(Piece.WHITE_KNIGHT, new Coordinate(2, 4));
		board.setPieceAt(Piece.BLACK_PAWN, new Coordinate(3, 3));
		board.setPieceAt(Piece.WHITE_PAWN, new Coordinate(3, 2));

		// when

		MoveValidator moveValidator = new BlackPawnMoveValidator();
		moveValidator.setCurrentBoard(board);
		boolean result = moveValidator.isMovePossible(new Coordinate(3, 3), new Coordinate(2, 2));

		// then
		assertEquals(false, result);

	}

	@Test
	public void testShouldReturnTrueWhenCheckingIfDiagonallCaptureIsPossibleForPawn() {

		// given
		Board board = new Board();
		board.getMoveHistory().add(createDummyMove(board));
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(7, 0));
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(1, 7));
		board.setPieceAt(Piece.WHITE_KNIGHT, new Coordinate(2, 4));
		board.setPieceAt(Piece.BLACK_PAWN, new Coordinate(3, 3));
		board.setPieceAt(Piece.WHITE_PAWN, new Coordinate(3, 2));
		board.setPieceAt(Piece.WHITE_PAWN, new Coordinate(4, 2));

		// when

		MoveValidator moveValidator = new BlackPawnMoveValidator();
		moveValidator.setCurrentBoard(board);
		boolean result = moveValidator.isMovePossible(new Coordinate(3, 3), new Coordinate(4, 2));
		MoveType checkedMoveType = moveValidator.getTypeOfTheValidatedMove();

		// then
		assertEquals(true, result);
		assertEquals(MoveType.CAPTURE, checkedMoveType);

	}

	@Test
	public void testShouldReturnTrueWhenCheckingIfEnPassanteIsPossible() {

		// given
		Board board = new Board();
		BoardManager boardManager = new BoardManager(board);
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(7, 0));
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(1, 7));
		board.setPieceAt(Piece.WHITE_KNIGHT, new Coordinate(2, 4));
		board.setPieceAt(Piece.BLACK_PAWN, new Coordinate(3, 3));
		board.setPieceAt(Piece.WHITE_PAWN, new Coordinate(2, 1));
		Move previousMove = new Move();
		previousMove.setFrom(new Coordinate(2, 1));
		previousMove.setTo(new Coordinate(2, 3));
		previousMove.setMovedPiece(Piece.WHITE_PAWN);
		previousMove.setType(MoveType.ATTACK);
		board.getMoveHistory().add(previousMove);

		// when

		MoveValidator moveValidator = new BlackPawnMoveValidator();
		moveValidator.setCurrentBoard(board);
		boolean result = moveValidator.isMovePossible(new Coordinate(3, 3), new Coordinate(2, 2));
		MoveType checkedMoveType = moveValidator.getTypeOfTheValidatedMove();

		// then
		assertEquals(true, result);
		assertEquals(MoveType.EN_PASSANT, checkedMoveType);

	}

	// ----------------------------------------------------------------------------------------
	// KING

	@Test
	public void testShouldReturnFalseWhenCheckingIfThreeStepsStraightMoveIsPossibleForKing() {

		// given
		Board board = new Board();
		board.getMoveHistory().add(createDummyMove(board));
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(1, 7));
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(3, 3));
		board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(1, 1));

		// when

		MoveValidator moveValidator = new KingMoveValidator();
		moveValidator.setCurrentBoard(board);
		boolean result = moveValidator.isMovePossible(new Coordinate(3, 3), new Coordinate(3, 0));

		// then
		assertEquals(false, result);

	}

	@Test
	public void testShouldReturnTrueWhenCheckingIfOneStepsStraightCaptureIsPossibleForKing() {

		// given
		Board board = new Board();
		board.getMoveHistory().add(createDummyMove(board));
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(1, 7));
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(3, 3));
		board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(3, 2));

		// when

		MoveValidator moveValidator = new KingMoveValidator();
		moveValidator.setCurrentBoard(board);
		boolean result = moveValidator.isMovePossible(new Coordinate(3, 3), new Coordinate(3, 2));
		MoveType checkedMoveType = moveValidator.getTypeOfTheValidatedMove();

		// then
		assertEquals(true, result);
		assertEquals(MoveType.CAPTURE, checkedMoveType);

	}

	@Test
	public void testShouldReturnTrueWhenCheckingIfOneStepsDiagonalCaptureIsPossibleForKing() {

		// given
		Board board = new Board();
		board.getMoveHistory().add(createDummyMove(board));
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(1, 7));
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(3, 3));
		board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(4, 2));

		// when

		MoveValidator moveValidator = new KingMoveValidator();
		moveValidator.setCurrentBoard(board);
		boolean result = moveValidator.isMovePossible(new Coordinate(3, 3), new Coordinate(4, 2));
		MoveType checkedMoveType = moveValidator.getTypeOfTheValidatedMove();

		// then
		assertEquals(true, result);
		assertEquals(MoveType.CAPTURE, checkedMoveType);

	}

	@Test
	public void testShouldReturnTrueWhenCheckingIfOneStepsDiagonalAttackIsPossibleForKing() {

		// given
		Board board = new Board();
		board.getMoveHistory().add(createDummyMove(board));
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(1, 7));
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(3, 3));

		// when

		MoveValidator moveValidator = new KingMoveValidator();
		moveValidator.setCurrentBoard(board);
		boolean result = moveValidator.isMovePossible(new Coordinate(3, 3), new Coordinate(4, 2));
		MoveType checkedMoveType = moveValidator.getTypeOfTheValidatedMove();

		// then
		assertEquals(true, result);
		assertEquals(MoveType.ATTACK, checkedMoveType);

	}

	// ----------------------------------------------------------------------------------------
	//

	@Test
	public void testUpdateBoardStateStaleMate() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.getMoveHistory().add(createDummyMove(board));
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(7, 0));

		board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(2, 1));
		board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(6, 5));

		board.setPieceAt(Piece.WHITE_KING, new Coordinate(3, 7));

		// when
		BoardManager boardManager = new BoardManager(board);
		BoardState boardState = boardManager.updateBoardState();

		// then
		assertEquals(BoardState.STALE_MATE, boardState);
	}

	@Test
	public void testShouldThrowKingInCheck() {
		// given
		Board board = new Board();

		board.getMoveHistory().add(createDummyMove(board));
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(7, 0));
		board.setPieceAt(Piece.WHITE_KNIGHT, new Coordinate(5, 2));
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(6, 2));

		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(7, 0), new Coordinate(6, 0));
		} catch (InvalidMoveException e) {
			exceptionThrown = e instanceof KingInCheckException;
		}

		// then
		assertTrue(exceptionThrown);

	}

	@Test
	public void testShouldPerformKingsCaptureAndUpdateBoardStateToCheckMate() throws InvalidMoveException {
		// given
		Board board = new Board();

		board.getMoveHistory().add(createDummyMove(board));
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(0, 0));
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(2, 2));
		board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(0, 3));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(0, 2));

		// when
		BoardManager boardManager = new BoardManager(board);
		Move move = boardManager.performMove(new Coordinate(0, 2), new Coordinate(2, 2));
		boardManager.updateBoardState();
		BoardState boardState = board.getState();

		// then
		assertEquals(Piece.BLACK_ROOK, move.getMovedPiece());
		assertEquals(BoardState.CHECK_MATE, boardState);

	}

	@Test
	public void testShouldThrowKingInCheckAfterPawnCapture() {
		// given
		Board board = new Board();

		board.getMoveHistory().add(createDummyMove(board));
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(7, 0));
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(3, 7));
		board.setPieceAt(Piece.BLACK_PAWN, new Coordinate(2, 6));
		board.setPieceAt(Piece.WHITE_BISHOP, new Coordinate(1, 5));
		board.setPieceAt(Piece.WHITE_KNIGHT, new Coordinate(3, 5));

		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(2, 6), new Coordinate(3, 5));
		} catch (InvalidMoveException e) {
			exceptionThrown = e instanceof KingInCheckException;
		}

		// then
		assertTrue(exceptionThrown);

	}

	@Test
	public void testShouldThrowKingInCheckAfterEnPassante() {
		// given
		Board board = new Board();
		BoardManager boardManager = new BoardManager(board);
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(7, 0));
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(1, 3));
		board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(6, 3));
		board.setPieceAt(Piece.BLACK_PAWN, new Coordinate(3, 3));
		board.setPieceAt(Piece.WHITE_PAWN, new Coordinate(2, 3));
		Move previousMove = new Move();
		previousMove.setFrom(new Coordinate(2, 1));
		previousMove.setTo(new Coordinate(2, 3));
		previousMove.setMovedPiece(Piece.WHITE_PAWN);
		previousMove.setType(MoveType.ATTACK);
		board.getMoveHistory().add(previousMove);

		// when
		boolean exceptionThrown = false;

		try {
			boardManager.performMove(new Coordinate(3, 3), new Coordinate(2, 2));
		} catch (InvalidMoveException e) {
			exceptionThrown = e instanceof KingInCheckException;
		}

		// then
		assertTrue(exceptionThrown);

	}

	private Move createDummyMove(Board board) {

		Move move = new Move();

		if (board.getMoveHistory().size() % 2 == 0) {
			board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(0, 0));
			move.setMovedPiece(Piece.WHITE_ROOK);
		} else {
			board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(0, 0));
			move.setMovedPiece(Piece.BLACK_ROOK);
		}
		move.setFrom(new Coordinate(0, 0));
		move.setTo(new Coordinate(0, 0));
		move.setType(MoveType.ATTACK);
		board.setPieceAt(null, new Coordinate(0, 0));
		return move;
	}

}
