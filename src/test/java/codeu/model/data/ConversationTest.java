// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package codeu.model.data;

import java.time.Instant;
import java.util.UUID;
import java.util.HashSet;
import org.junit.Assert;
import org.junit.Test;

public class ConversationTest {

  @Test
  public void testCreate() {
    UUID id = UUID.randomUUID();
    UUID owner = UUID.randomUUID();
    String title = "Test_Title";
    Instant creation = Instant.now();
    Boolean isPrivate = true;
    HashSet<String> members = new HashSet<>();
    members.add("testUser1"); members.add("testUser2");
    Boolean containsTestUser1 = true;
    Boolean containsTestUser2 = true;

    Conversation conversation = new Conversation(id, owner, title, creation);
    conversation.makePrivate();
    conversation.addMember("testUser1");
    conversation.addMember("testUser2");

    Assert.assertEquals(id, conversation.getId());
    Assert.assertEquals(owner, conversation.getOwnerId());
    Assert.assertEquals(title, conversation.getTitle());
    Assert.assertEquals(creation, conversation.getCreationTime());
    Assert.assertEquals(isPrivate, conversation.isPrivate());
    Assert.assertEquals(members, conversation.getMembers());
    Assert.assertEquals(containsTestUser1, conversation.containsMember("testUser1"));

    conversation.removeMember("testUser1");
    containsTestUser1 = false;

    Assert.assertEquals(containsTestUser1, conversation.containsMember("testUser1"));
    Assert.assertEquals(containsTestUser2, conversation.containsMember("testUser2"));
  }
}
