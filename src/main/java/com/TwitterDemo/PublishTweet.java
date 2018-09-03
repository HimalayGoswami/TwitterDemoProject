package com.TwitterDemo;

import com.TwitterDemo.api.Tweet;
import twitter4j.TwitterException;

import java.util.Scanner;

public class PublishTweet {

    private ITwitter twitter;

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
                System.out.println("Tweet can not be empty.");
                System.exit(-2);
            }
            publishTweet.publishTheTweet(tweet);
            System.exit(0);
        } catch (TwitterException e) {
            e.printStackTrace();
            System.out.println("Failed to tweet: " + e.getMessage());
            System.exit(-1);
        }

    }

    public Tweet publishTheTweet(String tweet) throws TwitterException {
        String status = twitter.publishTheTweet(tweet);
        System.out.println("Successfully tweeted [" + status + "].");
        return new Tweet(status);
    }

    public String getTweetInput() {
        System.out.println("Please input the tweet to publish: ");
        Scanner scanner = new Scanner(System.in);
        String tweet = scanner.nextLine();
        return tweet.trim();
    }
}
