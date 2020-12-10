package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.CheckersGameService;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.when;


/**
 * @author <a href='mailto:nda7419@rit.edu'>Nicholas Antiochos</a>
 *
 * Tests PostCheckTurnRoute's route handling functionality
 */
@Tag("UI-tier")
public class PostCheckTurnRouteTest {
    //Class under testing
    private PostCheckTurnRoute CuT;

    //Mock
    private TemplateEngine engine;
    private Request request;
    private Response response;
    private Session session;


    //Player names
    private final String STARTING = "StartingPlayer";
    private final String WAITING = "WaitingPlayer";

    //Friendlies
    private Player starting;
    private Player waiting;
    private CheckersGameService checkersGameService;
    private Gson gson;

    /**
     * Sets up the test environment
     */
    @BeforeEach
    public void setup(){
        gson = new Gson();
        checkersGameService = new CheckersGameService();

        starting = new Player(STARTING);
        waiting = new Player(WAITING);

        checkersGameService.startGame(starting, waiting);

        CuT = new PostCheckTurnRoute(checkersGameService, gson);

        response = Mockito.mock(Response.class);
        request = Mockito.mock(Request.class);
        session = Mockito.mock(Session.class);

        when(request.session()).thenReturn(session);
    }

    /**
     * Tests the scenario where the session's player is the starting player (aka this player's turn)
     */
    @Test
    public void testStartingPlayer(){
        when(session.attribute("player")).thenReturn(starting);

        Object result = CuT.handle(request, response);
        assertNotNull(result);

        Message msg = gson.fromJson((String)result, Message.class);

        assertEquals(msg.getType(), Message.Type.INFO);
        assertEquals(msg.getText(), Boolean.toString(true), "Message object should be true for player" +
                " currently in turn");
    }

    /**
     * Tests the scenario where it is not the session's player's turn.
     */
    @Test
    public void testWaitingPlayer(){
        when(session.attribute("player")).thenReturn(waiting);

        Object result = CuT.handle(request, response);
        assertNotNull(result);

        Message msg = gson.fromJson((String)result, Message.class);

        assertEquals(msg.getType(), Message.Type.INFO);
        assertEquals(msg.getText(), Boolean.toString(false), "Message object should be false for player" +
                " waiting for turn");
    }

    /**
     * Tests the scenario where the session's player is null
     */
    @Test
    public void nullPlayer(){
        when(session.attribute("player")).thenReturn(null);

        Object result = CuT.handle(request, response);
        assertNotNull(result);

        Message msg = gson.fromJson((String)result, Message.class);

        assertEquals(msg.getType(), Message.Type.INFO);
        assertEquals(msg.getText(), Boolean.toString(true), "Message object should be true for null player");
    }

    /**
     * Tests the scenario where the session's player has no game.
     */
    @Test
    public void nullGame(){
        when(session.attribute("player")).thenReturn(new Player("lonely"));

        Object result = CuT.handle(request, response);
        assertNotNull(result);

        Message msg = gson.fromJson((String)result, Message.class);

        assertEquals(msg.getType(), Message.Type.INFO);
        assertEquals(msg.getText(), Boolean.toString(true), "Message object should be true for player in " +
                "a null game.");
    }
}
