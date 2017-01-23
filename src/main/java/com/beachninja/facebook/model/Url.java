package com.beachninja.facebook.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Url {

  public static final Url of(final String url) {
    return new Url(url);
  }

  @JsonProperty("url")
  private final String url;

  public Url(@JsonProperty("url") final String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || !(obj instanceof  Url)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    final Url rhs = (Url) obj;
    return Objects.equal(url, rhs.url);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(url);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("url", url).toString();
  }
}
