package com.TwitterDemo;

import com.TwitterDemo.Services.ITwitter;
import org.slf4j.LoggerFactory;
import twitter4j.*;

import java.lang.invoke.MethodHandles;
import java.util.List;

public class RetrieveTimeline {

    private ITwitter twitter;

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public RetrieveTimeline(ITwitter iTwitter){
        twitter = iTwitter;
    }

    public static void main(String[] args) {

        try {
            RetrieveTimeline retrieveTimeline = new RetrieveTimeline(ITwitter.getInstance());

            retrieveTimeline.retrieveTheTimeline();

            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            logger.error("Error while retrieving timeline.", te);
            System.out.println("Failed to retrieve timeline: " + te.getMessage());
            System.exit(-1);
        }
    }

    protected List<String> getUserTimeLine() throws TwitterException {
        return twitter.getUserTimeline();
    }

    public List<String> getHomeTimeLine() throws TwitterException {
        return twitter.getHomeTimeline();
    }

    public void retrieveTheTimeline() throws TwitterException {
        System.out.println("\nUser Timeline :");
        System.out.println("---------------");
        List<String> userTimeLine = getUserTimeLine();
        userTimeLine.forEach(status -> System.out.println("\n" + status));

        System.out.println("\nHome Timeline :");
        System.out.println("---------------");
        List<String> homeTimeLine = getHomeTimeLine();
        homeTimeLine.forEach(status -> System.out.println("\n" + status));
    }
}
