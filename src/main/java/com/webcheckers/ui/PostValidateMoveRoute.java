package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.CheckersGameService;
import com.webcheckers.model.*;
import com.webcheckers.util.Message;
import com.webcheckers.model.TurnUtil;
import spark.Request;
import spark.Response;
import spark.Route;
import java.util.Objects;
import static com.webcheckers.ui.GetGameRoute.PLAYER;

/**
 * The POST /validateMove route that activates when a user makes a move on the board. This route
 * checks that the move is valid and then adds it to the stack of moves this user has made this turn.
 *
 * @author <a href='mailto:mjd6839@rit.edu'>Matthew DellaNeve</a>
 */
public class PostValidateMoveRoute implements Route {
    //error messages
    private final String ERROR_MESSAGE = "The move you attempted to make was invalid. Please make a valid move.";
    private final String INFO_MESSAGE = "Move accepted.";

    private final Gson gson;
    private final CheckersGameService checkersGameService;

    public PostValidateMoveRoute(Gson gson, CheckersGameService checkersGameService) {
        this.gson = gson;
        this.checkersGameService = checkersGameService;
        Objects.requireNonNull(gson, "gson must not be null");
        Objects.requireNonNull(checkersGameService, "checkersGameService must not be null");
    }

    /**
     * Handles the request to make a move, and responds with a message in JSON format
     * @param request the request containing a JSON representation of a Move
     * @param response HTTP response
     * @return a JSON object containing a message conveying whether or not the move was valid
     */
    @Override
    public Object handle(Request request, Response response) {
        String moveJson = request.queryParams("actionData");
        Move move = gson.fromJson(moveJson, Move.class);

        Player currentPlayer = request.session().attribute(PLAYER);
        CheckersGame game = checkersGameService.getGameByPlayer(currentPlayer);
        BoardView board = game.getBoard();
        Message message;

        Piece pieceMoved = game.getFirstMovePiece();

        if (game.getActiveColor() == Piece.Color.WHITE){
            move.switchMoveSide();
        }

        Position start = move.getStart();
        Position end = move.getEnd();

        if (game.getFirstMovePiece() == null){
            pieceMoved = board.getSpaceAtRow(start.getRow(), start.getCell()).getPiece();
        }

        if (TurnUtil.isMoveValid(pieceMoved.getType(), start, end, game)) {
            game.addMove(move);
            message = Message.info(INFO_MESSAGE);
        } else {
            message = Message.error(ERROR_MESSAGE);
        }
        return gson.toJson(message);
    }
}
