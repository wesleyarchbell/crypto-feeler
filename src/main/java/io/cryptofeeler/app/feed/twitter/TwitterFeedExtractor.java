package io.cryptofeeler.app.feed.twitter;

import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.dto.tweet.Tweet;
import com.github.redouane59.twitter.dto.tweet.TweetSearchResponse;
import io.cryptofeeler.app.config.AppProperties;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class TwitterFeedExtractor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterFeedExtractor.class);

    private final TwitterClient twitterClient;
    private final AppProperties appProperties;

    public TwitterFeedExtractor(TwitterClient twitterClient, AppProperties appProperties) {
        this.twitterClient = twitterClient;
        this.appProperties = appProperties;
    }

    public List<TwitterFeed> extractFeed(List<String> searchTerms) {
        LOGGER.info("Start: Extract twitter feed");
        long start = System.currentTimeMillis();

        List<TwitterFeed> collect = searchTerms.stream()
                .map(this::getFeed).filter(Objects::nonNull)
                .collect(Collectors.toList());

        long end = System.currentTimeMillis() - start;
        LOGGER.info("End: Extract twitter feed, took: " + DurationFormatUtils.formatDurationWords(end, false, false));
        return collect;
    }

    private TwitterFeed getFeed(String searchTerm) {
        TweetSearchResponse response = twitterClient
                .searchForTweetsWithin7days(searchTerm, appProperties.twitterMaxSearchLimit, null);
        List<Tweet> tweets = response.getTweets();
        if (Optional.ofNullable(tweets).isPresent()) {
            return new TwitterFeed(searchTerm, tweets, response.getNextToken());
        }
        return null;
    }
}
