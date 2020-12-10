package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.CheckersGameService;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for PostValidateMoveRouteTest
 *
 * @author <a href='mailto:mjd6839@rit.edu'>Matthew DellaNeve</a>
 */
@Tag("UI-tier")
public class PostValidateMoveRouteTest {

    // Component under test
    private PostValidateMoveRoute CuT;

    // Mock
    private Request request;
    private Response response;
    private Session session;

    // Friendlies
    private final Gson gson = new Gson();
    private CheckersGameService gameService;

    // Strings
    private final String JSON_GOOD = "{\"start\":{\"row\":5,\"cell\":4},\"end\":{\"row\":4,\"cell\":5}}";
    private final String JSON_BAD = "{\"start\":{\"row\":6,\"cell\":1},\"end\":{\"row\":2,\"cell\":2}}";
    private final String JSON_INFO = "{\"text\":\"Move accepted.\",\"type\":\"INFO\"}";
    private final String JSON_ERROR = "{\"text\":\"The move you attempted to make was invalid. " +
            "Please make a valid move.\",\"type\":\"ERROR\"}";
    private final String ACTION_DATA = "actionData";

    private Player player = new Player("player");

    /**
     * Set up mocks for each test
     */
    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        response = mock(Response.class);
        session = mock(Session.class);

        when(request.session()).thenReturn(session);
        when(request.session().attribute("player")).thenReturn(player);

        gameService = new CheckersGameService();
        gameService.startGame(player, player);

        CuT = new PostValidateMoveRoute(gson, gameService);
    }

    /**
     * Test that the CuT responds with the proper message on a valid move
     */
    @Test
    public void test_move_is_valid() {
        when(request.queryParams(ACTION_DATA)).thenReturn(JSON_GOOD);
        String result = CuT.handle(request, response).toString();

        assertEquals(JSON_INFO, result);
    }

    /**
     * Test that the CuT responds with the proper message on an invalid move
     */
    @Test
    public void test_move_is_not_valid() {
        when(request.queryParams(ACTION_DATA)).thenReturn(JSON_BAD);
        String result = CuT.handle(request, response).toString();

        assertEquals(JSON_ERROR, result);
    }
}
