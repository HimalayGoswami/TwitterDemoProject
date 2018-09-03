package com.TwitterDemo.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Tweet {

    private String tweet;

    public Tweet() {}

    public Tweet(String tweetText) {
        tweet = tweetText;
    }

    @JsonProperty
    public String getTweet() {
        return tweet;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        } else if (this == obj) {
            return true;
        } else if(obj instanceof Tweet) {
            Tweet tweetObj = (Tweet)obj;
            if((tweet == null && tweetObj.getTweet() == null)
                || (tweet != null && tweet.equals(tweetObj.getTweet()))){
                return true;
            }
        }
        return false;
    }
}
