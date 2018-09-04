package com.TwitterDemo.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    @JsonProperty
    private String twitterHandle;

    @JsonProperty
    private String name;

    @JsonProperty
    private String profileImgURL;

    public User(twitter4j.User user) {
        twitterHandle = user.getScreenName();
        name = user.getName();
        profileImgURL = user.getProfileImageURL();
    }

}
