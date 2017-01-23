package com.beachninja.facebook.post;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * This will contain the Facebook Post API Response.
 *
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FacebookPostResponse {

  public static Builder builder() {
    return new Builder();
  }

  @JsonProperty("id")
  private final String id;

  /**
   * For Jackson deserializing
   */
  private FacebookPostResponse() {
    this.id = null;
  }

  private FacebookPostResponse(@JsonProperty("id") final String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("id", id).toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || obj.getClass() != getClass() || !(obj instanceof FacebookPostResponse)) {
      return false;
    }
    if(obj == this) {
      return true;
    }
    final FacebookPostResponse rhs = (FacebookPostResponse) obj;
    return Objects.equal(id, rhs.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  /**
   * Builder class
   */
  public static class Builder {
    private String id;

    public Builder id(final String id) {
      this.id = id;
      return this;
    }

    public FacebookPostResponse build() {
      return new FacebookPostResponse(id);
    }
  }
}
