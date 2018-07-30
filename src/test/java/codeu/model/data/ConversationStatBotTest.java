package codeu.model.data;

import codeu.model.data.Conversation;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ConversationStatBotTest {

  private ConversationStatBot conversationStatBot;
  private Conversation mockConversation;

  @Before
  public void setup() {
    conversationStatBot = new ConversationStatBot();
    mockConversation = Mockito.mock(Conversation.class);
  }

  @Test
  public void testCreate() {
    String name = "ConversationStatBot";
    UUID id = UUID.nameUUIDFromBytes(name.getBytes());
    String aboutMe = "I'm " + name + ". Want to talk to me? Simply @ mention me in any conversation!";
    String mentionKey = "@" + name;

    Assert.assertEquals(id, conversationStatBot.getId());
    Assert.assertEquals(name, conversationStatBot.getName());
    Assert.assertEquals(mentionKey, conversationStatBot.getMentionKey());
    Assert.assertEquals(aboutMe, conversationStatBot.getAboutMe());
    Assert.assertTrue("true", conversationStatBot.getIsAdmin());
    Assert.assertNull(conversationStatBot.getLastSeenNotifications());
  }

  @Test
  public void getKeyword_lowerCase() {
    String message = "@conversationstatbot can you help me?";
    String keyword = "help";
    String result = conversationStatBot.getKeyword(message);

    Assert.assertEquals(keyword, result);
  }

  @Test
  public void getKeyword_upperCase() {
    String message = "@CONVERSATIONSTATBOT can you HELP me?";
    String keyword = "help";
    String result = conversationStatBot.getKeyword(message);

    Assert.assertEquals(keyword, result);
  }

  @Test
  public void getKeyword_withPunctuation() {
    String message = "@conversationstatbot! can you help???";
    String keyword = "help";
    String result = conversationStatBot.getKeyword(message);

    Assert.assertEquals(keyword, result);
  }

  @Test
  public void getKeyword_mixed() {
    String message = "@CONversAtiOnSTATboT!? can you ?HeLp?? me";
    String keyword = "help";
    String result = conversationStatBot.getKeyword(message);

    Assert.assertEquals(keyword, result);
  }

  @Test
  public void getKeyword_null() {
    String message = "@ConversationStatBot what's the weather today?";
    String result = conversationStatBot.getKeyword(message);

    Assert.assertNull(result);
  }

  @Test
  public void answerMessage_validKeyword() {
    String message = "@ConversationStatBot can you help me?";
    String answer = "Feel free to ask me about the following for this "
        + "conversation: The owner, when it was created, the privacy status, "
        + "and the amount of users and messages.";
    String result = conversationStatBot.answerMessage(message, mockConversation);

    Assert.assertEquals(answer, result);
  }

  @Test
  public void answerMessage_nullKeyword() {
    String message = "@ConversationStatBot what's the weather today?";
    String answer = "I'm sorry. I didn't understand that. "
        + "Send a message with the word \"Help\".";
    String result = conversationStatBot.answerMessage(message, mockConversation);

    Assert.assertEquals(answer, result);
  }
}