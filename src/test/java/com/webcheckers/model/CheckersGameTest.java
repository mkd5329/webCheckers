package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing class for the CheckersGame Class
 *
 * @author <a href='mailto:mjd6839@rit.edu'>Matthew DellaNeve</a>
 */
@Tag("Model-tier")
public class CheckersGameTest {

    private final Player redPlayer = new Player("redPlayer");
    private final Player whitePlayer = new Player("whitePlayer");
    private final Player otherPlayer = new Player("otherPlayer");

    // Player names for special board cases
    private final Player redWins = new Player(BoardView.REDWINS);
    private final Player whiteWins = new Player(BoardView.WHITEWINS);
    private final Player AIWins = new Player(BoardView.AIWINS);
    private final Player AILoses = new Player(BoardView.AILOSES);
    private final Player blocked = new Player(BoardView.BLOCKED);
    private final Player kingMultiple = new Player(BoardView.KINGMULTIPLE);
    private final Player kingSimple = new Player(BoardView.KINGSIMPLE);
    private final Player singleMultiple = new Player(BoardView.SINGLEMULTIPLE);


    private final CheckersGame CuT = new CheckersGame(redPlayer, whitePlayer);

    /**
     * Tests values after a CheckersGame object has been created
     */
    @Test
    public void test_create_game() {
        final Piece.Color color = Piece.Color.RED;

        assertNotNull(redPlayer);
        assertNotNull(whitePlayer);

        assertEquals(redPlayer, CuT.getRedPlayer());
        assertEquals(whitePlayer, CuT.getWhitePlayer());

        assertEquals(color, CuT.getActiveColor());

        assertNotNull(CuT.getBoard(redPlayer));

        assertNotNull(CuT.getTurnMoves());
    }

    /**
     * Tests the hasPlayer method
     */
    @Test
    public void test_has_player() {
        assertTrue(CuT.hasPlayer(redPlayer));
        assertTrue(CuT.hasPlayer(whitePlayer));
        assertTrue(CuT.hasPlayer("redPlayer"));
        assertTrue(CuT.hasPlayer("whitePlayer"));
        assertFalse(CuT.hasPlayer(otherPlayer));
        assertFalse(CuT.hasPlayer("otherPlayer"));
    }

    @Test
    public void test_get_inverted_board() {
        BoardView invertedBoard = CuT.getBoard(whitePlayer);
        assertNotNull(invertedBoard);
        assertNotEquals(CuT.getBoard(redPlayer), invertedBoard);
    }

    @Test
    public void test_add_and_remove_move() {
        Move move = new Move(new Position(6, 1), new Position(5, 2));
        CuT.addMove(move);
        assertEquals(move, CuT.removeMove());
    }

    @Test
    public void test_clear_moves() {
        Move move = new Move(new Position(6, 1), new Position(5, 2));
        CuT.addMove(move);
        CuT.addMove(move);
        CuT.clearMoves();
        assertTrue(CuT.getTurnMoves().empty());
    }

    /**
     * Tests the equals method for CheckersGame when two objects are equal
     */
    @Test
    public void test_two_equal_objects() {
        final CheckersGame CuT2 = new CheckersGame(redPlayer, whitePlayer);

        assertNotSame(CuT, CuT2);
        assertEquals(CuT, CuT2);
    }

    /**
     * Tests the equals method for CheckersGame when two objects are not equal
     */
    @Test
    public void test_two_unequal_games() {
        final CheckersGame CuT2 = new CheckersGame(redPlayer, otherPlayer);

        assertNotSame(CuT, CuT2);
        assertNotEquals(CuT, CuT2);
    }

    /**
     * Tests the equals method for CheckersGame when one object is not a CheckersGame
     */
    @Test
    public void test_unequal_with_non_game() {
        final Object foo = new Object();

        assertNotSame(CuT, foo);
        assertNotEquals(CuT, foo);
    }

    @Test
    public void test_remove_bad_player(){
        assertFalse(CuT.removePlayer(redPlayer), "Should not be able to remove from game in progress");

        CuT.resignGame(redPlayer);

        assertFalse(CuT.removePlayer(otherPlayer), "should not be able to remove player who is not in the" +
                " game");
    }

    /**
     * Tests if the game changes its' running state after a player
     * reaches 0 pieces left on the board
     */
    @Test
    public void test_endGame_domination1(){
        CheckersGame CuT1 = new CheckersGame(redPlayer, whitePlayer, 0, 1);
        Move move = new Move(new Position(5, 0), new Position(4, 1));
        CuT1.addMove(move);
        //white player wins
        CuT1.submitTurn();
        assertTrue(CuT1.isOver());
        assertEquals("whitePlayer has captured all the pieces.", CuT1.getGameOverMessage());
    }

    /**
     * Second test where the other player wins
     */
    @Test
    public void test_endGame_domination2(){
        CheckersGame CuT1 = new CheckersGame(redPlayer, whitePlayer, 1, 0);
        Move move = new Move(new Position(5, 0), new Position(4, 1));
        CuT1.addMove(move);
        //red player wins
        CuT1.submitTurn();
        assertTrue(CuT1.isOver());
        assertEquals("redPlayer has captured all the pieces.", CuT1.getGameOverMessage());
    }

    /**
     * Tests each creation of all the special boards.
     */
    @Test
    public void test_all_board_types(){
        CheckersGame game1 = new CheckersGame(redWins, whitePlayer);
        CheckersGame game2 = new CheckersGame(whiteWins, whitePlayer);
        CheckersGame game3 = new CheckersGame(AILoses, whitePlayer);
        CheckersGame game4 = new CheckersGame(AIWins, whitePlayer);
        CheckersGame game5 = new CheckersGame(blocked, whitePlayer);
        CheckersGame game6 = new CheckersGame(kingMultiple, whitePlayer);
        CheckersGame game7 = new CheckersGame(kingSimple, whitePlayer);
        CheckersGame game8 = new CheckersGame(singleMultiple, whitePlayer);

        assertNotSame(CuT, game1);
        assertNotSame(CuT, game2);
        assertNotSame(CuT, game3);
        assertNotSame(CuT, game4);
        assertNotSame(CuT, game5);
        assertNotSame(CuT, game6);
        assertNotSame(CuT, game7);
        assertNotSame(CuT, game8);
    }
}
