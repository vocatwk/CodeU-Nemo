package codeu.model.data;

import java.time.Instant;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;
import org.mindrot.jbcrypt.BCrypt;

/** Class representing a statistics Bot for conversations. */
public class ConversationStatBot extends User implements Bot {

  /** "@" + Bot's name. */
  private String mentionKey;

  /** Map that stores a keyword(lower case) to its answer. */
  private Map<String, String> answerMap;

  /**
  * Constructs a new ConversationStatBot.
  */
  public ConversationStatBot() {
    super(
        UUID.randomUUID(), 
        "ConversationStatBot", 
        BCrypt.hashpw("ConversationStatBotPassword2018", BCrypt.gensalt()), 
        Instant.now());
    setAboutMe("I'm ConversationStatBot. Want to talk to me? Simply @ mention me in any conversation!");
    setIsAdmin(true);
    setLastSeenNotifications(null);
    mentionKey = "@ConversationStatBot";
    initializeMap();
  }

  /**
  * Initializes a map. 
  * 
  * Note that ConversationStatBot ignores case only on keys so all keys are in lower case.
  */
  private void initializeMap() {
    answerMap = new HashMap<String, String>();
    // TODO
  }

  /** Returns "@" + Bot's name. */
  public String getMentionKey() {
    return mentionKey;
  }

  /** Creates a reply based on the keyword. */
  public String answerMessage(String message) {
    String keyword = getKeyword(message);

    if (keyword == null) {
      return "Stats loading...";
    }

    return answerMap.get(keyword);
  }

  /** 
  * Scans a message to identify keywords in the map. Ignores case.
  *
  * @return null if no word is registered.
  */
  private String getKeyword(String message) {
    for (String word : message.split("[[\\p{Punct}&&[^@]]\\s]+")) {
      word = word.toLowerCase();
      if (answerMap.get(word) != null) {
        return word;
      }
    }

    return null;
  }

}