package io.cryptofeeler.app.feed.coinmarketcap;

import io.cryptofeeler.app.config.AppProperties;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

public class GlobalMetricsFeedExtractorTest {

    private static final String CREDENTIALS_PATH = "src/test/resources/coinmarketcap_credentials.properties";
    private static GlobalMetricsFeedExtractor globalMetricsFeedExtractor;

    @BeforeAll
    public static void beforeAll() throws IOException {
        String content = FileUtils.readFileToString(Paths.get(CREDENTIALS_PATH).toFile(), StandardCharsets.UTF_8);
        Properties properties = new Properties();
        properties.load(new StringReader(content));
        String apiKey = properties.getProperty("apiKey");

        AppProperties appProperties = new AppProperties();
        appProperties.coinMarketCapGlobalMetricsLatestUrl = "https://pro-api.coinmarketcap.com/v1/global-metrics/quotes/latest";

        globalMetricsFeedExtractor = new GlobalMetricsFeedExtractor(new CoinMarketCapCredentials(apiKey), appProperties);
    }

    @Test
    public void shouldExtractGlobalMetricsData() {
        GlobalMetricsFeed metricsFeed = globalMetricsFeedExtractor.extract();
        assertThat(metricsFeed.btcDominance, not(0d));
        assertThat(metricsFeed.ethDominance, not(0d));

    }
}