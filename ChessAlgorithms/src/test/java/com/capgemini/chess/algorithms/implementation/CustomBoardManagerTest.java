package com.capgemini.chess.algorithms.implementation;

import static org.junit.Assert.*;

import org.junit.Test;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.MoveValidator;
import com.capgemini.chess.algorithms.data.enums.BoardState;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;
import com.capgemini.chess.algorithms.implementation.exceptions.KingInCheckException;

public class CustomBoardManagerTest {
	// ----------------------------------------------------------------------------------------
	// BISHOP
	@Test
	public void testShouldReturnTrueWhenCheckingIfThreeStepsDiagonallMoveIsPossibleForBishop()
			throws InvalidMoveException {

		// given
		Board board = new Board();
		// bez dummy move zaczyna bialy
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(7, 0));
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(1, 7));
		board.setPieceAt(Piece.WHITE_BISHOP, new Coordinate(3, 3));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(1, 1));

		// when
		BoardManager boardManager = new BoardManager(board);
		MoveValidator moveValidator = new BishopMoveValidator();
		moveValidator.setCurrentBoard(board);
		boolean result = moveValidator.isMovePossible(new Coordinate(3, 3), new Coordinate(5, 1));

		// then
		assertEquals(true, result);

	}

	@Test
	public void testShouldPerformThreeStepsDiagonallMoveWithBishop() throws InvalidMoveException {

		// given
		Board board = new Board();
		// bez dummy move zaczyna bialy
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
	public void testShouldPerformTwoStepsDiagonallCaptureWithBishop() throws InvalidMoveException {

		// given
		Board board = new Board();
		// bez dummy move zaczyna bialy
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
		// bez dummy move zaczyna bialy
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
	public void testPerformMoveBishopCapture() throws InvalidMoveException {
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

	// ----------------------------------------------------------------------------------------
	// KNIGHT

	// ----------------------------------------------------------------------------------------
	// PAWN

	// ----------------------------------------------------------------------------------------
	// KING

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
