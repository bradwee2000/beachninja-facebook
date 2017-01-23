package com.beachninja.facebook.request;

import com.beachninja.facebook.batch.BatchItem;
import com.beachninja.facebook.post.FacebookPostRequest;
import com.beachninja.facebook.util.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bradwee2000@gmail.com
 */
public class FacebookPostRequestTest {
  private static final Logger LOG = LoggerFactory.getLogger(FacebookPostRequestTest.class);

  private final FacebookPostRequest orig = FacebookPostRequest.builder().facebookId("id")
      .title("title").message("message").description("description").imageUrl("imgurl").link("link").build();
  private final FacebookPostRequest same = FacebookPostRequest.builder().facebookId("id")
      .title("title").message("message").description("description").imageUrl("imgurl").link("link").build();
  private final FacebookPostRequest diffId = FacebookPostRequest.builder().facebookId("diffId")
      .title("title").message("message").description("description").imageUrl("imgurl").link("link").build();
  private final FacebookPostRequest diffTitle = FacebookPostRequest.builder().facebookId("id")
      .title("DIFF").message("message").description("description").imageUrl("imgurl").link("link").build();
  private final FacebookPostRequest diffMessage = FacebookPostRequest.builder().facebookId("id")
      .title("title").message("DIFF").description("description").imageUrl("imgurl").link("link").build();
  private final FacebookPostRequest diffDesc = FacebookPostRequest.builder().facebookId("id")
      .title("title").message("message").description("DIFF").imageUrl("imgurl").link("link").build();
  private final FacebookPostRequest diffImgUrl = FacebookPostRequest.builder().facebookId("id")
      .title("title").message("message").description("description").imageUrl("DIFF").link("link").build();
  private final FacebookPostRequest diffLink = FacebookPostRequest.builder().facebookId("id")
      .title("title").message("message").description("description").imageUrl("imgurl").link("DIFF").build();

  @Test
  public void testBuild_shouldSetProperties() {
    assertThat(orig.getFacebookId()).isEqualTo("id");
    assertThat(orig.getName().get()).isEqualTo("title");
    assertThat(orig.getMessage().get()).isEqualTo("message");
    assertThat(orig.getDescription().get()).isEqualTo("description");
    assertThat(orig.getLink().get()).isEqualTo("link");
    assertThat(orig.getImageUrl().get()).isEqualTo("imgurl");
    assertThat(orig.isPublished()).isTrue();
    assertThat(orig.getScheduledPublishTimeEpoch()).isEqualTo(0);
  }

  @Test
  public void testToUrlParams_shouldReturnAttributesAsUrlParameters() {
    assertThat(orig.toUrlParams())
        .contains("&name=title")
        .contains("&message=message")
        .contains("&link=link")
        .contains("&picture=imgurl")
        .contains("&description=description");
  }

  @Test
  public void testToBatchItem_shouldReturnBatchItem() {
    final BatchItem batchItem = orig.toBatchItem();
    assertThat(batchItem.getMethod()).isEqualTo("POST");
    assertThat(batchItem.getRelativeUrl()).isEqualTo("id/feed");
    assertThat(batchItem.getBody())
        .contains("&name=title")
        .contains("&message=message")
        .contains("&link=link")
        .contains("&picture=imgurl")
        .contains("&description=description");
  }

  @Test
  public void testEquals_shouldBeEqualIfAllPropertiesAreEqual() {
    assertThat(same).isEqualTo(orig);
    assertThat(orig).isEqualTo(orig).isEqualTo(same)
        .isNotEqualTo(diffId).isNotEqualTo(diffTitle).isNotEqualTo(diffMessage)
        .isNotEqualTo(diffDesc).isNotEqualTo(diffLink).isNotEqualTo(diffImgUrl);
  }

  @Test
  public void testHashcode_shouldHaveEqualHashcodeIfAllPropertiesAreEqual() {
    assertThat(Sets.newHashSet(orig, same, diffId, diffTitle, diffMessage, diffDesc, diffLink, diffImgUrl))
        .containsExactlyInAnyOrder(orig, diffId, diffTitle, diffMessage, diffDesc, diffLink, diffImgUrl);
  }

  @Test
  public void testSerializeDeserialize_shouldReturnEqualObject() throws IOException {
    final ObjectMapper om = TestUtil.om();
    final String json = om.writeValueAsString(orig);
    final FacebookPostRequest request = om.readValue(json, FacebookPostRequest.class);

    assertThat(request).isEqualTo(orig);
  }

  @Test
  public void testDeserializeWithNullValues_shouldConvertNullToOptionalAbsent() throws IOException {
    final ObjectMapper om = TestUtil.om();
    final String json = "{\"facebookId\":\"id\", \"name\":null, \"message\":null, \"link\":null," +
        "\"imageUrl\":null, \"description\":null}";

    final FacebookPostRequest request = om.readValue(json, FacebookPostRequest.class);
    assertThat(request).isEqualTo(FacebookPostRequest.builder().facebookId("id").build());
  }

  @Test
  public void testScheduledPostRequest_shouldSetPublishToFalse() {
    final DateTime publishDate = new DateTime(2016, 2, 1, 12, 13, 45, DateTimeZone.UTC);
    final FacebookPostRequest request = FacebookPostRequest.builder().schedulePublish(publishDate).build();

    assertThat(request.isPublished()).isFalse();
    assertThat(request.getScheduledPublishTimeEpoch()).isEqualTo(1454328825);
  }
}
