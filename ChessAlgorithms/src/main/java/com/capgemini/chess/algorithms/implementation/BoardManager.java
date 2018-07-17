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

		updateBoardState();

		return move;
	}

	/**
	 * Calculates state of the chess board.
	 *
	 * @return state of the chess board
	 */

	private void initMoveValidators() {
		// TODO moja wlasna metoda (wywolana w konstruktorach)

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
		// sprawdza czy jest mozliwosc ruchu, bo jesli jedyny mozliwy ruch
		// powoduje ze nasz krol bedzie w szachu to wtedy
		// nie jest mozliwy ruch
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
		// TODO moja wlasna metoda
		int coordinateX = coordinate.getX();
		int coordinateY = coordinate.getY();

		if (coordinateX <= Board.SIZE - 1 && coordinateX >= 0 && coordinateY <= Board.SIZE - 1 && coordinateY >= 0) {
			return true;
		}

		return false;
	}

	private boolean generalValidation(Coordinate from, Coordinate to) throws InvalidMoveException {

		// sprawdzenie zasady 50 ruchow
		boolean resultOfCheckingFiftyMoveRule = checkFiftyMoveRule();
		if (resultOfCheckingFiftyMoveRule == true) {
			throw new InvalidMoveException("Nie mozna wykonywac dalszych ruchow, poniewaz w poprzednich 50 rundach "
					+ "zaden z graczy nie poruszyl pionkiem ani zadna figura nie zostala zbita!");
		}

		// sprawdzamy wspolrzedne
		boolean CoordinateFromIsInRange = checkIfCoordinateIsInBoardRange(from);
		boolean CoordinateToIsInRange = checkIfCoordinateIsInBoardRange(to);
		if (!CoordinateFromIsInRange || !CoordinateToIsInRange) {
			throw new InvalidMoveException("Wybrano wspolrzedne wykraczajace poza obszar planszy!");
		}

		if (from.equals(to)) {
			throw new InvalidMoveException("Wspolrzedne poczatkowe sa takie same jak wspolrzedne koncowe!");
		}

		// sprawdzamy czy pole 'from' nie jest puste lub nie stoi na nim figura
		// przeciwnika
		// sprawdzenie czy na polu 'to' czy nie stoi nasza figura
		Piece PieceStandingOnFromCoordinate = board.getPieceAt(from);
		Piece PieceStandingOnToCoordinate = board.getPieceAt(to);

		if (PieceStandingOnFromCoordinate == null) {
			throw new InvalidMoveException("Na polu poczatkowym ruchu nie ma figury!");
		}
		if (PieceStandingOnFromCoordinate.getColor() != calculateNextMoveColor()) {
			throw new InvalidMoveException("Na polu poczatkowym ruchu znajduje sie figura przeciwnika!");
		}
		if (PieceStandingOnToCoordinate != null) {
			if (PieceStandingOnToCoordinate.getColor() == calculateNextMoveColor()) {
				throw new InvalidMoveException("Na polu koncowym znajduje sie jedna z twoich figur!");
			}
		}

		return true;
	}

	private Move validateMove(Coordinate from, Coordinate to) throws InvalidMoveException, KingInCheckException {
		// TODO please add implementation here

		generalValidation(from, to);

		Piece PieceStandingOnFromCoordinate = board.getPieceAt(from);

		MoveValidator movingRules = mapOfMovingRules.get(PieceStandingOnFromCoordinate);
		movingRules.setCurrentBoard(getBoard());

		boolean isMovePossible = movingRules.isMovePossible(from, to);

		if (isMovePossible) {
			Move possibleMove = new Move();
			possibleMove.setFrom(from);
			possibleMove.setTo(to);
			possibleMove.setMovedPiece(PieceStandingOnFromCoordinate);
			possibleMove.setType(movingRules.getTypeOfTheValidatedMove());
			// sprawdzamy czy w wyniku tego ruchu nasz krol nie bedzie w szachu
			willKingBeInCheck(possibleMove);

			return possibleMove;
		} else {
			throw new InvalidMoveException("Wybrana figura nie ma mozliwosci wykonania takiego ruchu!");
		}

	}

	private boolean willKingBeInCheck(Move testedMove) throws InvalidMoveException {

		Color kingColor = calculateNextMoveColor();

		Coordinate testedMovesFromCoordinate = new Coordinate(testedMove.getFrom().getX(), testedMove.getFrom().getY());
		Coordinate testedMovesToCoordinate = new Coordinate(testedMove.getTo().getX(), testedMove.getTo().getY());
		Piece pieceThatIsGoingToBeCaptured = null;
		if (board.getPieceAt(testedMovesToCoordinate) != null) {
			pieceThatIsGoingToBeCaptured = board.getPieceAt(testedMovesToCoordinate);
			// chyba nie dziala przy en passant
		}

		board.setPieceAt(testedMove.getMovedPiece(), testedMovesToCoordinate);

		// metoda wycofaj ostatni ruch
		if (isKingInCheck(kingColor)) {

			Piece lastMovedPiece = testedMove.getMovedPiece();

			board.setPieceAt(lastMovedPiece, testedMovesFromCoordinate);
			board.setPieceAt(pieceThatIsGoingToBeCaptured, testedMovesToCoordinate);

			throw new KingInCheckException();
		}

		Piece lastMovedPiece = testedMove.getMovedPiece();

		board.setPieceAt(lastMovedPiece, testedMovesFromCoordinate);
		board.setPieceAt(pieceThatIsGoingToBeCaptured, testedMovesToCoordinate);

		return false;

	}

	private boolean isKingInCheck(Color kingColor) {

		// TODO please add implementation here

		// petla na znalezienie krola
		Coordinate kingsCoordinate = null;
		Coordinate currentlyCheckedCoordinate = null;

		for (int column = 0; column < Board.SIZE; column++) {
			for (int row = 0; row < Board.SIZE; row++) {

				currentlyCheckedCoordinate = new Coordinate(column, row);
				Piece PieceAtCurrentlyCheckedCoordinate = board.getPieceAt(currentlyCheckedCoordinate);
				boolean spotIsNotEmpty = (PieceAtCurrentlyCheckedCoordinate != null);

				if (spotIsNotEmpty) {
					PieceType pieceTypeOfPieceAtCurrentlyCheckedCoordinate = board
							.getPieceAt(currentlyCheckedCoordinate).getType();
					boolean pieceTypeOfPieceAtCurrentlyCheckedCoordinateIsKing = pieceTypeOfPieceAtCurrentlyCheckedCoordinate
							.equals(PieceType.KING);

					if (!pieceTypeOfPieceAtCurrentlyCheckedCoordinateIsKing) {
						continue;
					} else {// sprawdzenie czy to krol w naszym kolorze
						Color currentlyCheckedKingsColor = board.getPieceAt(currentlyCheckedCoordinate).getColor();
						if (currentlyCheckedKingsColor.equals(kingColor)) {
							kingsCoordinate = new Coordinate(currentlyCheckedCoordinate.getX(),
									currentlyCheckedCoordinate.getY());
							break;
						}

					}

				}

			}

			// tutaj sprawdzam znowu czy juz mam dobrego krola i jesli tak to
			// break; do zmiany

			Piece PieceAtCurrentlyCheckedCoordinate = board.getPieceAt(currentlyCheckedCoordinate);
			boolean spotIsNotEmpty = (PieceAtCurrentlyCheckedCoordinate != null);

			if (spotIsNotEmpty) {
				if (board.getPieceAt(currentlyCheckedCoordinate).getType().equals(PieceType.KING)) {
					if (board.getPieceAt(currentlyCheckedCoordinate).getColor().equals(kingColor)) {
						break;
					}
				}
			}

		}

		// petla na sprawdzenie czy ktoras figura przeciwnika moze stanac na
		// polu krola
		if (kingsCoordinate != null) {

			for (int column = 0; column < Board.SIZE; column++) {
				for (int row = 0; row < Board.SIZE; row++) {
					currentlyCheckedCoordinate = new Coordinate(column, row);
					Piece PieceAtCurrentlyCheckedCoordinate = board.getPieceAt(currentlyCheckedCoordinate);
					try {
						generalValidation(currentlyCheckedCoordinate, kingsCoordinate);
					} catch (InvalidMoveException e) {
						continue;
					}

					Color colorOfPieceAtCurrentlyCheckedCoordinate = PieceAtCurrentlyCheckedCoordinate.getColor();
					if (colorOfPieceAtCurrentlyCheckedCoordinate.equals(kingColor)) {
						break;
					} else {

						MoveValidator movingRules = mapOfMovingRules.get(PieceAtCurrentlyCheckedCoordinate);
						movingRules.setCurrentBoard(getBoard());
						boolean moveToKingsCoordinateIsPossible = movingRules.isMovePossible(currentlyCheckedCoordinate,
								kingsCoordinate);
						if (moveToKingsCoordinateIsPossible) {
							return true;
						}

					}

				}

			}
		}

		return false;
	}

	private boolean isAnyMoveValidUnderCheck(Color nextMoveColor) {
		// jezeli krol jest w szachu (czyli wywola isKingInCheck) to
		// isAnyMoveValid sprawdza czy mozna w jakis sposob:
		// (przesunac krola na pole niebedace w szachu, zbic figure ktora
		// szachuje krola, zaslonic krola wlasna bierka)
		// jesli sie nie da, to zwraca FALSE i zmienia sie BoardState na
		// Check_Mate

		return true;
	}

	private boolean isAnyMoveValid(Color nextMoveColor) {
		// TODO please add implementation here

		if (isKingInCheck(nextMoveColor)) {
			if (isAnyMoveValidUnderCheck(nextMoveColor)) {
				return true;
			} else {
				return false;
			}
		}

		Coordinate checkedCoordinate;
		Piece pieceOnBoard;

		// jezeli krol nie jest w szachu to sprawdza metoda validateMove mozliwe
		// ruchy

		// zbieranie naszych figur
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

				// kolejna petla walidujaca ruch ze sprawdzanej figury

				boolean thereIsAPossibleMoveForThatFigure = checkIfThereIsAPossibleMoveForOneOfTheNextMovePlayersRemainingFigure(
						checkedCoordinate);

				if (thereIsAPossibleMoveForThatFigure) {
					return true;
				}

			}
		}

		return false;
	}

	private boolean checkIfThereIsAPossibleMoveForOneOfTheNextMovePlayersRemainingFigure(
			Coordinate coordinateOfTheCheckedFigure) {

		boolean thereIsAPossibleMoveForThatFigure = false;

		Coordinate destinationCheckedCoordinate;
		for (int column = 0; column < Board.SIZE; column++) {
			for (int row = 0; row < Board.SIZE; row++) {
				destinationCheckedCoordinate = new Coordinate(column, row);
				try {
					validateMove(coordinateOfTheCheckedFigure, destinationCheckedCoordinate);
					thereIsAPossibleMoveForThatFigure = true;

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
