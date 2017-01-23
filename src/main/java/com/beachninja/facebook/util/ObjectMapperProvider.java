package com.beachninja.facebook.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.inject.Singleton;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class ObjectMapperProvider {

  private static ObjectMapper objectMapper;

  private static ObjectMapper create() {
    return new ObjectMapper()
        .registerModule(new JodaModule())
        .registerModule(new GuavaModule().configureAbsentsAsNulls(false));
  }

  public ObjectMapper get() {
    if (objectMapper == null) {
      objectMapper = create();
    }
    return objectMapper;
  }
}
