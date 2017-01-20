package com.beachninja.facebook.batch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BatchRequest {

  public static final Builder builder() {
    return new Builder();
  }

  @JsonProperty("access_token")
  private final String accessToken;

  @JsonProperty("batch")
  private final List<BatchItem> batchItems;

  /**
   * For Jackson serialization.
   */
  private BatchRequest() {
    this.accessToken = null;
    this.batchItems = null;
  }

  /**
   * Use Builder class to construct.
   * @param accessToken
   * @param batchItems
   */
  private BatchRequest(final String accessToken,
                       final Collection<BatchItem> batchItems) {
    this.accessToken = accessToken;
    this.batchItems = Lists.newArrayList(batchItems);
  }

  public String getAccessToken() {
    return accessToken;
  }

  public List<BatchItem> getBatchItems() {
    return batchItems;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || obj.getClass() != getClass() || !(obj instanceof BatchRequest)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    final BatchRequest rhs = (BatchRequest) obj;
    return Objects.equal(accessToken, rhs.accessToken)
        && Objects.equal(batchItems, rhs.batchItems);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(accessToken, batchItems);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("accessToken", accessToken)
        .add("batchItems", batchItems)
        .toString();
  }

  /**
   * Builder class
   */
  public static final class Builder {

    private String accessToken;
    private final List<BatchItem> batchItems = Lists.newArrayList();

    public Builder accessToken(final String accessToken) {
      this.accessToken = accessToken;
      return this;
    }

    public Builder addItem(final BatchItem batchItem) {
      batchItems.add(batchItem);
      return this;
    }

    public Builder addItems(final Collection<BatchItem> batchItems) {
      this.batchItems.addAll(batchItems);
      return this;
    }

    public Builder addItems(final BatchItem batchItem, final BatchItem ... more) {
      batchItems.addAll(Lists.asList(batchItem, more));
      return this;
    }

    public BatchRequest build() {
      return new BatchRequest(accessToken, batchItems);
    }
  }
}
