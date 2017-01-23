package com.beachninja.facebook.scrape;

import com.beachninja.facebook.error.FacebookError;
import com.beachninja.facebook.model.Website;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.appengine.repackaged.com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

/**
 * This will contain the Facebook Scrape API response.
 *
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FacebookScrapeResponse {

  public static final Builder builder() {
    return new Builder();
  }

  @JsonProperty("websites")
  private final List<Website> websites;

  @JsonProperty("errors")
  private final List<FacebookError> errors;

  public FacebookScrapeResponse(@JsonProperty("websites") final List<Website> websites,
                                @JsonProperty("errors") final List<FacebookError> errors) {
    this.websites = websites;
    this.errors = errors;
  }

  public List<Website> getWebsites() {
    return websites;
  }

  public List<FacebookError> getErrors() {
    return errors;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || obj.getClass() != getClass() || !(obj instanceof FacebookScrapeResponse)) {
      return false;
    }
    if(obj == this) {
      return true;
    }
    final FacebookScrapeResponse rhs = (FacebookScrapeResponse) obj;
    return Objects.equal(websites, rhs.websites)
        && Objects.equal(errors, rhs.errors);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(websites, errors);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("websites", websites).toString();
  }

  public static final class Builder {
    private final List<Website> websites = Lists.newArrayList();
    private final List<FacebookError> errors = Lists.newArrayList();

    public Builder addWebsite(final Website website) {
      websites.add(website);
      return this;
    }

    public Builder addWebsites(final Collection<Website> websites) {
      websites.addAll(websites);
      return this;
    }

    public Builder addWebsites(final Website website, final Website ... more) {
      websites.addAll(Lists.asList(website, more));
      return this;
    }

    public Builder addError(final FacebookError error) {
      errors.add(error);
      return this;
    }

    public FacebookScrapeResponse build() {
      return new FacebookScrapeResponse(websites, errors);
    }
  }
}
