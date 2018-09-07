package com.TwitterDemo.services;

import com.TwitterDemo.models.Tweet;
import com.google.common.cache.CacheLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twitter4j.TwitterException;

import java.util.List;
import java.util.Optional;

@Component
public class TwitterCacheLoader extends CacheLoader {

    @Autowired
    private ITwitter iTwitter;

    @Override
    public Optional<List<Tweet>> load(Object key) throws TwitterException {
        return iTwitter.getFilteredUserTweets((String) key);
    }
}
