package com.TwitterDemo;

import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.util.ArrayList;
import java.util.List;

public class ITwitter {

    private static ITwitter _ref = null;

    private Twitter twitter4j;

    public static final String propertyFilePath = "../";

    private ITwitter(){
        twitter4j = new TwitterFactory(propertyFilePath).getInstance();
    }

    public static ITwitter getInstance() {
        if(_ref == null){
            synchronized (ITwitter.class) {
                if(_ref == null) {
                    _ref = new ITwitter();
                }
            }
        }
        return _ref;
    }


    @Deprecated
    public Twitter getTwitter4jInstance(){
        return twitter4j;
    }

    @Deprecated
    public static void setInstance(ITwitter twitter) { _ref = twitter; };

    public List<String> getHomeTimeline() throws TwitterException {
        ResponseList<Status> statuses = twitter4j.getHomeTimeline();
        return getStatuseTexts(statuses);
    }

    private List<String> getStatuseTexts(ResponseList<Status> statuses) {
        List<String> statusTexts = new ArrayList<>();
        statuses.forEach(status -> statusTexts.add(status.getText()));
        return statusTexts;
    }

    public String publishTheTweet(String tweet) throws TwitterException {
        return twitter4j.updateStatus(tweet).getText();
    }

    public List<String> getUserTimeline() throws TwitterException {
        ResponseList<Status> statuses = twitter4j.getUserTimeline();
        return getStatuseTexts(statuses);
    }

    public RequestToken getOAuthRequestToken() throws TwitterException {
        return twitter4j.getOAuthRequestToken();
    }

    public AccessToken getOAuthAccessToken(RequestToken requestToken, String pin) throws TwitterException {
        return twitter4j.getOAuthAccessToken(requestToken, pin);
    }
}
