package com.webcheckers.ui;

import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Testing class for the GetSignInRoute component
 *
 * @author <a href='mailto:zah1276@rit.edu'>Zeb Hollinger</a>
 */
@Tag("UI-tier")
class GetSignInRouteTest {
    //component under testing
    private GetSignInRoute CuT;

    //mocks
    private TemplateEngine engine;
    private Request request;
    private Response response;
    private Session session;


    @BeforeEach
    public void setup(){
        engine = mock(TemplateEngine.class);
        request = mock(Request.class);
        response = mock(Response.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        CuT = new GetSignInRoute(engine);
    }


    /**
     * Tests to see if the route correctly handles when
     * a new player to the game tries to sign in
     */
    @Test
    public void newPlayer() {
        Player player = null;
        when(session.attribute("player")).thenReturn(player);

        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        //Start the test
        CuT.handle(request, response);

        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewName("signin.ftl");

    }

    /**
     * Tests to see if the route correctly handles a player who
     * is already signed in and tries to sign in again
     */
    @Test
    public void signedInPlayer() {
        Player player = new Player("test");
        when(session.attribute("player")).thenReturn(player);

        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        //Start the test
        try{
            Object result = CuT.handle(request, response);
            assertNull(result);
        }catch (Exception e) {
            assertTrue(e instanceof HaltException);
        }
    }
}