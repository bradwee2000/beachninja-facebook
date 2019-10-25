package com.beachninja.facebook.util;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author bradwee2000@gmail.com
 */
public class TestUtil {

  private static ObjectMapper OM_INSTANCE;

  public static ObjectMapper om() {
    if (OM_INSTANCE == null) {
      OM_INSTANCE = new ObjectMapper();
    }
    return OM_INSTANCE;
  }
}
