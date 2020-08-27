package com.TwitterDemo;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import java.util.Scanner;

public class PublishTweet {
    public static void main(String[] args) {
        try {
            String tweet = getTweetInput();
            publishTheTweet(tweet);
            System.exit(0);
        } catch (TwitterException var2) {
            var2.printStackTrace();
            System.out.println("Failed to tweet: " + var2.getMessage());
            System.exit(-1);
        } catch (Exception var3) {
            var3.printStackTrace();
            System.out.println("Failed to tweet: " + var3.getMessage());
            System.exit(-1);
        }

    }

    protected static void publishTheTweet(String tweet) throws TwitterException {
        Twitter twitter = new TwitterFactory("../").getInstance();
        Status status = twitter.updateStatus(tweet);
        System.out.println("Successfully tweeted [" + status.getText() + "].");
    }

    protected static String getTweetInput() throws Exception {
        System.out.println("Please input the tweet to publish: ");
        Scanner scanner = new Scanner(System.in);
        String tweet = scanner.nextLine();
        if (tweet != null && !tweet.trim().equals("")) {
            return tweet;
        } else {
            throw new Exception("Tweet can not be empty");
        }
    }
}
