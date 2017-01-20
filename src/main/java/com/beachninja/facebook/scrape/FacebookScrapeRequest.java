package com.beachninja.facebook.scrape;

import com.beachninja.facebook.batch.BatchItem;
import com.google.appengine.repackaged.com.google.common.base.MoreObjects;
import com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonProperty;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

/**
 * @author bradwee2000@gmail.com
 */
public class FacebookScrapeRequest {

  public static Builder builder() {
    return new Builder();
  }

  @JsonProperty("accessToken")
  private final String accessToken; // User or page access token

  @JsonProperty("links")
  private final List<String> links; // List of links to scrape

  /**
   * For jackson deserialization.
   */
  private FacebookScrapeRequest() {
    accessToken = null;
    links = null;
  }

  /**
   * Use Builder class for constructing.
   * @param links
   */
  private FacebookScrapeRequest(final String accessToken,
                                final Collection<String> links) {
    this.accessToken = accessToken;
    this.links = Lists.newArrayList(links);
  }

  public String getAccessToken() {
    return accessToken;
  }

  public List<String> getLinks() {
    return Lists.newArrayList(links);
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || obj.getClass() != getClass() || !(obj instanceof FacebookScrapeRequest)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    final FacebookScrapeRequest rhs = (FacebookScrapeRequest) obj;
    return Objects.equal(accessToken, rhs.accessToken) &&
        Objects.equal(links, rhs.links);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(accessToken, links);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("link", links)
        .toString();
  }

  public List<BatchItem> toBatchItems() {
    final List<BatchItem> batchItems = Lists.newArrayList();
    for (final String link : links) {
      batchItems.add(BatchItem.builder().post().body(toUrlParams(link)).build());
    }
    return batchItems;
  }

  private String toUrlParams(final String link) {
    return new StringBuilder()
        .append("&id=").append(link)
        .append("&scrape=true")
        .toString();
  }

  /**
   * Builder class.
   */
  public static final class Builder {
    private String accessToken;
    private final List<String> links = Lists.newArrayList();

    public Builder accessToken(final String accessToken) {
      this.accessToken = accessToken;
      return this;
    }

    public Builder addLink(final String link) {
      this.links.add(link);
      return this;
    }

    public Builder addLinks(final Collection<String> links) {
      this.links.addAll(links);
      return this;
    }

    public Builder addLinks(final String links, final String ... more) {
      this.links.addAll(Lists.asList(links, more));
      return this;
    }

    public FacebookScrapeRequest build() {
      return new FacebookScrapeRequest(accessToken, links);
    }
  }
}
