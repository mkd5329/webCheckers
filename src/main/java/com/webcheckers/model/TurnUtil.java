package com.webcheckers.model;


import java.lang.Math;

import java.util.HashSet;
import java.util.Stack;

/**
 * Utility class that manages turn-based operations
 *
 * @author <a href='mailto:mjd6839@rit.edu'>Matthew DellaNeve</a>
 * @author <a href='mailto:daw1882@rit.edu'>Dade Wood</a>
 * @author <a href='mailto:zah1276@rit.edu'>Zeb Hollinger</a>
 * @author Michael Driscoll
 */
public class TurnUtil {

    public static final int ROWS = 8;
    public static final int CELLS_IN_ROW = 8;
    public static final int JUMP_DIST = 2;


    public static final String MORE_JUMP_ERROR_MESSAGE = "There is a jump move that can be made.";
    public static final String INVALID_COMBINATION_ERROR_MESSAGE = "That was an invalid combination of moves.";

    /**
     * Determines whether the current move is valid or not.
     *
     * @param start the starting position of this move
     * @param end   the ending position of this move
     * @return true if the given move is valid, false otherwise
     */
    public static boolean isMoveValid(Piece.Type rank, Position start, Position end, CheckersGame game) {
        Piece.Color activeColor = game.getActiveColor();

        HashSet<Piece> piecesJumped = game.getPiecesJumped();


        if (rank == Piece.Type.KING && !game.getTurnMoves().empty()) {
            Move lastMove = game.getTurnMoves().peek();
            if (lastMove.getStart().equals(end)) {
                return false;
            }
        }

        BoardView board = game.getBoard();
        int[] diffs = findDifferences(start, end);

        boolean simpleMove = isMoveSimple(diffs[0], diffs[1]);
        boolean jumpMove = isMoveJump(diffs[0], diffs[1]);

        if (!simpleMove && !jumpMove) {
            return false;
        } else {

            if (jumpMove) {
                Space jumpedSpace = board.getSpaceAtRow(start.getRow() - (diffs[0] / 2), start.getCell() - (diffs[1] / 2));
                Piece jumpedPiece = jumpedSpace.getPiece();


                if (rank == Piece.Type.KING) {
                    if (jumpedPiece != null && activeColor == Piece.Color.RED && !piecesJumped.contains(jumpedPiece)) {
                        piecesJumped.add(jumpedPiece);
                        return (jumpedPiece.getColor() == Piece.Color.WHITE) && (diffs[0] != 0);
                    } else if (jumpedPiece != null && activeColor == Piece.Color.WHITE
                            && !piecesJumped.contains(jumpedPiece)) {
                        piecesJumped.add(jumpedPiece);
                        return (jumpedPiece.getColor() == Piece.Color.RED) && (diffs[0] != 0);
                    }
                } else {
                    if (jumpedPiece != null && activeColor == Piece.Color.RED && !piecesJumped.contains(jumpedPiece)) {
                        piecesJumped.add(jumpedPiece);
                        return (jumpedPiece.getColor() == Piece.Color.WHITE) && (diffs[0] > 0);
                    } else if (jumpedPiece != null && activeColor == Piece.Color.WHITE && !piecesJumped.contains(jumpedPiece)) {
                        piecesJumped.add(jumpedPiece);
                        return (jumpedPiece.getColor() == Piece.Color.RED) && (diffs[0] > 0);
                    } else {
                        return false;
                    }
                }
            } else {
                if (rank == Piece.Type.KING) {
                    return Math.abs(diffs[0]) == 1;
                } else {
                    return diffs[0] > 0;
                }
            }
        }
        return false;
    }

    /**
     * Checks if the move made is a simple move by checking that the move was only
     * by one diagonal cell over.
     *
     * @param rowDifference  Difference between the starting and ending row of
     *                       the move
     * @param cellDifference Difference between the starting and ending cell of
     *                       the move
     * @return true if it was a simple move, false otherwise
     */
    public static boolean isMoveSimple(int rowDifference, int cellDifference) {
        return (Math.abs(rowDifference) == 1 && Math.abs(cellDifference) == 1);
    }

    /**
     * Checks if the move made is a jump move by checking that the move was only
     * two diagonal cells over.
     *
     * @param rowDifference  Difference between the starting and ending row of
     *                       the move
     * @param cellDifference Difference between the starting and ending cell of
     *                       the move
     * @return true if it was a jump move, false otherwise
     */
    public static boolean isMoveJump(int rowDifference, int cellDifference) {
        return (Math.abs(rowDifference) == JUMP_DIST && Math.abs(cellDifference) == JUMP_DIST);
    }

    /**
     * Finds the distances(differences) between the start and end rows and cells
     *
     * @param start starting position of the move
     * @param end   ending position of the move
     * @return an integer array of the cell and row differences
     */
    public static int[] findDifferences(Position start, Position end) {
        int[] ans = new int[2];

        int startingRow = start.getRow();
        int startingCell = start.getCell();

        int endingRow = end.getRow();
        int endingCell = end.getCell();

        ans[0] = startingRow - endingRow;
        ans[1] = startingCell - endingCell;

        return ans;
    }

    /**
     * Checks if an entire turn was valid. Its valid if all the moves were valid
     * and there are only multiple jump type moves and not multiple simple moves.
     *
     * @param game - Checker game being played
     * @return true if the entire turn was valid, false otherwise
     */
    public static boolean isTurnValid(CheckersGame game, BoardView board) {
        Piece.Color activeColor = game.getActiveColor();
        Piece.Type rank = game.getFirstMovePiece().getType();
        Stack<Move> turnMoves = game.getTurnMoves();
        Stack<Move> copy = (Stack<Move>) turnMoves.clone();
        Move lastMoveMade = copy.peek();

        if (copy.size() > 1) {
            Move currentMove;
            Position start, end;

            while (!copy.empty()) {
                currentMove = copy.pop();
                start = currentMove.getStart();
                end = currentMove.getEnd();
                int[] diffs = findDifferences(start, end);
                if (isMoveSimple(diffs[0], diffs[1])) {
                    game.setInvalidTurnErrorMessage(INVALID_COMBINATION_ERROR_MESSAGE);
                    return false;
                }
            }
        } else {
            Move currentMove = copy.pop();
            Position start = currentMove.getStart();
            Position end = currentMove.getEnd();
            int[] diffs = findDifferences(start, end);
            if (isMoveSimple(diffs[0], diffs[1])) {
                return canJumpMove(game, lastMoveMade) == null;
            }
        }
        Piece.Color enemyColor;
        boolean canJump;
        if (activeColor == Piece.Color.RED) {
            enemyColor = Piece.Color.WHITE;
        } else {
            enemyColor = Piece.Color.RED;
        }
        int row = lastMoveMade.getEndRow();
        int cell = lastMoveMade.getEndCell();
        canJump = checkForwardJumps(row, cell, board, enemyColor, lastMoveMade.getStart(), game.getPiecesJumped()) != null;
        if (canJump) {
            game.setInvalidTurnErrorMessage(MORE_JUMP_ERROR_MESSAGE);
            return false;
        }
        if (rank == Piece.Type.KING) {
            canJump = checkBackwardJumps(row, cell, board, enemyColor, lastMoveMade.getStart(), game.getPiecesJumped()) != null;
        }

        if(canJump){
            game.setInvalidTurnErrorMessage(MORE_JUMP_ERROR_MESSAGE);
        }
        return !canJump;
    }

    /**
     * Checks if there are any available forward jumps that can still be made this turn
     *
     * @param row        the row that the piece currently resides in
     * @param cell       the cell in the given row that the piece resides in
     * @param board      the board that keeps track of the state of the current game
     * @param enemyColor the color of the pieces of the opposing player
     * @param start      the starting position of the piece at the beginning of this turn
     * @return forward jump Move if one is available, null otherwise
     */
    private static Move checkForwardJumps(int row, int cell, BoardView board, Piece.Color enemyColor, Position start,
                                          HashSet<Piece> piecesJumped) {
        int startRow = start.getRow();
        int startCell = start.getCell();

        Position moveStart = new Position(row, cell);

        if (row - JUMP_DIST >= 0) {
            Piece jumpedPiece;
            Piece pieceAtJump;
            if ((cell - JUMP_DIST >= 0) && (cell - JUMP_DIST != startCell || row - JUMP_DIST != startRow)) {
                jumpedPiece = board.getPieceAtSpace(row - 1, cell - 1);
                pieceAtJump = board.getPieceAtSpace(row - JUMP_DIST, cell - JUMP_DIST);
                if (pieceAtJump == null && jumpedPiece != null && jumpedPiece.getColor() == enemyColor
                        && !piecesJumped.contains(jumpedPiece)) {
                    return new Move(moveStart, new Position(row - JUMP_DIST, cell - JUMP_DIST));
                }
            }
            if ((cell + JUMP_DIST < ROWS) && (cell + JUMP_DIST != startCell || row - JUMP_DIST != startRow)) {

                jumpedPiece = board.getPieceAtSpace(row - 1, cell + 1);
                pieceAtJump = board.getPieceAtSpace(row - JUMP_DIST, cell + JUMP_DIST);
                if (pieceAtJump == null && jumpedPiece != null && jumpedPiece.getColor() == enemyColor
                        && !piecesJumped.contains(jumpedPiece)) {
                    return new Move(moveStart, new Position(row - JUMP_DIST, cell + JUMP_DIST));
                }
            }
        }
        return null;
    }

    /**
     * Checks if there are any available backward jumps that can still be made this turn. This only
     * applies if the current piece is a king piece.
     *
     * @param row        the row that the piece currently resides in
     * @param cell       the cell in the given row that the piece resides in
     * @param board      the board that keeps track of the state of the current game
     * @param enemyColor the color of the pieces of the opposing player
     * @param start      the starting position of the piece at the beginning of this turn
     * @return a jump Move if available, null otherwise
     */
    private static Move checkBackwardJumps(int row, int cell, BoardView board, Piece.Color enemyColor, Position start,
                                           HashSet<Piece> piecesJumped) {
        int startRow = start.getRow();
        int startCell = start.getCell();

        Position moveStart = new Position(row, cell);

        if (row + JUMP_DIST < ROWS) {
            Piece jumpedPiece;
            Piece pieceAtJump;
            if ((cell - JUMP_DIST >= 0) && (cell - JUMP_DIST != startCell || row + JUMP_DIST != startRow)) {
                jumpedPiece = board.getPieceAtSpace(row + 1, cell - 1);
                pieceAtJump = board.getPieceAtSpace(row + JUMP_DIST, cell - JUMP_DIST);
                if (pieceAtJump == null && jumpedPiece != null && jumpedPiece.getColor() == enemyColor
                        && !piecesJumped.contains(jumpedPiece)) {
                    return new Move(moveStart, new Position(row + JUMP_DIST, cell - JUMP_DIST));
                }
            }
            if ((cell + JUMP_DIST < CELLS_IN_ROW) && (cell + JUMP_DIST != startCell || row + JUMP_DIST != startRow)) {
                jumpedPiece = board.getPieceAtSpace(row + 1, cell + 1);
                pieceAtJump = board.getPieceAtSpace(row + JUMP_DIST, cell + JUMP_DIST);
                if (pieceAtJump == null && jumpedPiece != null && jumpedPiece.getColor() == enemyColor
                        && !piecesJumped.contains(jumpedPiece)) {
                    return new Move(moveStart, new Position(row + JUMP_DIST, cell + JUMP_DIST));
                }
            }
        }
        return null;
    }

    /**
     * Method that checks if any of the active players moves could have jumped. This method is called when the player
     * made a simple move, and makes sure they couldn't have jumped. Returns a jump move if there is one
     *
     * @param game - the game of checkers being played
     * @return Move - jump move if any pieces could jump, null otherwise
     * This works by looping over every possible space and going through a bunch of if statements to check if a piece
     * could jump.
     */
    public static Move canJumpMove(CheckersGame game, Move lastMoveMade) {
        Move move;
        Piece.Color activeColor = game.getActiveColor();
        Piece.Color enemyColor;
        if (activeColor == Piece.Color.RED) {
            enemyColor = Piece.Color.WHITE;
        } else {
            enemyColor = Piece.Color.RED;
        }

        BoardView board = game.getBoard();

        for (int row = 0; row < ROWS; row++) {
            for (int cell = 0; cell < CELLS_IN_ROW; cell++) {
                Piece piece = board.getPieceAtSpace(row, cell);
                if (piece != null && piece.getColor() == activeColor) {
                    move = checkForwardJumps(row, cell, board, enemyColor, lastMoveMade.getStart(), game.getPiecesJumped());
                    if (move != null) {
                        game.setInvalidTurnErrorMessage(MORE_JUMP_ERROR_MESSAGE);
                        return move;
                    }
                    if (piece.getType() == Piece.Type.KING) {
                        move = checkBackwardJumps(row, cell, board, enemyColor, lastMoveMade.getStart(), game.getPiecesJumped());
                    }
                    if (move != null) {
                        game.setInvalidTurnErrorMessage(MORE_JUMP_ERROR_MESSAGE);
                        return move;
                    }
                }

            }
        }
        return null;
    }

    /**
     * Checks to see if a given piece has any available valid simple moves
     *
     * @param row   the row of the given piece
     * @param cell  the cell of the given piece
     * @param piece the piece itself
     * @param board the board that keeps track of the state of the game
     * @return true if this piece has valid simple moves, false otherwise
     */
    private static Move checkSimpleMoves(int row, int cell, Piece piece, BoardView board) {
        Position start = new Position(row, cell);
        Move move = null;
        if ((row - 1 >= 0 && cell - 1 >= 0 && board.getPieceAtSpace(row - 1, cell - 1) == null)) {
            move = new Move(start, new Position(row - 1, cell - 1));
        }
        if (row - 1 >= 0 && cell + 1 < ROWS && board.getPieceAtSpace(row - 1, cell + 1) == null) {
            move = new Move(start, new Position(row - 1, cell + 1));
        }

        if (piece.getType() == Piece.Type.KING) {
            if (row + 1 < ROWS && cell - 1 >= 0 && board.getPieceAtSpace(row + 1, cell - 1) == null) {
                move = new Move(start, new Position(row + 1, cell - 1));
            }
            if (row + 1 < ROWS && cell + 1 < ROWS && board.getPieceAtSpace(row + 1, cell + 1) == null) {
                move = new Move(start, new Position(row + 1, cell + 1));
            }
        }

        return move;
    }

    /**
     * Checks to see if the current player has any valid moves they can make, prioritizing jump moves
     *
     * @param board       the board containing the state of the game
     * @param activeColor the piece color of the player whose turn it is
     * @return Returns a move if there is one, null otherwise
     */
    public static Move hasValidMoves(BoardView board, Piece.Color activeColor) {
        Piece piece;
        Move move;
        Move jump = null;
        Move simple = null;
        for (int row = 0; row < ROWS; row++) {
            for (int cell = 0; cell < CELLS_IN_ROW; cell++) {
                piece = board.getPieceAtSpace(row, cell);
                if (piece != null && piece.getColor() == activeColor) {
                    move = hasValidJump(board, piece, row, cell, new HashSet<>());
                    if (move != null) {
                        jump = move;
                    }
                    move = checkSimpleMoves(row, cell, piece, board);
                    if (move != null) {
                        simple = move;
                    }
                }
            }
        }
        if (jump != null) {
            return jump;
        } else if (simple != null) {
            return simple;
        }

        return null;
    }

    /**
     * Returns true if the passed in piece would have a valid jump on the given board at the given coordinates,
     * excluding the spot it would have came from (null if this is the first jump)
     *
     * @param board        BoardView
     * @param piece        piece in question
     * @param row          starting row of piece
     * @param cell         starting cell of piece
     * @param piecesJumped previous move (null if nonexistent)
     * @return a valid Move, or null if one does not exist
     */
    public static Move hasValidJump(BoardView board, Piece piece, int row, int cell, HashSet<Piece> piecesJumped) {
        Position position = new Position(row, cell);

        Move jump = null;
        Move move = checkForwardJumps(row, cell, board, piece.otherColor(), position, piecesJumped);
        if (move != null) {
            jump = move;
        }

        if (piece.getType() == Piece.Type.KING) {
            move = checkBackwardJumps(row, cell, board, piece.otherColor(), position, piecesJumped);
            if (move != null) {
                jump = move;
            }
        }
        return jump;
    }

}
