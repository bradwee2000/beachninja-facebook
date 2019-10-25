package com.beachninja.facebook.post;

import com.beachninja.facebook.batch.BatchItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.Optional;

/**
 *
 * https://developers.facebook.com/docs/graph-api/reference/v2.8/link
 *
 * @author bradwee2000@gmail.com
 */
public class FacebookPostRequest {

  public static Builder builder() {
    return new Builder();
  }

  private String accessToken; // User or page access token
  private String facebookId; // Facebook user or page ID
  private Optional<String> name; // The name of the link
  private Optional<String> message; // The optional message from the user about this link.
  private Optional<String> link; // The post permalink
  private Optional<String> imageUrl; // A URL to the thumbnail image used in the link post
  private Optional<String> description; // A description of the link (appears beneath the link as caption).
  private boolean isPublished; // If post is immediately published. Default: True
  private long scheduledPublishTimeEpoch; // Time in Unix timestamp when post will be published.

  /**
   * For Jackson deserializing.
   */
  public FacebookPostRequest() {

  }

  /**
   * Use Builder class to create.
   */
  private FacebookPostRequest(final String accessToken,
                              final String facebookId,
                              final String name,
                              final String message,
                              final String link,
                              final String imageUrl,
                              final String description,
                              final LocalDateTime scheduledPublishDateTime) {
    this.accessToken = accessToken;
    this.facebookId = facebookId;
    this.name = Optional.ofNullable(name);
    this.message = Optional.ofNullable(message);
    this.link = Optional.ofNullable(link);
    this.imageUrl = Optional.ofNullable(imageUrl);
    this.description = Optional.ofNullable(description);

    if (scheduledPublishDateTime == null) {
      this.isPublished = true;
      this.scheduledPublishTimeEpoch = 0;
    } else {
      this.isPublished = false;
      this.scheduledPublishTimeEpoch = scheduledPublishDateTime.atZone(ZoneId.systemDefault()).toEpochSecond();
    }
  }

  public String getAccessToken() {
    return accessToken;
  }

  public String getFacebookId() {
    return facebookId;
  }

  public Optional<String> getName() {
    return name;
  }

  public Optional<String> getMessage() {
    return message;
  }

  public Optional<String> getLink() {
    return link;
  }

  public Optional<String> getImageUrl() {
    return imageUrl;
  }

  public Optional<String> getDescription() {
    return description;
  }

  public FacebookPostRequest setAccessToken(String accessToken) {
    this.accessToken = accessToken;
    return this;
  }

  public FacebookPostRequest setFacebookId(String facebookId) {
    this.facebookId = facebookId;
    return this;
  }

  public FacebookPostRequest setName(String name) {
    this.name = Optional.ofNullable(name);
    return this;
  }

  public FacebookPostRequest setMessage(String message) {
    this.message = Optional.ofNullable(message);
    return this;
  }

  public FacebookPostRequest setLink(String link) {
    this.link = Optional.ofNullable(link);
    return this;
  }

  public FacebookPostRequest setImageUrl(String imageUrl) {
    this.imageUrl = Optional.ofNullable(imageUrl);
    return this;
  }

  public FacebookPostRequest setDescription(String description) {
    this.description = Optional.ofNullable(description);
    return this;
  }

  public FacebookPostRequest setPublished(boolean published) {
    isPublished = published;
    return this;
  }

  public FacebookPostRequest setScheduledPublishTimeEpoch(long scheduledPublishTimeEpoch) {
    this.scheduledPublishTimeEpoch = scheduledPublishTimeEpoch;
    return this;
  }

  @JsonIgnore
  public boolean isPublished() {
    return isPublished;
  }

  public long getScheduledPublishTimeEpoch() {
    return scheduledPublishTimeEpoch;
  }

  public String toUrlParams() {
    final StringBuilder sb = new StringBuilder();
    name.ifPresent(v -> sb.append("&name=").append(v));
    message.ifPresent(v -> sb.append("&message=").append(v));
    link.ifPresent(v -> sb.append("&link=").append(v));
    imageUrl.ifPresent(v -> sb.append("&picture=").append(v));
    description.ifPresent(v -> sb.append("&description=").append(v));


    if (!isPublished) {
      sb.append("&published=").append(isPublished);
      sb.append("&scheduled_publish_time=").append(scheduledPublishTimeEpoch);
    }
    return sb.toString();
  }

  public BatchItem toBatchItem() {
    return BatchItem.builder().post()
        .relativeUrl(facebookId + "/feed")
        .body(toUrlParams())
        .build();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FacebookPostRequest that = (FacebookPostRequest) o;
    return isPublished == that.isPublished &&
            scheduledPublishTimeEpoch == that.scheduledPublishTimeEpoch &&
            Objects.equals(accessToken, that.accessToken) &&
            Objects.equals(facebookId, that.facebookId) &&
            Objects.equals(name, that.name) &&
            Objects.equals(message, that.message) &&
            Objects.equals(link, that.link) &&
            Objects.equals(imageUrl, that.imageUrl) &&
            Objects.equals(description, that.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accessToken, facebookId, name, message, link, imageUrl, description, isPublished, scheduledPublishTimeEpoch);
  }

  @Override
  public String toString() {
    return "FacebookPostRequest{" +
            "accessToken='" + accessToken + '\'' +
            ", facebookId='" + facebookId + '\'' +
            ", name=" + name +
            ", message=" + message +
            ", link=" + link +
            ", imageUrl=" + imageUrl +
            ", description=" + description +
            ", isPublished=" + isPublished +
            ", scheduledPublishTimeEpoch=" + scheduledPublishTimeEpoch +
            '}';
  }

  /**
   * Builder class
   */
  public static final class Builder {
    private String accessToken;
    private String facebookId;
    private String title;
    private String message;
    private String link;
    private String imageUrl;
    private String description;
    private LocalDateTime publishDateTime;

    public Builder accessToken(final String accessToken) {
      this.accessToken = accessToken;
      return this;
    }

    public Builder facebookId(final String facebookId) {
      this.facebookId = facebookId;
      return this;
    }

    public Builder title(final String title) {
      this.title = title;
      return this;
    }

    public Builder message(final String message) {
      this.message = message;
      return this;
    }

    public Builder link(final String link) {
      this.link = link;
      return this;
    }

    public Builder imageUrl(final String imageUrl) {
      this.imageUrl = imageUrl;
      return this;
    }

    public Builder description(final String description) {
      this.description = description;
      return this;
    }

    public Builder schedulePublish(final LocalDateTime publishDateTime) {
      this.publishDateTime = publishDateTime;
      return this;
    }

    public FacebookPostRequest build() {
      return new FacebookPostRequest(accessToken, facebookId, title, message,
          link, imageUrl, description, publishDateTime);
    }
  }
}
