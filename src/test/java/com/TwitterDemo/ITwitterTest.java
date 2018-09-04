package com.TwitterDemo;

import org.junit.Rule;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

@PrepareForTest({ITwitter.class, Twitter.class, TwitterFactory.class, Status.class,
RequestToken.class, AccessToken.class})
public class ITwitterTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Test
    public void getInstance() throws Exception {

        Twitter twitter = mock(Twitter.class);
        ITwitter iTwitter = getMockedITwitterInstance(twitter);
        assertTrue(twitter == iTwitter.getTwitter4jInstance());
    }

    private static class ResponseListImplShunt extends ArrayList implements ResponseList {

        @Override
        public RateLimitStatus getRateLimitStatus() {
            return null;
        }

        @Override
        public int getAccessLevel() {
            return 0;
        }
    }

    @Test
    public void getTimeLines() throws Exception {

        Status status = mock(Status.class);
        when(status.getText()).thenReturn("status1");

        ResponseList<Status> statusResponseList = new ResponseListImplShunt();
        statusResponseList.add(status);

        Twitter twitter = mock(Twitter.class);
        ITwitter iTwitter = getMockedITwitterInstance(twitter);

        when(twitter.getHomeTimeline()).thenReturn(statusResponseList);
        when(twitter.getUserTimeline()).thenReturn(statusResponseList);

        List<String> statuses = iTwitter.getUserTimeline();
        assertTrue("status1".equals(statuses.get(0)));

        statuses = iTwitter.getHomeTimeline();
        assertTrue("status1".equals(statuses.get(0)));

        iTwitter.setInstance(null);
    }

    @Test
    public void publishTheTweet() throws Exception {

        Status status = mock(Status.class);
        Twitter twitter = mock(Twitter.class);
        ITwitter iTwitter = getMockedITwitterInstance(twitter);


        String tweet = "Hello Twitter!!!";
        when(status.getText()).thenReturn(tweet);
        when(twitter.updateStatus(tweet)).thenReturn(status);
        String tweetText = iTwitter.publishTheTweet(tweet);

        verify(twitter.updateStatus(tweet));
        assertEquals(tweet, tweetText);

        ITwitter.setInstance(null);
    }

    protected static ITwitter getMockedITwitterInstance(Twitter twitter) throws Exception {
        ITwitter.setInstance(null);
        TwitterFactory twitterFactory = mock(TwitterFactory.class);
        PowerMockito.whenNew(TwitterFactory.class).withAnyArguments().thenReturn(twitterFactory);
        when(twitterFactory.getInstance()).thenReturn(twitter);
        return ITwitter.getInstance();
    }

    @Test
    public void getOAuthRequestToken() throws Exception {
        Twitter twitter = mock(Twitter.class);
        ITwitter iTwitter = getMockedITwitterInstance(twitter);
        RequestToken requestToken = mock(RequestToken.class);
        when(twitter.getOAuthRequestToken()).thenReturn(requestToken);
        assertTrue(requestToken == iTwitter.getOAuthRequestToken());
    }

    @Test
    public void getOAuthAccessToken() throws Exception {
        Twitter twitter = mock(Twitter.class);
        ITwitter iTwitter = getMockedITwitterInstance(twitter);
        AccessToken accessToken = mock(AccessToken.class);
        RequestToken requestToken = mock(RequestToken.class);
        String pin = "fsf3k44kd4eced";
        when(iTwitter.getOAuthAccessToken(requestToken, pin)).thenReturn(accessToken);
        assertTrue(accessToken == iTwitter.getOAuthAccessToken(requestToken, pin));
    }
}
