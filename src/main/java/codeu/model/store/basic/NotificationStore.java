package codeu.model.store.basic;

import codeu.model.data.Notification;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import codeu.model.data.User;

/**
 * Store class that uses in-memory data structures to hold values and automatically loads from and
 * saves to PersistentStorageAgent. It's a singleton so all servlet classes can access the same
 * instance.
 */

public class NotificationStore {

  private static NotificationStore instance;
  /** Singleton instance of NotifcationStore. */

  public static NotificationStore getInstance() {
    if (instance == null) {
      instance = new NotificationStore(PersistentStorageAgent.getInstance());
    }
    return instance;
  }

  /**
   * Instance getter function used for testing. Supply a mock for PersistentStorageAgent.
   *
   * @param persistentStorageAgent a mock used for testing
   */
  public static NotificationStore getTestInstance(PersistentStorageAgent persistentStorageAgent) {
    return new NotificationStore(persistentStorageAgent);
  }
  /**
   * The PersistentStorageAgent responsible for loading Notifications from and saving Notifications to Datastore.
   */
  private PersistentStorageAgent persistentStorageAgent;

  /** The in-memory list of Notifications. */

  private List<Notification> notifications;

  /** This class is a singleton, so its constructor is private. Call getInstance() instead. */

  private NotificationStore(PersistentStorageAgent persistentStorageAgent) {
    this.persistentStorageAgent = persistentStorageAgent;
    notifications = new ArrayList<>();
  }

  /** Access the current set of notifications known to the application. */

  public List<Notification> getAllNotification() {
    return notifications;
  }

  /**
   * Add a new notification to the current set of notifications known to the application. This should only be called
   * to add a new notification, not to update an existing notification.
   */
  public void addNotification(Notification notification) {
    notifications.add(notification);
    persistentStorageAgent.writeThrough(notification);
  }

  /**
   * Access the notification object with the given UUID.
   *
   * @return null if the UUID does not match any existing notification.
   */
  public Notification getNotification(UUID id) {
    for (Notification notification : notifications) {
      if (notification.getId().equals(id)) {
        return notification;
      }
    }
    return null;
  }

  /**
   * Delete a seen notification from  the current set of notifications known to the application. This should only be
   * called to delete seen notification, not to delete unseen notification.
   */
  public List<Notification> deleteNotification(UUID id){
    for (Notification notification : notifications) {
      if (notification.getId().equals(id)) {
          notifications.remove(notification);
      }
    }
    return notifications;
  }

  /** Access the current set of notifications from the receiver. */

  public List<Notification> NotificationForUser(UUID receiverId) {
    List<Notification> notificationForUser = new ArrayList<>();
    for (Notification notification : notifications) {
      User receiver = notification.getReceiver();
      if (receiver.getId().equals(receiverId)) {
        notificationForUser.add(notification);
      }
    }
    return notificationForUser;
  }

}
