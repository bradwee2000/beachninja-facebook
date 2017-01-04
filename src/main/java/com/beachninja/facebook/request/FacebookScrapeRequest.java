package com.beachninja.facebook.request;

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

  @JsonProperty("links")
  private final List<String> links;

  /**
   * For jackson deserialization.
   */
  private FacebookScrapeRequest() {
    links = null;
  }

  /**
   * Use Builder class for constructing.
   * @param links
   */
  private FacebookScrapeRequest(final Collection<String> links) {
    this.links = Lists.newArrayList(links);
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
    return Objects.equal(links, rhs.links);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(links);
  }


  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("link", links)
        .toString();
  }

  /**
   * Builder class.
   */
  public static final class Builder {
    private final List<String> links = Lists.newArrayList();

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
      return new FacebookScrapeRequest(links);
    }
  }
}
