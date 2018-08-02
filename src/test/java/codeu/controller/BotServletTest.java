package codeu.controller;

import codeu.model.data.Bot;
import codeu.model.data.NemoBot;
import codeu.model.data.ConversationStatBot;
import codeu.controller.BotController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class BotServletTest {

  private BotServlet botServlet;
  private HttpServletRequest mockRequest;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;
  private BotController mockBotController;

  @Before
  public void setUp() {
    botServlet = new BotServlet();

    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/bots.jsp"))
        .thenReturn(mockRequestDispatcher);

    mockBotController = Mockito.mock(BotController.class);
    botServlet.setBotController(mockBotController);
  }

  @Test
  public void testDoGet() throws IOException, ServletException {
    List<Bot> fakeBotList = new ArrayList<Bot>();

    NemoBot nemoBot = new NemoBot();
    fakeBotList.add(nemoBot);

    ConversationStatBot conversationStatBot= new ConversationStatBot();
    fakeBotList.add(conversationStatBot);

    Mockito.when(mockBotController.getAllBots()).thenReturn(fakeBotList);

    botServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockRequest).setAttribute("bots", fakeBotList);
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }
}