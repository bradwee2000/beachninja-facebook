package com.beachninja.facebook.response;

import com.beachninja.common.json.ObjectMapperProvider;
import com.beachninja.facebook.scrape.FacebookScrapeRequest;
import com.beachninja.facebook.scrape.FacebookScrapeResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * @author bradwee2000@gmail.com
 */
public class FacebookScrapeResponseTest {

  private final FacebookScrapeRequest request = mock(FacebookScrapeRequest.class);
  private final Exception exception = new Exception("Error Msg");

  private final FacebookScrapeResponse orig = FacebookScrapeResponse.builder()
      .addResult("link1", "Success").build();
  private final FacebookScrapeResponse same = FacebookScrapeResponse.builder()
      .addResult("link1", "Success").build();
  private final FacebookScrapeResponse diffResult = FacebookScrapeResponse.builder()
      .addResult("link1", "Diff Response").build();

  @Test
  public void testBuild_shouldSetProperties() {
    assertThat(orig.getResults()).containsOnlyKeys("link1").containsValue("Success");
  }

  @Test
  public void testEquals_shouldBeEqualIfAllPropertiesAreEqual() {
    assertThat(same).isEqualTo(orig);
    assertThat(orig).isEqualTo(orig).isEqualTo(same).isNotEqualTo(diffResult);
  }

  @Test
  public void testHashcode_shouldHaveSameHashcodeIfAllPropertiesAreEqual() {
    assertThat(Sets.newHashSet(orig, same, diffResult))
        .containsExactlyInAnyOrder(orig, diffResult);
  }

  @Test
  public void testSerializeDeserialize_shouldReturnEqualObject() throws IOException {
    final FacebookScrapeRequest request = FacebookScrapeRequest.builder().build();
    final FacebookScrapeResponse response = FacebookScrapeResponse.builder().addResult("link1", "Success").build();

    final ObjectMapper om = new ObjectMapperProvider().get();
    final String json = om.writeValueAsString(response);
    final FacebookScrapeResponse deserialized = om.readValue(json, FacebookScrapeResponse.class);

    assertThat(deserialized).isEqualTo(response);
  }
}
