package com.TwitterDemo.models;

import com.TwitterDemo.models.Tweet;
import org.junit.Test;

import static org.junit.Assert.*;

public class TweetTest {

    @Test
    public void equals(){
        Tweet tweet = new Tweet("sdfsc");
        Tweet tweet1 = null;
        assertFalse(tweet.equals(tweet1));

        tweet1 = tweet;
        assertTrue(tweet.equals(tweet1));

        Object obj = new Object();
        assertFalse(tweet.equals(obj));

        tweet1 = new Tweet();
        assertFalse(tweet.equals(tweet1));

        tweet1 = new Tweet("sdfsd");
        assertFalse(tweet.equals(tweet1));

        tweet = new Tweet();
        assertFalse(tweet.equals(tweet1));

        tweet1 = new Tweet();
        assertTrue(tweet.equals(tweet1));

        tweet1 = new Tweet("sdfsd");
        assertFalse(tweet.equals(tweet1));

    }
}
