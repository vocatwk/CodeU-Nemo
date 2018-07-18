package codeu.model.data;

import java.time.Instant;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

/**
 * Class representing an event, which includes Users joining, Conversations 
 * being created, and Messages being sent. 
 */
public class Event {

  private final UUID id;
  private final String type;
  private final Instant creation;
  /** 
   * information stores the following as necessary:
   * 1. Username
   * 2. Updated "About Me"/Conversation Title based on type
   * 3. Message Content
   * 4. Conversation Id
   * NOTE: At minimum, must store Username
   */
  private final List<String> information;
  
  /**
   * Constructs a new Event
   * 
   * @param id the ID of this Event
   * @param type the type of this Event
   * @param creation the creation time of this Event
   * @param information the information of this Event 
   */
  public Event(UUID id, String type, Instant creation, List<String> information) {
    this.id = id;
    this.type = type;
    this.creation = creation;
    this.information = information;
  }

  /** Returns the ID of this User. */
  public UUID getId() {
    return id;
  }

  /** Returns the type of this Event. */
  public String getType() {
    return type;
  }

  /** Returns the creation time of this Event. */
  public Instant getCreationTime() {
    return creation;
  }

  /** Returns the information of this Event. */
  public List<String> getInformation() {
    return information;
  }
}