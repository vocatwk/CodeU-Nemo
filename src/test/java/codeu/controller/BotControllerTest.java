package codeu.controller;

import codeu.model.data.Bot;
import codeu.model.data.NemoBot;
import codeu.model.data.User;
import codeu.model.store.basic.UserStore;
import java.util.Map;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mockito;

public class BotControllerTest {

  private BotController botController;

  private final NemoBot NEMO_BOT = new NemoBot();

  private UserStore mockUserStore;

  @Before
  public void setup() {
    botController = BotController.getTestInstance();

    mockUserStore = Mockito.mock(UserStore.class);
    botController.setUserStore(mockUserStore);

    Map<String, Bot> botMap = new HashMap<String, Bot>();
    botMap.put("@NEMO_BOT", NEMO_BOT);
    botController.setBots(botMap);
  }

  @Test
  public void testGetBot_found_upperCase() {
    Bot resultBot = botController.getBot("@NEMO_BOT");

    assertEquals(NEMO_BOT, resultBot);
  }

  @Test
  public void testGetBot_found_lowerCase() {
    Bot resultBot = botController.getBot("@nemo_bot");

    assertEquals(NEMO_BOT, resultBot);
  }

  @Test
  public void testGetBot_notFound() {
    Bot resultBot = botController.getBot("@NotABot");

    Assert.assertNull(resultBot);
  }

  @Test
  public void testRegisterBot() {
    NemoBot nemoBot = new NemoBot();

    botController.registerBot(nemoBot);
    Bot resultBot = botController.getBot(nemoBot.getMentionKey());

    Mockito.verify(mockUserStore).addUser((User)nemoBot);

    assertEquals(nemoBot, resultBot);
  }

  @Test
  public void testRegisterBot_upperCase() {
    NemoBot nemoBot = new NemoBot();

    botController.registerBot(nemoBot);
    Bot resultBot = botController.getBot("@NEMOBOT");

    Mockito.verify(mockUserStore).addUser((User)nemoBot);

    assertEquals(nemoBot, resultBot);
  }

  @Test
  public void testRegisterBot_lowerCase() {
    NemoBot nemoBot = new NemoBot();

    botController.registerBot(nemoBot);
    Bot resultBot = botController.getBot("@nemobot");

    Mockito.verify(mockUserStore).addUser((User)nemoBot);

    assertEquals(nemoBot, resultBot);
  }

  private void assertEquals(Bot expectedBot, Bot actualBot) {
    Assert.assertEquals(expectedBot.getName(), actualBot.getName());
    Assert.assertEquals(expectedBot.getId(), actualBot.getId());
  }

}