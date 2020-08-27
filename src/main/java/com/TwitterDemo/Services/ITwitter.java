package com.TwitterDemo.services;

import com.TwitterDemo.TwitterDemoConfiguration;
import com.TwitterDemo.models.Tweet;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import javax.swing.text.html.Option;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

@Service
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

    public Optional<List<Tweet>> getHomeTimeline() throws TwitterException {
        return getTweets(Optional.ofNullable(twitter4j.getHomeTimeline()));
    }

    private Optional<List<Tweet>> getTweets(Optional<ResponseList<Status>> statuses) {
        Optional<List<Tweet>> tweets = Optional.empty();
        if(statuses.isPresent()){
            List<Tweet> tweetList = statuses.get().stream().map(status -> new Tweet(status)).collect(Collectors.toList());
            tweets = Optional.ofNullable(tweetList);
        }
        return tweets;
    }

    public Optional<Status> publishTheTweet(String tweet) throws TwitterException {
        return Optional.ofNullable(twitter4j.updateStatus(tweet));
    }

    public Optional<List<Tweet>> getUserTimeline() throws TwitterException {
        return getTweets(Optional.ofNullable(twitter4j.getUserTimeline()));
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
