package io.cryptofeeler.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AppProperties {

    @Value("#{new Integer('${twitter.search.limit}')}")
    public int twitterMaxSearchLimit;

    @Value("${google.trends.api.explore.url}")
    public String googleTrendsApiExploreUrl;

    @Value("${google.trends.api.multiline.url}")
    public String googleTrendsApiMultilineUrl;

    @Value("#{'${feed.coin.list}'.split(',')}")
    public List<String> feedCoinList;
}
