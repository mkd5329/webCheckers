package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.CheckersGameService;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import static com.webcheckers.ui.GetGameRoute.PLAYER;


/**
 * The UI controller for a player who resigns during a game
 *
 * @author <a href='mailto:nda7419@rit.edu'>Nicholas Antiochos</a>
 */
public class PostResignGameRoute implements Route{
    Gson gson;
    CheckersGameService checkersGameService;

    /**
     * Creates a new PostResignGameRoute instance.
     * @param gson - converts back and forth between JSON and java objects.
     * @param checkersGameService - CheckersGameService instance.
     */
    public PostResignGameRoute(final Gson gson,
                               final CheckersGameService checkersGameService){
        this.gson = gson;
        this.checkersGameService = checkersGameService;
    }

    /**
     * Handles the POST /resignGame route
     * @param request - HTTP request
     * @param response - HTTP response
     * @return - message object of type INFO if resign successful, type ERROR otherwise
     */
    @Override
    public Object handle(Request request, Response response) {
        Session session = request.session();

        Player currentUser = session.attribute(PLAYER);
        if(currentUser == null){
            return errorResigning("you do not exist.");
        }

        CheckersGame game = checkersGameService.getGameByPlayer(currentUser);

        if(game == null){
            return errorResigning("game doesn't exist.");
        }

        if(game.resignGame(currentUser)) {
            return makeResign();
        }else{
            return errorResigning("game already over.");
        }
    }

    /**
     * Creates a message that notifies the client that there was an error resigning
     * @param reason - further describes why game could not be resigned from
     * @return Json object
     */
    private Object errorResigning(String reason){
        Message m = Message.error("error resigning: " + reason);
        return gson.toJson(m);
    }

    /**
     * Creates a resign message object in JSON
     * @return - JSON rep. of resign message
     */
    private Object makeResign(){
        Message m = Message.info("resigned");
        return gson.toJson(m);
    }
}
