package codeu.model.store.persistence;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.data.Event;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import java.time.Instant;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for PersistentDataStore. The PersistentDataStore class relies on DatastoreService,
 * which in turn relies on being deployed in an AppEngine context. Since this test doesn't run in
 * AppEngine, we use LocalServiceTestHelper to do all of the AppEngine setup so we can test. More
 * info: https://cloud.google.com/appengine/docs/standard/java/tools/localunittesting
 */
public class PersistentDataStoreTest {

  private PersistentDataStore persistentDataStore;
  private final LocalServiceTestHelper appEngineTestHelper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  @Before
  public void setup() {
    appEngineTestHelper.setUp();
    persistentDataStore = new PersistentDataStore();
  }

  @After
  public void tearDown() {
    appEngineTestHelper.tearDown();
  }

  @Test
  public void testSaveAndLoadUsers() throws PersistentDataStoreException {
    UUID idOne = UUID.fromString("10000000-2222-3333-4444-555555555555");
    String nameOne = "test_username_one";
    String passwordHashOne = "$2a$10$BNte6sC.qoL4AVjO3Rk8ouY6uFaMnsW8B9NjtHWaDNe8GlQRPRT1S";
    Instant creationOne = Instant.ofEpochMilli(1000);
    User inputUserOne = new User(idOne, nameOne, passwordHashOne, creationOne);

    UUID idTwo = UUID.fromString("10000001-2222-3333-4444-555555555555");
    String nameTwo = "test_username_two";
    String passwordHashTwo = "$2a$10$ttaMOMMGLKxBBuTN06VPvu.jVKif.IczxZcXfLcqEcFi1lq.sLb6i";
    Instant creationTwo = Instant.ofEpochMilli(2000);
    User inputUserTwo = new User(idTwo, nameTwo, passwordHashTwo, creationTwo);

    // save
    persistentDataStore.writeThrough(inputUserOne);
    persistentDataStore.writeThrough(inputUserTwo);

    // load
    List<User> resultUsers = persistentDataStore.loadUsers();

    // confirm that what we saved matches what we loaded
    User resultUserOne = resultUsers.get(0);
    Assert.assertEquals(idOne, resultUserOne.getId());
    Assert.assertEquals(nameOne, resultUserOne.getName());
    Assert.assertEquals(passwordHashOne, resultUserOne.getPasswordHash());
    Assert.assertEquals(creationOne, resultUserOne.getCreationTime());

    User resultUserTwo = resultUsers.get(1);
    Assert.assertEquals(idTwo, resultUserTwo.getId());
    Assert.assertEquals(nameTwo, resultUserTwo.getName());
    Assert.assertEquals(passwordHashTwo, resultUserTwo.getPasswordHash());
    Assert.assertEquals(creationTwo, resultUserTwo.getCreationTime());
  }

  @Test
  public void testSaveAndLoadConversations() throws PersistentDataStoreException {
    UUID idOne = UUID.fromString("10000000-2222-3333-4444-555555555555");
    UUID ownerOne = UUID.fromString("10000001-2222-3333-4444-555555555555");
    String titleOne = "Test_Title";
    Instant creationOne = Instant.ofEpochMilli(1000);
    Conversation inputConversationOne = new Conversation(idOne, ownerOne, titleOne, creationOne);

    UUID idTwo = UUID.fromString("10000002-2222-3333-4444-555555555555");
    UUID ownerTwo = UUID.fromString("10000003-2222-3333-4444-555555555555");
    String titleTwo = "Test_Title_Two";
    Instant creationTwo = Instant.ofEpochMilli(2000);
    Conversation inputConversationTwo = new Conversation(idTwo, ownerTwo, titleTwo, creationTwo);

    // save
    persistentDataStore.writeThrough(inputConversationOne);
    persistentDataStore.writeThrough(inputConversationTwo);

    // load
    List<Conversation> resultConversations = persistentDataStore.loadConversations();

    // confirm that what we saved matches what we loaded
    Conversation resultConversationOne = resultConversations.get(0);
    Assert.assertEquals(idOne, resultConversationOne.getId());
    Assert.assertEquals(ownerOne, resultConversationOne.getOwnerId());
    Assert.assertEquals(titleOne, resultConversationOne.getTitle());
    Assert.assertEquals(creationOne, resultConversationOne.getCreationTime());

    Conversation resultConversationTwo = resultConversations.get(1);
    Assert.assertEquals(idTwo, resultConversationTwo.getId());
    Assert.assertEquals(ownerTwo, resultConversationTwo.getOwnerId());
    Assert.assertEquals(titleTwo, resultConversationTwo.getTitle());
    Assert.assertEquals(creationTwo, resultConversationTwo.getCreationTime());
  }

  @Test
  public void testSaveAndLoadMessages() throws PersistentDataStoreException {
    UUID idOne = UUID.fromString("10000000-2222-3333-4444-555555555555");
    UUID conversationOne = UUID.fromString("10000001-2222-3333-4444-555555555555");
    UUID authorOne = UUID.fromString("10000002-2222-3333-4444-555555555555");
    String contentOne = "test content one";
    Instant creationOne = Instant.ofEpochMilli(1000);
    Message inputMessageOne =
        new Message(idOne, conversationOne, authorOne, contentOne, creationOne);

    UUID idTwo = UUID.fromString("10000003-2222-3333-4444-555555555555");
    UUID conversationTwo = UUID.fromString("10000004-2222-3333-4444-555555555555");
    UUID authorTwo = UUID.fromString("10000005-2222-3333-4444-555555555555");
    String contentTwo = "test content one";
    Instant creationTwo = Instant.ofEpochMilli(2000);
    Message inputMessageTwo =
        new Message(idTwo, conversationTwo, authorTwo, contentTwo, creationTwo);

    // save
    persistentDataStore.writeThrough(inputMessageOne);
    persistentDataStore.writeThrough(inputMessageTwo);

    // load
    List<Message> resultMessages = persistentDataStore.loadMessages();

    // confirm that what we saved matches what we loaded
    Message resultMessageOne = resultMessages.get(0);
    Assert.assertEquals(idOne, resultMessageOne.getId());
    Assert.assertEquals(conversationOne, resultMessageOne.getConversationId());
    Assert.assertEquals(authorOne, resultMessageOne.getAuthorId());
    Assert.assertEquals(contentOne, resultMessageOne.getContent());
    Assert.assertEquals(creationOne, resultMessageOne.getCreationTime());

    Message resultMessageTwo = resultMessages.get(1);
    Assert.assertEquals(idTwo, resultMessageTwo.getId());
    Assert.assertEquals(conversationTwo, resultMessageTwo.getConversationId());
    Assert.assertEquals(authorTwo, resultMessageTwo.getAuthorId());
    Assert.assertEquals(contentTwo, resultMessageTwo.getContent());
    Assert.assertEquals(creationTwo, resultMessageTwo.getCreationTime());
  }

  @Test
  public void testSaveAndLoadEvents() throws PersistentDataStoreException {
    UUID idOne = UUID.randomUUID();
    String typeOne = "User";
    Instant creationOne = Instant.ofEpochMilli(1000);
    List<String> informationOne = new ArrayList<>();
    informationOne.add("user_one");
    Event userEvent = 
        new Event(idOne, typeOne, creationOne, informationOne);

    UUID idTwo = UUID.randomUUID();
    String typeTwo = "About Me";
    Instant creationTwo = Instant.ofEpochMilli(2000);
    List<String> informationTwo = new ArrayList<>();
    informationTwo.add("user_two");
    informationTwo.add("about_me");
    Event aboutMeEvent = 
        new Event(idTwo, typeTwo, creationTwo, informationTwo);

    UUID idThree = UUID.randomUUID();
    String typeThree = "Conversation";
    Instant creationThree = Instant.ofEpochMilli(3000);
    List<String> informationThree = new ArrayList<>();
    informationThree.add("user_three");
    informationThree.add("conversation_one_title");
    Event conversationEvent =
        new Event(idThree, typeThree, creationThree, informationThree);

    UUID idFour = UUID.randomUUID();
    String typeFour = "Message";
    Instant creationFour = Instant.ofEpochMilli(4000);
    List<String> informationFour = new ArrayList<>();
    informationFour.add("user_four");
    informationFour.add("conversation_two_title");
    informationFour.add("message_content");
    Event messageEvent = 
        new Event(idFour, typeFour, creationFour, informationFour);

    // save
    persistentDataStore.writeThrough(userEvent);
    persistentDataStore.writeThrough(aboutMeEvent);
    persistentDataStore.writeThrough(conversationEvent);
    persistentDataStore.writeThrough(messageEvent);

    // load
    List<Event> resultEvents = persistentDataStore.loadEvents();

    // confirm that what we saved matches what we loaded
    Event resultEventOne = resultEvents.get(0);
    Assert.assertEquals(idOne, resultEventOne.getId());
    Assert.assertEquals(typeOne, resultEventOne.getType());
    Assert.assertEquals(creationOne, resultEventOne.getCreationTime());
    Assert.assertEquals(informationOne, resultEventOne.getInformation());

    Event resultEventTwo = resultEvents.get(1);
    Assert.assertEquals(idTwo, resultEventTwo.getId());
    Assert.assertEquals(typeTwo, resultEventTwo.getType());
    Assert.assertEquals(creationTwo, resultEventTwo.getCreationTime());
    Assert.assertEquals(informationTwo, resultEventTwo.getInformation());

    Event resultEventThree = resultEvents.get(2);
    Assert.assertEquals(idThree, resultEventThree.getId());
    Assert.assertEquals(typeThree, resultEventThree.getType());
    Assert.assertEquals(creationThree, resultEventThree.getCreationTime());
    Assert.assertEquals(informationThree, resultEventThree.getInformation());

    Event resultEventFour = resultEvents.get(3);
    Assert.assertEquals(idFour, resultEventFour.getId());
    Assert.assertEquals(typeFour, resultEventFour.getType());
    Assert.assertEquals(creationFour, resultEventFour.getCreationTime());
    Assert.assertEquals(informationFour, resultEventFour.getInformation());
  }
}
