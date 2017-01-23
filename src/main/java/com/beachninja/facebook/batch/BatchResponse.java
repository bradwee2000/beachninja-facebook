package com.beachninja.facebook.batch;

import com.beachninja.facebook.model.Header;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author bradwee2000@gmail.com
 */
public class BatchResponse {

  @JsonProperty("code")
  private final int code;

  @JsonProperty("headers")
  private final List<Header> headers;

  @JsonProperty("body")
  private final String body;

  public BatchResponse() {
    code = 0;
    headers = null;
    body = null;
  }

  public int getCode() {
    return code;
  }

  public List<Header> getHeaders() {
    return Lists.newArrayList(headers);
  }

  public String getBody() {
    return body;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || obj.getClass() != getClass() || !(obj instanceof BatchResponse)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    final BatchResponse rhs = (BatchResponse) obj;
    return Objects.equal(code, rhs.code)
        && Objects.equal(headers, rhs.headers)
        && Objects.equal(body, rhs.body);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(code, headers, body);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("code", code)
        .add("headers", headers)
        .add("body", body)
        .toString();
  }
}
