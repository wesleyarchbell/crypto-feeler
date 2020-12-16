package io.cryptofeeler.app.nlp;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class SentimentNLP {

    private StanfordCoreNLP tokenizer;
    private StanfordCoreNLP pipeline;

    public SentimentNLP(StanfordCoreNLP tokenizer, StanfordCoreNLP pipeline) {
        this.tokenizer = tokenizer;
        this.pipeline = pipeline;
    }

    public List<String> getSentiment(String content) {
        Annotation annotation = tokenizer.process(content);
        pipeline.annotate(annotation);
        return annotation.get(CoreAnnotations.SentencesAnnotation.class).stream()
                .map(s -> s.get(SentimentCoreAnnotations.SentimentClass.class))
                .collect(Collectors.toList());
    }

}
