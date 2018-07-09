package codeu.model.data;

/** Interface for Bot implementations. */
public interface Bot {

  /** Creates a reply based on the keyword.*/
  public String answerMessage(String message);
  
}