package codeu.model.data;

import codeu.model.data.User;
import codeu.model.data.Conversation;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;
import org.mindrot.jbcrypt.BCrypt;

/** Class representing a help Bot. */
public class NemoBot extends User implements Bot {

  /** "@" + Bot's name. */
  private String mentionKey;

  /** Map that stores a keyword(lower case) to its answer. */
  private Map<String, String> answerMap;
  
  /**
  * Constructs a new NemoBot.
  */
  public NemoBot() {
    super(
        UUID.nameUUIDFromBytes("NemoBot".getBytes()), 
        "NemoBot", 
        BCrypt.hashpw("NemoBotPassword2018", BCrypt.gensalt()), 
        Instant.now());
    setAboutMe("I'm NemoBot. Want to talk to me? Simply @ mention me in any conversation!");
    setIsAdmin(true);
    setLastSeenNotifications(null);
    mentionKey = "@NemoBot";
    initializeMap();
  }

  /** Returns "@" + Bot's name. */
  public String getMentionKey() {
    return mentionKey;
  }

  /**
  * Initializes a map based on the given file path. 
  * 
  * The file should be formatted in the following way:
  * key \\t value
  * (Empty Line)
  * key \\t value
  * 
  * Note that NemoBot ignores case only on keys so all keys are in lower case.
  */
  private void initializeMap() {
    answerMap = new HashMap<String, String>();

    try {
      InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("NemoBot.txt");
      InputStreamReader inStreamReader = new InputStreamReader(inStream);
      BufferedReader bufferedReader = new BufferedReader(inStreamReader);

      String line;

      while ((line = bufferedReader.readLine()) != null) {
        if (line.length() > 0) {
          String[] keyValue = line.split("\\t");
          answerMap.put(keyValue[0].toLowerCase(), keyValue[1]);
        }
      }

      bufferedReader.close();
    }
    catch (FileNotFoundException e) {
      System.out.println("File not found exception. Check the stack trace to see what's wrong.");
      e.printStackTrace();
    }
    catch (IOException e) {
      System.out.println("IO exception. Check the stack trace to see what's wrong.");
      e.printStackTrace();
    }
  }

  /** Creates a reply based on the keyword. */
  public String answerMessage(String message, Conversation conversation) {
    String keyword = getKeyword(message);

    if (keyword == null) {
      return "I'm sorry. I didn't understand that. Send a message with the word \"Help\".";
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