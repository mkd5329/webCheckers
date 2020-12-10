package com.webcheckers.ui;

import com.webcheckers.application.CheckersGameService;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.Player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Testing class for the GetHomeRoute class.
 *
 * @author Dade Wood
 */
@Tag("UI-tier")
public class GetHomeRouteTest {

    private GetHomeRoute CuT;

    // Mock
    private TemplateEngine templateEngine;
    private PlayerLobby playerLobby;
    private CheckersGameService gameService;
    private Request request;
    private Response response;
    private Session session;

    private TemplateEngineTester tester;

    // Attribute
    private static final String TITLE = "Welcome!";

    /**
     * Setup method that mocks injected classes plus the request and response,
     * and then creates a new GetHomeRoute component
     */
    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        response = mock(Response.class);
        session = mock(Session.class);
        gameService = mock(CheckersGameService.class);

        templateEngine = mock(TemplateEngine.class);

        when(request.session()).thenReturn(session);
        playerLobby = mock(PlayerLobby.class);
        when(playerLobby.getPlayer(null)).thenReturn(null);

        CuT = new GetHomeRoute(playerLobby, templateEngine, gameService);
    }

    /**
     * Test that the appropriate error message is assigned when the didErrorOccur
     * boolean is not null.
     */
    @Test
    public void test_error_occurred(){
        tester = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(tester.makeAnswer());
        when(session.attribute("didErrorOccur")).thenReturn(true);

        CuT.handle(request, response);

        tester.assertViewModelAttribute("message", GetHomeRoute.ERROR_MSG);
    }

    /**
     * Test that the handle method successfully renders the home.ftl when there
     * are no errors.
     */
    @Test
    public void test_render_home(){
        tester = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(tester.makeAnswer());

        Player user = new Player("currentUser");
        when(session.attribute("player")).thenReturn(user);
        when(session.attribute("didErrorOccur")).thenReturn(null);

        CuT.handle(request, response);

        tester.assertViewModelExists();
        tester.assertViewModelIsaMap();

        tester.assertViewModelAttribute("title", TITLE);
        tester.assertViewModelAttribute("message", GetHomeRoute.WELCOME_MSG);
        tester.assertViewModelAttribute("numberOfPlayers", playerLobby.getNumberOfPlayers());
        tester.assertViewModelAttribute("currentUser", user);
        tester.assertViewModelAttribute("players", playerLobby.getPlayerList(user.getName()));

        tester.assertViewName("home.ftl");
    }
}
