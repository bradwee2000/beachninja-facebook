package com.beachninja.facebook.service;

import com.beachninja.facebook.batch.BatchRequest;
import com.beachninja.facebook.batch.BatchResponse;
import com.beachninja.facebook.error.FacebookErrorResponse;
import com.beachninja.facebook.exception.FacebookException;
import com.beachninja.facebook.model.Website;
import com.beachninja.facebook.post.FacebookPostRequest;
import com.beachninja.facebook.post.FacebookPostResponse;
import com.beachninja.facebook.scrape.FacebookScrapeRequest;
import com.beachninja.facebook.scrape.FacebookScrapeResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.beachninja.facebook.util.FacebookConstants.BATCH_URL;
import static com.beachninja.facebook.util.FacebookConstants.CHARSET_UTF8;
import static com.beachninja.facebook.util.FacebookConstants.POST_URL;

/**
 * Service to post on Facebook account time line.
 *
 * This API requires Facebook long-lived access token, which expires within 60 days of no use.
 *
 * To request for long-lived access tokens:
 *
 * 1. Request user_access_token via https://developers.facebook.com/tools/explorer
 *
 * 2. Submit GET request to ninjalottopcso?fields=access_token to get page_access_token
 *
 * 3. Exchange page_access_token for long-lived access token via:
 * curl -X GET "https://graph.facebook.com/oauth/access_token?grant_type=fb_exchange_token
 * &client_id={CLIENT_ID}&client_secret={CLIENT_SECRET}&fb_exchange_token={PAGE_ACCESS_TOKEN}
 *
 * 4. To debug a token:
 * https://developers.facebook.com/tools/debug/
 *
 * @author bradwee2000@gmail.com
 */
public class FacebookService {
  private static final Logger LOG = LoggerFactory.getLogger(FacebookService.class);

  private final ObjectMapper om;

  public FacebookService(final ObjectMapper om) {
    this.om = om;
  }

  /**
   * Post on Facebook user / page's timeline.
   * @param request
   * @return FacebookPostResponse
   */
  public FacebookPostResponse post(final FacebookPostRequest request) {
    try {
      final HttpResponse response = Request.Post(String.format(POST_URL, request.getFacebookId()))
              .bodyForm(Form.form()
                      .add("title", request.getName().orElse(""))
                      .add("message", request.getMessage().orElse(""))
                      .add("link", request.getLink().orElse(""))
                      .add("picture", request.getImageUrl().orElse(""))
                      .add("description", request.getDescription().orElse(""))
                      .add("access_token", request.getAccessToken())
                      .build())
              .execute()
              .returnResponse();

      assertSuccessfulResponse(response);
      final String apiResponse  = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
      return om.readValue(apiResponse, FacebookPostResponse.class);
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Request Facebook to scrape URLs. This forces Facebook to update its cache on the
   * URL metadata (e.g. title, description, photo, etc.)
   *
   * @param request
   * @return FacebookScrapeResponse
   */
  public FacebookScrapeResponse scrape(final FacebookScrapeRequest request) {
    final BatchRequest batchRequest = BatchRequest.builder()
        .accessToken(request.getAccessToken())
        .addItems(request.toBatchItems())
        .build();

    LOG.debug("Batch Scrape Request: {}", batchRequest);

    final List<BatchResponse> batchResponses = submitBatch(batchRequest);

    final FacebookScrapeResponse.Builder scrapeResponseBuilder = FacebookScrapeResponse.builder();
    try {
      for (final BatchResponse batchResponse : batchResponses) {
        LOG.debug("Batch Scrape Response: {}", batchResponse);

        if (batchResponse.getCode() == 200)   {
          scrapeResponseBuilder.addWebsite(om.readValue(batchResponse.getBody(), Website.class));
        } else {
          final FacebookErrorResponse errorResponse = om.readValue(batchResponse.getBody(), FacebookErrorResponse.class);
          scrapeResponseBuilder.addError(errorResponse.getFacebookError());
        }
      }
      return scrapeResponseBuilder.build();
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Submits a batch request to Facebook Graph API
   * @param request batch request
   * @return Facebook Graph API response
   * @throws JsonProcessingException
   * @throws ExecutionException
   * @throws InterruptedException
   */
  public List<BatchResponse> submitBatch(final BatchRequest request) {
    try {
      final HttpResponse response = Request.Post(BATCH_URL)
              .bodyForm(Form.form()
                      .add("access_token", request.getAccessToken())
                      .add("batch", URLEncoder.encode(om.writeValueAsString(request.getBatchItems()), CHARSET_UTF8))
                      .build()
              ).execute()
              .returnResponse();

      assertSuccessfulResponse(response);

      final String apiResponse = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);

      return om.readValue(apiResponse, new TypeReference<List<BatchResponse>>() {});
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void assertSuccessfulResponse(final HttpResponse response) {
    try {
      if (response.getStatusLine().getStatusCode() != 200) {
        final InputStream is = response.getEntity().getContent();
        final FacebookErrorResponse errorResponse = om.readValue(IOUtils.toString(is, Charsets.UTF_8),
            FacebookErrorResponse.class);
        throw new FacebookException(errorResponse);
      }
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }
}
