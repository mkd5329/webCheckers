package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.CheckersGameService;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Move;
import com.webcheckers.model.Player;
import com.webcheckers.model.Position;
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
public class PostBackupMoveRouteTest {

    //mocks
    private Request request;
    private Response response;
    private CheckersGameService gameService;
    private CheckersGame game;

    //friendlies
    private Gson gson;

    //CuT
    public PostBackupMoveRoute CuT;

    @BeforeEach
    public void setup()
    {
        request = mock(Request.class);
        response = mock(Response.class);
        gameService = mock(CheckersGameService.class);
        gson = new Gson();


        CuT = new PostBackupMoveRoute(gson, gameService);


        Player player = mock(Player.class);
        Session session = mock(Session.class);
        when(request.session()).thenReturn(session);
        when(session.attribute("player")).thenReturn(player);

        game = mock(CheckersGame.class);

        when(gameService.getGameByPlayer(player)).thenReturn(game);
    }

    @Test
    public void testEmptyBackup()
    {
        Stack<Move> moveStack = new Stack();

        when(game.getTurnMoves()).thenReturn(moveStack);
        Object result = null;
        try {
            result = CuT.handle(request,response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertNotNull(result);

        Message message = gson.fromJson((String)result, Message.class);
        assertEquals(message.getType(), Message.Type.INFO);
        assertEquals(message.getText(), "Backup submitted.");

    }

    @Test
    public void testNotEmptyBackup()
    {
        Stack<Move> moveStack = new Stack();
        Position start = new Position(1,1);
        Position end = new Position(2,2);
        Move move = new Move(start, end);
        moveStack.push(move);

        when(game.getTurnMoves()).thenReturn(moveStack);
        Object result = null;
        try {
            result = CuT.handle(request,response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertNotNull(result);

        Message message = gson.fromJson((String)result, Message.class);
        assertEquals(message.getType(), Message.Type.INFO);
        assertEquals(message.getText(), "Backup submitted.");

    }

}
