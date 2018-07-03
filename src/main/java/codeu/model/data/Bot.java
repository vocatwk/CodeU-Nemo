package codeu.model.data;

import java.util.UUID;

/** Class representing a Bot. */
public class Bot {
  private final UUID id;
  /**
  * Constructs a new Bot.
  */
  public Bot() {
    id = UUID.fromString("0000000-0000-0000-0000-000000000000");
  }

  /*
  * Scans a message to identify keywords.
  */
  public String parseMessage(String messageContent) {
    String keyword;
    // TODO find keyword

    // If and only if no keyword was found.
    keyword = "N/A";

    return getMessageContent(keyword);
  }

  /*
  * Creates a Message object based on the keyword.
  */
  private String getMessageContent(String keyword) {
    if (!keyword.equals("N/A")) {
      // TODO get appropriate response
    }

    return "I'm sorry. I didn't understand that.";
  }

  /** Returns the ID of this Bot. */
  public UUID getId() {
    return id;
  }
}