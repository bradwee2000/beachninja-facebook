# beachninja-facebook
Easy-to-use wrapper for Facebook API, meant for use on Google App Engine. Uses App Engine's URLFetchServiceFactory URLFetchServiceFactory.

Requires [beachninja-url-fetch](https://github.com/bradwee2000/beachninja-url-fetch) - a wrapper for Google App Engine's URLFetchServiceFactory.

## Posting to Facebook Usage Example

```java
final FacebookPostResponse response = facebookService.post(FacebookPostRequest.builder()
    .facebookId("id")
    .title("Post Title")
    .message("Post Message")
    .description("Post Description")
    .link("https://localhost:8080/post_link")
    .imageUrl("https://localhost:8080/image.png")
    .build());
```

## Facebook Scrape Usage Example

```java
final FacebookScrapeResponse response = facebookService.scrape(FacebookScrapeRequest.builder()
    .addLink("https://test1.link.com")
    .addLink("https://test2.link.com")
    .build());
```

```java
final FacebookScrapeResponse response = facebookService.scrape(FacebookScrapeRequest.builder()
    .addLinks("https://test1.link.com", "https://test2.link.com")
    .build());
```
