package io.cryptofeeler.app.nlp;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import io.cryptofeeler.app.config.ApplicationConfiguration;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SentimentNLPTest {

    private SentimentNLP sentimentNLP;

    @BeforeEach
    public void beforeAll() {
        ApplicationConfiguration applicationConfiguration = new ApplicationConfiguration();
        StanfordCoreNLP pipeline = applicationConfiguration.pipeline();
        StanfordCoreNLP tokenizer = applicationConfiguration.tokenizer();

        sentimentNLP = new SentimentNLP(tokenizer, pipeline);
    }

    @Test
    public void shouldDetermineSentiment() {
        List<String> sentiment = sentimentNLP.getSentiment("Bitcoin's price is not doing very well, im worried");

        MatcherAssert.assertThat(sentiment.get(0), Matchers.equalTo("Negative"));

        sentiment = sentimentNLP.getSentiment("Bitcoin's too the moon!");
        MatcherAssert.assertThat(sentiment.get(0), Matchers.equalTo("Neutral"));

        sentiment = sentimentNLP.getSentiment("Bitcoin is great!");
        MatcherAssert.assertThat(sentiment.get(0), Matchers.equalTo("Positive"));
    }

}