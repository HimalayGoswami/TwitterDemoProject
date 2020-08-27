package com.TwitterDemo.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import twitter4j.Status;

import java.util.Date;

public class Tweet {

    @JsonProperty
    private String tweetMessage;

    @JsonProperty
    private User user;

    @JsonProperty
    private Date createdAt;

    public Tweet() {}

    public Tweet(String tweetText) {
        tweetMessage = tweetText;
    }

    public Tweet(Status status) {
        tweetMessage = status.getText();
        user = new User(status.getUser());
        createdAt = status.getCreatedAt();
    }

    public String getTweet() {
        return tweetMessage;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        } else if (this == obj) {
            return true;
        } else if(obj instanceof Tweet) {
            Tweet tweetObj = (Tweet)obj;
            if((tweetMessage == null && tweetObj.getTweet() == null)
                || (tweetMessage != null && tweetMessage.equals(tweetObj.getTweet()))){
                return true;
            }
        }
        return false;
    }
}
