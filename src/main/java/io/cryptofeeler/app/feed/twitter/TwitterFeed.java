package io.cryptofeeler.app.feed.twitter;

import com.github.redouane59.twitter.dto.tweet.Tweet;

import java.util.List;

public class TwitterFeed {

    public final String coin;
    public final List<Tweet> tweets;
    public final String nextToken;

    public TwitterFeed(String coin, List<Tweet> tweets, String nextToken) {
        this.coin = coin;
        this.tweets = tweets;
        this.nextToken = nextToken;
    }
}
