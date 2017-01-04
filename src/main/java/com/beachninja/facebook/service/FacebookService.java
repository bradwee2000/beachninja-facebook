package com.beachninja.facebook.service;


import com.beachninja.facebook.annotation.FacebookAccessToken;
import com.beachninja.facebook.request.FacebookPostRequest;
import com.beachninja.facebook.request.FacebookScrapeRequest;
import com.beachninja.facebook.response.FacebookPostResponse;
import com.beachninja.facebook.response.FacebookScrapeResponse;
import com.beachninja.urlfetch.UrlFetcher;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;

import static com.beachninja.facebook.util.FacebookConstants.POST_URL;
import static com.beachninja.facebook.util.FacebookConstants.SCRAPE_URL;

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
  private final String accessToken;

  @Inject
  public FacebookService(final UrlFetcher urlFetcher,
                         @FacebookAccessToken final String accessToken) {
    this.urlFetcher = urlFetcher;
    this.accessToken = accessToken;
  }

  /**
   * Post on Facebook user / page's timeline.
   * @param request
   * @return FacebookPostResponse
   */
  public FacebookPostResponse post(final FacebookPostRequest request) {
    try {
      final HTTPResponse httpResponse = urlFetcher.connect(String.format(POST_URL, request.getFacebookId()))
          .data("title", request.getTitle().or(""))
          .data("message", request.getMessage().or(""))
          .data("link", request.getLink().or(""))
          .data("picture", request.getImageUrl().or(""))
          .data("description", request.getDescription().or(""))
          .data("access_token", accessToken)
          .post().get();
      final String apiResponse  = new String(httpResponse.getContent());

      return FacebookPostResponse.builder().request(request).apiResponse(apiResponse).build();
    } catch (final Exception e) {
      return FacebookPostResponse.fail(e);
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
    final FacebookScrapeResponse.Builder responseBuilder = FacebookScrapeResponse.builder().request(request);
    for (final String link : request.getLinks()) {
      try {
        responseBuilder.addResult(link, scrapeUrl(link));
      } catch (final Exception e) {
        responseBuilder.addResult(link, e);
      }
    }
    return responseBuilder.build();
  }

  /**
   * Request Facebook to scrape URLs. This forces Facebook to update its cache on the
   * URL metadata (e.g. title, description, photo, etc.)
   *
   * @param url the URL to scrape
   * @return Facebook Scrape API response.
   * @throws MalformedURLException
   * @throws ExecutionException
   * @throws InterruptedException
   */
  private String scrapeUrl(final String url) throws MalformedURLException, ExecutionException, InterruptedException {
    final HTTPResponse response = urlFetcher.connect(SCRAPE_URL)
        .data("id", url)
        .data("scrape", "true")
        .data("access_token", accessToken)
        .post().get();
    return new String(response.getContent());
  }
}
