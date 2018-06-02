package codeu.model.data;

import codeu.model.data.User;
import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.store.basic.UserStore;
import codeu.model.store.basic.ConversationStore;
import java.time.Instant;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

/**
 * Class representing an event, which includes Users joining, Conversations 
 * being created, and Messages being sent. 
 */
public class Event {

  private final String type;
  private final Instant creation;
  /** 
   * information stores the following as necessary:
   * 1. Username
   * 2. Conversation Title
   * 3. Message Content
   * NOTE: At minimum, must store Username
   */
  private final List<String> information;
  private UserStore userStore;
  private ConversationStore conversationStore;
  
  /**
   * Constructs a new Event
   * 
   * @param type the type of event
   * @param creation the time the event was created
   * @param classType the class of the event 
   */
  public Event(String type, Instant creation, Object classType) {
    this.type = type;
    this.creation = creation;
    information = new ArrayList<>();
    setUserStore(userStore.getInstance());
    setConversationStore(conversationStore.getInstance());

    if (type.equals("User")) {
      User user = (User)classType;
      information.add(user.getName());
    }
    else if (type.equals("Conversation")) {
      Conversation conversation = (Conversation)classType;
      information.add(userStore.getUser(conversation.getOwnerId()).getName());
      information.add(conversation.getTitle());
    }
    else if (type.equals("Message")) {
      Message message = (Message)classType;
      information.add(userStore.getUser(message.getAuthorId()).getName());
      information.add(conversationStore.getConversation(message.getConversationId()).getTitle());
      information.add(message.getContent());
    }
  }

  /**
   * Sets the UserStore used by this class. This function provides a common 
   * setup method for use by the constructor.
   */
  private void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  } 

  /**
   * Sets the ConversationStore used by this class. This function provides a 
   * common setup method for use by the constructor.
   */
  private void setConversationStore(ConversationStore conversationStore) {
    this.conversationStore = conversationStore;
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