package io.cryptofeeler.app.feed.google;

import io.cryptofeeler.app.config.AppProperties;
import org.junit.jupiter.api.Test;

public class GoogleTrendsFeedExtractorTest {

    @Test
    public void shouldExtractGoogleTrends() {
        AppProperties appProperties = new AppProperties();
        appProperties.googleTrendsApiExploreUrl = "https://trends.google.com/trends/api/explore";
        appProperties.googleTrendsApiMultilineUrl = "https://trends.google.com/trends/api/widgetdata/multiline";
        GoogleTrendsFeedExtractor googleTrendsFeedExtractor = new GoogleTrendsFeedExtractor(appProperties);
        googleTrendsFeedExtractor.extractFeed("bitcoin");
    }

}