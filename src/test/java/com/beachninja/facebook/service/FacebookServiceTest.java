package com.beachninja.facebook.service;

import com.beachninja.facebook.batch.BatchItem;
import com.beachninja.facebook.batch.BatchRequest;
import com.beachninja.facebook.batch.BatchResponse;
import com.beachninja.facebook.model.Url;
import com.beachninja.facebook.model.Website;
import com.beachninja.facebook.post.FacebookPostRequest;
import com.beachninja.facebook.scrape.FacebookScrapeRequest;
import com.beachninja.facebook.scrape.FacebookScrapeResponse;
import com.beachninja.facebook.util.ObjectMapperProvider;
import com.beachninja.facebook.util.TestUtil;
import com.beachninja.urlfetch.UrlFetchBuilder;
import com.beachninja.urlfetch.UrlFetcher;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.data.Index.atIndex;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author bradwee2000@gmail.com
 */
public class FacebookServiceTest {

  private static String SCRAPE_JSON_RESPONSE;
  private static String SCRAPE_JSON_ERROR_RESPONSE;

  private Future<HTTPResponse> futureResponse;
  private HTTPResponse httpResponse;

  private UrlFetcher urlFetcher;
  private UrlFetchBuilder urlFetchBuilder;
  private ObjectMapperProvider objectMapperProvider;

  private FacebookService facebookService;

  @BeforeClass
  public static void setup() throws IOException {
    SCRAPE_JSON_RESPONSE =
        IOUtils.toString(FacebookServiceTest.class.getResourceAsStream("/batch-scrape-success-response.json"),
            Charsets.UTF_8);
    SCRAPE_JSON_ERROR_RESPONSE =
        IOUtils.toString(FacebookServiceTest.class.getResourceAsStream("/batch-scrape-error-response.json"),
            Charsets.UTF_8);
  }

  @Before
  public void before() throws ExecutionException, InterruptedException {
    futureResponse = mock(Future.class);
    httpResponse = mock(HTTPResponse.class);
    urlFetcher = mock(UrlFetcher.class);
    urlFetchBuilder = mock(UrlFetchBuilder.class);
    objectMapperProvider = mock(ObjectMapperProvider.class);

    when(urlFetcher.connect(anyString())).thenReturn(urlFetchBuilder);
    when(urlFetchBuilder.data(anyString(), any(String.class))).thenReturn(urlFetchBuilder);
    when(urlFetchBuilder.post()).thenReturn(futureResponse);
    when(futureResponse.get()).thenReturn(httpResponse);
    when(httpResponse.getContent()).thenReturn("{\"id\":\"sampleId\"}".getBytes());
    when(httpResponse.getResponseCode()).thenReturn(200);
    when(objectMapperProvider.get()).thenReturn(TestUtil.om());

    facebookService = new FacebookService(urlFetcher, objectMapperProvider);
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
        .getId())
        .isEqualTo("sampleId");
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
    when(httpResponse.getContent()).thenReturn(SCRAPE_JSON_RESPONSE.getBytes());

    final ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);

    // Call scrape
    facebookService.scrape(FacebookScrapeRequest.builder().addLink("localhost").build());

    // Verify urlFetcher is used
    verify(urlFetcher).connect(urlCaptor.capture());

    // Verify correct Facebook Scrape API URL is used
    assertThat(urlCaptor.getValue()).isEqualTo("https://graph.facebook.com");
  }

  @Test
  public void testScrapePayload_shouldSetParametersToPayload() throws IOException {
    when(httpResponse.getContent()).thenReturn(SCRAPE_JSON_RESPONSE.getBytes());

    final ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
    final ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);

    // Call scrape
    facebookService.scrape(FacebookScrapeRequest.builder()
        .accessToken("mock_token").addLink("localhost1").addLink("localhost2").build());

    // Verify urlFetcher is used
    verify(urlFetchBuilder, atLeastOnce()).data(keyCaptor.capture(), valueCaptor.capture());

    // Verify that all key parameters are included
    assertThat(keyCaptor.getAllValues()).containsExactly("access_token", "batch");

    // Verify that all submitted values are included
    assertThat(valueCaptor.getAllValues()).contains("mock_token", atIndex(0));
    assertThat(valueCaptor.getAllValues().get(1)).contains("localhost1", "localhost2");
  }

  @Test
  public void testScrapeResponse_shouldReturnResponse() throws IOException {
    when(httpResponse.getContent()).thenReturn(SCRAPE_JSON_RESPONSE.getBytes());

    final FacebookScrapeResponse response = facebookService.scrape(FacebookScrapeRequest.builder().build());

    // Verify responses
    assertThat(response.getWebsites()).hasSize(2);

    // Verify first website
    final Website website1 = response.getWebsites().get(0);
    assertThat(website1.getTitle()).isEqualTo("Sample Title");
    assertThat(website1.getUrl()).isEqualTo("http://localhost:8080/");
    assertThat(website1.getDescription()).isEqualTo("Sample Description.");
    assertThat(website1.getId()).isEqualTo("1187960447920593");
    assertThat(website1.getType()).isEqualTo("website");
    assertThat(website1.getImages()).containsExactly(
        Url.of("http://localhost:8080/img/img1.png"),
        Url.of("http://localhost:8080/img/img2.png")
    );

    // Verify first website
    final Website website2 = response.getWebsites().get(1);
    assertThat(website2.getTitle()).isEqualTo("Yet another website");
    assertThat(website2.getUrl()).isEqualTo("http://localhost:8080/yet/another/web/site");
    assertThat(website2.getDescription()).isEqualTo("Another description");
    assertThat(website2.getId()).isEqualTo("1194001093982772");
    assertThat(website2.getType()).isEqualTo("website");
    assertThat(website2.getImages()).containsExactly(
        Url.of("http://localhost:8080/img/img3.png")
    );
  }

  @Test
  public void testScrapeWithErrorResponse_shouldKeepErrorsInResponse() throws IOException {
    when(httpResponse.getContent()).thenReturn(SCRAPE_JSON_ERROR_RESPONSE.getBytes());

    final FacebookScrapeResponse response = facebookService.scrape(FacebookScrapeRequest.builder().build());

    assertThat(response.getWebsites()).hasSize(1);
    assertThat(response.getErrors()).hasSize(2);
    assertThat(response.getErrors().get(0).getMessage()).isEqualTo("Invalid parameter");
    assertThat(response.getErrors().get(1).getMessage()).isEqualTo("Cannot specify an empty identifier");
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

    final List<BatchResponse> responses = facebookService.submitBatch(request);

    assertThat(responses).hasSize(1);
    assertThat(responses.get(0).getCode()).isEqualTo(200);
    assertThat(responses.get(0).getBody()).isEqualTo("SUCCESS");
  }
}
