package com.TwitterDemo;

import twitter4j.*;

public class RetrieveTimeline {

    public static void main(String[] args) {

        try {
            retrieveTheTimeline();
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to retrieve timeline: " + te.getMessage());
            System.exit(-1);
        }
    }

    protected static void retrieveTheTimeline() throws TwitterException {
        Twitter twitter = new TwitterFactory("../").getInstance();

        ResponseList<Status> userTimeLine = twitter.getUserTimeline();

        ResponseList<Status> homeTimeLine = twitter.getHomeTimeline();

        System.out.println("\nUser Timeline :");
        System.out.println("---------------");
        userTimeLine.forEach(status -> System.out.println("\n" + status.getText()));

        System.out.println("\nHome Timeline :");
        System.out.println("---------------");
        homeTimeLine.forEach(status -> System.out.println("\n" + status.getText()));
    }

    public static ResponseList<Status> getHomeTimeLine() throws TwitterException {
        Twitter twitter = new TwitterFactory("../").getInstance();
        return twitter.getHomeTimeline();
    }
}
