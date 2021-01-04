package io.cryptofeeler.app.feed.coinmarketcap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.cryptofeeler.app.config.AppProperties;
import io.cryptofeeler.app.exception.CryptoFeelerException;
import io.cryptofeeler.app.feed.twitter.TwitterFeedExtractor;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.Map;

public class GlobalMetricsFeedExtractor {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalMetricsFeedExtractor.class);

    private static final Type JSON_MAP_TYPE = new TypeToken<Map<String, Object>>() {}.getType();
    private final Gson gson = (new GsonBuilder()).create();

    private final CoinMarketCapCredentials credentials;
    private final AppProperties appProperties;

    public GlobalMetricsFeedExtractor(CoinMarketCapCredentials credentials, AppProperties appProperties) {
        this.credentials = credentials;
        this.appProperties = appProperties;
    }

    public GlobalMetricsFeed extract() {

        LOGGER.info("Start: Extracting global trends data");
        long start = System.currentTimeMillis();
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            String globalMetricsData = getGlobalMetricsData(credentials, appProperties, httpClient);
            Map<String, Object> metricsJson = gson.fromJson(globalMetricsData, JSON_MAP_TYPE);

            Map dataMap = (Map)metricsJson.get("data");
            String btcDominance = dataMap.get("btc_dominance").toString();
            String ethDominance = dataMap.get("eth_dominance").toString();

            long end = System.currentTimeMillis() - start;
            LOGGER.info("End: Extracting global trends data, took: " + DurationFormatUtils.formatDurationWords(end, false, false));

            return new GlobalMetricsFeed(Double.parseDouble(btcDominance), Double.parseDouble(ethDominance));

        } catch (Exception e) {
            throw new CryptoFeelerException("Failed to extract global metrics data", e);
        }
    }

    private String getGlobalMetricsData(CoinMarketCapCredentials credentials, AppProperties appProperties,
                                        CloseableHttpClient httpClient) throws URISyntaxException, IOException {
        URIBuilder uriBuilder = new URIBuilder(appProperties.coinMarketCapGlobalMetricsLatestUrl);

        HttpGet request = new HttpGet(uriBuilder.build());

        request.setHeader(HttpHeaders.ACCEPT, "application/json");
        request.addHeader("X-CMC_PRO_API_KEY", credentials.apiKey);

        CloseableHttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();
        return EntityUtils.toString(entity);
    }
}
