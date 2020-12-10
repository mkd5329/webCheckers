package com.webcheckers.ui;


import com.webcheckers.application.CheckersGameService;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import spark.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Nicholas Antiochos
 *
 * Tests PostHomeRoute
 */

@Tag("UI-tier")
public class PostHomeRouteTest {

    //Class under testing
    private PostHomeRoute CuT;

    //Mock
    private PlayerLobby lobby;
    private TemplateEngine engine;
    private Request request;
    private Response response;
    private Session session;

    //Player names
    private final String STARTING = "StartingPlayer";
    private final String WAITING = "WaitingPlayer";

    //Friendlies
    private CheckersGameService gameService;
    private Player starting;
    private Player waiting;

    TemplateEngineTester testHelper;

    /**
     * Creates the player objects
     */
    @BeforeEach
    public void makePlayers(){
        starting = new Player(STARTING);
        waiting = new Player(WAITING);
    }

    /**
     * Sets up necessary mocks and class instances for PostHomeRoute execution
     */
    @BeforeEach
    public void setup(){

        request = mock(Request.class);
        response = mock(Response.class);
        session = mock(Session.class);

        engine = mock(TemplateEngine.class);


        when(request.session()).thenReturn(session);

        lobby = mock(PlayerLobby.class);
        when(lobby.getPlayer(null)).thenReturn(null);

        gameService = new CheckersGameService();

        CuT = new PostHomeRoute(lobby, gameService);
    }

    /**
     * Tests PostHomeRoute in the event where one player is null
     */
    @Test
    public void testOneNullPlayer(){
        testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        when(request.queryParams(PostHomeRoute.AWAITING_PLAYER)).thenReturn(null);
        when(request.queryParams(PostHomeRoute.STARTING_PLAYER)).thenReturn(STARTING);

        when(lobby.getPlayer(STARTING)).thenReturn(starting);
        when(lobby.getPlayer(WAITING)).thenReturn(waiting);

        //when(gameService.gameHasPlayer(WAITING)).thenReturn(true);

        CuT.handle(request, response);
        verify(session).attribute(PostHomeRoute.DID_SIGN_OUT_OCCUR, true);


    }

    /**
     * Tests PostHomeRoute in the event where other player is null
     */
    @Test
    public void testOtherNullPlayer(){
        testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        when(request.queryParams(PostHomeRoute.AWAITING_PLAYER)).thenReturn(WAITING);
        when(request.queryParams(PostHomeRoute.STARTING_PLAYER)).thenReturn(null);

        when(lobby.getPlayer(STARTING)).thenReturn(starting);
        when(lobby.getPlayer(WAITING)).thenReturn(waiting);

        //when(gameService.gameHasPlayer(WAITING)).thenReturn(true);

        CuT.handle(request, response);
        verify(session).attribute(PostHomeRoute.DID_SIGN_OUT_OCCUR, true);


    }

    /**
     * Tests PostHomeRoute in the event that both players are valid players.
     */
    @Test
    public void testNonNullPlayers(){
        testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        when(request.queryParams(PostHomeRoute.AWAITING_PLAYER)).thenReturn(WAITING);
        when(request.queryParams(PostHomeRoute.STARTING_PLAYER)).thenReturn(STARTING);

        when(lobby.getPlayer(STARTING)).thenReturn(starting);
        when(lobby.getPlayer(WAITING)).thenReturn(waiting);

        //when(gameService.gameHasPlayer(WAITING)).thenReturn(false);


        CuT.handle(request, response);
        verify(session).attribute(PostHomeRoute.DID_ERROR_OCCUR, false);
        assertTrue(gameService.gameHasPlayer(starting));


    }
}
