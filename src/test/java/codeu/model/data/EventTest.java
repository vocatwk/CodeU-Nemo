package codeu.model.data;

import java.time.Instant;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

public class EventTest {
  
  @Test 
  public void testCreate() {
    UUID id = UUID.randomUUID();
    String type = "Message";
    Instant creation = Instant.now();
    List<String> information = 
        new ArrayList<>(
          Arrays.asList(
            "test_username", 
            "test_conversation_title", 
            "test_message_content",
            UUID.randomUUID().toString()));

    Event event = new Event(id, type, creation, information);

    Assert.assertEquals(id, event.getId());
    Assert.assertEquals(type, event.getType());
    Assert.assertEquals(creation, event.getCreationTime());
    Assert.assertEquals(information, event.getInformation());
  }
}