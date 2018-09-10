package com.TwitterDemo.services;

import com.TwitterDemo.models.Tweet;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.*;

public class CacheServiceTest {

    @Test
    public void updateCache() throws ExecutionException {

        Optional<Tweet> statusOptional = Optional.ofNullable(null);
        CacheService cacheService = mock(CacheService.class);
        doCallRealMethod().when(cacheService).updateCache(any(Optional.class));

        cacheService.updateCache(statusOptional);
        verify(cacheService, times(0)).get(any(String.class));

        Tweet tweet = mock(Tweet.class);
        statusOptional = Optional.of(tweet);
        when(tweet.getTweet()).thenReturn("Hello Twitter!");
        when(cacheService.isPresent("Hello")).thenReturn(true);
        when(cacheService.isPresent("Twitter")).thenReturn(false);
        when(cacheService.get("Hello")).thenReturn(Optional.of(new ArrayList<Tweet>()));
        cacheService.updateCache(statusOptional);
        verify(cacheService).get(any(String.class));

        when(cacheService.get(any(String.class))).thenThrow(new ExecutionException("Exception", new Exception()));
        cacheService.updateCache(statusOptional);
        verify(cacheService, times(2)).get(any(String.class));
    }
}
