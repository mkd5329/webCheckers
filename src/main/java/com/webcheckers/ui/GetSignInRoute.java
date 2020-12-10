package com.webcheckers.ui;

import com.webcheckers.model.Player;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import static com.webcheckers.ui.GetGameRoute.PLAYER;
import static spark.Spark.halt;

/**
 * The UI Controller to GET the Sign In page.
 *
 * @author <a href='mailto:mjd6839@rit.edu'>Matthew DellaNeve</a>
 */
public class GetSignInRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetSignInRoute.class.getName());
    private final TemplateEngine templateEngine;

    public GetSignInRoute(final TemplateEngine templateEngine) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        LOG.config("GetSignInRoute is initialized.");
    }

    /**
     * Render the WebCheckers Sign In page.
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the rendered HTML for the Sign In page
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("GetSignInRoute is invoked.");

        Player player = request.session().attribute(PLAYER);

        // Player should not be able to access sign-in page if they are already signed in
        if (player == null) {
            Map<String, Object> vm = new HashMap<>();
            return templateEngine.render(new ModelAndView(vm, "signin.ftl"));
        } else {
            response.redirect(WebServer.HOME_URL);
            halt();
            return null;
        }
    }
}
