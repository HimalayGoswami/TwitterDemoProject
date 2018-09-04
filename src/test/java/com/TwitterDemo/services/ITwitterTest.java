package com.TwitterDemo.services;

import com.TwitterDemo.NoExitSecurityManager;
import com.TwitterDemo.TwitterDemoConfiguration;
import com.TwitterDemo.models.Tweet;
import com.TwitterDemo.services.ITwitter;
import org.junit.Rule;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.io.BufferedReader;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
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
        when(status.getCreatedAt()).thenReturn(new Date());
        User user = mock(User.class);
        when(user.getName()).thenReturn("userName");
        when(user.getScreenName()).thenReturn("screenName");
        when(user.getProfileImageURL()).thenReturn("PROFILE_IMG_URL");
        when(status.getUser()).thenReturn(user);

        ResponseList<Status> statusResponseList = new ResponseListImplShunt();
        statusResponseList.add(status);

        Twitter twitter = mock(Twitter.class);
        ITwitter iTwitter = getMockedITwitterInstance(twitter);

        when(twitter.getHomeTimeline()).thenReturn(statusResponseList);
        when(twitter.getUserTimeline()).thenReturn(statusResponseList);

        List<Tweet> statuses = iTwitter.getUserTimeline();
        assertTrue("status1".equals(statuses.get(0).getTweet()));

        statuses = iTwitter.getHomeTimeline();
        assertTrue("status1".equals(statuses.get(0).getTweet()));

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
        Status tweetRespStatus = iTwitter.publishTheTweet(tweet);

        verify(twitter).updateStatus(tweet);
        assertEquals(tweet, tweetRespStatus.getText());

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

    @Test
    public void setOAuthConsumer() throws Exception {
        Twitter twitter = mock(Twitter.class);
        ITwitter iTwitter = getMockedITwitterInstance(twitter);
        doNothing().when(twitter).setOAuthConsumer(any(String.class), any(String.class));
        iTwitter.setOAuthConsumer("dfs", "sdfsd");
        verify(twitter).setOAuthConsumer("dfs", "sdfsd");
    }

    @Test
    public void getAccessTokenFromConfiguration() throws Exception {
        ITwitter iTwitter = mock(ITwitter.class);
        doNothing().when(iTwitter).setOAuthConsumer(any(String.class), any(String.class));
        doCallRealMethod().when(iTwitter).getAccessToken(any(TwitterDemoConfiguration.class));
        TwitterDemoConfiguration twitterDemoConfiguration = mock(TwitterDemoConfiguration.class);
        when(twitterDemoConfiguration.getConsumerKey()).thenReturn("consumerKey");
        when(twitterDemoConfiguration.getConsumerSecret()).thenReturn("consumerKeySecret");
        doNothing().when(iTwitter).populateAccessKeyToken(any(Properties.class));
        iTwitter.getAccessToken(twitterDemoConfiguration);
        verify(iTwitter).populateAccessKeyToken(any(Properties.class));
    }

    @Test
    public void populateAccessKeyToken() throws Exception {
        Properties properties = mock(Properties.class);
        Twitter twitter = mock(Twitter.class);
        ITwitter iTwitter = getMockedITwitterInstance(twitter);
        RequestToken requestToken = mock(RequestToken.class);

        when(twitter.getOAuthRequestToken()).thenReturn(requestToken);
        when(requestToken.getAuthorizationURL()).thenReturn("AuthorizationURL");

        String pin = "d4k5tv44ef34f3";
        AccessToken accessToken = mock(AccessToken.class);
        when(twitter.getOAuthAccessToken(requestToken, pin)).thenThrow(new TwitterException("TwitterException"))
                .thenReturn(accessToken);
        when(accessToken.getToken()).thenReturn("dsrf3r335gy");
        when(accessToken.getTokenSecret()).thenReturn("ew34f5898nb77");

        when(properties.setProperty(any(String.class), any(String.class))).thenReturn(new Object());

        BufferedReader bufferedReader = mock(BufferedReader.class);
        whenNew(BufferedReader.class).withAnyArguments().thenReturn(bufferedReader);
        when(bufferedReader.readLine()).thenReturn("").thenReturn(pin);

        //doCallRealMethod().when(iTwitter).populateAccessKeyToken(any(Properties.class));
        iTwitter.populateAccessKeyToken(properties);

        verify(properties, times(2)).setProperty(any(String.class),
                any(String.class));

        when(twitter.getOAuthRequestToken()).thenThrow(new TwitterException("TwitterException"));

        SecurityManager securityManager = System.getSecurityManager();
        System.setSecurityManager(new NoExitSecurityManager());
        try {
            iTwitter.populateAccessKeyToken(properties);
        } catch (NoExitSecurityManager.ExitException e) {
            assertEquals(-1, e.status);
        }

        System.setSecurityManager(securityManager);
        ITwitter.setInstance(null);
    }
}
