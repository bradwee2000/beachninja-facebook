package com.beachninja.facebook.batch;

import com.beachninja.facebook.util.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bradwee2000@gmail.com
 */
public class BatchItemTest {

  private final BatchItem orig = BatchItem.builder().post().relativeUrl("/test").body("message=hello").build();
  private final BatchItem same = BatchItem.builder().post().relativeUrl("/test").body("message=hello").build();
  private final BatchItem diffMethod = BatchItem.builder().get().relativeUrl("/test").body("message=hello").build();
  private final BatchItem diffUrl = BatchItem.builder().post().relativeUrl("/diff").body("message=hello").build();
  private final BatchItem diffBody = BatchItem.builder().post().relativeUrl("/test").body("message=diff").build();

  @Test
  public void testBuild_shouldSetProperties() {
    assertThat(orig.getMethod()).isEqualTo("POST");
    assertThat(orig.getRelativeUrl()).isEqualTo("/test");
    assertThat(orig.getBody()).isEqualTo("message=hello");

    assertThat(diffMethod.getMethod()).isEqualTo("GET");
  }

  @Test
  public void testEquals_shouldBeEqualIfAllPropertiesAreEqual() {
    assertThat(same).isEqualTo(orig);
    assertThat(orig).isEqualTo(orig).isEqualTo(same)
        .isNotEqualTo(diffMethod).isNotEqualTo(diffUrl).isNotEqualTo(diffBody);
  }

  @Test
  public void testHashcode_shouldHaveEqualHashcodeIfAllPropertiesAreEqual() {
    assertThat(Sets.newHashSet(orig, same, diffMethod, diffUrl, diffBody))
        .containsOnly(orig, diffMethod, diffUrl, diffBody);
  }

  @Test
  public void testSerializeDeserialize_shouldReturnEqualObject() throws IOException {
    final ObjectMapper om = TestUtil.om();
    final String json = om.writeValueAsString(orig);
    final BatchItem batchItem = om.readValue(json, BatchItem.class);
    assertThat(batchItem).isEqualTo(orig);
  }
}
