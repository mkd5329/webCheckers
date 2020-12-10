package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.CheckersGameService;
import com.webcheckers.model.AITurn;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import static com.webcheckers.ui.GetGameRoute.PLAYER;

/**
 * @author <a href='mailto:nda7419@rit.edu'>Nicholas Antiochos</a>
 *
 * Class that handles the checkTurn route. Returns a message in the html with 'true' if it is this player's turn,
 * 'false' otherwise.
 */
public class PostCheckTurnRoute implements Route {

    private CheckersGameService checkersGameService;
    private Gson gson;

    /**
     * Creates a new PostCheckTurnRoute
     * @param checkersGameService - server's CheckersGameService
     * @param gson - converts back and forth between Java Object and JSON
     */
    public PostCheckTurnRoute(final CheckersGameService checkersGameService,
                              final Gson gson) {
        this.checkersGameService = checkersGameService;
        this.gson = gson;
    }

    /**
     * Checks if it is the current player's turn. Returns an INFO message in JSON containing 'true' if it is this
     * player's turn, 'false' otherwise.
     * @param request - the HTTP request
     * @param response - the HTTP response
     */
    @Override
    public Object handle(Request request, Response response){

        Session session = request.session();
        Player currentPlayer = session.attribute(PLAYER);
        Message message;

        if(currentPlayer == null){
            message = Message.info(Boolean.toString(true));
        }else{
            CheckersGame game = checkersGameService.getGameByPlayer(currentPlayer);
            if(game == null || game.isOver()) {
                message = Message.info(Boolean.toString(true));
            }else {

                //Gets player color (if current player is red color, must be red. White otherwise)
                Piece.Color playerColor;
                if (currentPlayer.equals(game.getRedPlayer())) {
                    playerColor = Piece.Color.RED;
                    if(game.isAIgame() && game.getActiveColor() == Piece.Color.WHITE){
                        AITurn.makeTurn(game);
                    }
                } else {
                    playerColor = Piece.Color.WHITE;
                }

                //Creates a message. Message contains true if player's color is equal to active color, false otherwise.
                if (playerColor.equals(game.getActiveColor())) {
                    message = Message.info(Boolean.toString(true));
                } else {
                    message = Message.info(Boolean.toString(false));
                }
            }
        }
        return gson.toJson(message);
    }
}
