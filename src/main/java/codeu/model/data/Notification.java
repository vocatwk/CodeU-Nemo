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

public class Notification{
  private final List<String> notification;
  private User receiver;
  private User sender;
  private UUID id;
  private Object thingTheNotifcationIsAbout;
  private List<Object> notification;

  public Notification(UUID id, User receiver, User sender, Object thingTheNotifcationIsAbout){
      this.receiver = receiver;
      this.sender = sender;
      this.id = id;
      this.thingTheNotifcationIsAbout = thingTheNotifcationIsAbout;

  }

  public User getReceiver(){
    return receiver;
  }

  public User getSender(){
    return sender;
  }

  public List<Object> getNotification(){
    notification.add(receiver);
    notification.add(sender);
    notification.add(id);
    notification.add(thingTheNotifcationIsAbout);
  }

}
