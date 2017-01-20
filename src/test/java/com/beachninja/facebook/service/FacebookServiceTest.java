package com.beachninja.facebook.service;

import com.beachninja.common.json.ObjectMapperProvider;
import com.beachninja.facebook.batch.BatchItem;
import com.beachninja.facebook.batch.BatchRequest;
import com.beachninja.facebook.batch.BatchResponse;
import com.beachninja.facebook.post.FacebookPostRequest;
import com.beachninja.facebook.scrape.FacebookScrapeRequest;
import com.beachninja.facebook.scrape.FacebookScrapeResponse;
import com.beachninja.urlfetch.UrlFetchBuilder;
import com.beachninja.urlfetch.UrlFetcher;
import com.google.appengine.api.urlfetch.HTTPResponse;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author bradwee2000@gmail.com
 */
public class FacebookServiceTest {

  private Future<HTTPResponse> futureResponse;
  private HTTPResponse httpResponse;

  private UrlFetcher urlFetcher;
  private UrlFetchBuilder urlFetchBuilder;

  private FacebookService facebookService;

  @Before
  public void before() throws ExecutionException, InterruptedException {
    futureResponse = mock(Future.class);
    httpResponse = mock(HTTPResponse.class);
    urlFetcher = mock(UrlFetcher.class);
    urlFetchBuilder = mock(UrlFetchBuilder.class);

    facebookService = new FacebookService(urlFetcher, new ObjectMapperProvider());

    when(urlFetcher.connect(anyString())).thenReturn(urlFetchBuilder);
    when(urlFetchBuilder.data(anyString(), any(String.class))).thenReturn(urlFetchBuilder);
    when(urlFetchBuilder.post()).thenReturn(futureResponse);
    when(futureResponse.get()).thenReturn(httpResponse);
    when(httpResponse.getContent()).thenReturn("SUCCESS".getBytes());
    when(httpResponse.getResponseCode()).thenReturn(200);
  }

  @Test
  public void testPost_shouldConnectToFacebookPostAPI() {
    final ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);

    // Call post
    facebookService.post(FacebookPostRequest.builder().facebookId("facebookId").build());

    // Verify urlFetcher is called
    verify(urlFetcher).connect(urlCaptor.capture());

    // Verify correct Facebook Post API URL is used
    assertThat(urlCaptor.getValue()).isEqualTo("https://graph.facebook.com/v2.8/facebookId/feed");
  }

  @Test
  public void testPost_shouldReturnPostResponse() {
    // Verify that a response is returned
    assertThat(facebookService.post(FacebookPostRequest.builder()
        .facebookId("id").build())
        .getApiResponse())
        .isEqualTo("SUCCESS");
  }

  @Test
  public void testPostWithErrorResponse_shouldThrowExceptionWithFacebookErrorMessage() {
    when(httpResponse.getResponseCode()).thenReturn(400);
    when(httpResponse.getContent()).thenReturn(("{\"error\":{" +
        "\"message\":\"Sample Facebook Error Message\"," +
        "\"type\":\"OAuthException\"," +
        "\"code\":100," +
        "\"fbtrace_id\":\"FpPiqIX8gRv\"}}").getBytes());

    assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
      @Override
      public void call() throws Throwable {
        facebookService.post(FacebookPostRequest.builder()
            .facebookId("id").build());
      }
    }).hasMessageContaining("Sample Facebook Error Message");
  }

  @Test
  public void testPostPayload_shouldSetPropertiesToPayload() {
    final ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
    final ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);

    // Call post
    facebookService.post(FacebookPostRequest.builder()
        .facebookId("id")
        .accessToken("mock_access_token")
        .title("Post Title")
        .message("Post Message")
        .description("Post Description")
        .link("localhost:8080/post_link")
        .imageUrl("localhost:8080/image.png")
        .build());

    // Verify UrlFetch is used
    verify(urlFetchBuilder, atLeast(5)).data(keyCaptor.capture(), valueCaptor.capture());

    // Verify all key names are added
    assertThat(keyCaptor.getAllValues())
        .containsExactly("title", "message", "link", "picture", "description", "access_token");

    // Verify all values are added
    assertThat(valueCaptor.getAllValues())
        .containsExactly("Post Title", "Post Message", "localhost:8080/post_link",
            "localhost:8080/image.png", "Post Description", "mock_access_token");
  }

  @Test
  public void testScrape_shouldConnectToFacebookScrapeAPI() {
    when(httpResponse.getContent()).thenReturn("[{\"code\":200,\"body\":\"SUCCESS\"}]".getBytes());
    final ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);

    // Call scrape
    facebookService.scrape(FacebookScrapeRequest.builder().addLink("localhost").build());

    // Verify urlFetcher is used
    verify(urlFetcher).connect(urlCaptor.capture());

    // Verify correct Facebook Scrape API URL is used
    assertThat(urlCaptor.getValue()).isEqualTo("https://graph.facebook.com");
  }

  @Test
  public void testScrapePayload_shouldSetParametersToPayload() {
    when(httpResponse.getContent()).thenReturn("[{\"code\":200,\"body\":\"SUCCESS\"}]".getBytes());

    final ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
    final ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);

    // Call scrape
    facebookService.scrape(FacebookScrapeRequest.builder().addLink("localhost1").addLink("localhost2").build());

    // Verify urlFetcher is used
    verify(urlFetchBuilder, atLeastOnce()).data(keyCaptor.capture(), valueCaptor.capture());

    //TODO
    // Verify that all key parameters are included
    assertThat(keyCaptor.getAllValues())
        .containsExactly("id", "scrape", "access_token", "id", "scrape", "access_token");

    // Verify that all submitted values are included
    assertThat(valueCaptor.getAllValues())
        .contains("localhost1", "localhost2", "true", "true");
  }

  @Test
  public void testScrapeResponse_shouldReturnResponse() {
    when(httpResponse.getContent()).thenReturn("[{\"code\":200,\"body\":\"SUCCESS\"}]".getBytes());
    final FacebookScrapeResponse response = facebookService.scrape(FacebookScrapeRequest.builder()
        .addLink("localhost1").addLink("localhost2").build());

    // Verify that each link contains a response.
    assertThat(response.getApiResponse().getBody())
        .isEqualTo("SUCCESS");
  }

  @Test
  public void testScrapeWithErrorResponse_shouldThrowExceptionWithFacebookErrorMessage() {
    when(httpResponse.getResponseCode()).thenReturn(400);
    when(httpResponse.getContent()).thenReturn(("{\"error\":{" +
        "\"message\":\"Sample Facebook Error Message\"," +
        "\"type\":\"OAuthException\"," +
        "\"code\":100," +
        "\"fbtrace_id\":\"FpPiqIX8gRv\"}}").getBytes());

    // TODO
//    assertThat(facebookService.scrape(FacebookScrapeRequest.builder().addLink("link").build())
//        .getResults().get("link"))
//        .isEqualTo("Sample Facebook Error Message");
  }

  @Test
  public void testBatch_shouldParametersToPayload() throws UnsupportedEncodingException {
    final ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
    final ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);

    when(httpResponse.getContent()).thenReturn("[{\"code\":200,\"body\":\"SUCCESS\"}]".getBytes());

    facebookService.submitBatch(BatchRequest.builder().accessToken("sample_token")
        .addItem(BatchItem.builder().post().relativeUrl("/post").body("message=hello&description=world").build())
        .addItem(BatchItem.builder().get().relativeUrl("/get?key=1").build())
        .build());

    verify(urlFetchBuilder, atLeastOnce()).data(keyCaptor.capture(), valueCaptor.capture());

    assertThat(keyCaptor.getAllValues()).containsOnly("access_token", "batch");

    assertThat(valueCaptor.getAllValues())
        .containsOnly("sample_token",
            URLEncoder.encode("[{\"method\":\"POST\",\"relative_url\":\"/post\",\"body\":\"message=hello&description=world\"}," +
                "{\"method\":\"GET\",\"relative_url\":\"/get?key=1\"}]", "UTF-8"));
  }

  @Test
  public void testBatchResponse_shouldReturnResponse() {
    when(httpResponse.getContent()).thenReturn("[{\"code\":200,\"body\":\"SUCCESS\"}]".getBytes());

    final BatchRequest request = BatchRequest.builder()
        .addItem(BatchItem.builder().post().relativeUrl("/post").body("message=hello&description=world").build())
        .addItem(BatchItem.builder().get().relativeUrl("/get?key=1").build())
        .build();

    final BatchResponse response = facebookService.submitBatch(request);

    assertThat(response.getCode()).isEqualTo(200);
    assertThat(response.getBody()).isEqualTo("SUCCESS");
  }
}
