package com.TwitterDemo.services;

import com.TwitterDemo.TwitterDemoConfiguration;
import com.TwitterDemo.models.Tweet;
import org.slf4j.LoggerFactory;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ITwitter {

    private static ITwitter _ref = null;

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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

    public List<Tweet> getHomeTimeline() throws TwitterException {
        ResponseList<Status> statuses = twitter4j.getHomeTimeline();
        return getTweets(statuses);
    }

    private List<Tweet> getTweets(ResponseList<Status> statuses) {
        List<Tweet> tweets = new ArrayList<>();
        statuses.forEach(status -> tweets.add(new Tweet(status)));
        return tweets;
    }

    public Status publishTheTweet(String tweet) throws TwitterException {
        return twitter4j.updateStatus(tweet);
    }

    public List<Tweet> getUserTimeline() throws TwitterException {
        ResponseList<Status> statuses = twitter4j.getUserTimeline();
        return getTweets(statuses);
    }

    public RequestToken getOAuthRequestToken() throws TwitterException {
        return twitter4j.getOAuthRequestToken();
    }

    public AccessToken getOAuthAccessToken(RequestToken requestToken, String pin) throws TwitterException {
        return twitter4j.getOAuthAccessToken(requestToken, pin);
    }

    public void setOAuthConsumer(String consumerKey, String consumerSecret) {
        twitter4j.setOAuthConsumer(consumerKey, consumerSecret);
    }

    public void populateAccessKeyToken(Properties prop) throws IOException {
        try {
            RequestToken requestToken = getOAuthRequestToken();
            System.out.println("Got request token.");
            AccessToken accessToken = null;

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while (null == accessToken) {
                System.out.println("Open the following URL and grant access to your account:");
                System.out.println(requestToken.getAuthorizationURL());

                System.out.print("Enter the PIN(if available) and hit enter after you granted access.[PIN]:");
                String pin = br.readLine();
                try {
                    logger.info("PIN entered: {}", pin);
                    if (pin.length() > 0) {
                        accessToken = getOAuthAccessToken(requestToken, pin);
                    } else {
                        System.out.println("Incorrect PIN, Please try again.");
                    }
                } catch (TwitterException te) {
                    te.printStackTrace();
                    logger.error("Error while getting Access Token: ", te);
                }
            }
            System.out.println("Got access token.");
            System.out.println("Access token: " + accessToken.getToken());
            System.out.println("Access token secret: " + accessToken.getTokenSecret());

            prop.setProperty("oauth.accessToken", accessToken.getToken());
            prop.setProperty("oauth.accessTokenSecret", accessToken.getTokenSecret());

            br.close();
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get accessToken: " + te.getMessage());
            logger.error("Error while getting Request Token: ", te);
            System.exit(-1);
        }
    }

    public void getAccessToken(TwitterDemoConfiguration twitterDemoConfiguration) throws IOException {
        setOAuthConsumer(twitterDemoConfiguration.getConsumerKey(), twitterDemoConfiguration.getConsumerSecret());
        Properties prop = new Properties();
        populateAccessKeyToken(prop);
    }
}
