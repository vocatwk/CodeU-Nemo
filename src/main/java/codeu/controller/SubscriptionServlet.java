import codeu.model.data.User;
import codeu.model.store.basic.UserStore;



public class SubscriptionServlet extends HttpServlet{
  private UserStore userStore;

  @Override
  public void init() throws ServletException {
    super.init();
    setUserStore(UserStore.getInstance());
  }
  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
        String username = (String)request.getSession().getAttribute("user");
        User user = userStore.getUser(username);
        

        request.setAttribute("subscriptions",subscriptions);

        request.getRequestDispatcher("/WEB-INF/view/subscriptions.jsp").forward(request, response);
      }

}
