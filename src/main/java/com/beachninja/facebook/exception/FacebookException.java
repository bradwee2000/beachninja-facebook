package com.beachninja.facebook.exception;

import com.beachninja.facebook.error.FacebookError;
import com.beachninja.facebook.error.FacebookErrorResponse;

/**
 * @author bradwee2000@gmail.com
 */
public class FacebookException extends RuntimeException {

  private final FacebookError facebookError;

  public FacebookException(final FacebookErrorResponse errorResponse) {
    this(errorResponse.getMessage(), errorResponse.getFacebookError());
  }

  public FacebookException(final String message, final FacebookError facebookError) {
    super(message);
    this.facebookError = facebookError;
  }

  public FacebookError getFacebookError() {
    return facebookError;
  }
}
