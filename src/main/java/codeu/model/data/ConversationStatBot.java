package codeu.model.data;

import codeu.model.data.Conversation;
import codeu.model.store.basic.UserStore;
import codeu.model.store.basic.MessageStore;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
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
        UUID.nameUUIDFromBytes("ConversationStatBot".getBytes()), 
        "ConversationStatBot", 
        BCrypt.hashpw(
            "GoogleCodeUTeamNemoConversationStatBotPassword2018", 
            BCrypt.gensalt()), 
        Instant.now());
    setAboutMe(
        "I'm ConversationStatBot. Want to talk to me? Simply @ mention me in any conversation!"
            + " Feel free to ask me about the following for any conversation: "
            + "The owner, when it was created, the privacy status, and the amount of users and messages.");
    setIsAdmin(true);
    setLastSeenNotifications(null);
    mentionKey = "@ConversationStatBot";
    initializeMap();
  }

  /**
  * Initializes a map based on the given file path. 
  * 
  * The file should be formatted in the following way:
  * key \\t value
  * (Empty Line)
  * key \\t value
  * 
  * Note that ConversationStatBot ignores case only on keys so all keys are in lower case.
  */
  private void initializeMap() {
    answerMap = new HashMap<String, String>();
    
    try {
      InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("ConversationStatBot.txt");
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

  /** Returns "@" + Bot's name. */
  public String getMentionKey() {
    return mentionKey;
  }

  /** Creates a reply based on the keyword. */
  public String answerMessage(String message, Conversation conversation) {
    String keyword = getKeyword(message);

    if (keyword == null) {
      return "I'm sorry. I didn't understand that. Send a message with the word \"Help\".";
    }

    String botAnswer = answerMap.get(keyword);

    if (keyword.equalsIgnoreCase("owner")) {
      return String.format(botAnswer, getOwner(conversation.getOwnerId()));
    }
    else if (keyword.equalsIgnoreCase("created")) {
      return String.format(botAnswer, getCreation(conversation.getCreationTime()));
    }
    else if (keyword.equalsIgnoreCase("privacy")) {
      return String.format(botAnswer, getPrivacy(conversation.isPrivate()));
    }
    else if (keyword.equalsIgnoreCase("users")) {
      int numberOfUsers = getNumberOfUsers(conversation);
      return String.format(botAnswer, numberOfUsers) 
          + (numberOfUsers > 1 ? "s." : ".");
    }
    else if (keyword.equalsIgnoreCase("messages")) {
      int numberOfMessages = getNumberOfMessages(conversation);
      return String.format(botAnswer, numberOfMessages) 
          + (numberOfMessages > 1 ? "s." : ".");
    }

    return answerMap.get(keyword);
  }

  /** 
  * Scans a message to identify keywords in the map. Ignores case.
  *
  * @return null if no word is registered.
  */
  public String getKeyword(String message) {
    for (String word : message.split("[[\\p{Punct}&&[^@]]\\s]+")) {
      word = word.toLowerCase();
      if (answerMap.get(word) != null) {
        return word;
      }
    }

    return null;
  }

  /** Returns the owner of the conversation. */
  private String getOwner(UUID conversationOwnerId) {
    return UserStore.getInstance().getUser(conversationOwnerId).getName();
  }

  /** Returns the creation time of the conversation as a Date. */
  private Date getCreation(Instant conversationCreationTime) {
    return Date.from(conversationCreationTime);
  }

  /** Returns the privacy status of the conversation. */
  private String getPrivacy(Boolean conversationPrivacyStatus) {
    if (conversationPrivacyStatus == true) {
      return "private";
    }

    return "public";
  }

  /** Returns the number of users of the conversation. */
  private int getNumberOfUsers(Conversation conversation) {
    return conversation.getMembers().size();
  }

  /** Returns the number of messages of the conversation. */
  private int getNumberOfMessages(Conversation conversation) {
    return MessageStore.getInstance().getMessagesInConversation(conversation.getId()).size();
  }
}