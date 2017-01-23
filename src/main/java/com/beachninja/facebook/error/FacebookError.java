package com.beachninja.facebook.error;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FacebookError {

  public static final Builder builder() {
    return new Builder();
  }

  @JsonProperty("message")
  private String message;

  @JsonProperty("type")
  private String type;

  @JsonProperty("code")
  private int code;

  @JsonProperty("fbtrace_id")
  private String fbtraceId;

  @JsonProperty("error_user_title")
  private String errorUserTitle;

  @JsonProperty("error_user_msg")
  private String errorUserMsg;

  public FacebookError(@JsonProperty("message") final String message,
                       @JsonProperty("type") final String type,
                       @JsonProperty("code") final int code,
                       @JsonProperty("fbtrace_id") final String fbtraceId,
                       @JsonProperty("error_user_title") final String errorUserTitle,
                       @JsonProperty("error_user_msg") final String errorUserMsg) {
    this.message = message;
    this.type = type;
    this.code = code;
    this.fbtraceId = fbtraceId;
    this.errorUserTitle = errorUserTitle;
    this.errorUserMsg = errorUserMsg;
  }

  public String getMessage() {
    return message;
  }

  public String getType() {
    return type;
  }

  public int getCode() {
    return code;
  }

  public String getFbtraceId() {
    return fbtraceId;
  }

  public String getErrorUserTitle() {
    return errorUserTitle;
  }

  public String getErrorUserMsg() {
    return errorUserMsg;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || obj.getClass() != getClass() || !(obj instanceof FacebookError)) {
      return false;
    }
    if(obj == this) {
      return true;
    }
    final FacebookError rhs = (FacebookError) obj;
    return Objects.equal(message, rhs.message)
        && Objects.equal(type, rhs.type)
        && Objects.equal(code, rhs.code)
        && Objects.equal(fbtraceId, rhs.fbtraceId)
        && Objects.equal(errorUserMsg, rhs.errorUserMsg)
        && Objects.equal(errorUserTitle, rhs.errorUserTitle);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(message, type, code, fbtraceId, errorUserMsg, errorUserTitle);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("message", message)
        .add("type", type)
        .add("code", code)
        .add("fbtraceId", fbtraceId)
        .add("errorUserTitle", errorUserTitle)
        .add("errorUserMsg", errorUserMsg)
        .toString();
  }

  /**
   * Builder class
   */
  public static final class Builder {
    private String message;
    private String type;
    private int code;
    private String fbtraceId;
    private String errorUserTitle;
    private String errorUserMsg;

    public Builder message(final String message) {
      this.message = message;
      return this;
    }
    public Builder type(final String type) {
      this.type = type;
      return this;
    }
    public Builder code(final int code) {
      this.code = code;
      return this;
    }
    public Builder fbtraceId(final String fbtraceId) {
      this.fbtraceId = fbtraceId;
      return this;
    }
    public Builder errorUserTitle(final String errorUserTitle) {
      this.errorUserTitle = errorUserTitle;
      return this;
    }
    public Builder errorUserMsg(final String errorUserMsg) {
      this.errorUserMsg = errorUserMsg;
      return this;
    }
    public FacebookError build() {
      return new FacebookError(message, type, code, fbtraceId, errorUserTitle, errorUserMsg);
    }
  }
}
