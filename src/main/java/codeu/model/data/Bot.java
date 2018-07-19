package codeu.model.data;

import java.util.UUID;

/** Interface for Bot implementations. */
public interface Bot {

  /** Returns the Bot's name. */
  public String getName();

  /** Returns "@" + Bot's name. */
  public String getMentionKey();

  /** Returns the Bot's id. */
  public UUID getId();

  /** Creates a reply based on the keyword. */
  public String answerMessage(String message);
  
}