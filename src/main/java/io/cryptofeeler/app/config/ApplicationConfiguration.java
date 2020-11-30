package io.cryptofeeler.app.config;

import com.github.redouane59.twitter.TwitterClient;
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
    public TwitterClient twitterClient() {
        return new TwitterClient();
    }
}
