package codeu.model.data;

/** Interface for Bot implementations. */
public interface Bot {

  /** Returns the Bot's name. */
  public String getName();

  /** Creates a reply based on the keyword. */
  public String answerMessage(String message);
  
}