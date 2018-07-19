package codeu.model.store.basic;

import codeu.model.data.Event;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.time.Instant;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class EventStoreTest {

  private EventStore eventStore;
  private PersistentStorageAgent mockPersistentStorageAgent;

  private final List<String> INFORMATION_ONE =
      Arrays.asList("user_one");
  private final List<String> INFORMATION_TWO =
      Arrays.asList("user_two", "about_me");
  private final List<String> INFORMATION_THREE =
      Arrays.asList("user_three", "conversation_title");
  private final List<String> INFORMATION_FOUR =
      Arrays.asList("user_four", "conversation_title", "message_content", UUID.randomUUID().toString());

  private final Event EVENT_ONE =
      new Event(UUID.randomUUID(), "User", Instant.now(), INFORMATION_ONE);
  private final Event EVENT_TWO =
      new Event(UUID.randomUUID(), "About Me", Instant.now(), INFORMATION_TWO);
  private final Event EVENT_THREE =
      new Event(UUID.randomUUID(), "Conversation", Instant.now(), INFORMATION_THREE);
  private final Event EVENT_FOUR =
      new Event(UUID.randomUUID(), "Message", Instant.now(), INFORMATION_FOUR);

  @Before
  public void setUp() {
    mockPersistentStorageAgent = Mockito.mock(PersistentStorageAgent.class);
    eventStore = EventStore.getTestInstance(mockPersistentStorageAgent);

    final List<Event> eventList = new ArrayList<Event>();
    eventList.add(EVENT_ONE);
    eventList.add(EVENT_TWO);
    eventList.add(EVENT_THREE);
    eventList.add(EVENT_FOUR);
    eventStore.setEvents(eventList);
  }

  @Test
  public void testGetEvent_byId_found() {
    Event resultEvent = eventStore.getEvent(EVENT_ONE.getId());

    assertEquals(EVENT_ONE, resultEvent);
  }

  @Test
  public void testGetEvent_byId_notFound() {
    Event resultEvent = eventStore.getEvent(UUID.randomUUID());

    Assert.assertNull(resultEvent);
  }

  @Test
  public void testAddEvent() {
    UUID eventId = UUID.randomUUID();
    List<String> testInformation =
        Arrays.asList(
<<<<<<< HEAD
          "test_username",
          "test_conversation_title",
          "test_message_content");
=======
          "test_username",
          "test_conversation_title",
          "test_message_content",
          UUID.randomUUID().toString());
>>>>>>> a6cfeb0235dd15d6bdb22e345e20c57d1ded3948
    Event inputEvent = new Event(eventId, "Message", Instant.now(), testInformation);

    eventStore.addEvent(inputEvent);
    Event resultEvent = eventStore.getEvent(eventId);

    assertEquals(inputEvent, resultEvent);
    Mockito.verify(mockPersistentStorageAgent).writeThrough(inputEvent);
  }

  @Test
  public void testGetAllEvents_inReverseOrder() {
    List<Event> resultEvents = eventStore.getAllEvents();

    assertEquals(EVENT_FOUR, resultEvents.get(3));
    assertEquals(EVENT_THREE, resultEvents.get(2));
    assertEquals(EVENT_TWO, resultEvents.get(1));
    assertEquals(EVENT_ONE, resultEvents.get(0));
  }

  private void assertEquals(Event expectedEvent, Event actualEvent) {
    Assert.assertEquals(expectedEvent.getId(), actualEvent.getId());
    Assert.assertEquals(expectedEvent.getType(), actualEvent.getType());
    Assert.assertEquals(expectedEvent.getCreationTime(), actualEvent.getCreationTime());
    Assert.assertEquals(expectedEvent.getInformation(), actualEvent.getInformation());
  }

}
