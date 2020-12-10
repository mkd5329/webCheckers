package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.CheckersGameService;
import com.webcheckers.model.BoardView;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;
import com.webcheckers.model.TurnUtil;

import java.util.Objects;

import static com.webcheckers.ui.GetGameRoute.PLAYER;

public class PostSubmitTurnRoute implements Route {
    private final Gson gson;
    private final CheckersGameService checkersGameService;

    //error messages
    public final String JUMP_ERROR_MESSAGE = "There is a jump move that can be made";
    public final String INVALID_ERROR_MESSAGE = "That was an invalid combination of moves.";

    /**
     * Creates a new PostSubmitTurnRoute instance.
     * @param gson - converts back and forth between JSON and java objects.
     * @param checkersGameService - CheckersGameService instance.
     */
    public PostSubmitTurnRoute(Gson gson,
                               CheckersGameService checkersGameService){
        this.gson = gson;
        this.checkersGameService = checkersGameService;
        Objects.requireNonNull(gson, "gson must not be null");
        Objects.requireNonNull(checkersGameService, "checkersGameService must not be null");
    }

    /**
     * Handles the POST /submitTurn route
     * @param request - HTTP request
     * @param response - HTTP response
     * @return - message object of type INFO if resign successful, type ERROR otherwise
     */
    @Override
    public Object handle(Request request, Response response) {
        Player currentPlayer = request.session().attribute(PLAYER);
        CheckersGame game = checkersGameService.getGameByPlayer(currentPlayer);
        Message message = Message.info("Turn submitted.");
        BoardView board = game.getBoard();
        if (TurnUtil.isTurnValid(game, board)){
            game.submitTurn();
        }else {
            message = Message.error(game.getInvalidTurnErrorMessage());
        }
        return gson.toJson(message);
    }
}
