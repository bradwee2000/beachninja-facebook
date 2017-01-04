# beachninja-facebook
Easy to use wrapper for Facebook API, meant for use on Google App Engine

Requires beachninja-url-fetch

## Posting to Facebook Usage

'''java
facebookService.post(FacebookPostRequest.builder()
    .facebookId("id")
    .title("Post Title")
    .message("Post Message")
    .description("Post Description")
    .link("localhost:8080/post_link")
    .imageUrl("localhost:8080/image.png")
    .build());
'''

## Facebook Scrape Usage

'''java
facebookService.scrape(FacebookScrapeRequest.builder()
    .addLink("test1.link.com")
    .addLink("test2.link.com")
    .build());
'''

'''java
facebookService.scrape(FacebookScrapeRequest.builder()
    .addLinks("test1.link.com", "test2.link.com")
    .build());
'''
