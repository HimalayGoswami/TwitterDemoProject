package com.TwitterDemo;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import twitter4j.TwitterException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;

@PrepareForTest(ITwitter.class)
public class RetrieveTimelineTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Test
    public void retrieveTheTimeLine() throws TwitterException {
        ITwitter iTwitter = mock(ITwitter.class);

        RetrieveTimeline retrieveTimeline = new RetrieveTimeline(iTwitter);

        retrieveTimeline.retrieveTheTimeline();
        verify(iTwitter).getHomeTimeline();
        verify(iTwitter).getUserTimeline();
    }

    @Test
    public void main() throws TwitterException {
        SecurityManager securityManager = System.getSecurityManager();
        System.setSecurityManager(new NoExitSecurityManager());

        ITwitter iTwitter = Mockito.mock(ITwitter.class);
        PowerMockito.mockStatic(ITwitter.class);
        when(ITwitter.getInstance()).thenReturn(iTwitter);

        List<String> userTweets = new ArrayList<>();
        userTweets.add("userTweet1");
        userTweets.add("userTweet2");

        List<String> homeTweets = new ArrayList<>();
        homeTweets.add("homeTweet1");
        homeTweets.add("homeTweet2");

        when(iTwitter.getHomeTimeline()).thenReturn(userTweets);
        when(iTwitter.getUserTimeline()).thenReturn(homeTweets);
        try{
            RetrieveTimeline.main(new String[]{});
        } catch (NoExitSecurityManager.ExitException e) {
            assertEquals(0, e.status);
        }

        when(iTwitter.getHomeTimeline()).thenThrow(new TwitterException("twitterException"));
        try{
            RetrieveTimeline.main(new String[]{});
        } catch (NoExitSecurityManager.ExitException e) {
            assertEquals(-1, e.status);
        }

        System.setSecurityManager(securityManager);
    }
}
