package com.TwitterDemo;

import com.TwitterDemo.api.Tweet;
import org.junit.Rule;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.io.*;
import java.util.NoSuchElementException;
import java.util.Properties;

import static com.TwitterDemo.ITwitterTest.getMockedITwitterInstance;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

@PrepareForTest({DemoMain.class, RetrieveTimeline.class, PublishTweet.class, Properties.class,
        File.class, FileInputStream.class, FileOutputStream.class, FileNotFoundException.class,
        RequestToken.class, ITwitter.class, Twitter.class, AccessToken.class, BufferedReader.class})
public class DemoMainTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Test
    public void main() throws Exception {

        DemoMain demoMain = mock(DemoMain.class);
        PowerMockito.whenNew(DemoMain.class).withAnyArguments().thenReturn(demoMain);
        doNothing().when(demoMain).getAccessToken(any(String[].class));

        InputStream stdin = System.in;

        String data = "1\n";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        RetrieveTimeline retrieveTimeline = mock(RetrieveTimeline.class);
        PowerMockito.whenNew(RetrieveTimeline.class).withAnyArguments().thenReturn(retrieveTimeline);
        doNothing().when(retrieveTimeline).retrieveTheTimeline();

        try {
            DemoMain.main(new String[]{});
        } catch (NoSuchElementException e) {
            verify(retrieveTimeline).retrieveTheTimeline();
        }

        System.setIn(new ByteArrayInputStream(data.getBytes()));
        TwitterException twitterException = mock(TwitterException.class);
        doThrow(twitterException).when(retrieveTimeline).retrieveTheTimeline();

        try {
            DemoMain.main(new String[]{});
        } catch (NoSuchElementException e) {
            verify(twitterException).printStackTrace();
        }

        data = "2\n";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        PublishTweet publishTweet = mock(PublishTweet.class);
        PowerMockito.whenNew(PublishTweet.class).withAnyArguments().thenReturn(publishTweet);
        when(publishTweet.getTweetInput()).thenReturn("Hello Twitter!");
        Tweet tweet = new Tweet("Hello Twitter!");
        when(publishTweet.publishTheTweet("Hello Twitter!")).thenReturn(tweet);

        try {
            DemoMain.main(new String[]{});
        } catch (NoSuchElementException e) {
            verify(publishTweet).publishTheTweet("Hello Twitter!");
        }

        System.setIn(new ByteArrayInputStream(data.getBytes()));
        when(publishTweet.publishTheTweet("Hello Twitter!")).thenThrow(twitterException);

        try {
            DemoMain.main(new String[]{});
        } catch (NoSuchElementException e) {
            verify(twitterException,times(2)).printStackTrace();
        }

        SecurityManager securityManager = System.getSecurityManager();
        System.setSecurityManager(new NoExitSecurityManager());
        data = "3\n";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        try{
            DemoMain.main(new String[]{});
        } catch (NoExitSecurityManager.ExitException e) {
            assertEquals(0, e.status);
        }
        System.setSecurityManager(securityManager);

        System.setIn(stdin);
    }

    @Test
    public void getAccessToken() throws Exception {

        SecurityManager securityManager = System.getSecurityManager();
        System.setSecurityManager(new NoExitSecurityManager());

        DemoMain demoMain = mock(DemoMain.class);
        doCallRealMethod().when(demoMain).getAccessToken(any(String[].class));
        doNothing().when(demoMain).populateExistingUserProperties(any(File.class),
                any(Properties.class), any(String[].class));
        doNothing().when(demoMain).pupulateDefaultTwitter4jProperties(any(Properties.class));
        doThrow(new IOException()).when(demoMain).populateAccessKeyToken(any(Properties.class));
        Properties properties = mock(Properties.class);
        PowerMockito.whenNew(Properties.class).withAnyArguments().thenReturn(properties);
        when(properties.getProperty("oauth.consumerKey")).thenReturn("UserKey");

        demoMain.getAccessToken(new String[]{"UserKey", "SecretKey"});

        when(properties.getProperty("oauth.consumerKey")).thenReturn("NotUserKey");
        try{
            demoMain.getAccessToken(new String[]{"UserKey", "SecretKey"});
        } catch (NoExitSecurityManager.ExitException e) {
            assertEquals(e.status, -1);
        }
        System.setSecurityManager(securityManager);

        doNothing().when(demoMain).populateAccessKeyToken(any(Properties.class));
        FileOutputStream fileOutputStream = mock(FileOutputStream.class);
        PowerMockito.whenNew(FileOutputStream.class).withAnyArguments().thenReturn(fileOutputStream);
        doNothing().when(fileOutputStream).close();
        doNothing().when(properties).store(any(OutputStream.class), any(String.class));
        demoMain.getAccessToken(new String[]{"UserKey", "SecretKey"});
        verify(fileOutputStream).close();

    }

    @Test
    public void populateExistingUserProperties() throws Exception {

        SecurityManager securityManager = System.getSecurityManager();
        System.setSecurityManager(new NoExitSecurityManager());

        FileInputStream fileInputStream = mock(FileInputStream.class);
        PowerMockito.whenNew(FileInputStream.class).withAnyArguments().thenReturn(fileInputStream);
        doNothing().when(fileInputStream).close();
        Properties properties = mock(Properties.class);
        doNothing().when(properties).load(fileInputStream);
        String[] args = new String[]{};
        DemoMain demoMain = new DemoMain();
        demoMain.populateExistingUserProperties(null, properties, args);
        verify(fileInputStream).close();

        File file = mock(File.class);
        PowerMockito.whenNew(FileInputStream.class).withArguments(file)
                .thenThrow(new FileNotFoundException());
        try {
            demoMain.populateExistingUserProperties(file, properties, args);
        } catch (NoExitSecurityManager.ExitException exitException) {
            assertEquals(-1, exitException.status);
        }
        System.setSecurityManager(securityManager);

        args = new String[]{"userKey, userSecretKey"};
        demoMain.populateExistingUserProperties(null, properties, args);
    }

    @Test
    public void pupulateDefaultTwitter4jProperties() throws IOException {
        Properties properties = mock(Properties.class);
        doNothing().when(properties).load(any(InputStream.class));
        DemoMain demoMain = new DemoMain();
        demoMain.pupulateDefaultTwitter4jProperties(properties);
        verify(properties).load(any(InputStream.class));
    }

    @Test
    public void populateAccessKeyToken() throws Exception {
        Properties properties = mock(Properties.class);
        Twitter twitter = mock(Twitter.class);
        ITwitter iTwitter = getMockedITwitterInstance(twitter);
        RequestToken requestToken = mock(RequestToken.class);

        when(iTwitter.getOAuthRequestToken()).thenReturn(requestToken);
        when(requestToken.getAuthorizationURL()).thenReturn("AuthorizationURL");

        String pin = "d4k5tv44ef34f3";
        AccessToken accessToken = mock(AccessToken.class);
        when(iTwitter.getOAuthAccessToken(requestToken, pin)).thenThrow(new TwitterException("TwitterException"))
            .thenReturn(accessToken);
        when(accessToken.getToken()).thenReturn("dsrf3r335gy");
        when(accessToken.getTokenSecret()).thenReturn("ew34f5898nb77");

        when(properties.setProperty(any(String.class), any(String.class))).thenReturn(new Object());

        BufferedReader bufferedReader = mock(BufferedReader.class);
        whenNew(BufferedReader.class).withAnyArguments().thenReturn(bufferedReader);
        when(bufferedReader.readLine()).thenReturn("").thenReturn(pin);

        DemoMain demoMain = new DemoMain();
        demoMain.populateAccessKeyToken(properties);

        verify(properties, times(2)).setProperty(any(String.class),
            any(String.class));

        when(iTwitter.getOAuthRequestToken()).thenThrow(new TwitterException("TwitterException"));

        SecurityManager securityManager = System.getSecurityManager();
        System.setSecurityManager(new NoExitSecurityManager());
        try {
            demoMain.populateAccessKeyToken(properties);
        } catch (NoExitSecurityManager.ExitException e) {
            assertEquals(-1, e.status);
        }

        System.setSecurityManager(securityManager);
        ITwitter.setInstance(null);
    }
}
