package com.webcheckers.application;

import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Testing class for the PlayerLobby component
 *
 * @author <a href='mailto:zah1276@rit.edu'>Zeb Hollinger</a>
 */
@Tag("Application-tier")
class PlayerLobbyTest {
    //component under testing
    private PlayerLobby CuT;

    //mock
    private CheckersGameService gameService;

    //Player names
    private final String PLAYER_1 = "player1";
    private final String PLAYER_2 = "player2";
    private final String PLAYER_3 = "player3";
    private final String PLAYER_4 = "player4";

    //Bad names
    private final String PLAYER_1_COPY = "player1";
    private final String PLAYER_OUTSIDE_LOBBY = "not_here";
    private final String INVALID = " ";

    //player objects
    private final Player player1 = new Player(PLAYER_1);




    @BeforeEach
    public void setup() {
        gameService = mock(CheckersGameService.class);
        CuT = new PlayerLobby(gameService);

        //fill lobby with valid players to start
        CuT.signIn(PLAYER_1);
        CuT.signIn(PLAYER_2);
        CuT.signIn(PLAYER_3);
    }

    /**
     * Tests to see if PlayerLobby correctly signs in a
     * player who has an invalid username
     */
    @Test
    void signIn_invalidName() {
        Player invalidPlayer = new Player(INVALID);

        //test begins
        Player test = CuT.signIn(invalidPlayer.getName());
        assertNotEquals(test, invalidPlayer);
        assertNull(test);
    }

    /**
     * Tests to see if PlayerLobby correctly signs in a
     * player who is already in the lobby
     */
    @Test
    void signIn_inLobby() {
        Player samePlayer = new Player(PLAYER_1_COPY);

        //test begins
        Player test = CuT.signIn(samePlayer.getName());
        assertNotEquals(test, samePlayer);
        assertNull(test);
    }

    /**
     * Tests to see if PlayerLobby correctly signs in a
     * player who has a valid name and is new
     */
    @Test
    void signIn_newPlayer() {
        Player player4 = new Player(PLAYER_4);

        //test begins
        Player test = CuT.signIn(player4.getName());
        assertEquals(test, player4);
    }

    /**
     * Checks to see if a player gets taken out of the
     * lobby when a valid name is entered of a player
     * who is in the lobby
     */
    @Test
    void signOut_inLobby() {
        ArrayList<String> expected = new ArrayList<>();
        expected.add(PLAYER_3);

        //test begins
        CuT.signOut(PLAYER_1);
        ArrayList<String> test = CuT.getPlayerList(PLAYER_2);
        assertEquals(expected, test);
        assertNotNull(test);
        assertEquals(1, test.size());

    }

    /**
     * Checks to see if a player gets taken out of the
     * lobby when a player who is not in the lobby is entered
     */
    @Test
    void signOut_notInLobby() {
        ArrayList<String> expected = new ArrayList<>();
        expected.add(PLAYER_1);
        expected.add(PLAYER_3);

        //test begins
        CuT.signOut(PLAYER_OUTSIDE_LOBBY);
        ArrayList<String> test = CuT.getPlayerList(PLAYER_2);
        assertEquals(expected, test);
        assertNotNull(test);
        assertEquals(2, test.size());
    }

    /**
     * Checks to see how a invalid name is handled when asked
     * to sign that person out
     */
    @Test
    void signOut_inValidName() {
        ArrayList<String> expected = new ArrayList<>();
        expected.add(PLAYER_1);
        expected.add(PLAYER_3);

        //test begins
        CuT.signOut(INVALID);
        ArrayList<String> test = CuT.getPlayerList(PLAYER_2);
        assertEquals(expected, test);
        assertNotNull(test);
        assertEquals(2, test.size());
    }

    /**
     * Extra test used to cover the 2nd sign
     * out method
     */
    @Test
    void signOut2() {
        ArrayList<String> expected = new ArrayList<>();
        expected.add(PLAYER_3);

        //test begins
        CuT.signOut(player1);
        ArrayList<String> test = CuT.getPlayerList(PLAYER_2);
        assertEquals(expected, test);
        assertNotNull(test);
        assertEquals(1, test.size());

    }

    /**
     * Tests the base case where the player is in the
     * PlayerLobby and has not been added to a game held
     * in the CheckersGameService object
     */
    @Test
    void isPlayerReady_InLobby() {
        //test begins
        boolean result = CuT.isPlayerReady(PLAYER_1);
        assertTrue(result);
    }

    /**
     * Tests the case where the player is not in the
     * PlayerLobby.
     */
    @Test
    void isPlayerReady_notInLobby() {
        //test begins
        boolean result = CuT.isPlayerReady(PLAYER_OUTSIDE_LOBBY);
        assertFalse(result);
    }

    /**
     * Tests the case where the player is invalid
     * and not in the PlayerLobby.
     */
    @Test
    void isPlayerReady_invalidPlayer() {
        //test begins
        boolean result = CuT.isPlayerReady(INVALID);
        assertFalse(result);
    }

    /**
     * Tests the case where the player is in the
     * PlayerLobby and has been added to a game held
     * in the CheckersGameService object
     */
    @Test
    void isPlayerReady_InGame() {
        when(gameService.gameHasPlayer(player1)).thenReturn(true);

        //test begins
        boolean result = CuT.isPlayerReady(PLAYER_1);
        assertFalse(result);
    }

    /**
     * Tests that the getPlayer method returns
     * null when the player is not ready
     */
    @Test
    void getPlayer_playerNotReady() {
        when(gameService.gameHasPlayer(player1)).thenReturn(true);

        //test begins
        Player test = CuT.getPlayer(PLAYER_1);
        assertNull(test);
    }

    /**
     * Given the player is "ready", tests that the
     * getPlayer method returns the correct Player
     * object instance of the one asked for.
     */
    @Test
    void getPlayer_playerReady() {
        //test begins
        Player test = CuT.getPlayer(PLAYER_1);
        assertNotNull(test);
        assertEquals(player1, test);
    }

    /**
     * Tests to make sure that null is returned on
     * a invalid player username getPlayer call
     */
    @Test
    void getPlayer_invalidPlayer() {
        //test begins
        Player test = CuT.getPlayer(INVALID);
        assertNull(test);
    }

    @Test
    void getPlayerList() {
        ArrayList<String> expected = new ArrayList<>();
        expected.add(PLAYER_2);
        expected.add(PLAYER_3);

        //test begins
        ArrayList<String> test = CuT.getPlayerList(PLAYER_1);
        assertEquals(expected, test);
    }

    @Test
    void getNumberOfPlayers() {
        int expected = 3;

        //test begins
        int test = CuT.getNumberOfPlayers();
        assertEquals(expected, test);
    }
}
