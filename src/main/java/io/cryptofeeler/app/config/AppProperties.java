package io.cryptofeeler.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppProperties {

    @Value("#{new Integer('${twitter.search.limit}')}")
    public int twitterMaxSearchLimit;
}
