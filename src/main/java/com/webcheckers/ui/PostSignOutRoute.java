package com.webcheckers.ui;

import com.webcheckers.application.CheckersGameService;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.Player;
import spark.*;
import java.util.Objects;
import java.util.logging.Logger;

import static com.webcheckers.ui.GetGameRoute.PLAYER;
import static spark.Spark.halt;

/**
 * The {@code POST /signout} route handler
 *
 * @author <a href='mailto:zah1276@rit.edu'>Zeb Hollinger</a>
 */
public class PostSignOutRoute implements Route{
    private static final Logger LOG = Logger.getLogger(PostSignOutRoute.class.getName());

    //attributes
    private final PlayerLobby playerLobby;
    private final CheckersGameService gameService;

    /**
     * The constructor for the {@code POST /signin} route handler
     *
     * @param playerLobby application tier component used to handle sign-out actions
     * @param gameService application tier component used to handle checkers games
     */
    public PostSignOutRoute(PlayerLobby playerLobby, CheckersGameService gameService) {
        Objects.requireNonNull(playerLobby, "playerLobby must not be null");
        Objects.requireNonNull(playerLobby, "CheckersGameService must not be null");

        this.playerLobby = playerLobby;
        this.gameService = gameService;
        LOG.config("PostSignInRoute is initialized.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.fine("PostSignOutRoute is invoked");

        //get the currentUser
        final Player player = request.session().attribute(PLAYER);
        if(player != null) {
            if (gameService.gameHasPlayer(player)) {
                gameService.getGameByPlayer(player).resignGame(player);
            }
            playerLobby.signOut(player);

            //remove player name from session
            request.session().removeAttribute(PLAYER);
            response.redirect(WebServer.HOME_URL);
            halt();
        }
        response.redirect(WebServer.HOME_URL);
        return null;
    }
}
