package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.CheckersGameService;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import spark.*;

import static org.mockito.Mockito.*;

/**
 * @author - Nicholas Antiochos
 *
 * Tests the ResignGameRoute's ability to handle the POST /resignGame route
 */
@Tag("UI-Tier")
public class PostResignGameRouteTest {

    private PostResignGameRoute CuT;

    private CheckersGameService checkersGameService;
    private Gson gson;

    private Request request;
    private Response response;
    private Session session;

    private Player starting;
    private Player waiting;

    private String STARTING = "starting";
    private String WAITING = "waiting";

    /**
     * Sets up common aspects of the testing environment.
     */
    @BeforeEach
    public void setup(){
        starting = new Player(STARTING);
        waiting = new Player(WAITING);

        checkersGameService = new CheckersGameService();
        gson = new Gson();

        CuT = new PostResignGameRoute(gson, checkersGameService);

        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);

        response = mock(Response.class);

        checkersGameService.startGame(starting, waiting);
    }

    /**
     * Tests the case where the starting player is the one who resigns.
     */
    @Test
    public void testStartingPlayer(){
        when(session.attribute("player")).thenReturn(starting);

        Object result = CuT.handle(request, response);
        assertNotNull(result);

        Message msg = gson.fromJson((String)result, Message.class);

        assertEquals(msg.getType(), Message.Type.INFO);

    }

    /**
     * Tests the case where the waiting player is the one who resigns
     */
    @Test
    public void testWaitingPlayer(){
        when(session.attribute("player")).thenReturn(waiting);

        Object result = CuT.handle(request, response);
        assertNotNull(result);

        Message msg = gson.fromJson((String)result, Message.class);

        assertEquals(msg.getType(), Message.Type.INFO);

    }

    /**
     * tests the case where the player is null
     */
    @Test
    public void testNullPlayer(){
        when(session.attribute("player")).thenReturn(null);

        Object result = CuT.handle(request, response);
        assertNotNull(result);

        Message msg = gson.fromJson((String)result, Message.class);

        assertEquals(msg.getType(), Message.Type.ERROR);
    }

    /**
     * Tests the case where the player is not in a game
     */
    @Test
    public void testNullGame(){
        when(session.attribute("player")).thenReturn(new Player("gameless"));

        Object result = CuT.handle(request, response);
        assertNotNull(result);

        Message msg = gson.fromJson((String)result, Message.class);

        assertEquals(msg.getType(), Message.Type.ERROR);
    }

    /**
     * Tests the case where the game is already over.
     */
    @Test
    public void testEndedGame(){
        when(session.attribute("player")).thenReturn(starting);

        CheckersGame game = checkersGameService.getGameByPlayer(starting);

        game.resignGame(waiting);

        Object result = CuT.handle(request, response);
        assertNotNull(result);

        Message msg = gson.fromJson((String)result, Message.class);

        assertEquals(msg.getType(), Message.Type.ERROR);
    }

}
