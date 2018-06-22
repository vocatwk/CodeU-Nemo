package codeu.model.data;

import codeu.model.data.User;
import codeu.model.data.Conversation;
import codeu.model.data.Message;
import java.time.Instant;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import codeu.model.data.Event;
import codeu.model.store.basic.EventStore;
import java.time.Instant;


public class Notification{

  private User receiver;
  private User sender;
  private UUID id;
  private Event theNotification;
  private boolean seenNotification;
  private Instant lastSeen;

  public Notification(UUID id, User receiver, User sender, Event theNotification){
      this.receiver = receiver;
      this.sender = sender;
      this.id = id;
      this.theNotification = theNotification;
      seenNotification = false;
  }

  public User getReceiver(){
    return receiver;
  }

  public User getSender(){
    return sender;
  }

  public UUID getId(){
    return id;
  }

 public void setSeenNotification(boolean seenNotification){
   this.seenNotification = seenNotification;
 }

  public boolean getSeenNotification(){
    return seenNotification;
  }

  public void setTimeSeen(Instant lastSeen){
    this.lastSeen = lastSeen;
  }

  public Instant getTimeSeen(){
    return lastSeen;
  }


}
