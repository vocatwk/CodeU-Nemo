package codeu.controller;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/** Servlet class responsible for the Admin page. */
public class AdminServlet extends HttpServlet {

  private UserStore userStore;

  public void init() throws ServletException {
    super.init();
    setUserStore(UserStore.getInstance());
  }

  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
      request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);

    User user = userStore.getUser(username);
      String[] admin = new String[]{"jay","memo","ver"};
      for (int i = 0; i < admin.length; i++){
        if(user != admin[i]){
          response.sendRedirect("/profile");
          return;
        }

      }

}


}
