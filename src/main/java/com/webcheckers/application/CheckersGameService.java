package com.webcheckers.application;

import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href='mailto:mjd6839@rit.edu'>Matthew DellaNeve</a>
 *
 * Class containing a set of all games currently being played including functionality to created, remove,
 * and retrieve a game as well as check if a given player is in a game.
 */
public class CheckersGameService {

    // The set of games currently in session
    private final Set<CheckersGame> games;

    /**
     * Constructor for CheckersGameService that initializes a set of games
     */
    public CheckersGameService() {
        games = new HashSet<>();
    }

    /**
     * Given a player, find the game this player is in
     * @param player the player in a CheckersGame that is being searched for
     * @return the CheckersGame that the Player is currently in, or null otherwise
     */
    public CheckersGame getGameByPlayer(Player player) {
        for (CheckersGame g : games) {
            if (g.hasPlayer(player)) {
                return g;
            }
        }
        return null;
    }

    /**
     * Given a player, find the game this player is in
     * @param player the player in a CheckersGame that is being searched for
     * @return the CheckersGame that the Player is currently in, or null otherwise
     */
    public CheckersGame getGameByPlayer(String player) {
        for (CheckersGame g : games) {
            if (g.hasPlayer(player)) {
                return g;
            }
        }
        return null;
    }

    /**
     * Allows checkersGame service to see if a game is empty, and remove it if it is. Should be called anytime after
     * a player is removed from the game.
     * @param g checkers game
     */
    public boolean checkEmpty(CheckersGame g){
        if(g != null && games.contains(g)) {
            if (g.hasNoPlayers()) {
                games.remove(g);
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a new CheckersGame and adds it to the set
     * @param redPlayer the red Player for this game
     * @param whitePlayer the white Player for this game
     */
    public void startGame(Player redPlayer, Player whitePlayer) {
        games.add(new CheckersGame(redPlayer, whitePlayer));
    }

    /**
     * Creates a new CheckersGame with the AI for a second player and adds it to the set
     * @param redPlayer - the red player for this game
     */
    public void startGame(Player redPlayer) {
        games.add(new CheckersGame(redPlayer));
    }

    /**
     * Removes a given game from the set of active games
     * @param game the game to remove
     */
    public void endGame(CheckersGame game) {
        games.remove(game);
    }

    /**
     * Check to see if there is an active game with the given Player playing it
     * @param player the Player for which we are checking if they are in a game
     * @return true if the Player is in a game, false otherwise
     */
    public boolean gameHasPlayer(Player player) {
        return getGameByPlayer(player) != null;
    }

    /**
     * Check to see if there is an active game with the given Player playing it
     * @param player the Player for which we are checking if they are in a game
     * @return true if the Player is in a game, false otherwise
     */
    public boolean gameHasPlayer(String player) {
        return getGameByPlayer(player) != null;
    }
}
