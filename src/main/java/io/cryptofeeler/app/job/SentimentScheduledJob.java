package io.cryptofeeler.app.job;

import io.cryptofeeler.app.config.AppProperties;
import io.cryptofeeler.app.feed.coinmarketcap.GlobalMetricsFeed;
import io.cryptofeeler.app.feed.coinmarketcap.GlobalMetricsFeedExtractor;
import io.cryptofeeler.app.feed.google.GoogleTrendsFeed;
import io.cryptofeeler.app.feed.google.GoogleTrendsFeedExtractor;
import io.cryptofeeler.app.feed.twitter.TwitterFeed;
import io.cryptofeeler.app.feed.twitter.TwitterFeedExtractor;
import io.cryptofeeler.app.nlp.SentimentNLP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.stream.Collectors;

public class SentimentScheduledJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(SentimentScheduledJob.class);

    private final AppProperties appProperties;
    private final TwitterFeedExtractor twitterFeedExtractor;
    private final GoogleTrendsFeedExtractor googleTrendsFeedExtractor;
    private final GlobalMetricsFeedExtractor globalMetricsFeedExtractor;
    private final SentimentNLP sentimentNLP;

    public SentimentScheduledJob(AppProperties appProperties,
                                 TwitterFeedExtractor twitterFeedExtractor,
                                 GoogleTrendsFeedExtractor googleTrendsFeedExtractor,
                                 GlobalMetricsFeedExtractor globalMetricsFeedExtractor,
                                 SentimentNLP sentimentNLP) {
        this.appProperties = appProperties;
        this.twitterFeedExtractor = twitterFeedExtractor;
        this.googleTrendsFeedExtractor = googleTrendsFeedExtractor;
        this.globalMetricsFeedExtractor = globalMetricsFeedExtractor;
        this.sentimentNLP = sentimentNLP;
    }

    @Scheduled(fixedDelay = 1000 * 60)
    public void pullSentimentFromFeeds() {

        LOGGER.info("Fetching sentiment from feeds...");

        appProperties.feedCoinList.forEach(coin -> {

            String name = coin.substring(0, coin.indexOf("("));
            String symbol = coin.substring(coin.indexOf("(") + 1, coin.indexOf(")"));

            LOGGER.info("Fetching twitter feed for {}", coin);
            TwitterFeed twitterFeed = twitterFeedExtractor.extractFeed(name);

            LOGGER.info("Fetching sentiment for {}", coin);
            List<String> sentiment = twitterFeed.tweets.stream()
                    .flatMap(tweet -> sentimentNLP.getSentiment(tweet.getText()).stream())
                    .collect(Collectors.toList());

            sentiment.forEach(s -> {
                LOGGER.info("Sentiment for twitter: {}", s);
            });

            GoogleTrendsFeed googleTrendsFeed = googleTrendsFeedExtractor.extractFeed(coin);
            LOGGER.info("Google trends timeline for {} ", coin);
            googleTrendsFeed.timelineData.forEach(i -> LOGGER.info(i.formattedTime + " - " + i.value));
        });

        GlobalMetricsFeed globalMetricsFeed = globalMetricsFeedExtractor.extract();
        LOGGER.info("BTC Dominance, {}%", globalMetricsFeed.btcDominance);
        LOGGER.info("ETH Dominance, {}%", globalMetricsFeed.ethDominance);
    }
}
