package codeu.model.data;

import codeu.model.data.Conversation;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class NemoBotTest {

  private NemoBot nemoBot;
  private Conversation mockConversation;

  @Before
  public void setup() {
    nemoBot = new NemoBot();

    mockConversation = Mockito.mock(Conversation.class);
  }

  @Test
  public void testCreate() {
    String name = "NemoBot";
    UUID id = UUID.nameUUIDFromBytes(name.getBytes());
    String aboutMe = "I'm " + name + ". Want to talk to me? Simply @ mention me in any conversation!"
        + " Feel free to ask me about the following: Conversations, Profiles, " 
        + "Activtity Feed, Notifications, Searching for users, and Bots.";
    String mentionKey = "@" + name;
    
    Assert.assertEquals(id, nemoBot.getId());
    Assert.assertEquals(name, nemoBot.getName());
    Assert.assertEquals(mentionKey, nemoBot.getMentionKey());
    Assert.assertEquals(aboutMe, nemoBot.getAboutMe());
    Assert.assertTrue("true", nemoBot.getIsAdmin());
    Assert.assertNull(nemoBot.getLastSeenNotifications());
  }

  @Test
  public void getKeyword_lowerCase() {
    String message = "@nemobot can you help me?";
    String keyword = "help";
    String result = nemoBot.getKeyword(message);

    Assert.assertEquals(keyword, result);
  }

  @Test
  public void getKeyword_upperCase() {
    String message = "@NEMOBOT can you HELP me?";
    String keyword = "help";
    String result = nemoBot.getKeyword(message);

    Assert.assertEquals(keyword, result);
  }

  @Test
  public void getKeyword_withPunctuation() {
    String message = "@nemobot! can you help???";
    String keyword = "help";
    String result = nemoBot.getKeyword(message);

    Assert.assertEquals(keyword, result);
  }

  @Test
  public void getKeyword_mixed() {
    String message = "@NeMOBot!? can you ?HeLp?? me";
    String keyword = "help";
    String result = nemoBot.getKeyword(message);

    Assert.assertEquals(keyword, result);
  }

  @Test
  public void getKeyword_null() {
    String message = "@NemoBot what's the weather today?";
    String result = nemoBot.getKeyword(message);

    Assert.assertNull(result);
  }

  @Test
  public void answerMessage_validKeyword() {
    String message = "@NemoBot can you help me?";
    String answer = "Feel free to ask me about the following: Conversations, "
        + "Profiles, Activtity Feed, Notifications, Searching for users, and Bots.";
    String result = nemoBot.answerMessage(message, mockConversation);

    Assert.assertEquals(answer, result);
  }

  @Test
  public void answerMessage_nullKeyword() {
    String message = "@NemoBot what's the weather today?";
    String answer = "I'm sorry. I didn't understand that. "
        + "Send a message with the word \"Help\".";
    String result = nemoBot.answerMessage(message, mockConversation);

    Assert.assertEquals(answer, result);
  }
}