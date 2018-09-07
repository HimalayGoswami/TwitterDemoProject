package com.TwitterDemo.services;

import com.TwitterDemo.TwitterDemoConfiguration;
import com.TwitterDemo.models.Tweet;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.Weigher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
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
}
