package com.TwitterDemo;

import com.TwitterDemo.models.Tweet;
import com.TwitterDemo.services.ITwitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.lang.invoke.MethodHandles;
import java.util.Optional;
import java.util.Scanner;

@Service
public class PublishTweet {

    @Autowired
    private ITwitter twitter;

    private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public PublishTweet(ITwitter iTwitter){
        twitter = iTwitter;
    }

    public PublishTweet() {

    }

    public static void main(String[] args) {
        try {
            PublishTweet publishTweet =  new PublishTweet(ITwitter.getInstance());
            String tweet = publishTweet.getTweetInput();
            if (tweet.equals("")){
                logger.info("Tweet Entered is Empty.");
                System.out.println("Tweet can not be empty.");
                System.exit(-2);
            }
            publishTweet.publishTheTweet(tweet);
            System.exit(0);
        } catch (TwitterException e) {
            e.printStackTrace();
            System.out.println("Failed to tweet: " + e.getMessage());
            logger.error("Error while Tweeting.", e);
            System.exit(-1);
        }

    }

    public Optional<Tweet> publishTheTweet(String tweet) throws TwitterException {
        Optional<Status> status = twitter.publishTheTweet(tweet);
        System.out.println("Successfully tweeted [" + status + "].");
        if (status.isPresent())
            return Optional.of(new Tweet(status.get()));
        throw new TwitterException("Twitter response Status is null.",new Exception(), 500);
    }

    public String getTweetInput() {
        System.out.println("Please input the tweet to publish: ");
        Scanner scanner = new Scanner(System.in);
        String tweet = scanner.nextLine();
        return tweet.trim();
    }
}
