package com.beachninja.facebook.batch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Header {

  @JsonProperty("name")
  private final String name;

  @JsonProperty("value")
  private final String value;

  private Header(){
    name = null;
    value = null;
  }

  public Header(final String name, final String value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || obj.getClass() != getClass() || !(obj instanceof Header)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    final Header rhs = (Header) obj;
    return Objects.equal(name, rhs.name)
        && Objects.equal(value, rhs.value);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name, value);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("name", name)
        .add("value", value)
        .toString();
  }
}
