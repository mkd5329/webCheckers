package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for testing AITurn class
 */
@Tag("Model-tier")
public class AITurnTest {

    Player redPlayer = new Player("redPlayer");

    CheckersGame game;
    BoardView board;

    /**
     * Create a fresh game with an empty board for every test
     */
    @BeforeEach
    public void setup() {
        game = new CheckersGame(redPlayer);
        board = game.getBoard();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board.RemovePiece(i, j);
            }
        }
    }

    /**
     * Test that the makeTurn method ends the game if the AI has
     * no more valid moves
     */
    @Test
    public void test_make_turn_no_moves() {
        board.PlaceSinglePiece(0, 7, Piece.Color.WHITE);
        board.PlaceSinglePiece(1, 6, Piece.Color.RED);
        board.PlaceSinglePiece(2, 5, Piece.Color.RED);

        assertNull(TurnUtil.hasValidMoves(board, Piece.Color.WHITE));

        AITurn.makeTurn(game);

        assertTrue(game.isOver());
        assertEquals(game.getGameOverMessage(), String.format(CheckersGame.WIN_BY_NO_MOVES_MSG,
                game.getWhitePlayer().getName(), game.getRedPlayer().getName()));
    }

    /**
     * Test that the AI can make a simple move
     */
    @Test
    public void test_make_turn_simple_move() {
        board.PlaceSinglePiece(7, 0, Piece.Color.WHITE);

        assertNotNull(TurnUtil.hasValidMoves(board, Piece.Color.WHITE));

        AITurn.makeTurn(game);

        assertFalse(game.isOver());
        assertNull(game.getGameOverMessage());
        assertNull(board.getPieceAtSpace(7, 0));
        assertNotNull(board.getPieceAtSpace(6, 1));
    }

    /**
     * Test that the AI can make a jump move
     */
    @Test
    public void test_make_turn_jump_move() {
        board.PlaceSinglePiece(7, 0, Piece.Color.WHITE);
        board.PlaceSinglePiece(6, 1, Piece.Color.RED);

        AITurn.makeTurn(game);

        assertFalse(game.isOver());
        assertNull(game.getGameOverMessage());
        assertNull(board.getPieceAtSpace(7, 0));
        assertNull(board.getPieceAtSpace(6, 1));
        assertNotNull(board.getPieceAtSpace(5, 2));
    }

    /**
     * Test that the AI can make a double jump move
     */
    @Test
    public void test_make_turn_double_jump_move() {
        board.PlaceSinglePiece(7, 0, Piece.Color.WHITE);
        board.PlaceSinglePiece(6, 1, Piece.Color.RED);
        board.PlaceSinglePiece(4, 3, Piece.Color.RED);

        AITurn.makeTurn(game);

        assertFalse(game.isOver());
        assertNull(game.getGameOverMessage());
        assertNull(board.getPieceAtSpace(7, 0));
        assertNull(board.getPieceAtSpace(6, 1));
        assertNull(board.getPieceAtSpace(4, 3));
        assertNotNull(board.getPieceAtSpace(3, 4));
    }
}
