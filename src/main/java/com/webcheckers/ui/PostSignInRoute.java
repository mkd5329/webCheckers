package com.webcheckers.ui;

import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.Player;
import spark.*;

import java.util.*;

import static com.webcheckers.ui.GetGameRoute.PLAYER;
import static spark.Spark.halt;

/**
 * The {@code POST /signin} route handler
 *
 * @author <a href='mailto:mjd6839@rit.edu'>Matthew DellaNeve</a>
 */
public class PostSignInRoute implements Route {

    private final PlayerLobby playerLobby;
    private final TemplateEngine templateEngine;

    //this variable allows for us to increase testability of code
    private boolean invalidUsername;
    private boolean didStorePlayer;


    static final String USERNAME_PARAM = "username";
    static final String MESSAGE_ATTR = "message";
    static final String MESSAGE_TYPE_ATTR = "messageType";
    static final String ERROR_TYPE = "error";
    static final String VIEW_NAME = "signin.ftl";
    static final String ERROR_MESSAGE = "Your username was either taken or invalid. Please enter " +
            "a valid username containing at least one alphanumeric character and no special characters.";

    /**
     * The constructor for the {@code POST /signin} route handler
     *
     * @param playerLobby application tier component used to handle sign-in actions
     * @param templateEngine template engine to use for rendering HTML page
     */
    public PostSignInRoute(PlayerLobby playerLobby, TemplateEngine templateEngine) {
        Objects.requireNonNull(playerLobby, "playerLobby must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");

        this.playerLobby = playerLobby;
        this.templateEngine = templateEngine;

        // added for testability
        invalidUsername = false;
        didStorePlayer = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object handle(Request request, Response response) {
        final Map<String, Object> vm = new HashMap<>();

        // Get the Player's entered username from the sign in page
        final String username = request.queryParams(USERNAME_PARAM);

        // Validate username and retrieve new Player object
        Player newPlayer = playerLobby.signIn(username);
        if (newPlayer == null) {
            invalidUsername = true;
            // Invalid username or taken, render sign in page again
            ModelAndView mv = error(vm, ERROR_MESSAGE);
            return templateEngine.render(mv);
        } else {
            // Store new Player in session and redirect to home page
            storePlayer(newPlayer, request.session());
            response.redirect(WebServer.HOME_URL);
            halt();
            return null;
        }
    }

    /**
     * Store the newly created Player object in the session
     * @param player the new Player object
     * @param session the current session
     */
    private void storePlayer(Player player, Session session) {
        didStorePlayer = true;
        session.attribute(PLAYER, player);
    }

    /**
     * Render an error message to the sign in template
     * @param vm the map of attributes and values for signin.ftl
     * @param message the error message to display
     * @return a new ModelAndView containing the error
     */
    private ModelAndView error(final Map<String, Object> vm, final String message) {
        vm.put(MESSAGE_ATTR, message);
        vm.put(MESSAGE_TYPE_ATTR, ERROR_TYPE);
        return new ModelAndView(vm, VIEW_NAME);
    }

    /**
     * @return T if username was invalid
     */
    public boolean getWasInvalidUsername() { return invalidUsername; }

    /**
     * @return T if player was stored
     */
    public boolean getDidStorePlayer() { return didStorePlayer; }
}
