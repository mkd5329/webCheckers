package com.webcheckers.application;

import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Nicholas Antiochos
 * class for testing the CheckersGameService class.
 */
@Tag("Application-tier")
public class CheckersGameServiceTest {

    public static String PLAYER_1 = "player1";
    public static String PLAYER_2 = "player2";

    public static String PLAYER_3 = "player3";
    public static String PLAYER_4 = "player4";

    public static String PLAYER_5 = "player5";

    public static String FAKE_PLAYER = "stinky";

    PlayerLobby lobby;


    @Test
    public void EndNullGame(){
        CheckersGameService cgs = new CheckersGameService();

        try{
            cgs.endGame(null);
        }catch( Exception e){
            fail("Ending null game threw exception");
        }

    }

    /**
     * Tests gameHasPlayer on 2 players on a CheckersGameService with no games in progress
     */
    @Test
    public void GameHasPlayer_NoGames(){
        CheckersGameService cgs = new CheckersGameService();
        Player p1 = new Player(PLAYER_1);
        Player p2 = new Player(PLAYER_2);

        assertFalse(cgs.gameHasPlayer(p1), "No games in progress, yet gameHasPlayer returned true for p1");
        assertFalse(cgs.gameHasPlayer(p2), "No games in progress, yet gameHasPlayer returned true for p2");
    }

    /**
     * Tests gameHasPlayer with 1 game in progress. Sees if gameHasPlayer returns true when players are in game, and
     * returns false if the given player is not in the game
     */
    @Test
    public void gameHasPlayer_SingleGame(){
        CheckersGameService cgs = new CheckersGameService();
        Player p1 = new Player(PLAYER_1);
        Player p2 = new Player(PLAYER_2);

        Player bad = new Player(FAKE_PLAYER);

        cgs.startGame(p1, p2);

        assertTrue(cgs.gameHasPlayer(p1), "p1 in game, yet gameHasPlayer returns false");
        assertTrue(cgs.gameHasPlayer(p2), "p2 in game, yet gameHasPlayer returns false");

        assertFalse(cgs.gameHasPlayer(bad), "stinky is not in an active game, yet gameHasPlayer returns true");
    }

    /**
     * Tests if gameHasPlayer returns false for players after their game has been removed
     */
    @Test
    public void gameHasPlayer_AfterRemove(){
        CheckersGameService cgs = new CheckersGameService();
        Player p1 = new Player(PLAYER_1);
        Player p2 = new Player(PLAYER_2);

        Player bad = new Player(FAKE_PLAYER);

        cgs.startGame(p1, p2);

        cgs.endGame(cgs.getGameByPlayer(p1));

        assertFalse(cgs.gameHasPlayer(p1), "p1's game removed, yet gameHasPlayer returns true for p1");
        assertFalse(cgs.gameHasPlayer(p2), "p2's game removed, yet gameHasPlayer returns true for p2");



    }

    /**
     * Tests getGameByPlayer method when multiple games are currently running. Makes sure players can fetch their game,
     * and that 2 players in different games do not fetch equal games.
     */
    @Test
    public void getMultipleGames(){
        CheckersGameService cgs = new CheckersGameService();
        Player p1 = new Player(PLAYER_1);
        Player p2 = new Player(PLAYER_2);

        Player p3 = new Player(PLAYER_3);
        Player p4 = new Player(PLAYER_4);

        Player p5 = new Player(PLAYER_5);

        cgs.startGame(p1, p2);
        cgs.startGame(p3, p4);
        //for ai game
        cgs.startGame(p5);

        assertNotNull(cgs.getGameByPlayer(p1), "Created game could not be fetched");
        assertNotNull(cgs.getGameByPlayer(p3), "Created game could not be fetched");
        assertNotNull(cgs.getGameByPlayer(p5), "Created game could not be fetched");

        assertNotEquals(cgs.getGameByPlayer(p1), cgs.getGameByPlayer(p3),
                "Player objects belonging to 2 players in different games fetched the same game.");
    }

    /**
     * Tests getGameByPlayer on a nonexistent game. Should return null for all cases
     */
    @Test
    public void getNonexistentGame(){
        CheckersGameService cgs = new CheckersGameService();
        Player p1 = new Player(PLAYER_1);
        Player p2 = new Player(PLAYER_2);
        assertNull(cgs.getGameByPlayer(p1), "No games exist, yet game gotten.");
        assertNull(cgs.getGameByPlayer(p2), "No games exist, yet game gotten.");

        cgs.startGame(p1, p2);

        Player bad = new Player(FAKE_PLAYER);
        assertNull(cgs.getGameByPlayer(bad), "No games exist for this player, yet a game was gotten");
    }

    /**
     * Tests that getGameByPlayer returns the same game for both players that share a game of checkers.
     */
    @Test
    public void gameSameForBothPlayers(){
        CheckersGameService cgs = new CheckersGameService();
        Player p1 = new Player(PLAYER_1);
        Player p2 = new Player(PLAYER_2);

        cgs.startGame(p1, p2);

        assertEquals(cgs.getGameByPlayer(p1), cgs.getGameByPlayer(p2),
                "Both players do not fetch the same game");
    }

    /**
     * Tests that getGameByPlayer returns null for a game after it has been removed.
     */
    @Test
    public void testRemoveGame(){
        CheckersGameService cgs = new CheckersGameService();
        Player p1 = new Player(PLAYER_1);
        Player p2 = new Player(PLAYER_2);

        Player p3 = new Player(PLAYER_3);
        Player p4 = new Player(PLAYER_4);

        cgs.startGame(p1, p2);
        cgs.startGame(p3, p4);

        assertNotNull(cgs.getGameByPlayer(p1), "Game not properly fetched after created");

        cgs.endGame(cgs.getGameByPlayer(p1));

        assertNull(cgs.getGameByPlayer(p1), "Game was not properly removed after ended. Can still be fetched");
        assertNull(cgs.getGameByPlayer(p2), "Game was not properly removed after ended. Can still be fetched");

        assertNotNull(cgs.getGameByPlayer(p3), "Removing one game removed another in the process");
    }

    @Test
    public void testDestroyEmptyGame(){
        CheckersGameService cgs = new CheckersGameService();

        lobby = new PlayerLobby(cgs);
        Player p1 = lobby.signIn(PLAYER_1);
        Player p2 = lobby.signIn(PLAYER_2);

        cgs.startGame(p1, p2);
        CheckersGame game = cgs.getGameByPlayer(p1);
        game.resignGame(p1);

        game.removePlayer(p1);
        assertFalse(cgs.gameHasPlayer(p1), "p1 not in game, yet gameHasPlayer returns true");

        game.removePlayer(p2);
        assertFalse(cgs.gameHasPlayer(p2), "p2 not in game, yet gameHasPlayer returns true");
        assertTrue(cgs.checkEmpty(game));

        assertTrue(lobby.isPlayerReady(p1.getName()));
        assertTrue(lobby.isPlayerReady(p2.getName()));
        //Both players should be "ready", as they are no longer in a game


    }
}
