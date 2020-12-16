package io.cryptofeeler.app.feed.google;

import java.util.ArrayList;
import java.util.List;

public class GoogleTrendsFeed {

    public final List<TimeLineEntry> timelineData = new ArrayList();

    public static class TimeLineEntry {
        public long time;
        public String formattedTime;
        public double value;

        public TimeLineEntry(long time, String formattedTime, double value) {
            this.time = time;
            this.formattedTime = formattedTime;
            this.value = value;
        }
    }
}
