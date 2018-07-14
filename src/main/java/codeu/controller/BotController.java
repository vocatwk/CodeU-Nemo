package codeu.controller;

import codeu.model.data.Bot;
import java.util.Map;
import java.util.HashMap;

/**
 * Controller class is responsible for controlling all Bots. 
 * It's a singleton so all servlet classes can access the same instance.
 */
public class BotController {

  /** Singleton instance of BotController. */
  private static BotController instance;

  /** Map that stores a mention key to its Bot. */
  private Map<String, Bot> botMap;

  private BotController() {
    botMap = new HashMap<String, Bot>();
  }

  /** Returns the singleton instance of BotController that should be shared between all servlet classes. */
  public static BotController getInstance() {
    if (instance == null) {
      instance = new BotController();
    }
    return instance;
  }

  /** Instance getter function used for testing. */
  public static BotController getTestInstance() {
    return new BotController();
  }

  /** Adds an entry to the map. */
  public void registerBot(String mentionKey, Bot bot) {
    botMap.put(mentionKey, bot);
  }

  /** 
  * Access the Bot with the given String.
  * 
  * @return null if the given String does not match any existing Bot. 
  */
  public Bot getBot(String mentionKey) {
    return botMap.get(mentionKey);
  }

  /** 
  * Sets the Bots to be used. 
  * This should only be called once and is ideally only used for testing. 
  */
  public void setBots(Map<String, Bot> botMap) {
    this.botMap = botMap;
  }
}