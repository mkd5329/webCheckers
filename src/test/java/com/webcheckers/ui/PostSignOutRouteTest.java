package com.webcheckers.ui;

import com.webcheckers.application.CheckersGameService;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Testing class for the PostSignOutRoute component
 *
 * @author <a href='mailto:zah1276@rit.edu'>Zeb Hollinger</a>
 */
@Tag("UI-tier")
class PostSignOutRouteTest {
    //component under testing
    private PostSignOutRoute CuT;

    //mocks
    private TemplateEngine engine;
    private Request request;
    private Session session;
    private Response response;

    //frendlies
    private PlayerLobby playerLobby;
    private CheckersGameService gameService;


    @BeforeEach
    public void setup(){
        gameService = new CheckersGameService();
        playerLobby = new PlayerLobby(gameService);
        engine = mock(TemplateEngine.class);
        request = mock(Request.class);
        session = mock(Session.class);
        response = mock(Response.class);
        when(request.session()).thenReturn(session);
        CuT = new PostSignOutRoute(playerLobby, gameService);
    }

    /**
     * Tests the handle method with a player
     * who is currently signed in
     */
    @Test
    public void handle_signedIn() {
        Player test = new Player("test");
        when(request.session().attribute("player")).thenReturn(test);
        playerLobby.signIn("firstPlayer");
        playerLobby.signIn("test");
        assertEquals(2, playerLobby.getNumberOfPlayers());
        try{
            CuT.handle(request, response);
        }catch( Exception e ){
            assertTrue(e instanceof HaltException);
        }
        assertEquals(1, playerLobby.getNumberOfPlayers());
        assertNull(playerLobby.getPlayer("test"));
        assertEquals(new ArrayList<>(), playerLobby.getPlayerList("firstPlayer"));
    }
}
