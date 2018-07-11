package codeu.model.data;

import codeu.model.store.basic.UserStore;
import java.time.Instant;
import java.util.UUID;
import org.mindrot.jbcrypt.BCrypt;

/** Class representing a help Bot. */
public class NemoBot extends User implements Bot {

  private String mentionName;
  
  /**
  * Constructs a new Bot.
  */
  public NemoBot() {
    super(
        UUID.randomUUID(), 
        "NemoBot", 
        BCrypt.hashpw("NemoBotPassword2018", BCrypt.gensalt()), 
        Instant.now());
    setAboutMe("I'm NemoBot. Want to talk to me? Simply @ mention me in any conversation!");
    setIsAdmin(true);
    setLastSeenNotifications(null);
    UserStore.getInstance().addUser(this);
    mentionName = "NemoBot";
  }

  /** Scans a message to identify keywords.*/
  private String getKeyword(String message) {
    String keyword;
    // TODO find keyword

    // If and only if no keyword was found.
    keyword = "N/A";

    return keyword;
  }

  /** Creates a reply based on the keyword.*/
  public String answerMessage(String message) {
    String keyword = getKeyword(message);

    if (!keyword.equals("N/A")) {
      // TODO get appropriate response
    }

    return "I'm sorry. I didn't understand that.";
  }
}