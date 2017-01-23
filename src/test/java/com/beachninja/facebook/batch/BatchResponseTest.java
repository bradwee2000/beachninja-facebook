package com.beachninja.facebook.batch;

import com.beachninja.facebook.model.Header;
import com.beachninja.facebook.util.TestUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bradwee2000@gmail.com
 */
public class BatchResponseTest {
  private static final Logger LOG = LoggerFactory.getLogger(BatchResponseTest.class);
  private static final ObjectMapper om = TestUtil.om();

  @Test
  public void testDeserialize_shouldConstructObjectFromJson() throws IOException {
    final String json = IOUtils.toString(getClass().getResourceAsStream("/sample-batch-response.json"), "UTF-8");
    final List<BatchResponse> responses = om.readValue(json, new TypeReference<List<BatchResponse>>() {});

    assertThat(responses).hasSize(1);

    final BatchResponse batchResponse = responses.get(0);

    assertThat(batchResponse.getCode()).isEqualTo(200);
    assertThat(batchResponse.getHeaders()).hasSize(7)
        .contains(new Header("Access-Control-Allow-Origin", "*"))
        .contains(new Header("Content-Type", "text/javascript; charset=UTF-8"));
    assertThat(batchResponse.getBody())
        .contains("https://www.google.com")
        .contains("Search the world's information, including webpages, images, videos and more.");
  }
}
