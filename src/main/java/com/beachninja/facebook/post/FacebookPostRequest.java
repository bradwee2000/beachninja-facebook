package com.beachninja.facebook.post;

import com.beachninja.facebook.batch.BatchItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonIgnoreProperties;
import com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import org.joda.time.DateTime;

/**
 *
 * https://developers.facebook.com/docs/graph-api/reference/v2.8/link
 *
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FacebookPostRequest {

  public static Builder builder() {
    return new Builder();
  }

  @JsonProperty("accessToken")
  private final String accessToken; // User or page access token

  @JsonProperty("facebookId")
  private final String facebookId; // Facebook user or page ID

  @JsonProperty("name")
  private final String name; // The name of the link

  @JsonProperty("message")
  private final String message; // The optional message from the user about this link.

  @JsonProperty("link")
  private final String link; // The post permalink

  @JsonProperty("imageUrl")
  private final String imageUrl; // A URL to the thumbnail image used in the link post

  @JsonProperty("description")
  private final String description; // A description of the link (appears beneath the link as caption).

  @JsonProperty("isPublished")
  private final boolean isPublished; // If post is immediately published. Default: True

  @JsonProperty("scheduledPublishTimeEpoch")
  private final long scheduledPublishTimeEpoch; // Time in Unix timestamp when post will be published.

  /**
   * For Jackson deserializing.
   */
  private FacebookPostRequest() {
    this.facebookId = null;
    this.name = null;
    this.message = null;
    this.link = null;
    this.imageUrl = null;
    this.description = null;
    this.isPublished = true;
    this.scheduledPublishTimeEpoch = 0;
    this.accessToken = null;
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
                              final DateTime scheduledPublishDateTime) {
    this.accessToken = accessToken;
    this.facebookId = facebookId;
    this.name = name;
    this.message = message;
    this.link = link;
    this.imageUrl = imageUrl;
    this.description = description;

    if (scheduledPublishDateTime == null) {
      this.isPublished = true;
      this.scheduledPublishTimeEpoch = 0;
    } else {
      this.isPublished = false;
      this.scheduledPublishTimeEpoch = scheduledPublishDateTime.toDate().getTime() / 1000;
    }
  }

  public String getAccessToken() {
    return accessToken;
  }

  public String getFacebookId() {
    return facebookId;
  }

  public Optional<String> getName() {
    return Optional.fromNullable(name);
  }

  public Optional<String> getMessage() {
    return Optional.fromNullable(message);
  }

  public Optional<String> getLink() {
    return Optional.fromNullable(link);
  }

  public Optional<String> getImageUrl() {
    return Optional.fromNullable(imageUrl);
  }

  public Optional<String> getDescription() {
    return Optional.fromNullable(description);
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
    if (getName().isPresent()) {
      sb.append("&name=").append(name);
    }
    if (getMessage().isPresent()) {
      sb.append("&message=").append(message);
    }
    if (getLink().isPresent()) {
      sb.append("&link=").append(link);
    }
    if (getImageUrl().isPresent()) {
      sb.append("&picture=").append(imageUrl);
    }
    if (getDescription().isPresent()) {
      sb.append("&description=").append(description);
    }
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

  public boolean equals(final Object obj) {
    if (obj == null || obj.getClass() != getClass() || !(obj instanceof FacebookPostRequest)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    final FacebookPostRequest rhs = (FacebookPostRequest) obj;
    return Objects.equal(accessToken, rhs.accessToken)
        && Objects.equal(facebookId, rhs.facebookId)
        && Objects.equal(name, rhs.name)
        && Objects.equal(message, rhs.message)
        && Objects.equal(description, rhs.description)
        && Objects.equal(link, rhs.link)
        && Objects.equal(imageUrl, rhs.imageUrl)
        && Objects.equal(isPublished, rhs.isPublished)
        && Objects.equal(scheduledPublishTimeEpoch, rhs.scheduledPublishTimeEpoch);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(accessToken, facebookId, name, message, description,
        link, imageUrl, isPublished, scheduledPublishTimeEpoch);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("facebookId", facebookId)
        .add("name", name)
        .add("message", message)
        .add("description", description)
        .add("link", link)
        .add("imageUrl", imageUrl)
        .add("isPublished", isPublished)
        .add("scheduledPublishTimeEpoch", scheduledPublishTimeEpoch)
        .toString();
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
    private DateTime publishDateTime;

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

    public Builder schedulePublish(final DateTime publishDateTime) {
      this.publishDateTime = publishDateTime;
      return this;
    }

    public FacebookPostRequest build() {
      return new FacebookPostRequest(accessToken, facebookId, title, message,
          link, imageUrl, description, publishDateTime);
    }
  }
}
