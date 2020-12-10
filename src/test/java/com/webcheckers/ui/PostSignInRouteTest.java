package com.webcheckers.ui;


import com.webcheckers.application.PlayerLobby;
import static org.junit.jupiter.api.Assertions.*;

import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test Class for the PostSignInRoute Class.
 *
 * @author Michael Driscoll
 */
@Tag("UI-tier")
public class PostSignInRouteTest {

    //Component Under Test
    private PostSignInRoute CuT;

    //Mock objects
    private PlayerLobby playerLobby;
    private TemplateEngine templateEngine;
    private Request request;
    private Response response;
    private Session session;

    @BeforeEach
    public void setup()
    {
        playerLobby = mock(PlayerLobby.class);
        templateEngine = mock(TemplateEngine.class);
        request = mock(Request.class);
        response = mock(Response.class);
        session = mock(Session.class);

        CuT = new PostSignInRoute(playerLobby,templateEngine);

        when(request.session()).thenReturn(session);

    }


    /**
     * Makes sure that the route properly handles if the user inputs an invalid username
     */
    @Test
    public void testInvalidUsername() {
        when(request.queryParams(PostSignInRoute.USERNAME_PARAM)).thenReturn("");

        final TemplateEngineTester testHelper = new TemplateEngineTester();

        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        when(playerLobby.signIn(any(String.class))).thenReturn(null);


        CuT.handle(request, response);



        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewModelAttribute(PostSignInRoute.MESSAGE_ATTR,PostSignInRoute.ERROR_MESSAGE);
        testHelper.assertViewModelAttribute(PostSignInRoute.MESSAGE_TYPE_ATTR,PostSignInRoute.ERROR_TYPE);
        testHelper.assertViewName("signin.ftl");
        assertTrue(CuT.getWasInvalidUsername());



    }


    /**
     * Makes sure that the route properly redirects if the user inputs a valid username
     */
    @Test
    public void testValidUsername() {
        when(request.queryParams(PostSignInRoute.USERNAME_PARAM)).thenReturn("");

        final TemplateEngineTester testHelper = new TemplateEngineTester();

        when(playerLobby.signIn(any(String.class))).thenReturn(mock(Player.class));

        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());



        //the redirect will hause a halt exception, which is what we expect to happen
        try {
            assertNull(CuT.handle(request, response));
        }
        catch(HaltException error)
        {
            assertFalse(CuT.getWasInvalidUsername());

        }

    }


    /**
     * makes sure the StorePlayer method actually stores the player in the session attribute when they have a valid
     * username
     */
    @Test
    public void testStorePlayer()
    {
        when(request.queryParams(PostSignInRoute.USERNAME_PARAM)).thenReturn("ValidUser");

        final TemplateEngineTester testHelper = new TemplateEngineTester();

        Player ValidUser = new Player("ValidUser");

        when(playerLobby.signIn(any(String.class))).thenReturn(ValidUser);

        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());



        try {
            CuT.handle(request, response);
        }
        catch(HaltException error)
        {
            assertTrue(CuT.getDidStorePlayer());

        }

    }

    /**
     * This tests that in the event of an invalid username being inputted, the player should not be stored in the
     * session
     */
    @Test
    public void testDidntStorePlayer()
    {
        when(request.queryParams(PostSignInRoute.USERNAME_PARAM)).thenReturn("");

        final TemplateEngineTester testHelper = new TemplateEngineTester();

        when(playerLobby.signIn(any(String.class))).thenReturn(null);

        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        CuT.handle(request, response);

        assertFalse(CuT.getDidStorePlayer());

    }







}
