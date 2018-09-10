package com.TwitterDemo.services;

import com.TwitterDemo.TwitterDemoConfiguration;
import com.TwitterDemo.models.Tweet;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.Weigher;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class CacheService {

    private LoadingCache cache;

    @Autowired
    private TwitterDemoConfiguration twitterDemoConfiguration;

    @Autowired
    private TwitterCacheLoader cacheLoader;

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @PostConstruct
    public void init(){
        Weigher<String, Optional<List<Tweet>>> weigher = new Weigher<String, Optional<List<Tweet>>>() {
            @Override
            public int weigh(String key, Optional<List<Tweet>> value) {
                if(value.isPresent())
                    return value.get().size();
                return 1;
            }
        };
        cache = CacheBuilder.newBuilder().
                maximumWeight(twitterDemoConfiguration.getMaxFilterCacheWeight()).
                weigher(weigher).
                expireAfterAccess(1, TimeUnit.DAYS).
                build(cacheLoader);
    }

    public Optional<List<Tweet>> get(String keyword) throws ExecutionException {
        return (Optional<List<Tweet>>) cache.get(keyword);
    }

    public void updateCache(Optional<Tweet> status) {
        if(!status.isPresent())
            return;;

        List<String> tweetWords = Arrays.asList(status.get().getTweet().split(" "));
        tweetWords.forEach(word -> {
            if(isPresent(word)) {
                try {
                    Optional<List<Tweet>> value = get(word);
                    List<Tweet> tweets = value.get();
                    tweets.add(0, status.get());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    logger.error("Error while updating the Cache with new Tweet", e);
                }
            }
        });
    }

    protected boolean isPresent(String word) {
        return cache.getIfPresent(word) != null;
    }
}
