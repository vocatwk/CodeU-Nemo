package codeu.controller;

import codeu.model.data.User;
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import com.google.gson.Gson;

public class SearchServletTest {

  private SearchServlet searchServlet;
  private HttpServletRequest mockRequest;
  private HttpServletResponse mockResponse;
  private UserStore mockUserStore;

  @Before
  public void setup() {
    searchServlet = new SearchServlet();

    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockResponse = Mockito.mock(HttpServletResponse.class);
    
    mockUserStore = Mockito.mock(UserStore.class);
    searchServlet.setUserStore(mockUserStore);
  }

  @Test
  public void testDoGet() throws IOException, ServletException {
    List<User> fakeUserList = new ArrayList<User>();
    fakeUserList.add(new User(UUID.randomUUID(), "user", "password_hash", Instant.now()));
    Mockito.when(mockUserStore.getUsers("user")).thenReturn(fakeUserList);

    String json = new Gson().toJson(fakeUserList);

    Mockito.when(mockRequest.getParameter("searchRequest")).thenReturn("user");

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    Mockito.when(mockResponse.getWriter()).thenReturn(writer);

    searchServlet.doGet(mockRequest, mockResponse);

    Mockito.when(mockResponse.getContentType()).thenReturn("application/json");
    Mockito.when(mockResponse.getCharacterEncoding()).thenReturn("UTF-8");

    Assert.assertEquals("application/json", mockResponse.getContentType());
    Assert.assertEquals("UTF-8", mockResponse.getCharacterEncoding());
    Assert.assertEquals(json, stringWriter.toString());
  }
}