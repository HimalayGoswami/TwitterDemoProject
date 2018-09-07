package com.TwitterDemo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

public class TwitterDemoConfiguration extends Configuration {

    @JsonProperty
    private String consumerKey;

    @JsonProperty
    private String consumerSecret;

    @JsonProperty
    private long maxFilteredTweetsCacheWeight;

    public String getConsumerKey() {
        return consumerKey;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    public long getMaxFilterCacheWeight() {
        return maxFilteredTweetsCacheWeight;
    }
}
