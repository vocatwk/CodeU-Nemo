package codeu.controller;

import codeu.model.data.Bot;
import codeu.model.data.NemoBot;
import codeu.model.data.ConversationStatBot;
import codeu.model.data.User;
import codeu.model.store.basic.UserStore;
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

  /** Store class that gives access to Users. */
  private UserStore userStore;

  private BotController() {
    botMap = new HashMap<String, Bot>();
    setUserStore(UserStore.getInstance());
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

  /**
   * Sets the UserStore used by this controller. This function provides a common setup method for use
   * by the test framework or the controller's BotController() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  /** Adds an entry to the map and to the UserStore. */
  public void registerBot(Bot bot) {
    // Check if the bot is already in the UserStore
    User userStoreBot = userStore.getUser(bot.getName());

    // First time adding to the UserStore
    if (userStoreBot == null) {
      botMap.put(bot.getMentionKey().toLowerCase(), bot);
      userStore.addUser((User)bot);
    }
    // Don't add to the UserStore again.
    // Instead, build the appropriate bot with the correspoding User information.
    else {
      String userStoreBotName = userStoreBot.getName();
      Bot userBot = null;

      if (userStoreBotName.equals("NemoBot")) {
        userBot = new NemoBot(userStoreBot);
      }
      else if (userStoreBotName.equals("ConversationStatBot")) {
        userBot = new ConversationStatBot(userStoreBot);
      }
      
      // Only add to map if userBot isn't null
      if (userBot != null) {
        botMap.put(userBot.getMentionKey().toLowerCase(), userBot);
      }
    }
  }

  /** 
  * Access the Bot with the given String, ignoring case.
  * 
  * @return null if the given String does not match any existing Bot. 
  */
  public Bot getBot(String mentionKey) {
    return botMap.get(mentionKey.toLowerCase());
  }

  /** 
  * Sets the Bots to be used and adds them to the UserStore. 
  * This should only be called once and is ideally only used for testing. 
  */
  public void setBots(Map<String, Bot> botMap) {
    for (String key : botMap.keySet()) {
      this.botMap.put(key.toLowerCase(), botMap.get(key));
    }

    for (Bot bot : botMap.values()) {
      userStore.addUser((User)bot);
    }
  }
}