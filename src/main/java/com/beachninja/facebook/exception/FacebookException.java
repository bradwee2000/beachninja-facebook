package com.beachninja.facebook.exception;

import com.beachninja.facebook.error.FacebookError;
import com.beachninja.facebook.error.FacebookErrorResponse;

/**
 * @author bradwee2000@gmail.com
 */
public class FacebookException extends RuntimeException {

  private final FacebookError facebookError;

  public FacebookException(final FacebookErrorResponse errorResponse) {
    this(errorResponse.getFacebookError());

  }

  public FacebookException(final FacebookError facebookError) {
    super(facebookError.toString());
    this.facebookError = facebookError;
  }

  public FacebookError getFacebookError() {
    return facebookError;
  }
}
