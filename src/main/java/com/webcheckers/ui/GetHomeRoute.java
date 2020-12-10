package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import com.webcheckers.application.CheckersGameService;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import spark.*;

import com.webcheckers.util.Message;

import static com.webcheckers.ui.GetGameRoute.PLAYER;
import static com.webcheckers.ui.PostHomeRoute.DID_ERROR_OCCUR;
import static com.webcheckers.ui.PostHomeRoute.DID_SIGN_OUT_OCCUR;

/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 * @author <a href='mailto:zah1276@rit.edu'>Zeb Hollinger</a>
 * @author <a href='mailto:mjd6839@rit.edu'>Matthew DellaNeve</a>
 */
public class GetHomeRoute implements Route {
  private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

  static final Message WELCOME_MSG = Message.info("Welcome to the world of online Checkers.");
  static final Message ERROR_MSG = Message.error("You picked a player who was already in a game. Please try again.");
  static final Message SIGN_OUT_MSG = Message.error("You picked a player who has signed out. Please try again.");

  private static final String TITLE = "Welcome!";

  private final TemplateEngine templateEngine;
  private final PlayerLobby playerLobby;
  private final CheckersGameService checkersGameService;

  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
   *
   * @param templateEngine the HTML template rendering engine
   */
  public GetHomeRoute(final PlayerLobby playerLobby,
                      final TemplateEngine templateEngine,
                      final CheckersGameService checkersGameService) {
    this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobby is required");
    this.checkersGameService = Objects.requireNonNull(checkersGameService, "checkersGameService is required");
    //
    LOG.config("GetHomeRoute is initialized.");
  }

  /**
   * Render the WebCheckers Home page.
   *
   * @param request  the HTTP request
   * @param response the HTTP response
   * @return the rendered HTML for the Home page
   */
  @Override
  public Object handle(Request request, Response response) {
    LOG.finer("GetHomeRoute is invoked.");
    //
    final Session session = request.session();
    Map<String, Object> vm = new HashMap<>();
    vm.put("title", TITLE);

    // display a user message in the Home page

    boolean didErrorOccur;
    if (request.session().attribute(DID_ERROR_OCCUR) != null) {
      didErrorOccur = request.session().attribute(DID_ERROR_OCCUR);
    } else {
      didErrorOccur = false;
    }

    boolean didSignOutOccur;
    if (request.session().attribute(DID_SIGN_OUT_OCCUR) != null) {
      didSignOutOccur = request.session().attribute(DID_SIGN_OUT_OCCUR);
    } else {
      didSignOutOccur = false;
    }

    //displays either the info message or the error message depending on if the user got an error or not
    if (didErrorOccur) {
      vm.put("message", ERROR_MSG);
    } else if (didSignOutOccur) {
      vm.put("message", SIGN_OUT_MSG);
    } else {
      vm.put("message", WELCOME_MSG);
    }
    request.session().attribute(DID_SIGN_OUT_OCCUR, false);
    request.session().attribute(DID_ERROR_OCCUR, false);

    //show a non-signed-in player the number of people waiting to play
    //a game of checkers in the PlayerLobby
    vm.put("numberOfPlayers", playerLobby.getNumberOfPlayers());

    Player player = session.attribute(PLAYER);
    if (player != null) {
      // If player has been selected, redirect them to the game page
      if (checkersGameService.gameHasPlayer(player)) {
        CheckersGame game = checkersGameService.getGameByPlayer(player);
        if (game.isOver()) {
          game.removePlayer(player);
          checkersGameService.checkEmpty(game);
        } else {
          response.redirect(WebServer.GAME_URL);
        }
      }
      vm.put("currentUser", player);
      vm.put("players", playerLobby.getPlayerList(player.getName()));
    }

    // render the View
    return templateEngine.render(new ModelAndView(vm, "home.ftl"));
  }
}
