package com.beachninja.facebook.response;

import com.beachninja.facebook.util.TestUtil;
import com.beachninja.facebook.error.FacebookError;
import com.beachninja.facebook.model.Website;
import com.beachninja.facebook.scrape.FacebookScrapeResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bradwee2000@gmail.com
 */
public class FacebookScrapeResponseTest {
  private final FacebookScrapeResponse orig = FacebookScrapeResponse.builder()
      .addWebsite(Website.builder().url("url1").build()).build();
  private final FacebookScrapeResponse equal = FacebookScrapeResponse.builder()
      .addWebsite(Website.builder().url("url1").build()).build();
  private final FacebookScrapeResponse diffWebsite = FacebookScrapeResponse.builder()
      .addWebsite(Website.builder().url("url2").build()).build();
  private final FacebookScrapeResponse diffError = FacebookScrapeResponse.builder()
      .addWebsite(Website.builder().url("url1").build())
      .addError(FacebookError.builder().code(100).build())
      .build();

  @Test
  public void testBuild_shouldSetProperties() {
    assertThat(orig.getWebsites()).containsExactly(Website.builder().url("url1").build());
    assertThat(diffError.getErrors()).containsExactly(FacebookError.builder().code(100).build());
  }

  @Test
  public void testEquals_shouldBeEqualIfAllPropertiesAreEqual() {
    assertThat(equal).isEqualTo(orig);
    assertThat(orig).isEqualTo(orig).isEqualTo(equal)
        .isNotEqualTo(null)
        .isNotEqualTo("")
        .isNotEqualTo(diffWebsite)
        .isNotEqualTo(diffError);
  }

  @Test
  public void testHashcode_shouldHaveSameHashcodeIfAllPropertiesAreEqual() {
    assertThat(Sets.newHashSet(orig, equal, diffWebsite, diffError))
        .containsExactlyInAnyOrder(orig, diffWebsite, diffError);
  }

  @Test
  public void testSerializeDeserialize_shouldReturnEqualObject() throws IOException {
    final FacebookScrapeResponse response = FacebookScrapeResponse.builder()
        .addWebsite(Website.builder().url("url1").build())
        .addError(FacebookError.builder().code(100).build())
        .build();

    final ObjectMapper om = TestUtil.om();
    final String json = om.writeValueAsString(response);
    final FacebookScrapeResponse deserialized = om.readValue(json, FacebookScrapeResponse.class);

    assertThat(deserialized).isEqualTo(response);
  }
}
