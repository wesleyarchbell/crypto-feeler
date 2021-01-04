package io.cryptofeeler.app.feed.coinmarketcap;

public class GlobalMetricsFeed {

    public final double btcDominance;
    public final double ethDominance;

    public GlobalMetricsFeed(double btcDominance, double ethDominance) {
        this.btcDominance = btcDominance;
        this.ethDominance = ethDominance;
    }
}
