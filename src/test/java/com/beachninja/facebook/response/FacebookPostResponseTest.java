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
      .request(request).apiResponse("Success").dateTime(now).exception(exception).build();
  private final FacebookPostResponse same = FacebookPostResponse.builder()
      .request(request).apiResponse("Success").dateTime(now).exception(exception).build();
  private final FacebookPostResponse diffRequest = FacebookPostResponse.builder()
      .request(null).apiResponse("Success").dateTime(now).exception(exception).build();
  private final FacebookPostResponse diffApiResponse = FacebookPostResponse.builder()
      .request(request).apiResponse("Fail").dateTime(now).exception(exception).build();
  private final FacebookPostResponse diffDateTime = FacebookPostResponse.builder()
      .request(request).apiResponse("Success").dateTime(now.plusDays(1)).exception(exception).build();
  private final FacebookPostResponse diffException = FacebookPostResponse.builder()
      .request(request).apiResponse("Success").dateTime(now).exception(null).build();

  @Test
  public void testBuild_shouldSetProperties() {
    assertThat(orig.getRequest()).isEqualTo(request);
    assertThat(orig.getApiResponse()).isEqualTo("Success");
    assertThat(orig.getDateTime()).isEqualTo(now);
    assertThat(orig.getException()).isEqualTo(exception);
  }

  @Test
  public void testEquals_shouldBeEqualIfAllPropertiesAreEqual() {
    assertThat(same).isEqualTo(orig);
    assertThat(orig).isEqualTo(same)
        .isNotEqualTo(diffRequest)
        .isNotEqualTo(diffApiResponse)
        .isNotEqualTo(diffDateTime)
        .isNotEqualTo(diffException);
  }

  @Test
  public void testHashcode_shouldHaveSameHashcodeIfAllPropertiesAreEqual() {
    assertThat(Sets.newHashSet(orig, same, diffRequest, diffApiResponse, diffDateTime, diffException))
        .containsExactlyInAnyOrder(orig, diffRequest, diffApiResponse, diffDateTime, diffException);
  }

  @Test
  public void testSerializeDeserialize_shouldReturnEqualObject() throws IOException {
    final ObjectMapper om = new ObjectMapperProvider().get();
    final FacebookPostRequest facebookPostRequest = FacebookPostRequest.builder().facebookId("test_id").build();
    final FacebookPostResponse facebookPostResponse = FacebookPostResponse.builder()
        .dateTime(now).request(facebookPostRequest).apiResponse("Api Response").build();

    // Serialize to JSON
    final String json = om.writeValueAsString(facebookPostResponse);

    // Deserialize JSON
    final FacebookPostResponse response = om.readValue(json, FacebookPostResponse.class);

    assertThat(response).isEqualTo(facebookPostResponse);
  }
}
