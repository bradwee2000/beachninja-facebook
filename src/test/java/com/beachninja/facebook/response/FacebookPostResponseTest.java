package com.beachninja.facebook.response;

import com.beachninja.common.json.ObjectMapperProvider;
import com.beachninja.facebook.post.FacebookPostRequest;
import com.beachninja.facebook.post.FacebookPostResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * @author bradwee2000@gmail.com
 */
public class FacebookPostResponseTest {
  private static final Logger LOG = LoggerFactory.getLogger(FacebookPostResponseTest.class);

  private final LocalDateTime now = new LocalDateTime(2016, 2, 1, 12, 13, 45);
  private final FacebookPostRequest request = mock(FacebookPostRequest.class);
  private final Exception exception = mock(Exception.class);

  private final FacebookPostResponse orig = FacebookPostResponse.builder()
      .id("response_id").build();
  private final FacebookPostResponse equal = FacebookPostResponse.builder()
      .id("response_id").build();
  private final FacebookPostResponse diffId = FacebookPostResponse.builder()
      .id("diff_response_id").build();

  @Test
  public void testBuild_shouldSetProperties() {
    assertThat(orig.getId()).isEqualTo("response_id");
  }

  @Test
  public void testEquals_shouldBeEqualIfAllPropertiesAreEqual() {
    assertThat(equal).isEqualTo(orig);
    assertThat(orig).isEqualTo(equal).isEqualTo(orig)
        .isNotEqualTo(null)
        .isNotEqualTo("")
        .isNotEqualTo(diffId);
  }

  @Test
  public void testHashcode_shouldHaveSameHashcodeIfAllPropertiesAreEqual() {
    assertThat(Sets.newHashSet(orig, equal, diffId))
        .containsExactlyInAnyOrder(orig, diffId);
  }

  @Test
  public void testSerializeDeserialize_shouldReturnEqualObject() throws IOException {
    final ObjectMapper om = new ObjectMapperProvider().get();
    final FacebookPostResponse facebookPostResponse = FacebookPostResponse.builder().id("sampleId").build();

    // Serialize to JSON
    final String json = om.writeValueAsString(facebookPostResponse);

    // Deserialize JSON
    final FacebookPostResponse response = om.readValue(json, FacebookPostResponse.class);

    assertThat(response).isEqualTo(facebookPostResponse);
  }
}
