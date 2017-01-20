package com.beachninja.facebook.post;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.joda.ser.LocalDateTimeSerializer;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.joda.time.LocalDateTime;

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

  public static FacebookPostResponse fail(final Exception exception) {
    return new Builder().exception(exception).build();
  }

  private final FacebookPostRequest request;

  private final String apiResponse;

  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private final LocalDateTime dateTime;

  private final Exception exception;

  /**
   * For Jackson deserializing
   */
  private FacebookPostResponse() {
    this.request = null;
    this.apiResponse = null;
    this.dateTime = null;
    this.exception = null;
  }

  private FacebookPostResponse(final FacebookPostRequest request,
                              final String apiResponse,
                              final LocalDateTime dateTime,
                              final Exception exception) {
    this.request = request;
    this.apiResponse = apiResponse;
    this.dateTime = dateTime;
    this.exception = exception;
  }

  public FacebookPostRequest getRequest() {
    return request;
  }

  public String getApiResponse() {
    return apiResponse;
  }

  public LocalDateTime getDateTime() {
    return dateTime;
  }

  public Exception getException() {
    return exception;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("request", request)
        .add("apiResponse", apiResponse)
        .add("time", dateTime)
        .add("exception", ExceptionUtils.getFullStackTrace(exception))
        .toString();
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
    return Objects.equal(apiResponse, rhs.apiResponse)
        && Objects.equal(request, rhs.request)
        && Objects.equal(exception, rhs.exception)
        && Objects.equal(dateTime, rhs.dateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(request, apiResponse, exception, dateTime);
  }

  /**
   * Builder class
   */
  public static class Builder {
    private FacebookPostRequest request;
    private String apiResponse;
    private Exception exception;
    private LocalDateTime dateTime;

    public Builder request(final FacebookPostRequest request) {
      this.request = request;
      return this;
    }

    public Builder apiResponse(final String apiResponse) {
      this.apiResponse = apiResponse;
      return this;
    }

    public Builder exception(final Exception exception) {
      this.exception = exception;
      return this;
    }

    public Builder dateTime(final LocalDateTime dateTime) {
      this.dateTime = dateTime;
      return this;
    }

    public FacebookPostResponse build() {
      return new FacebookPostResponse(request, apiResponse, dateTime, exception);
    }
  }
}
