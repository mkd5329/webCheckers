package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.CheckersGameService;
import com.webcheckers.model.*;
import com.webcheckers.util.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;

import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@Tag("UI-tier")
public class PostSubmitTurnRouteTest {

    public PostSubmitTurnRoute CuT;
    public PostSubmitTurnRoute CuT2;

    //Friendlies
    private Gson gson;
    private CheckersGameService gameService;
    private CheckersGameService gameService2;

    Player p1 = new Player("p1");
    Player p2 = new Player("p2");

    CheckersGame testGame = new CheckersGame(p1,p2);

    //mocks
    private Request request = mock(Request.class);
    private Response response = mock(Response.class);
    private CheckersGame game;


    @BeforeEach
    public void setup()
    {
        gson = new Gson();
        gameService = mock(CheckersGameService.class);
        gameService2 = mock(CheckersGameService.class);

        CuT2 = new PostSubmitTurnRoute(gson, gameService);
        CuT = new PostSubmitTurnRoute(gson,gameService);

        Player player = mock(Player.class);
        Session session = mock(Session.class);
        when(request.session()).thenReturn(session);
        when(session.attribute("player")).thenReturn(player);

        game = mock(CheckersGame.class);

        when(gameService.getGameByPlayer(player)).thenReturn(game);
        when(gameService2.getGameByPlayer(player)).thenReturn(testGame);


    }

    @Test
    public void testValidTurnSubmit()
    {


        Stack<Move> moveStack = new Stack();

        Position start = new Position(5,0);
        Position end = new Position(4,1);
        Move move = new Move(start,end);
        moveStack.push(move);

        when(game.getTurnMoves()).thenReturn(moveStack);
        //when(TurnUtil.isTurnValid(any(Stack.class),any(CheckersGame.class))).thenReturn(true);

        Player p1 = new Player("p1");
        Player p2 = new Player("p2");

        CheckersGame testGame = new CheckersGame(p1,p2);

        Position dastart = new Position(5,0);
        Position daend = new Position(4,1);
        Move damove = new Move(start,end);
        testGame.addMove(damove);

        Session session = mock(Session.class);
        when(request.session()).thenReturn(session);
        when(session.attribute("player")).thenReturn(p1);
        when(gameService.getGameByPlayer(any(Player.class))).thenReturn(testGame);


        Object result = null;
        try {
            result = CuT2.handle(request, response);
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }

        //This causes a null pointer exception due to TurnUtil. Testing this properly would require TurnUtil to be 100%
        // valid, which is hard because we cannot mock static methods.
        assertNotNull(result);

        Message message = gson.fromJson((String)result, Message.class);
        assertEquals(message.getType(), Message.Type.INFO);
        assertEquals(message.getText(), "Turn submitted.");



    }

    @Test
    public void testInvalidTurnSubmit()
    {
        when(request.session().attribute("player")).thenReturn(p1);
        CheckersGameService checkersGameService = new CheckersGameService();
        checkersGameService.startGame(p1, p2);

        CheckersGame gamer = checkersGameService.getGameByPlayer(p1);
        Position start = new Position(6,1);
        Position end = new Position(5,2);
        Move move = new Move(start,end);
        Move move2 = new Move(start,end);
        gamer.addMove(move);
        gamer.addMove(move2);

        PostSubmitTurnRoute CuT2 = new PostSubmitTurnRoute(gson, checkersGameService);

        Object result = null;
        try {
            result = CuT2.handle(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertNotNull(result);

        Message message = gson.fromJson((String)result, Message.class);
        assertEquals(message.getType(), Message.Type.ERROR);
        assertEquals(message.getText(), CuT2.INVALID_ERROR_MESSAGE);
    }

}
