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
import java.util.Collection;

/**
 * Class representing a conversation, which can be thought of as a chat room. Conversations are
 * created by a User and contain Messages.
 */
public class Conversation {
  public final UUID id;
  public final UUID owner;
  public final Instant creation;
  public final String title;
  private Boolean isPrivate;
  private HashSet<String> members;

  /**
   * Constructs a new Conversation.
   *
   * @param id the ID of this Conversation
   * @param owner the ID of the User who created this Conversation
   * @param title the title of this Conversation
   * @param creation the creation time of this Conversation
   */
  public Conversation(UUID id, UUID owner, String title, Instant creation) {
    this.id = id;
    this.owner = owner;
    this.creation = creation;
    this.title = title;
    this.isPrivate = false;
    this.members = new HashSet<String>();
  }

  /** Returns the ID of this Conversation. */
  public UUID getId() {
    return id;
  }

  /** Returns the ID of the User who created this Conversation. */
  public UUID getOwnerId() {
    return owner;
  }

  /** Returns the title of this Conversation. */
  public String getTitle() {
    return title;
  }

  /** Returns the creation time of this Conversation. */
  public Instant getCreationTime() {
    return creation;
  }

  /** Returns the type of this conversation. */
  public Boolean isPrivate() {
    return isPrivate;
  }

  /** makes this conversation public. */
  public void makePublic() {
    this.isPrivate = false;
  }

  /** makes this conversation private. */
  public void makePrivate() {
    this.isPrivate = true;
  }

  /** adds a member to this conversation. */
  public void addMember(String username) {
    members.add(username);
  }

  /** adds a set of members to this conversation. */
  public void addMembers(HashSet<String> usernames) {
    members.addAll(usernames);
  }

  /** sets the members of this conversation. */
  public void setMembers(HashSet<String> usernames) {
    this.members = usernames;
  }

  /** Returns the members of this conversation. */
  public HashSet<String> getMembers() {
    return members;
  }

  /** Checks if a user is a member of this conversation. */
  public boolean containsMember(String username) {
    return members.contains(username);
  }

  /** Removes a member of this conversation. */
  public void removeMember(String username) {
    members.remove(username);
  }
}
