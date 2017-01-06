package com.beachninja.facebook.error;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FacebookError {

  @JsonProperty("message")
  private String message;

  @JsonProperty("type")
  private String type;

  @JsonProperty("code")
  private int code;

  @JsonProperty("fbtrace_id")
  private String fbtraceId;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getFbtraceId() {
    return fbtraceId;
  }

  public void setFbtraceId(String fbtraceId) {
    this.fbtraceId = fbtraceId;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("message", message)
        .add("type", type)
        .add("code", code)
        .add("fbtraceId", fbtraceId)
        .toString();
  }
}
