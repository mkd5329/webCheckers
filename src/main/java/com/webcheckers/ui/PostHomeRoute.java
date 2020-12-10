package com.webcheckers.ui;

import com.webcheckers.application.CheckersGameService;
import spark.*;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.Player;

import java.util.Objects;

import static com.webcheckers.ui.WebServer.HOME_URL;


/**
 * The {@code POST /home} route handler
 * Stores vital information for the game
 *
 * @author Michael Driscoll
 */
public class PostHomeRoute implements Route {

    //the player who chooses to initiate the game
    static final String STARTING_PLAYER = "currentUser";

    //the player who has been chosen by someone else to play a game.
    static final String AWAITING_PLAYER = "awaitingUser";
    static final String DID_ERROR_OCCUR = "didErrorOccur";
    static final String DID_SIGN_OUT_OCCUR = "didSignOutOccur";


    private boolean errorOccurred = false;
    private boolean signOutOccurred = false;

    private final PlayerLobby playerLobby;
    private final CheckersGameService checkersGameService;

    /**
     * Constructor for the POST Home Route
     *
     * @param playerLobby playerlobby object
     * @param checkersGameService controller for the checkers games
     */
    public PostHomeRoute(PlayerLobby playerLobby,
                         CheckersGameService checkersGameService) {
        this.playerLobby = playerLobby;
        this.checkersGameService = checkersGameService;
        Objects.requireNonNull(playerLobby, "webServer must not be null");
        Objects.requireNonNull(checkersGameService, "checkersGameService must not be null");
    }


    private void storeErrorStatus(Session session) {
        session.attribute(DID_ERROR_OCCUR, errorOccurred);
    }

    private void storeSignedOutStatus(Session session) {
        session.attribute(DID_SIGN_OUT_OCCUR, signOutOccurred);
    }


    /**
     * Handles the POST /backupMove route
     * @param request - HTTP request
     * @param response - HTTP response
     * @return - message object of type INFO if resign successful, type ERROR otherwise
     */
    @Override
    public Object handle(Request request, Response response) {
        errorOccurred = false;

        //username of the starting player
        final String startingPlayerUsername =
                request.queryParams(STARTING_PLAYER);

        //username of the awaiting player
        final String awaitingPlayerUsername =
                request.queryParams(AWAITING_PLAYER);

        final Player startingPlayer =
                playerLobby.getPlayer(startingPlayerUsername);

        final Player awaitingPlayer =
                playerLobby.getPlayer(awaitingPlayerUsername);

        //this will happen if one the players is already in a game
        if ((startingPlayer == null) || (awaitingPlayer == null)) {
            if (!checkersGameService.gameHasPlayer(awaitingPlayerUsername)) {
                signOutOccurred = true;
            } else {
                errorOccurred = true;
            }
            storeErrorStatus(request.session());
            storeSignedOutStatus(request.session());

            response.redirect(HOME_URL);
        } else {
            if(awaitingPlayer.equals(startingPlayer)){
                checkersGameService.startGame(startingPlayer);
            }else{
                checkersGameService.startGame(startingPlayer, awaitingPlayer);
            }
            //store the two players so they can be accessed elsewhere
            storeErrorStatus(request.session());
            storeSignedOutStatus(request.session());

            response.redirect(WebServer.GAME_URL);
        }
        return null;
    }
}
