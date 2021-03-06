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
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;


/** Class representing a registered user. */
public class User {
  private final UUID id;
  private final String name;
  private final String passwordHash;
  private final Instant creation;
  private String aboutMe;
  private boolean isAdmin;
  private Instant lastSeenNotifications;
  private List<UUID> subscriptions;
  private HashMap<UUID, Instant> lastSeenConversations;
  private String picture;
  /**
   * Constructs a new User.
   *
   * @param id the ID of this User
   * @param name the username of this User
   * @param passwordHash the password hash of this User
   * @param creation the creation time of this User
   */
  public User(UUID id, String name, String passwordHash, Instant creation) {
    this.id = id;
    this.name = name;
    this.passwordHash = passwordHash;
    this.creation = creation;
    aboutMe = "";
    isAdmin = false;
    lastSeenNotifications = creation;
    subscriptions = new ArrayList<>();
    lastSeenConversations = new HashMap<>();
    this.picture = null;
  }

  /** Returns the ID of this User. */
  public UUID getId() {
    return id;
  }

  /** Returns the username of this User. */
  public String getName() {
    return name;
  }

  /** Returns the password hash of this User. */
  public String getPasswordHash() {
    return passwordHash;
  }

  /** Returns the creation time of this User. */
  public Instant getCreationTime() {
    return creation;
  }

  /** sets users' about me message. */
  public void setAboutMe(String message) {
    aboutMe = message;
  }

  /** Return the About Me of a user. */
  public String getAboutMe() {
    return aboutMe;
  }

  /** Sets if the user is admin. */
  public void setIsAdmin(boolean a){
    isAdmin = a;
  }

  /** Returns true if the user is admin. Returns false if not */
  public boolean getIsAdmin(){
    return isAdmin;
  }

  /** Set the Instant the last time the user visted the notification page */
  public void setLastSeenNotifications(Instant lastSeen){
    lastSeenNotifications = lastSeen;
  }
  /** get the Instant the last time the user visted the notification page */
  public Instant getLastSeenNotifications(){
    return lastSeenNotifications;
  }

  public HashMap<UUID, Instant> getLastSeenConversations(){
    return lastSeenConversations;
  }

  public void setLastSeenConversations(HashMap<UUID, Instant> lastSeens){
    lastSeenConversations = lastSeens;
  }

  public void sawConversation(UUID conversationId){
    lastSeenConversations.put(conversationId, Instant.now());
  }
    /** Add a conversation id to the subscriptions list */
    public void addSubscription(UUID conversationId){
      subscriptions.add(conversationId);
    }

    /** get the subscriptions list*/
    public List<UUID> getSubscriptions(){
      return subscriptions;
    }
    /** remove a conversation id from the subscriptions list */
    public void removeSubscription(UUID conversationId){
      subscriptions.remove(conversationId);
    }
    /** set the subscriptions list*/
    public void setSubscriptions(List<UUID> subscriptions){
       this.subscriptions = subscriptions;
    }

    public List<String> getSubscriptionsAsString(){
      List<String> idsAsStrings = new ArrayList<>();
      for(UUID subId: subscriptions){
        String subIdAsString = subId.toString();
        idsAsStrings.add(subIdAsString);
      }
      return idsAsStrings;
  }

  public String getPicture(){
    return this.picture;
  }

  public void setPicture(String pictureUpload){
    this.picture = pictureUpload;
  }

}
