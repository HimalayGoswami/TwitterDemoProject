package com.TwitterDemo.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Tweet {
    private String tweet;

    @JsonProperty
    public String getTweet() {
        return tweet;
    }
}
