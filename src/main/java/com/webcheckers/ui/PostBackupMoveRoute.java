package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.CheckersGameService;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Objects;

import static com.webcheckers.ui.GetGameRoute.PLAYER;

/**
 * The UI Controller to backup a move made on the board
 */
public class PostBackupMoveRoute implements Route {
    private final Gson gson;
    private final CheckersGameService checkersGameService;

    /**
     * Creates a new PostBackupMoveRoute instance.
     * @param gson - converts back and forth between JSON and java objects.
     * @param checkersGameService - CheckersGameService instance.
     */
    public PostBackupMoveRoute(Gson gson, CheckersGameService checkersGameService){
        this.gson = gson;
        this.checkersGameService = checkersGameService;
        Objects.requireNonNull(gson, "gson must not be null");
        Objects.requireNonNull(checkersGameService, "checkersGameService must not be null");
    }

    /**
     * Handles the POST /backupMove route
     * @param request - HTTP request
     * @param response - HTTP response
     * @return - message object of type INFO if resign successful, type ERROR otherwise
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        Player currentPlayer = request.session().attribute(PLAYER);
        CheckersGame game = checkersGameService.getGameByPlayer(currentPlayer);
        Message message = Message.info("Backup submitted.");

        if (!game.getTurnMoves().empty()){
            game.removeMove();
        }
        return gson.toJson(message);
    }
}
