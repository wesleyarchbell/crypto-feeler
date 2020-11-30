package io.cryptofeeler.app.feed.twitter;

import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.dto.tweet.Tweet;
import com.github.redouane59.twitter.dto.tweet.TweetSearchResponse;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

class TwitterFeedExtractorTest {

    private static final String CREDENTIALS_PATH = "src/test/resources/twitter_credentials.json";

    private static TwitterClient twitterClient;

    @BeforeAll
    public static void before() {
        System.setProperty("twitter.credentials.file.path", CREDENTIALS_PATH);
        twitterClient = new TwitterClient();
    }

    @Test
    public void shouldExtractBasicFeed() {
        TweetSearchResponse response = twitterClient.searchForTweetsWithin7days("Bitcoin", 10, null);
        List<Tweet> tweets = response.getTweets();
        MatcherAssert.assertThat(tweets.size(), Matchers.equalTo(10));
    }

}