package com.capgemini.chess.algorithms.implementation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.MoveValidator;
import com.capgemini.chess.algorithms.data.enums.BoardState;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;
import com.capgemini.chess.algorithms.implementation.exceptions.KingInCheckException;

/**
 * Class for managing of basic operations on the Chess Board.
 *
 * @author Michal Bejm
 *
 */
public class BoardManager {

	private Board board = new Board();
	private HashMap<Piece, MoveValidator> mapOfMovingRules;

	public BoardManager() {
		initBoard();
		initMoveValidators();
	}

	public BoardManager(List<Move> moves) {
		initBoard();
		initMoveValidators();
		for (Move move : moves) {
			addMove(move);
		}
	}

	public BoardManager(Board board) {
		initMoveValidators();
		this.board = board;
	}

	/**
	 * Getter for generated board
	 *
	 * @return board
	 */
	public Board getBoard() {
		return this.board;
	}

	/**
	 * Performs move of the chess piece on the chess board from one field to
	 * another.
	 *
	 * @param from
	 *            coordinates of 'from' field
	 * @param to
	 *            coordinates of 'to' field
	 * @return move object which includes moved piece and move type
	 * @throws InvalidMoveException
	 *             in case move is not valid
	 */
	public Move performMove(Coordinate from, Coordinate to) throws InvalidMoveException {

		Move move = validateMove(from, to);

		addMove(move);

		return move;
	}

	/**
	 * Calculates state of the chess board.
	 *
	 * @return state of the chess board
	 */

	private void initMoveValidators() {

		BishopMoveValidator bishopMoveValidator = new BishopMoveValidator();
		KingMoveValidator kingMoveValidator = new KingMoveValidator();
		KnightMoveValidator knightMoveValidator = new KnightMoveValidator();
		WhitePawnMoveValidator whitePawnMoveValidator = new WhitePawnMoveValidator();
		BlackPawnMoveValidator blackPawnMoveValidator = new BlackPawnMoveValidator();
		QueenMoveValidator queenMoveValidator = new QueenMoveValidator();
		RookMoveValidator rookMoveValidator = new RookMoveValidator();

		mapOfMovingRules = new HashMap<>();

		mapOfMovingRules.put(Piece.WHITE_BISHOP, bishopMoveValidator);
		mapOfMovingRules.put(Piece.BLACK_BISHOP, bishopMoveValidator);
		mapOfMovingRules.put(Piece.WHITE_KING, kingMoveValidator);
		mapOfMovingRules.put(Piece.BLACK_KING, kingMoveValidator);
		mapOfMovingRules.put(Piece.WHITE_KNIGHT, knightMoveValidator);
		mapOfMovingRules.put(Piece.BLACK_KNIGHT, knightMoveValidator);
		mapOfMovingRules.put(Piece.WHITE_PAWN, whitePawnMoveValidator);
		mapOfMovingRules.put(Piece.BLACK_PAWN, blackPawnMoveValidator);
		mapOfMovingRules.put(Piece.WHITE_QUEEN, queenMoveValidator);
		mapOfMovingRules.put(Piece.BLACK_QUEEN, queenMoveValidator);
		mapOfMovingRules.put(Piece.WHITE_ROOK, rookMoveValidator);
		mapOfMovingRules.put(Piece.BLACK_ROOK, rookMoveValidator);

	}

	public BoardState updateBoardState() {

		Color nextMoveColor = calculateNextMoveColor();

		boolean isKingInCheck = isKingInCheck(nextMoveColor);
		boolean isAnyMoveValid = isAnyMoveValid(nextMoveColor);

		BoardState boardState;
		if (isKingInCheck) {
			if (isAnyMoveValid) {
				boardState = BoardState.CHECK;
			} else {
				boardState = BoardState.CHECK_MATE;
			}
		} else {
			if (isAnyMoveValid) {
				boardState = BoardState.REGULAR;
			} else {
				boardState = BoardState.STALE_MATE;
			}
		}

		if (getKingsCoordinate(nextMoveColor) == null) {
			boardState = BoardState.CHECK_MATE;
		}

		this.board.setState(boardState);
		return boardState;
	}

	/**
	 * Checks threefold repetition rule (one of the conditions to end the chess
	 * game with a draw).
	 *
	 * @return true if current state repeated at list two times, false otherwise
	 */
	public boolean checkThreefoldRepetitionRule() {

		// there is no need to check moves that where before last capture/en
		// passant/castling
		int lastNonAttackMoveIndex = findLastNonAttackMoveIndex();
		List<Move> omittedMoves = this.board.getMoveHistory().subList(0, lastNonAttackMoveIndex);
		BoardManager simulatedBoardManager = new BoardManager(omittedMoves);

		int counter = 0;
		for (int i = lastNonAttackMoveIndex; i < this.board.getMoveHistory().size(); i++) {
			Move moveToAdd = this.board.getMoveHistory().get(i);
			simulatedBoardManager.addMove(moveToAdd);
			boolean areBoardsEqual = Arrays.deepEquals(this.board.getPieces(),
					simulatedBoardManager.getBoard().getPieces());
			if (areBoardsEqual) {
				counter++;
			}
		}

		return counter >= 2;
	}

	/**
	 * Checks 50-move rule (one of the conditions to end the chess game with a
	 * draw).
	 *
	 * @return true if no pawn was moved or not capture was performed during
	 *         last 50 moves, false otherwise
	 */
	public boolean checkFiftyMoveRule() {

		// for this purpose a "move" consists of a player completing his turn
		// followed by his opponent completing his turn
		if (this.board.getMoveHistory().size() < 100) {
			return false;
		}

		for (int i = this.board.getMoveHistory().size() - 1; i >= this.board.getMoveHistory().size() - 100; i--) {
			Move currentMove = this.board.getMoveHistory().get(i);
			PieceType currentPieceType = currentMove.getMovedPiece().getType();
			if (currentMove.getType() != MoveType.ATTACK || currentPieceType == PieceType.PAWN) {
				return false;
			}
		}

		return true;
	}

	private void initBoard() {

		this.board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(0, 7));
		this.board.setPieceAt(Piece.BLACK_KNIGHT, new Coordinate(1, 7));
		this.board.setPieceAt(Piece.BLACK_BISHOP, new Coordinate(2, 7));
		this.board.setPieceAt(Piece.BLACK_QUEEN, new Coordinate(3, 7));
		this.board.setPieceAt(Piece.BLACK_KING, new Coordinate(4, 7));
		this.board.setPieceAt(Piece.BLACK_BISHOP, new Coordinate(5, 7));
		this.board.setPieceAt(Piece.BLACK_KNIGHT, new Coordinate(6, 7));
		this.board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(7, 7));

		for (int x = 0; x < Board.SIZE; x++) {
			this.board.setPieceAt(Piece.BLACK_PAWN, new Coordinate(x, 6));
		}

		this.board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(0, 0));
		this.board.setPieceAt(Piece.WHITE_KNIGHT, new Coordinate(1, 0));
		this.board.setPieceAt(Piece.WHITE_BISHOP, new Coordinate(2, 0));
		this.board.setPieceAt(Piece.WHITE_QUEEN, new Coordinate(3, 0));
		this.board.setPieceAt(Piece.WHITE_KING, new Coordinate(4, 0));
		this.board.setPieceAt(Piece.WHITE_BISHOP, new Coordinate(5, 0));
		this.board.setPieceAt(Piece.WHITE_KNIGHT, new Coordinate(6, 0));
		this.board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(7, 0));

		for (int x = 0; x < Board.SIZE; x++) {
			this.board.setPieceAt(Piece.WHITE_PAWN, new Coordinate(x, 1));
		}
	}

	private void addMove(Move move) {

		addRegularMove(move);

		if (move.getType() == MoveType.CASTLING) {
			addCastling(move);
		} else if (move.getType() == MoveType.EN_PASSANT) {
			addEnPassant(move);
		}

		this.board.getMoveHistory().add(move);
	}

	private void addRegularMove(Move move) {
		Piece movedPiece = this.board.getPieceAt(move.getFrom());
		this.board.setPieceAt(null, move.getFrom());
		this.board.setPieceAt(movedPiece, move.getTo());

		performPromotion(move, movedPiece);
	}

	private void performPromotion(Move move, Piece movedPiece) {
		if (movedPiece == Piece.WHITE_PAWN && move.getTo().getY() == (Board.SIZE - 1)) {
			this.board.setPieceAt(Piece.WHITE_QUEEN, move.getTo());
		}
		if (movedPiece == Piece.BLACK_PAWN && move.getTo().getY() == 0) {
			this.board.setPieceAt(Piece.BLACK_QUEEN, move.getTo());
		}
	}

	private void addCastling(Move move) {
		if (move.getFrom().getX() > move.getTo().getX()) {
			Piece rook = this.board.getPieceAt(new Coordinate(0, move.getFrom().getY()));
			this.board.setPieceAt(null, new Coordinate(0, move.getFrom().getY()));
			this.board.setPieceAt(rook, new Coordinate(move.getTo().getX() + 1, move.getTo().getY()));
		} else {
			Piece rook = this.board.getPieceAt(new Coordinate(Board.SIZE - 1, move.getFrom().getY()));
			this.board.setPieceAt(null, new Coordinate(Board.SIZE - 1, move.getFrom().getY()));
			this.board.setPieceAt(rook, new Coordinate(move.getTo().getX() - 1, move.getTo().getY()));
		}
	}

	private void addEnPassant(Move move) {
		Move lastMove = this.board.getMoveHistory().get(this.board.getMoveHistory().size() - 1);
		this.board.setPieceAt(null, lastMove.getTo());
	}

	private boolean checkIfCoordinateIsInBoardRange(Coordinate coordinate) {
		int coordinateX = coordinate.getX();
		int coordinateY = coordinate.getY();

		return (coordinateX <= Board.SIZE - 1 && coordinateX >= 0 && coordinateY <= Board.SIZE - 1 && coordinateY >= 0);
	}

	private boolean performInitialValidation(Coordinate from, Coordinate to) throws InvalidMoveException {

		boolean resultOfCheckingFiftyMoveRule = checkFiftyMoveRule();
		if (resultOfCheckingFiftyMoveRule == true) {
			throw new InvalidMoveException("Nie mozna wykonywac dalszych ruchow, poniewaz w poprzednich 50 rundach "
					+ "zaden z graczy nie poruszyl pionkiem ani zadna figura nie zostala zbita!");
		}

		boolean CoordinateFromIsInRange = checkIfCoordinateIsInBoardRange(from);
		boolean CoordinateToIsInRange = checkIfCoordinateIsInBoardRange(to);
		if (!CoordinateFromIsInRange || !CoordinateToIsInRange) {
			throw new InvalidMoveException("Wybrano wspolrzedne wykraczajace poza obszar planszy!");
		}

		if (from.equals(to)) {
			throw new InvalidMoveException("Wspolrzedne poczatkowe sa takie same jak wspolrzedne koncowe!");
		}

		Piece PieceStandingOnFromCoordinate = board.getPieceAt(from);
		Piece PieceStandingOnToCoordinate = board.getPieceAt(to);

		if (PieceStandingOnFromCoordinate == null) {
			throw new InvalidMoveException("Na polu poczatkowym ruchu nie ma figury!");
		}
		if (!PieceStandingOnFromCoordinate.getColor().equals(calculateNextMoveColor())) {
			throw new InvalidMoveException("Na polu poczatkowym ruchu znajduje sie figura przeciwnika!");
		}
		if (PieceStandingOnToCoordinate != null) {
			if (PieceStandingOnToCoordinate.getColor().equals(calculateNextMoveColor())) {
				throw new InvalidMoveException("Na polu koncowym znajduje sie jedna z twoich figur!");
			}
		}

		return true;
	}

	private Move validateMove(Coordinate from, Coordinate to) throws InvalidMoveException, KingInCheckException {
		// TODO please add implementation here

		performInitialValidation(from, to);

		Piece pieceStandingOnFromCoordinate = board.getPieceAt(from);

		MoveValidator movingRules = mapOfMovingRules.get(pieceStandingOnFromCoordinate);
		movingRules.setCurrentBoard(board);

		boolean moveIsPossible = movingRules.isMovePossible(from, to);

		if (moveIsPossible) {
			Move possibleMove = new Move();
			possibleMove.setFrom(from);
			possibleMove.setTo(to);
			possibleMove.setMovedPiece(pieceStandingOnFromCoordinate);
			possibleMove.setType(movingRules.getTypeOfTheValidatedMove());

			boolean theMoveIsCheckMate = checkIfTheMoveIsCheckMate(from, to);
			if (!theMoveIsCheckMate) {

				willKingBeInCheck(possibleMove);
			}

			return possibleMove;
		} else {
			throw new InvalidMoveException("Wybrana figura nie ma mozliwosci wykonania takiego ruchu!");
		}

	}

	private boolean checkIfTheMoveIsCheckMate(Coordinate from, Coordinate to) {
		Piece pieceStandingOnToCoordinate = board.getPieceAt(to);

		boolean thereIsAFigureOnToCoordinate = pieceStandingOnToCoordinate != null;
		if (thereIsAFigureOnToCoordinate) {
			boolean figureOnToCoordinateIsKing = pieceStandingOnToCoordinate.getType().equals(PieceType.KING);
			boolean figureOnToCoordinateIsOpponentsFigure = !pieceStandingOnToCoordinate.getColor()
					.equals(calculateNextMoveColor());
			if (figureOnToCoordinateIsKing && figureOnToCoordinateIsOpponentsFigure) {
				return true;
			}
		}

		return false;
	}

	private boolean willKingBeInCheck(Move testedMove) throws KingInCheckException {

		boolean moveIsEnPassant = testedMove.getType().equals(MoveType.EN_PASSANT);

		if (moveIsEnPassant) {
			willKingBeInCheckAfterEnPassant(testedMove);
		}

		Coordinate testedMovesFromCoordinate = new Coordinate(testedMove.getFrom().getX(), testedMove.getFrom().getY());
		Coordinate testedMovesToCoordinate = new Coordinate(testedMove.getTo().getX(), testedMove.getTo().getY());
		Piece testedMovesMovedPiece = testedMove.getMovedPiece();
		Piece pieceThatIsGoingToBeCaptured = board.getPieceAt(testedMovesToCoordinate);

		board.setPieceAt(testedMovesMovedPiece, testedMovesToCoordinate);
		board.setPieceAt(null, testedMovesFromCoordinate);

		boolean kingIsInCheck = isKingInCheck(testedMove.getMovedPiece().getColor());

		board.setPieceAt(testedMovesMovedPiece, testedMovesFromCoordinate);
		board.setPieceAt(pieceThatIsGoingToBeCaptured, testedMovesToCoordinate);

		if (kingIsInCheck) {
			throw new KingInCheckException();
		}

		return false;

	}

	private boolean willKingBeInCheckAfterEnPassant(Move testedEnPassant) throws KingInCheckException {
		Piece pieceThatIsGoingToBeCaptured = null;

		Coordinate testedMovesFromCoordinate = new Coordinate(testedEnPassant.getFrom().getX(),
				testedEnPassant.getFrom().getY());
		Coordinate testedMovesToCoordinate = new Coordinate(testedEnPassant.getTo().getX(),
				testedEnPassant.getTo().getY());
		Piece testedEnPassantMovedPiece = testedEnPassant.getMovedPiece();

		Coordinate coordinateOfTheCapturedPawn = new Coordinate(testedEnPassant.getTo().getX(),
				testedEnPassant.getFrom().getY());
		pieceThatIsGoingToBeCaptured = board.getPieceAt(coordinateOfTheCapturedPawn);

		board.setPieceAt(testedEnPassantMovedPiece, testedMovesToCoordinate);
		board.setPieceAt(null, testedMovesFromCoordinate);
		board.setPieceAt(null, coordinateOfTheCapturedPawn);

		boolean kingIsInCheck = isKingInCheck(testedEnPassantMovedPiece.getColor());

		board.setPieceAt(testedEnPassantMovedPiece, testedMovesFromCoordinate);
		board.setPieceAt(pieceThatIsGoingToBeCaptured, coordinateOfTheCapturedPawn);
		board.setPieceAt(null, testedMovesToCoordinate);

		if (kingIsInCheck) {
			throw new KingInCheckException();
		}

		return false;

	}

	private boolean isKingInCheck(Color kingColor) {
		// TODO please add implementation here
		Coordinate kingsCoordinate = getKingsCoordinate(kingColor);
		boolean thereIsAFigureThatCanStandOnKingsCoordinate = false;

		if (kingsCoordinate != null) {
			thereIsAFigureThatCanStandOnKingsCoordinate = checkIfAnyOfTheOpponentsFigureCanStandOnKingsSpot(
					kingsCoordinate);
		}
		return thereIsAFigureThatCanStandOnKingsCoordinate;
	}

	private Coordinate getKingsCoordinate(Color kingColor) {

		Coordinate currentlyCheckedCoordinate = null;

		for (int column = 0; column < Board.SIZE; column++) {
			for (int row = 0; row < Board.SIZE; row++) {

				currentlyCheckedCoordinate = new Coordinate(column, row);
				Piece PieceAtCurrentlyCheckedCoordinate = board.getPieceAt(currentlyCheckedCoordinate);
				boolean spotIsNotEmpty = (PieceAtCurrentlyCheckedCoordinate != null);

				if (spotIsNotEmpty) {

					boolean checkedFigureIsKingInTheRightColor = checkIfThatFigureIsKingOfThatColor(
							currentlyCheckedCoordinate, kingColor);

					if (checkedFigureIsKingInTheRightColor) {

						return currentlyCheckedCoordinate;
					}

				}
			}
		}

		return null;
	}

	private boolean checkIfThatFigureIsKingOfThatColor(Coordinate figuresCoordinate, Color kingColor) {

		Piece figure = board.getPieceAt(figuresCoordinate);
		PieceType figuresPieceType = figure.getType();
		Color figuresColor = figure.getColor();

		return (figuresPieceType.equals(PieceType.KING) && figuresColor.equals(kingColor));
	}

	private boolean checkIfAnyOfTheOpponentsFigureCanStandOnKingsSpot(Coordinate kingsCoordinate) {

		Coordinate currentlyCheckedCoordinate = null;

		for (int column = 0; column < Board.SIZE; column++) {
			for (int row = 0; row < Board.SIZE; row++) {
				currentlyCheckedCoordinate = new Coordinate(column, row);
				Piece PieceAtCurrentlyCheckedCoordinate = board.getPieceAt(currentlyCheckedCoordinate);
				try {
					performInitialValidationForOpponentsFigure(currentlyCheckedCoordinate, kingsCoordinate);
				} catch (InvalidMoveException e) {
					continue;
				}
				MoveValidator movingRules = mapOfMovingRules.get(PieceAtCurrentlyCheckedCoordinate);
				movingRules.setCurrentBoard(board);
				boolean moveToKingsCoordinateIsPossible = movingRules.isMovePossible(currentlyCheckedCoordinate,
						kingsCoordinate);
				if (moveToKingsCoordinateIsPossible) {
					return true;
				}

			}

		}

		return false;
	}

	private boolean performInitialValidationForOpponentsFigure(Coordinate from, Coordinate to)
			throws InvalidMoveException {

		boolean CoordinateFromIsInRange = checkIfCoordinateIsInBoardRange(from);
		boolean CoordinateToIsInRange = checkIfCoordinateIsInBoardRange(to);
		if (!CoordinateFromIsInRange || !CoordinateToIsInRange) {
			throw new InvalidMoveException("Wybrano wspolrzedne wykraczajace poza obszar planszy!");
		}

		if (from.equals(to)) {
			throw new InvalidMoveException("Wspolrzedne poczatkowe sa takie same jak wspolrzedne koncowe!");
		}

		Piece PieceStandingOnFromCoordinate = board.getPieceAt(from);

		if (PieceStandingOnFromCoordinate == null) {
			throw new InvalidMoveException("Na polu poczatkowym ruchu nie ma figury!");
		}

		if (PieceStandingOnFromCoordinate.getColor().equals(calculateNextMoveColor())) {
			throw new InvalidMoveException("Na polu poczatkowym ruchu znajduje sie figura przeciwnika!");
		}

		return true;
	}

	private boolean isAnyMoveValid(Color nextMoveColor) {
		// TODO please add implementation here

		Coordinate checkedCoordinate;
		Piece pieceOnBoard;

		for (int column = 0; column < Board.SIZE; column++) {
			for (int row = 0; row < Board.SIZE; row++) {

				checkedCoordinate = new Coordinate(column, row);

				pieceOnBoard = board.getPieceAt(checkedCoordinate);

				if (pieceOnBoard == null) {
					continue;
				}
				Color colorOfPieceOnBoard = pieceOnBoard.getColor();
				if (colorOfPieceOnBoard != nextMoveColor) {
					continue;
				}

				boolean thereIsAPossibleMoveForThatFigure = checkIfThereIsAPossibleMoveForOneOfTheNextMovePlayersRemainingFigure(
						checkedCoordinate);

				if (thereIsAPossibleMoveForThatFigure) {
					return true;
				}

			}
		}

		return false;
	}

	// TODO
	private boolean checkIfThereIsAPossibleMoveForOneOfTheNextMovePlayersRemainingFigure(
			Coordinate coordinateOfTheCheckedFigure) {

		boolean thereIsAPossibleMoveForThatFigure = false;

		Coordinate currentlyCheckedDestinationCoordinate;
		for (int column = 0; column < Board.SIZE; column++) {
			for (int row = 0; row < Board.SIZE; row++) {
				currentlyCheckedDestinationCoordinate = new Coordinate(column, row);
				try {
					validateMove(coordinateOfTheCheckedFigure, currentlyCheckedDestinationCoordinate);
					thereIsAPossibleMoveForThatFigure = true;

					return thereIsAPossibleMoveForThatFigure;

				} catch (KingInCheckException e) {
					continue;
				} catch (InvalidMoveException e) {
					continue;
				}

			}
		}

		return thereIsAPossibleMoveForThatFigure;
	}

	private Color calculateNextMoveColor() {
		if (this.board.getMoveHistory().size() % 2 == 0) {
			return Color.WHITE;
		} else {
			return Color.BLACK;
		}
	}

	private int findLastNonAttackMoveIndex() {
		int counter = 0;
		int lastNonAttackMoveIndex = 0;

		for (Move move : this.board.getMoveHistory()) {
			if (move.getType() != MoveType.ATTACK) {
				lastNonAttackMoveIndex = counter;
			}
			counter++;
		}

		return lastNonAttackMoveIndex;
	}

}
