package com.beachninja.facebook.error;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FacebookErrorResponse {

  @JsonProperty("error")
  private FacebookError facebookError;

  public FacebookError getFacebookError() {
    return facebookError;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("error", facebookError)
        .toString();
  }

  public void setFacebookError(FacebookError facebookError) {
    this.facebookError = facebookError;
  }
}
