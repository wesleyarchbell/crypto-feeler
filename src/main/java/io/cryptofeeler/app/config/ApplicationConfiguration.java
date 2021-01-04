package io.cryptofeeler.app.config;

import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.signature.TwitterCredentials;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import io.cryptofeeler.app.feed.coinmarketcap.CoinMarketCapCredentials;
import io.cryptofeeler.app.feed.coinmarketcap.GlobalMetricsFeed;
import io.cryptofeeler.app.feed.coinmarketcap.GlobalMetricsFeedExtractor;
import io.cryptofeeler.app.feed.google.GoogleTrendsFeedExtractor;
import io.cryptofeeler.app.feed.twitter.TwitterFeedExtractor;
import io.cryptofeeler.app.job.SentimentScheduledJob;
import io.cryptofeeler.app.nlp.SentimentNLP;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Properties;

@Configuration
@EnableScheduling
public class ApplicationConfiguration {

    @Bean(name = "tokenizer")
    public StanfordCoreNLP tokenizer() {
        Properties tokenizerProps = new Properties();
        tokenizerProps.setProperty("annotators", "tokenize ssplit");
        return new StanfordCoreNLP(tokenizerProps);
    }

    @Bean(name = "pipeline")
    public StanfordCoreNLP pipeline() {
        Properties pipelineProps = new Properties();
        pipelineProps.setProperty("annotators", "parse, sentiment");
        pipelineProps.setProperty("parse.binaryTrees", "true");
        pipelineProps.setProperty("enforceRequirements", "false");
        return new StanfordCoreNLP(pipelineProps);
    }

    @Bean
    public TwitterCredentials twitterCredentials() {
        String apiKey = System.getenv("twitterApiKey");
        String apiSecretKey = System.getenv("twitterApiSecretKey");
        String accessToken = System.getenv("twitterAccessToken");
        String accessTokenSecret = System.getenv("twitterAccessTokenSecret");
        return new TwitterCredentials(apiKey, apiSecretKey, accessToken, accessTokenSecret);
    }

    @Bean
    public CoinMarketCapCredentials coinMarketCapCredentials() {
        String coinMarketCapApiKey = System.getenv("coinmarketcap.api.key");
        return new CoinMarketCapCredentials(coinMarketCapApiKey);
    }

    @Bean
    public TwitterClient twitterClient(TwitterCredentials twitterCredentials) {
        return new TwitterClient(twitterCredentials);
    }

    @Bean
    public TwitterFeedExtractor twitterFeedExtractor(TwitterClient twitterClient, AppProperties appProperties) {
        return new TwitterFeedExtractor(twitterClient, appProperties);
    }

    @Bean
    public GoogleTrendsFeedExtractor googleTrendsFeedExtractor(AppProperties appProperties) {
        return new GoogleTrendsFeedExtractor(appProperties);
    }

    @Bean
    public GlobalMetricsFeedExtractor globalMetricsFeed(CoinMarketCapCredentials coinMarketCapCredentials,
                                               AppProperties appProperties) {
        return new GlobalMetricsFeedExtractor(coinMarketCapCredentials, appProperties);
    }

    @Bean
    public SentimentScheduledJob sentimentScheduledJob(AppProperties appProperties,
                                                       TwitterFeedExtractor twitterFeedExtractor,
                                                       GoogleTrendsFeedExtractor googleTrendsFeedExtractor,
                                                       GlobalMetricsFeedExtractor globalMetricsFeedExtractor,
                                                       SentimentNLP sentimentNLP) {
        return new SentimentScheduledJob(appProperties, twitterFeedExtractor, googleTrendsFeedExtractor,
                globalMetricsFeedExtractor, sentimentNLP);
    }

    @Bean
    public SentimentNLP sentimentNLP(StanfordCoreNLP tokenizer, StanfordCoreNLP pipeline) {
        return new SentimentNLP(tokenizer, pipeline);
    }
}
