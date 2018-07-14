package codeu.controller;

// import codeu.controller.BotController;
import codeu.model.data.Bot;
import codeu.model.data.NemoBot;
import java.util.Map;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.junit.Assert;

public class BotControllerTest {

  private BotController botController;

  private final NemoBot NEMO_BOT = new NemoBot();

  @Before
  public void setup() {
    botController = BotController.getTestInstance();

    Map<String, Bot> botMap = new HashMap<String, Bot>();
    botMap.put("@NEMO_BOT", NEMO_BOT);
    botController.setBots(botMap);
  }

  @Test
  public void testGetBot_found() {
    Bot resultBot = botController.getBot("@NEMO_BOT");

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

    botController.registerBot("@NemoBot", nemoBot);
    Bot resultBot = botController.getBot("@NemoBot");

    assertEquals(nemoBot, resultBot);

  }

  private void assertEquals(Bot expectedBot, Bot actualBot) {
    Assert.assertEquals(expectedBot.getName(), actualBot.getName());
    Assert.assertEquals(expectedBot.getId(), actualBot.getId());
  }

}