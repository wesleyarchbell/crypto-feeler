package io.cryptofeeler.app.feed.google;

import io.cryptofeeler.app.config.AppProperties;
import io.cryptofeeler.app.exception.CryptoFeelerException;
import io.cryptofeeler.app.feed.twitter.TwitterFeedExtractor;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public class GoogleTrendsFeedExtractor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterFeedExtractor.class);
    private AppProperties appProperties;

    public GoogleTrendsFeedExtractor(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public GoogleTrendsFeed extractFeed(String searchTerm) {

        LOGGER.info("Start: Extracting google trends feed for search term {}", searchTerm);
        long start = System.currentTimeMillis();
        GoogleTrendsFeed googleTrendsFeed = new GoogleTrendsFeed();
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String exploreData = getExploreData(searchTerm, httpClient);
            JsonParser jsonParser = JsonParserFactory.getJsonParser();
            Map<String, Object> stringObjectMap = jsonParser.parseMap(exploreData.substring(5));

        } catch (Exception e) {
            throw new CryptoFeelerException("Failed to extract google trends feed", e);
        }
        long end = System.currentTimeMillis() - start;
        LOGGER.info("End: Extracting google trends feed, took: " + DurationFormatUtils.formatDurationWords(end, false, false));
        return googleTrendsFeed;
    }

    private String getExploreData(String searchTerm, CloseableHttpClient httpClient) throws URISyntaxException, IOException {
        URIBuilder uriBuilder = new URIBuilder(appProperties.googleTrendsApiExploreUrl);
        uriBuilder.addParameter("hl", "en-US");
        uriBuilder.addParameter("tz", "-600");
        uriBuilder.addParameter("req", "{\"comparisonItem\":[{\"keyword\":\"" + searchTerm + "\",\"geo\":\"\"," +
                "\"time\":\"today 1-m\"}],\"category\":0,\"property\":\"\"}");

        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.addHeader("cookie", GoogleTrendsConfig.COOKIE);

        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        return EntityUtils.toString(entity);
    }

}
