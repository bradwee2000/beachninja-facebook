package com.beachninja.facebook.batch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BatchItem {

  public static final Builder builder() {
    return new Builder();
  }

  @JsonProperty("method")
  private final String method;

  @JsonProperty("relative_url")
  private final String relativeUrl;

  @JsonProperty("body")
  private final String body;

  /**
   * For Jackson serialization.
   */
  private BatchItem() {
    this.method = null;
    this.relativeUrl = null;
    this.body = null;
  }

  /**
   * Use Builder class to construct
   */
  private BatchItem(final String method,
                    final String relativeUrl,
                    final String body) {
    this.method = method;
    this.relativeUrl = relativeUrl;
    this.body = body;
  }

  public String getMethod() {
    return method;
  }

  public String getRelativeUrl() {
    return relativeUrl;
  }

  public String getBody() {
    return body;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || obj.getClass() != getClass() || !(obj instanceof BatchItem)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    final BatchItem rhs = (BatchItem) obj;
    return Objects.equal(method, rhs.method)
        && Objects.equal(relativeUrl, rhs.relativeUrl)
        && Objects.equal(body, rhs.body);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(method, relativeUrl, body);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("method", method)
        .add("relativeUrl", relativeUrl)
        .add("body", body)
        .toString();
  }

  /**
   * Builder class
   */
  public static final class Builder {
    private static final String GET_METHOD = "GET";
    private static final String POST_METHOD = "POST";

    private String method;
    private String relativeUrl;
    private String body;

    public Builder get() {
      method = GET_METHOD;
      return this;
    }

    public Builder post() {
      method = POST_METHOD;
      return this;
    }

    public Builder relativeUrl(final String relativeUrl) {
      this.relativeUrl = relativeUrl;
      return this;
    }

    public Builder body(final String body) {
      this.body = body;
      return this;
    }

    public BatchItem build() {
      return new BatchItem(method, relativeUrl, body);
    }
  }
}
