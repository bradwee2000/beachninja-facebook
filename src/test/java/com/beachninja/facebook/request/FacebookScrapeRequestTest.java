package com.beachninja.facebook.request;

import com.beachninja.common.json.ObjectMapperProvider;
import com.beachninja.facebook.scrape.FacebookScrapeRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bradwee2000@gmail.com
 */
public class FacebookScrapeRequestTest {

  private FacebookScrapeRequest orig = FacebookScrapeRequest.builder()
      .addLink("link1").addLink("link2").build();
  private FacebookScrapeRequest same = FacebookScrapeRequest.builder()
      .addLinks("link1", "link2").build();
  private FacebookScrapeRequest same2 = FacebookScrapeRequest.builder()
      .addLinks(Lists.newArrayList("link1", "link2")).build();
  private FacebookScrapeRequest diffLink = FacebookScrapeRequest.builder()
      .addLink("link1").addLink("diffLink").build();

  @Test
  public void testBuild_shouldSetParameters() {
    assertThat(orig.getLinks()).containsExactly("link1", "link2");
  }

  @Test
  public void testEquals_shouldBeEqualIfAllPropertiesAreEqual() {
    assertThat(same).isEqualTo(same2).isEqualTo(orig);
    assertThat(orig).isEqualTo(same).isEqualTo(same2).isNotEqualTo(diffLink);
  }

  @Test
  public void testHashcode_shouldHaveSameHashcodeIfAllPropertiesAreEqual() {
    assertThat(Sets.newHashSet(orig, same, same2, diffLink)).containsExactlyInAnyOrder(orig, diffLink);
  }

  @Test
  public void testSerializeDeserialize_shouldReturnEqualObject() throws IOException {
    final ObjectMapper om = new ObjectMapperProvider().get();
    final String json = om.writeValueAsString(orig);
    final FacebookScrapeRequest request = om.readValue(json, FacebookScrapeRequest.class);
    assertThat(request).isEqualTo(orig);
  }
}
