package codeu.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class LogoutServletTest {

  private LogoutServlet logoutServlet;
  private HttpServletRequest mockRequest;
  private HttpServletResponse mockResponse;
  private HttpSession mockSession;

  @Before
  public void setup() {
    logoutServlet = new LogoutServlet();

    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockSession = Mockito.mock(HttpSession.class);

    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);
  }

  @Test
  public void testDoPost() throws IOException, ServletException {
    
    logoutServlet.doPost(mockRequest, mockResponse);
    
    Mockito.verify(mockSession).setAttribute("user",null);
    Mockito.verify(mockResponse).sendRedirect("/");
  }
}
