ackage codeu.model.data;

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

  private UUID receiver;
  private UUID sender;
  private UUID id;
  private Event theNotification;
  private boolean seenNotification;
  private Instant timeCreated;

  public Notification(UUID id, UUID receiver, UUID sender, Event theNotification, Instant timeCreated){
      this.receiver = receiver;
      this.sender = sender;
      this.id = id;
      this.theNotification = theNotification;
      this.timeCreated = timeCreated;
      seenNotification = false;

  }

  public UUID getReceiver(){
    return receiver;
  }

  public UUID getSender(){
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

  public Event getTheNotification(){
    return theNotification;
  }

  public Instant getTimeCreated(){
    return timeCreated;
  }


}
