package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.CheckersGameService;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Player;
import com.webcheckers.model.TurnUtil;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The UI Controller to GET the Game page.
 * This page is where the game of checkers is actually played.
 *
 * @author Michael Driscoll
 * @author <a href='mailto:mjd6839@rit.edu'>Matthew DellaNeve</a>
 */
public class GetGameRoute implements Route {

    private TemplateEngine templateEngine;
    private CheckersGameService checkersGameService;
    private Gson gson;


    //these are all the strings that need to be put into the vm
    final static String PLAYER = "player";
    final static String CURRENT_USER = "currentUser";
    final static String VIEW_MODE = "viewMode";
    final static String RED_PLAYER = "redPlayer";
    final static String WHITE_PLAYER = "whitePlayer";
    final static String CURRENT_TURN_COLOR = "activeColor";
    final static String BOARD = "board";
    final static String TITLE = "WEBCHECKERS";

    final static String IS_GAME_OVER = "isGameOver";
    final static String GAME_OVER_MESSAGE = "gameOverMessage";
    final static String MODE_OPTIONS_AS_JSON = "modeOptionsAsJSON";


    private enum ViewMode {PLAY, SPECTATOR, REPLAY}

    /**
     * Constructor of GetGameRoute, intializes the playerlobby and template engine
     * @param gson converts back and forth between JSON and java objects.
     * @param templateEngine the HTML template rendering engine
     */
    public GetGameRoute(TemplateEngine templateEngine,
                        CheckersGameService checkersGameService,
                        Gson gson) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        this.checkersGameService = Objects.requireNonNull(checkersGameService, "checkersGameService is required");
        this.gson = Objects.requireNonNull(gson, "gson is required");
    }


    /**
     * This is the method that actually renders the game page. It gets the player who is starting the game and the one
     * who is accepting the game.
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return the rendered HTML for the Checkers Game
     */
    @Override
    public Object handle(Request request, Response response) {

        final Session session = request.session();

        Player currentPlayer = session.attribute(PLAYER);

        if (currentPlayer == null) {
            response.redirect(WebServer.HOME_URL);
            return null;

        } else {

            Map<String, Object> vm = new HashMap<>();

            CheckersGame game = checkersGameService.getGameByPlayer(currentPlayer);
            if (game == null) {
                response.redirect(WebServer.HOME_URL);
                return null;
            }
            // placeholders for now until we get something else
            vm.put("title", TITLE);
            vm.put("gameID", "1");

            vm.put(CURRENT_USER, currentPlayer);

            vm.put(RED_PLAYER, game.getRedPlayer());
            vm.put(WHITE_PLAYER, game.getWhitePlayer());


            // placeholder until there is something to control turns
            vm.put(CURRENT_TURN_COLOR, game.getActiveColor());
            vm.put(BOARD, game.getBoard(currentPlayer));

            vm.put(VIEW_MODE, ViewMode.PLAY);

            if (!game.isOver() && TurnUtil.hasValidMoves(game.getBoard(), game.getActiveColor()) == null) {
                if (game.getActiveColor() == Piece.Color.RED) {
                    game.endGame(game.getRedPlayer());
                } else {
                    game.endGame(game.getWhitePlayer());
                }
            }

            if (game.isOver()) {
                Map<String, Object> modeOptions = new HashMap<>(2);
                modeOptions.put(IS_GAME_OVER, true);
                modeOptions.put(GAME_OVER_MESSAGE, game.getGameOverMessage());
                vm.put(MODE_OPTIONS_AS_JSON, gson.toJson(modeOptions));
            }

            return templateEngine.render(new ModelAndView(vm, "game.ftl"));
        }
    }
}
