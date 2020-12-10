package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for TurnUtil methods
 *
 * @author <a href='mailto:mjd6839@rit.edu'>Matthew DellaNeve</a>
 * @author <a href='mailto:daw1882@rit.edu'>Dade Wood</a>
 * @author Michael Driscoll
 */
@Tag("Model-tier")
public class TurnUtilTest {

    private Player p1 = new Player("p1");
    private Player p2 = new Player("p2");
    private CheckersGame game;

    private BoardView board = new BoardView();


    /**
     * Create a fresh new game for each test
     */
    @BeforeEach
    public void setup() {
        game = new CheckersGame(p1,p2);
    }

    /**
     * Test that a move made by a single red piece is valid
     */
    @Test
    public void test_is_move_valid_single_red_true() {
        Move move1 = new Move(new Position(5, 4), new Position(4, 5));
        boolean result1 = TurnUtil.isMoveValid(Piece.Type.SINGLE, move1.getStart(), move1.getEnd(), game);

        Move move2 = new Move(new Position(3, 4), new Position(1, 6));
        boolean result2 = TurnUtil.isMoveValid(Piece.Type.SINGLE, move2.getStart(), move2.getEnd(), game);

        assertTrue(result1);
        assertTrue(result2);
    }

    /**
     * Test that a move made by a single red piece is invalid
     */
    @Test
    public void test_is_move_valid_single_red_false() {
        Move move1 = new Move(new Position(5, 4), new Position(3, 4));
        boolean result1= TurnUtil.isMoveValid(Piece.Type.SINGLE, move1.getStart(), move1.getEnd(), game);

        Move move2 = new Move(new Position(5, 4), new Position(3, 6));
        boolean result2 = TurnUtil.isMoveValid(Piece.Type.SINGLE, move2.getStart(), move2.getEnd(), game);

        assertFalse(result1);
        assertFalse(result2);
    }

    /**
     * Test that a move made by a king red piece is valid
     */
    @Test
    public void test_is_move_valid_king_red_true() {
        Move move1 = new Move(new Position(3, 4), new Position(4, 3));
        boolean result1= TurnUtil.isMoveValid(Piece.Type.KING, move1.getStart(), move1.getEnd(), game);

        Move move2 = new Move(new Position(3, 4), new Position(4, 5));
        boolean result2 = TurnUtil.isMoveValid(Piece.Type.KING, move2.getStart(), move2.getEnd(), game);

        Move move3 = new Move(new Position(2, 5), new Position(0, 7));
        boolean result3 = TurnUtil.isMoveValid(Piece.Type.KING, move3.getStart(), move3.getEnd(), game);

        game.getPiecesJumped().clear();
        game.getPiecesJumped().add(board.getPieceAtSpace(1, 6));
        Move move4 = new Move(new Position(0, 7), new Position(2, 5));
        boolean result4 = TurnUtil.isMoveValid(Piece.Type.KING, move4.getStart(), move4.getEnd(), game);

        assertTrue(result1);
        assertTrue(result2);
        assertTrue(result3);
        assertTrue(result4);
    }

    /**
     * Test that a move made by a king red piece is invalid
     */
    @Test
    public void test_is_move_valid_king_red_false() {
        Move move1 = new Move(new Position(3, 4), new Position(3, 6));
        boolean result1= TurnUtil.isMoveValid(Piece.Type.KING, move1.getStart(), move1.getEnd(), game);

        Move move2 = new Move(new Position(7, 2), new Position(5, 5));
        boolean result2 = TurnUtil.isMoveValid(Piece.Type.KING, move2.getStart(), move2.getEnd(), game);

        assertFalse(result1);
        assertFalse(result2);
    }

    /**
     * Test that a move made by a single white piece is valid
     */
    @Test
    public void test_is_move_valid_single_white_true() {
        Move move1 = new Move(new Position(2, 4), new Position(3, 5));
        move1.switchMoveSide();
        boolean result1 = TurnUtil.isMoveValid(Piece.Type.SINGLE, move1.getStart(), move1.getEnd(), game);

        Move move2 = new Move(new Position(1, 4), new Position(2, 3));
        move2.switchMoveSide();
        boolean result2 = TurnUtil.isMoveValid(Piece.Type.SINGLE, move2.getStart(), move2.getEnd(), game);

        assertTrue(result1);
        assertTrue(result2);
    }

    /**
     * Test that a move made by a single white piece is invalid
     */
    @Test
    public void test_is_move_valid_single_white_false() {
        Move move1 = new Move(new Position(2, 3), new Position(4, 5));
        move1.switchMoveSide();
        boolean result1= TurnUtil.isMoveValid(Piece.Type.SINGLE, move1.getStart(), move1.getEnd(), game);

        Move move2 = new Move(new Position(3, 2), new Position(5, 4));
        boolean result2 = TurnUtil.isMoveValid(Piece.Type.SINGLE, move2.getStart(), move2.getEnd(), game);

        assertFalse(result1);
        assertFalse(result2);
    }

    /**
     * Test that a move made by a king white piece is valid
     */
    @Test
    public void test_is_move_valid_king_white_true() {
        Move move1 = new Move(new Position(3, 4), new Position(4, 3));
        move1.switchMoveSide();
        boolean result1= TurnUtil.isMoveValid(Piece.Type.KING, move1.getStart(), move1.getEnd(), game);

        Move move2 = new Move(new Position(3, 4), new Position(4, 5));
        move2.switchMoveSide();
        boolean result2 = TurnUtil.isMoveValid(Piece.Type.KING, move2.getStart(), move2.getEnd(), game);

        Move move3 = new Move(new Position(5, 2), new Position(7, 0));
        move3.switchMoveSide();
        boolean result3 = TurnUtil.isMoveValid(Piece.Type.KING, move3.getStart(), move3.getEnd(), game);

        game.getPiecesJumped().clear();
        game.getPiecesJumped().add(board.getPieceAtSpace(6, 1));
        Move move4 = new Move(new Position(7, 0), new Position(5, 2));
        move4.switchMoveSide();
        boolean result4 = TurnUtil.isMoveValid(Piece.Type.KING, move4.getStart(), move4.getEnd(), game);

        assertTrue(result1);
        assertTrue(result2);
        assertTrue(result3);
        assertTrue(result4);
    }

    /**
     * Test that a move made by a king white piece is invalid
     */
    @Test
    public void test_is_move_valid_king_white_false() {
        Move move1 = new Move(new Position(3, 4), new Position(3, 6));
        move1.switchMoveSide();
        boolean result1= TurnUtil.isMoveValid(Piece.Type.KING, move1.getStart(), move1.getEnd(), game);

        Move move2 = new Move(new Position(7, 2), new Position(5, 5));
        move2.switchMoveSide();
        boolean result2 = TurnUtil.isMoveValid(Piece.Type.KING, move2.getStart(), move2.getEnd(), game);

        assertFalse(result1);
        assertFalse(result2);
    }

    /**
     * Test that the findDifferences method produces the correct difference
     * between rows and columns for starting and ending positions
     */
    @Test
    public void test_find_differences(){
        Position start = new Position(5, 4);
        Position end = new Position(4, 5);
        int[] diff = TurnUtil.findDifferences(start, end);
        assertNotNull(diff);
        assertEquals(1, diff[0]);
        assertEquals(-1, diff[1]);
    }

    /**
     * Test that isMoveSimple can detect what is and is not a simple move
     */
    @Test
    public void test_is_move_simple(){
        assertTrue(TurnUtil.isMoveSimple(1, -1));
        assertFalse(TurnUtil.isMoveSimple(0, 0));
        assertFalse(TurnUtil.isMoveSimple(-2, -2));
    }

    /**
     * Test that isMoveJump can detect what is and is not a jump move
     */
    @Test
    public void test_is_move_jump(){
        assertTrue(TurnUtil.isMoveJump(-2, -2));
        assertFalse(TurnUtil.isMoveJump(0, 0));
        assertFalse(TurnUtil.isMoveJump(1, 1));
    }

    /**
     * Test that isTurnValid returns true for a valid turn involving a single piece
     */
    @Test
    public void test_valid_turn_single(){

        game.addMove(new Move(new Position(1, 4), new Position(2, 3)));
        assertTrue(TurnUtil.isTurnValid(game, board));
        assertFalse(game.getTurnMoves().empty());

        CheckersGame game2 = new CheckersGame(p1, p2);

        game2.addMove(new Move(new Position(2, 5), new Position(4, 3)));
        game2.addMove(new Move(new Position(4, 3), new Position(6, 5)));
        assertTrue(TurnUtil.isTurnValid(game2, board));
        assertFalse(game2.getTurnMoves().empty());
    }

    /**
     * Test that isTurnValid returns false for an invalid turn involving a single piece
     */
    @Test
    public void test_invalid_turn_single(){
        BoardView testBoard = new BoardView(true);
        testBoard.PlaceSinglePiece(6, 1, Piece.Color.RED);
        CheckersGame game3 = new CheckersGame(p1, p2);
        game3.addMove(new Move(new Position(6, 1), new Position(5, 2)));
        game3.addMove(new Move(new Position(5, 2), new Position(4, 3)));

        assertFalse(TurnUtil.isTurnValid(game3, testBoard));
        assertFalse(game3.getTurnMoves().empty());
    }

    /**
     * Test that isTurnValid returns true for a valid turn involving a king piece
     */
    @Test
    public void test_valid_turn_king(){
        BoardView board = new BoardView(true);
        board.PlaceSinglePiece(1, 4, Piece.Color.WHITE);
        board.PlaceSinglePiece(3, 4, Piece.Color.WHITE);
        board.PlaceSinglePiece(2, 3, Piece.Color.RED);
        board.getSpaceAtRow(2, 3).getPiece().kingMe();
        game.addMove(new Move(new Position(0, 5), new Position(2, 3)));
        game.addMove(new Move(new Position(2, 3), new Position(4, 5)));

        assertTrue(TurnUtil.isTurnValid(game, board));
    }

    /**
     * Test that isTurnValid returns false for an invalid turn involving a king piece
     */
    @Test
    public void test_invalid_turn_king(){
        BoardView board = new BoardView(true);
        board.PlaceSinglePiece(1, 4, Piece.Color.WHITE);
        board.PlaceSinglePiece(2, 3, Piece.Color.RED);
        board.getSpaceAtRow(2, 3).getPiece().kingMe();
        game.addMove(new Move(new Position(0, 5), new Position(2, 3)));
        game.addMove(new Move(new Position(2, 3), new Position(3, 2)));

        assertFalse(TurnUtil.isTurnValid(game, board));
    }

    /**
     * Test that canJumpMove returns true for a single piece that has an
     * available jump move it could have taken
     */
    @Test
    public void test_can_jump_single() {
        BoardView board = game.getBoard(p1);

        board.PlaceSinglePiece(5,2,Piece.Color.WHITE);
        Move move = new Move(new Position(6,1),new Position(5,0));
        game.addMove(move);
        assertNotNull(TurnUtil.canJumpMove(game, move));
    }

    /**
     * Test that canJumpMove returns false for a single piece that has no
     * available jump move it could have taken
     */
    @Test
    public void test_cannot_jump_single() {
        CheckersGame game = new CheckersGame(p1, p2);

        Move move = new Move(new Position(6,1),new Position(5,2));
        game.addMove(move);

        assertNull(TurnUtil.canJumpMove(game, move));
    }

    /**
     * Test that canJumpMove returns true for a king piece that has an
     * available jump move it could have taken
     */
    @Test
    public void test_can_jump_king() {
        BoardView board = game.getBoard(p1);

        board.PlaceSinglePiece(4,3, Piece.Color.WHITE);
        board.PlaceSinglePiece(3, 4, Piece.Color.RED);
        board.getSpaceAtRow(3, 4).getPiece().kingMe();
        Move move = new Move(new Position(3,4),new Position(4,5));
        game.addMove(move);
        assertNotNull(TurnUtil.canJumpMove(game, move));
    }

    /**
     * Test that canJumpMove returns false for a king piece that has no
     * available jump move it could have taken
     */
    @Test
    public void test_cannot_jump_king() {
        board.PlaceSinglePiece(3, 4, Piece.Color.RED);
        board.getSpaceAtRow(3, 4).getPiece().kingMe();

        Move move = new Move(new Position(3,4),new Position(4,5));
        game.addMove(move);

        assertNull(TurnUtil.canJumpMove(game, move));
    }

    /**
     * Test that hasValidMoves returns true when the player whose turn it is has moves
     * they can make.
     */
    @Test
    public void test_has_valid_moves_true() {
        assertNotNull(TurnUtil.hasValidMoves(board, game.getActiveColor()));
        assertNotNull(TurnUtil.hasValidMoves(board.invertBoardView(), Piece.Color.WHITE));

        BoardView board2 = new BoardView(true);
        board2.PlaceSinglePiece(0, 7, Piece.Color.RED);
        board2.kingPieceAtSpace(0, 7);
        assertNotNull(TurnUtil.hasValidMoves(board2, game.getActiveColor()));

        board2.PlaceSinglePiece(1,6, Piece.Color.WHITE);
        assertNotNull(TurnUtil.hasValidMoves(board2, game.getActiveColor()));

        board2 = new BoardView(true);
        board2.PlaceSinglePiece(7, 0, Piece.Color.RED);
        board2.kingPieceAtSpace(7, 0);
        assertNotNull(TurnUtil.hasValidMoves(board2, game.getActiveColor()));

        board2.PlaceSinglePiece(6, 1, Piece.Color.WHITE);
        assertNotNull(TurnUtil.hasValidMoves(board2, game.getActiveColor()));

        board2 = new BoardView(true);
        board2.PlaceSinglePiece(3, 0, Piece.Color.RED);
        assertNotNull(TurnUtil.hasValidMoves(board2, game.getActiveColor()));
    }

    /**
     * Test that hasValidMoves returns false when the player whose turn it is has
     * no possible moves they can make.
     */
    @Test
    public void test_has_valid_moves_false() {
        BoardView board2 = new BoardView(true);
        board2.PlaceSinglePiece(4, 7, Piece.Color.RED);
        board2.PlaceSinglePiece(3, 6, Piece.Color.WHITE);
        board2.PlaceSinglePiece(2, 5, Piece.Color.WHITE);
        assertNull(TurnUtil.hasValidMoves(board2, game.getActiveColor()));

        board2 = new BoardView(true);
        board2.PlaceSinglePiece(0, 5, Piece.Color.WHITE);
        board2.invertBoardView();
        assertNull(TurnUtil.hasValidMoves(board2, Piece.Color.WHITE));

        board2 = new BoardView(true);
        board2.PlaceSinglePiece(6, 3, Piece.Color.RED);
        board2.kingPieceAtSpace(6, 3);
        board2.PlaceSinglePiece(7, 2, Piece.Color.WHITE);
        board2.PlaceSinglePiece(7, 4, Piece.Color.WHITE);
        board2.PlaceSinglePiece(5, 2, Piece.Color.WHITE);
        board2.PlaceSinglePiece(5, 4, Piece.Color.WHITE);
        board2.PlaceSinglePiece(4, 1, Piece.Color.WHITE);
        board2.PlaceSinglePiece(4, 5, Piece.Color.WHITE);
        assertNull(TurnUtil.hasValidMoves(board2, game.getActiveColor()));

        board2 = new BoardView(true);
        board2.PlaceSinglePiece(3, 6, Piece.Color.RED);
        board2.PlaceSinglePiece(2, 5, Piece.Color.WHITE);
        board2.PlaceSinglePiece(2, 7, Piece.Color.WHITE);
        board2.PlaceSinglePiece(1, 4, Piece.Color.WHITE);
        assertNull(TurnUtil.hasValidMoves(board2, game.getActiveColor()));
    }
}
