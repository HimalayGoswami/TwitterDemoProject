package com.TwitterDemo;

import com.TwitterDemo.models.Tweet;
import com.TwitterDemo.services.ITwitter;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import twitter4j.*;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;

@Service
public class RetrieveTimeline {

    @Autowired
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

    public Optional<List<Tweet>> getUserTimeLine() throws TwitterException {
        return twitter.getUserTimeline();
    }

    public Optional<List<Tweet>> getHomeTimeLine() throws TwitterException {
        return twitter.getHomeTimeline();
    }

    public void retrieveTheTimeline() throws TwitterException {

        Optional<List<Tweet>> userTimeLine = getUserTimeLine();
        if (userTimeLine.isPresent()){
            System.out.println("\nUser Timeline :");
            System.out.println("---------------");
            userTimeLine.get().forEach(status -> System.out.println("\n" + status));
        } else {
            System.out.println("\nUser Timeline is empty.");
        }

        Optional<List<Tweet>> homeTimeLine = getHomeTimeLine();
        if (homeTimeLine.isPresent()) {
            System.out.println("\nHome Timeline :");
            System.out.println("---------------");
            homeTimeLine.get().forEach(status -> System.out.println("\n" + status));
        } else {
            System.out.println("\nHome Timeline is empty.");
        }
    }
}
