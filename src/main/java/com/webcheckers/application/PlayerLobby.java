package com.webcheckers.application;

import com.webcheckers.model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author <a href='mailto:nda7419@rit.edu'>Nicholas Antiochos</a>
 * @author <a href='mailto:mjd6839@rit.edu'>Matthew DellaNeve</a>
 *
 * Class representing the list of signed in players, giving functionality to add to, subtract from, or manipulate the
 * list
 */
public class PlayerLobby {

    private final CheckersGameService checkersGameService;

    private HashMap<String, Player> players; // Hash map of all currently signed in players

    /**
     * Creates a new player lobby. Should be only 1 per server
     */
    public PlayerLobby(CheckersGameService checkersGameService) {
        players = new HashMap<>();
        this.checkersGameService = checkersGameService;
    }

    /**
     * Signs in a new player if their username is valid, then returns the created Player Entity
     *
     * @param username - New player's username
     * @return - Player entity associated with the username if sign-in was successful, null otherwise.
     */
    public Player signIn(String username) {

        boolean isValid = Player.validateUsername(username);
        if (!isValid) {
            return null;
        }

        if (players.containsKey(username)) {
            return null;
        }

        Player newPlayer = new Player(username);
        players.put(username, newPlayer);
        return newPlayer;
    }

    /**
     * Signs out a player based on the player object itself
     *
     * @param player - Player being signed out
     */
    public void signOut(Player player) {
        String username = player.getName();
        signOut(username);
    }

    /**
     * Signs player out based on their unique username
     *
     * @param username the username of the player to sign out
     */
    public void signOut(String username) {
        if (players.containsKey(username)) {
            players.remove(username);
        }
    }


    /**
     * Checks if the player going by the given username is ready to join a game (exists, and is not already in one)
     *
     * @param username - username of player being checked
     * @return - T if ready to join a game, F otherwise.
     */
    public boolean isPlayerReady(String username) {
        if (players.containsKey(username)) {
            Player player = players.get(username);
            return !checkersGameService.gameHasPlayer(player);
        }
        return false;
    }

    /**
     * Gets a player from the lobby based on username.
     *
     * @param username - username of player being fetched
     * @return - Corresponding Player entity if found, null if nonexistent or in game.
     */
    public Player getPlayer(String username) {
        if (isPlayerReady(username)) {
            Player player = (Player) players.get(username);
            return player;
        }
        return null;
    }

    /**
     * Gets a list of all usernames of players that the inquiring player should be able to start a game with.
     *
     * @param playerName - Name of the asking player (Player should not appear in their own list)
     * @return - ArrayList of all players available to play a game
     */
    public ArrayList<String> getPlayerList(String playerName) {
        Set usernames = players.keySet();
        ArrayList<String> playerList = new ArrayList<>();

        for (Object key : usernames) {
            String username = (String) key;

            if (isPlayerReady(username) && //Player is not currently in a game
                    !(username.equals(playerName))) //Not player who's receiving the list
            {
                playerList.add(username);
            }
        }

        return playerList;
    }

    /**
     * Simple accessor to display the number of Players that
     * are currently waiting to play a game.
     *
     * @return number of people signed in and
     * waiting to play a game of checkers
     */
    public int getNumberOfPlayers() {
        int count = 0;
        for(String key : players.keySet()){
            if(isPlayerReady(key)) {
                ++count;
            }
        }
        return count;
    }

}
