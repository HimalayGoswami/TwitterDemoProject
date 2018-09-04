package com.TwitterDemo;

import com.TwitterDemo.services.ITwitter;
import org.junit.Rule;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.User;


import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@PrepareForTest(ITwitter.class)
public class PublishTweetTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Test
    public void getTweetInput(){

        String data = "Hello, World!\n";
        InputStream stdin = System.in;
        PublishTweet publishTweet = new PublishTweet();
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        assertEquals("Hello, World!", publishTweet.getTweetInput());
        System.setIn(stdin);
    }

    @Test
    public void main() throws TwitterException {

        ITwitter iTwitter = mock(ITwitter.class);
        PowerMockito.mockStatic(ITwitter.class);
        when(ITwitter.getInstance()).thenReturn(iTwitter);
        Status status = mock(Status.class);
        when(status.getText()).thenReturn("dfsdggferr");
        when(iTwitter.publishTheTweet("dfsdggferr")).thenReturn(status);
        User user = mock(User.class);
        when(status.getUser()).thenReturn(user);
        when(iTwitter.publishTheTweet("Tweet!!!")).thenReturn(status);

        SecurityManager securityManager = System.getSecurityManager();
        System.setSecurityManager(new NoExitSecurityManager());

        InputStream stdin = System.in;

        String data = " \n";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        try{
            PublishTweet.main(new String[]{});
        } catch (NoExitSecurityManager.ExitException e) {
            assertEquals(-2, e.status);
        }

        data = "Tweet!!!\n";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        try{
            PublishTweet.main(new String[]{});
        } catch (NoExitSecurityManager.ExitException e) {
            assertEquals(0, e.status);
        }

        System.setIn(new ByteArrayInputStream(data.getBytes()));
        when(iTwitter.publishTheTweet("Tweet!!!")).thenThrow(new TwitterException("TwitterException"));
        try{
            PublishTweet.main(new String[]{});
        } catch (NoExitSecurityManager.ExitException e) {
            assertEquals(-1, e.status);
        }

        System.setSecurityManager(securityManager);
        System.setIn(stdin);
    }

}
