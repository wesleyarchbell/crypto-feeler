package io.cryptofeeler.app.feed.google;

import io.cryptofeeler.app.config.AppProperties;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

public class GoogleTrendsFeedExtractorTest {

    @Test
    public void shouldExtractGoogleTrends() {
        AppProperties appProperties = new AppProperties();
        appProperties.googleTrendsApiExploreUrl = "https://trends.google.com/trends/api/explore";
        appProperties.googleTrendsApiMultilineUrl = "https://trends.google.com/trends/api/widgetdata/multiline";
        GoogleTrendsFeedExtractor googleTrendsFeedExtractor = new GoogleTrendsFeedExtractor(appProperties);
        GoogleTrendsFeed bitcoin = googleTrendsFeedExtractor.extractFeed("bitcoin");
        assertThat(bitcoin.timelineData, notNullValue());
        assertThat(bitcoin.timelineData.size(), greaterThan(1));
    }

}