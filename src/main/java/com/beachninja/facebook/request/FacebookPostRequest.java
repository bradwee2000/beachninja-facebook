package com.beachninja.facebook.request;

import com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonIgnoreProperties;
import com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

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

  @JsonProperty("facebookId")
  private final String facebookId; // Facebook user / page ID

  @JsonProperty("title")
  private final String title; // The title

  @JsonProperty("message")
  private final String message; // The optional message from the user about this link.

  @JsonProperty("link")
  private final String link; // The post permalink

  @JsonProperty("imageUrl")
  private final String imageUrl; // A URL to the thumbnail image used in the link post

  @JsonProperty("description")
  private final String description; // A description of the link (appears beneath the link caption).

  /**
   * For Jackson deserializing.
   */
  private FacebookPostRequest() {
    this.facebookId = null;
    this.title = null;
    this.message = null;
    this.link = null;
    this.imageUrl = null;
    this.description = null;
  }

  /**
   * Use Builder class to create.
   */
  private FacebookPostRequest(final String facebookId,
                              final String title,
                              final String message,
                              final String link,
                              final String imageUrl,
                              final String description) {
    checkNotNull(facebookId, "facebookId must not be null");
    this.facebookId = facebookId;
    this.title = title;
    this.message = message;
    this.link = link;
    this.imageUrl = imageUrl;
    this.description = description;
  }

  public String getFacebookId() {
    return facebookId;
  }

  public Optional<String> getTitle() {
    return Optional.fromNullable(title);
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

  public boolean equals(final Object obj) {
    if (obj == null || obj.getClass() != getClass() || !(obj instanceof FacebookPostRequest)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    final FacebookPostRequest rhs = (FacebookPostRequest) obj;
    return Objects.equal(facebookId, rhs.facebookId)
        && Objects.equal(title, rhs.title)
        && Objects.equal(message, rhs.message)
        && Objects.equal(description, rhs.description)
        && Objects.equal(link, rhs.link)
        && Objects.equal(imageUrl, rhs.imageUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(facebookId, title, message, description, link, imageUrl);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("facebookId", facebookId)
        .add("title", title)
        .add("message", message)
        .add("description", description)
        .add("link", link)
        .add("imageUrl", imageUrl)
        .toString();
  }

  /**
   * Builder class
   */
  public static final class Builder {
    private String facebookId;
    private String title;
    private String message;
    private String link;
    private String imageUrl;
    private String description;


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
    public FacebookPostRequest build() {
      return new FacebookPostRequest(facebookId, title, message, link, imageUrl, description);
    }
  }
}
