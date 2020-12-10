package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.CheckersGameService;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.BoardView;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Testing class for the GetGameRoute component
 *
 * @author <a href='mailto:mjd6839@rit.edu'>Matthew DellaNeve</a>
 */
@Tag("UI-tier")
public class GetGameRouteTest {

    private GetGameRoute CuT;

    //Mock
    private PlayerLobby playerLobby;
    private TemplateEngine templateEngine;
    private Request request;
    private Response response;
    private Session session;
    private CheckersGameService gameService;

    //Attributes
    private final String PLAYER = "player";

    private TemplateEngineTester testHelper;

    private Gson gson;

    /**
     * Setup method that mocks injected classes plus the request and response, then creates
     * a new GetGameRoute component
     */
    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        response = mock(Response.class);
        session = mock(Session.class);
        gameService = mock(CheckersGameService.class);
        gson = new Gson();

        templateEngine = mock(TemplateEngine.class);

        when(request.session()).thenReturn(session);
        playerLobby = mock(PlayerLobby.class);
        when(playerLobby.getPlayer(null)).thenReturn(null);

        CuT = new GetGameRoute(templateEngine, gameService, gson);
    }

    /**
     * Test that a null Player object in the session causes the handle method to return null
     */
    @Test
    public void test_null_current_player() {
        when(session.attribute(PLAYER)).thenReturn(null);

        Object result = CuT.handle(request, response);
        assertNull(result);
    }

    /**
     * Test that a null CheckersGame object causes the handle method to return null
     */
    @Test
    public void test_null_game() {
        Player player = new Player(PLAYER);
        when(session.attribute(PLAYER)).thenReturn(player);
        when(gameService.getGameByPlayer(player)).thenReturn(null);

        Object result = CuT.handle(request, response);
        assertNull(result);
    }

    /**
     * Test that the handle method successfully renders the game.ftl file with the correct attributes
     */
    @Test
    public void test_render_game() {
        testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        CheckersGame game = new CheckersGame(player1, player2);
        when(session.attribute(PLAYER)).thenReturn(player1);
        when(gameService.getGameByPlayer(player1)).thenReturn(game);

        CuT.handle(request, response);

        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();

        testHelper.assertViewModelAttribute("title", GetGameRoute.TITLE);
        testHelper.assertViewModelAttribute("gameID", "1");
        testHelper.assertViewModelAttribute(GetGameRoute.CURRENT_USER, player1);
        testHelper.assertViewModelAttribute(GetGameRoute.RED_PLAYER, player1);
        testHelper.assertViewModelAttribute(GetGameRoute.WHITE_PLAYER, player2);
        testHelper.assertViewModelAttribute(GetGameRoute.CURRENT_TURN_COLOR, Piece.Color.RED);
        testHelper.assertViewModelAttribute(GetGameRoute.BOARD, game.getBoard(player1));

        testHelper.assertViewName("game.ftl");
    }

    @Test
    public void test_ended_game(){
        testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        CheckersGame game = new CheckersGame(player1, player2);
        when(session.attribute(PLAYER)).thenReturn(player1);
        when(gameService.getGameByPlayer(player1)).thenReturn(game);
        game.resignGame(player1);

        CuT.handle(request, response);

        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();

        Map<String, Object> modeoptions = new HashMap<>(2);
        modeoptions.put(GetGameRoute.IS_GAME_OVER, true);
        modeoptions.put(GetGameRoute.GAME_OVER_MESSAGE, String.format(CheckersGame.RESIGN_MSG, player1.getName()));
        String modeOptionsAsJSON = gson.toJson(modeoptions);

        testHelper.assertViewModelAttribute(GetGameRoute.MODE_OPTIONS_AS_JSON, modeOptionsAsJSON);
        testHelper.assertViewModelAttribute("title", GetGameRoute.TITLE);
        testHelper.assertViewModelAttribute("gameID", "1");
        testHelper.assertViewModelAttribute(GetGameRoute.CURRENT_USER, player1);
        testHelper.assertViewModelAttribute(GetGameRoute.RED_PLAYER, player1);
        testHelper.assertViewModelAttribute(GetGameRoute.WHITE_PLAYER, player2);
        testHelper.assertViewModelAttribute(GetGameRoute.CURRENT_TURN_COLOR, Piece.Color.RED);
        testHelper.assertViewModelAttribute(GetGameRoute.BOARD, game.getBoard(player1));
    }

    @Test
    public void test_end_game_no_valid_moves_red() {
        testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        CheckersGame game = new CheckersGame(player1, player2);
        BoardView board = game.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board.RemovePiece(i, j);
            }
        }
        board.PlaceSinglePiece(7, 0, Piece.Color.RED);
        board.PlaceSinglePiece(6, 1, Piece.Color.WHITE);
        board.PlaceSinglePiece(5, 2, Piece.Color.WHITE);

        when(session.attribute(PLAYER)).thenReturn(player1);
        when(gameService.getGameByPlayer(player1)).thenReturn(game);

        CuT.handle(request, response);

        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();

        Map<String, Object> modeoptions = new HashMap<>(2);
        modeoptions.put(GetGameRoute.IS_GAME_OVER, true);
        modeoptions.put(GetGameRoute.GAME_OVER_MESSAGE,
                String.format(CheckersGame.WIN_BY_NO_MOVES_MSG, player1.getName(), player2.getName()));
        String modeOptionsAsJSON = gson.toJson(modeoptions);

        testHelper.assertViewModelAttribute(GetGameRoute.MODE_OPTIONS_AS_JSON, modeOptionsAsJSON);
        testHelper.assertViewModelAttribute("title", GetGameRoute.TITLE);
        testHelper.assertViewModelAttribute("gameID", "1");
        testHelper.assertViewModelAttribute(GetGameRoute.CURRENT_USER, player1);
        testHelper.assertViewModelAttribute(GetGameRoute.RED_PLAYER, player1);
        testHelper.assertViewModelAttribute(GetGameRoute.WHITE_PLAYER, player2);
        testHelper.assertViewModelAttribute(GetGameRoute.CURRENT_TURN_COLOR, Piece.Color.RED);
        testHelper.assertViewModelAttribute(GetGameRoute.BOARD, game.getBoard(player1));
    }

    @Test
    public void test_end_game_no_valid_moves_white() {
        testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        CheckersGame game = new CheckersGame(player1, player2);
        game.setActiveColor(Piece.Color.WHITE);
        BoardView board = game.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board.RemovePiece(i, j);
            }
        }
        board.PlaceSinglePiece(7, 0, Piece.Color.WHITE);
        board.PlaceSinglePiece(6, 1, Piece.Color.RED);
        board.PlaceSinglePiece(5, 2, Piece.Color.RED);

        when(session.attribute(PLAYER)).thenReturn(player1);
        when(gameService.getGameByPlayer(player1)).thenReturn(game);

        CuT.handle(request, response);

        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();

        Map<String, Object> modeoptions = new HashMap<>(2);
        modeoptions.put(GetGameRoute.IS_GAME_OVER, true);
        modeoptions.put(GetGameRoute.GAME_OVER_MESSAGE,
                String.format(CheckersGame.WIN_BY_NO_MOVES_MSG, player2.getName(), player1.getName()));
        String modeOptionsAsJSON = gson.toJson(modeoptions);

        testHelper.assertViewModelAttribute(GetGameRoute.MODE_OPTIONS_AS_JSON, modeOptionsAsJSON);
        testHelper.assertViewModelAttribute("title", GetGameRoute.TITLE);
        testHelper.assertViewModelAttribute("gameID", "1");
        testHelper.assertViewModelAttribute(GetGameRoute.CURRENT_USER, player1);
        testHelper.assertViewModelAttribute(GetGameRoute.RED_PLAYER, player1);
        testHelper.assertViewModelAttribute(GetGameRoute.WHITE_PLAYER, player2);
        testHelper.assertViewModelAttribute(GetGameRoute.CURRENT_TURN_COLOR, Piece.Color.WHITE);
        testHelper.assertViewModelAttribute(GetGameRoute.BOARD, game.getBoard(player1));
    }
}
