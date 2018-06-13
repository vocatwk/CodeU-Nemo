package codeu.model.data;

import codeu.model.data.Message;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.model.data.User;


public class NotificationStore{
  private static NotificationStore instance;

  public static NotificationStore getInstance() {
    if (instance == null) {
      instance = new NotificationStore(PersistentStorageAgent.getInstance());
    }
    return instance;
  }
  public static NotificationStore getTestInstance(PersistentStorageAgent persistentStorageAgent) {
    return new NotificationStore(persistentStorageAgent);
  }
  private PersistentStorageAgent persistentStorageAgent;

  private final List<Notification> notifications;

  private NotificationStore(PersistentStorageAgent persistentStorageAgent) {
    this.persistentStorageAgent = persistentStorageAgent;
    notification = new ArrayList<>();
  }
  public List<Notification> getAllNotification() {
    return notification;
  }

  public List<Message> NotificationForUser(UUID authorId) {

    List<Message> notificationForUser = new ArrayList<>();

    for (Notification notification : notifications) {
      if (notification.getAuthorId().equals(authorId)) {
        notificationForUser.add(notification);
      }
    }
    return notificationForUser;
  }
}
