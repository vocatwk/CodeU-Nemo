package codeu.model.store.basic;

import codeu.model.data.Event;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import java.time.Instant;

/**
 * Store class that uses in-memory data structures to hold values and automatically loads from and
 * saves to PersistentStorageAgent. It's a singleton so all servlet classes can access the same
 * instance.
 */
public class EventStore {

  /** Singleton instance of EventStore. */
  private static EventStore instance;

  /**
   * Returns the singleton instance of EventStore that should be shared between all servlet classes.
   * Do not call this function from a test; use getTestInstance() instead.
   */
  public static EventStore getInstance() {
    if (instance == null) {
      instance = new EventStore(PersistentStorageAgent.getInstance());
    }
    return instance;
  }

  /**
   * Instance getter function used for testing. Supply a mock for PersistentStorageAgent.
   *
   * @param persistentStorageAgent a mock used for testing
   */
  public static EventStore getTestInstance(PersistentStorageAgent persistentStorageAgent) {
    return new EventStore(persistentStorageAgent);
  }

  /**
   * The PersistentStorageAgent responsible for loading Events from and saving Events to Datastore.
   */
  private PersistentStorageAgent persistentStorageAgent;

  /** The in-memory list of Events. */
  private List<Event> events;

  /** This class is a singleton, so its constructor is private. Call getInstance() instead. */
  private EventStore(PersistentStorageAgent persistentStorageAgent) {
    this.persistentStorageAgent = persistentStorageAgent;
    events = new ArrayList<>();
  }

  /** Access the current set of users known to the application. */
  public List<Event> getAllEvents() {
    return events;
  }

  /**
   * Access the Event object with the given UUID.
   *
   * @return null if the UUID does not match any existing Event.
   */
  public Event getEvent(UUID id) {
    for (Event event : events) {
      if (event.getId().equals(id)) {
        return event;
      }
    }
    return null;
  }

  /**
   * Add a new event to the current set of events known to the application. This should only be called
   * to add a new event, not to update an existing event.
   */
  public void addEvent(Event event) {
    events.add(event);
    persistentStorageAgent.writeThrough(event);
  }

  /**
   * Sets the List of Events stored by this EventStore. This should only be called once, when the data
   * is loaded from Datastore.
   */
  public void setEvents(List<Event> events) {
    this.events = events;
  }

  public List<Event> getEventsSince(Event lastSeen){
    List<Event> eventsToShow = new ArrayList<Event>();
    int indexOfEvent = events.indexOf(lastSeen);
    if(indexOfEvent != -1){
    eventsToShow = events.subList(indexOfEvent, events.size()-1);
    return eventsToShow;
  }else{
    return eventsToShow;
  }

  }

}
