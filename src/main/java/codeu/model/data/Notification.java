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

public class Notification{
  private final List<String> Notification;
  private UserStore userStore;
  private ConversationStore conversationStore;
  private MessageStore messageStore;
  private User receiver;
  private User sender;

  public Notification(User receiver, Object notification, User sender ){
      this.receiver = receiver;
      this.sender = sender;

  }
  private void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }
  private void setConversationStore(ConversationStore conversationStore) {
    this.conversationStore = conversationStore;
  }
  private void setMessageStore(MessageStore messageStore){
    this.messageStore = messageStore;

  }

  public User getReceiver(){
    return receiver;
  }

  public User getSender(){
    return sender;
  }


}
