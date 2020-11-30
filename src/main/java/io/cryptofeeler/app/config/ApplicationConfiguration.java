package io.cryptofeeler.app.config;

import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.signature.TwitterCredentials;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public StanfordCoreNLP tokenizer() {
        Properties tokenizerProps = new Properties();
        tokenizerProps.setProperty("annotators", "tokenize ssplit");
        return new StanfordCoreNLP(tokenizerProps);
    }

    @Bean
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
    public TwitterClient twitterClient(TwitterCredentials twitterCredentials) {
        return new TwitterClient(twitterCredentials);
    }
}
