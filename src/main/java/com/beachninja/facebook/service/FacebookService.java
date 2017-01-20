package com.beachninja.facebook.service;


import com.beachninja.common.json.ObjectMapperProvider;
import com.beachninja.facebook.batch.BatchRequest;
import com.beachninja.facebook.batch.BatchResponse;
import com.beachninja.facebook.error.FacebookErrorResponse;
import com.beachninja.facebook.exception.FacebookException;
import com.beachninja.facebook.post.FacebookPostRequest;
import com.beachninja.facebook.post.FacebookPostResponse;
import com.beachninja.facebook.scrape.FacebookScrapeRequest;
import com.beachninja.facebook.scrape.FacebookScrapeResponse;
import com.beachninja.urlfetch.UrlFetcher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.beachninja.facebook.util.FacebookConstants.*;

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
@Singleton
public class FacebookService {
  private static final Logger LOG = LoggerFactory.getLogger(FacebookService.class);

  private final UrlFetcher urlFetcher;
  private final ObjectMapper om;

  @Inject
  public FacebookService(final UrlFetcher urlFetcher,
                         final ObjectMapperProvider objectMapperProvider) {
    this.urlFetcher = urlFetcher;
    this.om = objectMapperProvider.get();
  }

  /**
   * Post on Facebook user / page's timeline.
   * @param request
   * @return FacebookPostResponse
   */
  public FacebookPostResponse post(final FacebookPostRequest request) {
    try {
      final HTTPResponse response = urlFetcher.connect(String.format(POST_URL, request.getFacebookId()))
          .data("title", request.getName().or(""))
          .data("message", request.getMessage().or(""))
          .data("link", request.getLink().or(""))
          .data("picture", request.getImageUrl().or(""))
          .data("description", request.getDescription().or(""))
          .data("access_token", request.getAccessToken())
          .post().get();
      assertSuccessfulResponse(response);
      final String apiResponse  = new String(response.getContent());
      return FacebookPostResponse.builder().request(request).apiResponse(apiResponse).build();
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

    final BatchResponse batchResponse = submitBatch(batchRequest);

    return FacebookScrapeResponse.builder().apiResponse(batchResponse).build();
  }

  /**
   * Submits a batch request to Facebook Graph API
   * @param request batch request
   * @return Facebook Graph API response
   * @throws JsonProcessingException
   * @throws ExecutionException
   * @throws InterruptedException
   */
  public BatchResponse submitBatch(final BatchRequest request) {
    try {
      final HTTPResponse response = urlFetcher.connect(BATCH_URL)
          .data("access_token", request.getAccessToken())
          .data("batch", URLEncoder.encode(om.writeValueAsString(request.getBatchItems()), CHARSET_UTF8))
          .post().get();

      assertSuccessfulResponse(response);

      final String apiResponse = new String(response.getContent(), CHARSET_UTF8);

      final List<BatchResponse> responses = om.readValue(apiResponse, new TypeReference<List<BatchResponse>>() {});
      return responses.isEmpty() ? new BatchResponse() : responses.get(0);
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void assertSuccessfulResponse(final HTTPResponse httpResponse) {
    try {
      if (httpResponse.getResponseCode() != 200) {
        final FacebookErrorResponse errorResponse = om.readValue(new String(httpResponse.getContent()),
            FacebookErrorResponse.class);
        throw new FacebookException(errorResponse);
      }
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }
}
