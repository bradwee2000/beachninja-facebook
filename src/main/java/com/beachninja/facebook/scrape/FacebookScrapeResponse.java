package com.beachninja.facebook.scrape;

import com.beachninja.facebook.batch.BatchResponse;
import com.google.appengine.repackaged.com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Maps;

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

  private final BatchResponse apiResponse;
  private final Map<String, String> results; // Api response per link

  /**
   * For jackson deserialization.
   */
  private FacebookScrapeResponse() {
    results = null;
    apiResponse = null;
  }

  /**
   * Use builder class to construct.
   * @param apiResponse
   * @param results
   */
  private FacebookScrapeResponse(final BatchResponse apiResponse,
                                 final Map<String, String> results) {
    this.apiResponse = apiResponse;
    this.results = Maps.newHashMap(results);
  }

  public BatchResponse getApiResponse() {
    return apiResponse;
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
    return Objects.equal(apiResponse, rhs.apiResponse)
        && Objects.equal(results, rhs.results);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(results, apiResponse);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("apiResponse", apiResponse)
        .add("results", results)
        .toString();
  }

  /**
   * Builder class
   */
  public static class Builder {

    private BatchResponse apiResponse;

    private final Map<String, String> results = Maps.newHashMap();

    public Builder apiResponse(final BatchResponse apiResponse) {
      this.apiResponse = apiResponse;
      return this;
    }

    public Builder addResult(final String link, String apiResponse) {
      results.put(link, apiResponse);
      return this;
    }

    public FacebookScrapeResponse build() {
      return new FacebookScrapeResponse(apiResponse, results);
    }
  }
}
