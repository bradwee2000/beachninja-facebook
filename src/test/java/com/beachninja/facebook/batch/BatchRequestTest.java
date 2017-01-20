package com.beachninja.facebook.batch;

import com.beachninja.common.json.ObjectMapperProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bradwee2000@gmail.com
 */
public class BatchRequestTest {

  private final BatchItem batchItem = BatchItem.builder().post().relativeUrl("/test").body("message=hello").build();

  private final BatchRequest orig = BatchRequest.builder().accessToken("token").addItem(batchItem).build();
  private final BatchRequest same = BatchRequest.builder().accessToken("token").addItem(batchItem).build();
  private final BatchRequest diffToken = BatchRequest.builder().accessToken("diffToken").addItem(batchItem).build();
  private final BatchRequest diffItems = BatchRequest.builder()
      .accessToken("token").addItems(batchItem, batchItem).build();

  @Test
  public void testBuild_shouldSetProperties() {
    assertThat(orig.getAccessToken()).isEqualTo("token");
    assertThat(orig.getBatchItems()).containsExactly(batchItem);
  }

  @Test
  public void testEquals_shouldBeEqualIfAllPropertiesAreEqual() {
    assertThat(same).isEqualTo(orig);
    assertThat(orig).isEqualTo(orig).isEqualTo(same)
        .isNotEqualTo(diffToken).isNotEqualTo(diffItems);
  }

  @Test
  public void testHashcode_shouldHaveEqualHashcodeIfAllPropertiesAreEqual() {
    assertThat(Sets.newHashSet(orig, same, diffToken, diffItems))
        .containsOnly(orig, diffToken, diffItems);
  }

  @Test
  public void testSerializeDeserialize_shouldReturnEqualObject() throws IOException {
    final ObjectMapper om = new ObjectMapperProvider().get();
    final String json = om.writeValueAsString(orig);
    final BatchRequest batchRequest = om.readValue(json, BatchRequest.class);
    assertThat(batchRequest).isEqualTo(orig);
  }
}
