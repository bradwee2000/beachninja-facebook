package com.beachninja.facebook.response;

import com.beachninja.facebook.request.FacebookScrapeRequest;
import com.google.appengine.repackaged.com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.util.Map;

/**
 * This will contain the Facebook Scrape API response.
 *
 * @author bradwee2000@gmail.com
 */
public class FacebookScrapeResponse {

  public static Builder builder() {
    return new Builder();
  }

  private final FacebookScrapeRequest request;
  private final Map<String, String> results; // Api response per link

  /**
   * For jackson deserialization.
   */
  private FacebookScrapeResponse() {
    request = null;
    results = null;
  }

  /**
   * Use builder class to construct.
   * @param request
   * @param results
   */
  private FacebookScrapeResponse(final FacebookScrapeRequest request,
                                 final Map<String, String> results) {
    this.request = request;
    this.results = Maps.newHashMap(results);
  }

  public FacebookScrapeRequest getRequest() {
    return request;
  }

  /**
   * @return API response per link submitted.
   */
  public Map<String, String> getResults() {
    return results;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || obj.getClass() != getClass() || !(obj instanceof FacebookScrapeResponse)) {
      return false;
    }
    if(obj == this) {
      return true;
    }
    final FacebookScrapeResponse rhs = (FacebookScrapeResponse) obj;
    return Objects.equal(request, rhs.request)
        && Objects.equal(results, rhs.results);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(request, results);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("request", request)
        .add("results", results)
        .toString();
  }

  /**
   * Builder class
   */
  public static class Builder {

    private FacebookScrapeRequest request;

    private final Map<String, String> results = Maps.newHashMap();

    public Builder request(final FacebookScrapeRequest request) {
      this.request = request;
      return this;
    }
    public Builder addResult(final String link, String apiResponse) {
      results.put(link, apiResponse);
      return this;
    }
    public Builder addResult(final String link, final Exception e) {
      results.put(link, ExceptionUtils.getFullStackTrace(e));
      return this;
    }

    public FacebookScrapeResponse build() {
      return new FacebookScrapeResponse(request, results);
    }
  }
}
