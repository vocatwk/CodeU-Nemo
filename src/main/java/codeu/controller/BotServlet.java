package codeu.controller;

import codeu.model.data.Bot;
import codeu.controller.BotController;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet class responsible for the bots page. */
public class BotServlet extends HttpServlet {

  /** Controller class that gives control to Bots. */
  private BotController botController;

  /** Set up state for handling bots page requests. */
  @Override
  public void init() throws ServletException {
    super.init();
    setBotController(BotController.getInstance());
  }

  /**
   * Sets the BotController used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setBotController(BotController botController) {
    this.botController = botController;
  }

  /**
   * This function fires when a user navigates to the bots page. 
   * It simply forwards to bots.jsp for rendering.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException, ServletException {
    List<Bot> bots = botController.getAllBots();
    request.setAttribute("bots", bots);

    request.getRequestDispatcher("/WEB-INF/view/bots.jsp").forward(request, response);
  }
}