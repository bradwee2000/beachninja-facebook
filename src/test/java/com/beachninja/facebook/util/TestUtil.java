package com.beachninja.facebook.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;

/**
 * @author bradwee2000@gmail.com
 */
public class TestUtil {

  private static ObjectMapper OM_INSTANCE;

  public static ObjectMapper om() {
    if (OM_INSTANCE == null) {
      OM_INSTANCE = new ObjectMapper()
          .registerModule(new JodaModule())
          .registerModule(new GuavaModule().configureAbsentsAsNulls(false));
    }
    return OM_INSTANCE;
  }
}
