package com.webcheckers.model;

import java.util.Objects;

/**
 * @author <a href='mailto:nda7419@rit.edu'>Nicholas Antiochos</a>
 * @author <a href='mailto:mjd6839@rit.edu'>Matthew DellaNeve</a>
 *
 * The Player class represents a player logged into the checkers game.
 *
 * *NOTE* - if player is put in a game, use joinGame(), because PlayerLobby depends on isInGame being true if the
 * player is in a game
 */
public class Player {

    private final String name; //Player's unique name that will identify them

    /**
     * Creates a new player
     * @param username - new player's username (must be unique)
     */
    public Player(String username){
        this.name = username;
    }

    /**
     * Validate a given username. A username is valid if it contains at least one alphanumeric character
     * and contains no non-alphanumeric characters. White space is allowed.
     * @param username the given username to validate
     * @return true if username meets given criteria, false otherwise
     */
    public static boolean validateUsername(String username) {
        boolean containsAlphanum = false;
        if (username.length() == 0) {
            return false;
        } else {
            for (int i = 0; i < username.length(); i++) {
                char ch = username.charAt(i);
                if (!Character.isLetterOrDigit(ch) && !Character.isWhitespace(ch)) {
                    return false;
                }else if(!containsAlphanum && Character.isLetterOrDigit(ch)){
                    containsAlphanum = true;
                }
            }
        }

        return containsAlphanum;
    }

    /**
     * Returns the player's username
     * @return - string
     */
    public String getName(){
        return name;
    }

    /**
     * String representation of a player (username)
     * @return - string
     */
    @Override
    public String toString(){
        return name;
    }

    /**
     * Integer representing the hash of a player
     * @return - unique hash code
     */
    @Override
    public int hashCode(){
        return Objects.hash(this.name);
    }

    /**
     * Checks if another object is equal to this player based on usernames
     * @param other - Object this player is being compared to
     * @return - T if equal, F otherwise
     */
    @Override
    public boolean equals(Object other){
        if(other == this){
            return true;
        }
        if(other instanceof Player){
            if(((Player)other).name.equals(this.name)){
                return true;
            }
        }
        return false;

    }

}
