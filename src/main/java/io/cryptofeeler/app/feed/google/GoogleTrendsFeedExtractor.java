package io.cryptofeeler.app.feed.google;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GoogleTrendsFeedExtractor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterFeedExtractor.class);
    private AppProperties appProperties;

    private final Gson gson = (new GsonBuilder()).create();

    public GoogleTrendsFeedExtractor(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public GoogleTrendsFeed extractFeed(String searchTerm) {

        LOGGER.info("Start: Extracting google trends feed for search term {}", searchTerm);
        long start = System.currentTimeMillis();
        GoogleTrendsFeed googleTrendsFeed = new GoogleTrendsFeed();
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String exploreData = getExploreData(searchTerm, httpClient);
            Map<String, Object> exploreJson = gson.fromJson(exploreData.substring(5), Map.class);
            String multilineData = getInterestOverTimeData(exploreJson, httpClient);
            Map<String, Object> timelineJson = gson.fromJson(multilineData.substring(5), Map.class);

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

    private String getInterestOverTimeData(Map<String, Object> exploreJson, CloseableHttpClient httpClient)
            throws URISyntaxException, IOException {

        List<Map> widgets = (List<Map>) exploreJson.get("widgets");
        Optional<Map> timeseries = widgets.stream().filter(i -> i.containsValue("TIMESERIES")).findFirst();
        if (timeseries.isPresent()) {

            URIBuilder uriBuilder = new URIBuilder(appProperties.googleTrendsApiMultilineUrl);
            uriBuilder.addParameter("hl", "en-US");
            uriBuilder.addParameter("tz", "-600");

            Map<String, Object> timeSeriesData = timeseries.get();
            Object requestData = timeSeriesData.get("request");
            uriBuilder.addParameter("req", this.gson.toJson(requestData));
            uriBuilder.addParameter("token", timeSeriesData.get("token").toString());
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            httpGet.addHeader("cookie", GoogleTrendsConfig.COOKIE);

            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);

        } else {
            throw new CryptoFeelerException("Could not find time series widget request from explore response.");
        }
    }

}
