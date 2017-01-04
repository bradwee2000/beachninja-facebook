package com.beachninja.facebook.response;

import com.beachninja.common.json.ObjectMapperProvider;
import com.beachninja.facebook.request.FacebookScrapeRequest;
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
      .request(request).addResult("link1", "Success").addResult("link2", exception).build();
  private final FacebookScrapeResponse same = FacebookScrapeResponse.builder()
      .request(request).addResult("link1", "Success").addResult("link2", exception).build();
  private final FacebookScrapeResponse diffRequest = FacebookScrapeResponse.builder()
      .request(null).addResult("link1", "Success").addResult("link2", exception).build();
  private final FacebookScrapeResponse diffResult = FacebookScrapeResponse.builder()
      .request(request).addResult("link1", "Diff Response").build();

  @Test
  public void testBuild_shouldSetProperties() {
    assertThat(orig.getRequest()).isEqualTo(request);
    assertThat(orig.getResults()).containsOnlyKeys("link1", "link2")
        .containsValue("Success");
    assertThat(orig.getResults().get("link2")).contains("Error Msg");
  }

  @Test
  public void testEquals_shouldBeEqualIfAllPropertiesAreEqual() {
    assertThat(same).isEqualTo(orig);
    assertThat(orig).isEqualTo(orig).isEqualTo(same)
        .isNotEqualTo(diffRequest).isNotEqualTo(diffResult);
  }

  @Test
  public void testHashcode_shouldHaveSameHashcodeIfAllPropertiesAreEqual() {
    assertThat(Sets.newHashSet(orig, same, diffRequest, diffResult))
        .containsExactlyInAnyOrder(orig, diffRequest, diffResult);
  }

  @Test
  public void testSerializeDeserialize_shouldReturnEqualObject() throws IOException {
    final FacebookScrapeRequest request = FacebookScrapeRequest.builder().build();
    final FacebookScrapeResponse response = FacebookScrapeResponse.builder()
        .request(request).addResult("link1", "Success").addResult("link2", exception).build();

    final ObjectMapper om = new ObjectMapperProvider().get();
    final String json = om.writeValueAsString(response);
    final FacebookScrapeResponse deserialized = om.readValue(json, FacebookScrapeResponse.class);

    assertThat(deserialized).isEqualTo(response);
  }
}
