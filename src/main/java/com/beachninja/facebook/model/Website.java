package com.beachninja.facebook.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.appengine.repackaged.com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Website {
  public static Builder builder() {
    return new Builder();
  }

  @JsonProperty("url")
  private final String url;

  @JsonProperty("type")
  private final String type;

  @JsonProperty("title")
  private final String title;

  @JsonProperty("image")
  private final List<Url> images;

  @JsonProperty("description")
  private final String description;

  @JsonProperty("id")
  private final String id;

  /**
   * Use builder class to construct.
   * @param url
   */
  public Website(@JsonProperty("url") final String url,
                 @JsonProperty("type") final String type,
                 @JsonProperty("title") final String title,
                 @JsonProperty("image") final List<Url> images,
                 @JsonProperty("description") final String description,
                 @JsonProperty("id") final String id) {
    this.url = url;
    this.type = type;
    this.title = title;
    this.images = images;
    this.description = description;
    this.id = id;
  }

  public String getUrl() {
    return url;
  }

  public String getType() {
    return type;
  }

  public String getTitle() {
    return title;
  }

  public List<Url> getImages() {
    return images;
  }

  public String getDescription() {
    return description;
  }

  public String getId() {
    return id;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || obj.getClass() != getClass() || !(obj instanceof Website)) {
      return false;
    }
    if(obj == this) {
      return true;
    }
    final Website rhs = (Website) obj;
    return Objects.equal(url, rhs.url)
        && Objects.equal(type, rhs.type)
        && Objects.equal(title, rhs.title)
        && Objects.equal(images, rhs.images)
        && Objects.equal(description, rhs.description)
        && Objects.equal(id, rhs.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(url, type, title, images, description, id);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("url", url).add("type", type).add("title", title).add("images", images).add("description", description)
        .toString();
  }

  /**
   * Builder class
   */
  public static class Builder {
    private String url;
    private String type;
    private String title;
    private final List<Url> images = Lists.newArrayList();
    private String description;
    private String id;

    public Builder url(final String url) {
      this.url = url;
      return this;
    }
    public Builder type(final String type) {
      this.type = type;
      return this;
    }
    public Builder title(final String title) {
      this.title = title;
      return this;
    }
    public Builder description(final String description) {
      this.description = description;
      return this;
    }
    public Builder id(final String id) {
      this.id = id;
      return this;
    }
    public Builder addImage(final Url image) {
      this.images.add(image);
      return this;
    }
    public Website build() {
      return new Website(url, type, title, images, description, id);
    }
  }
}
