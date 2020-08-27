package com.TwitterDemo.resources;

import com.TwitterDemo.services.ITwitter;
import com.TwitterDemo.models.Tweet;
import com.TwitterDemo.resources.TweetResource;
import org.junit.Rule;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.User;

import javax.ws.rs.core.Response;


import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TweetResourceTest {

    @Test
    public void publishTheTweet() throws Exception {
        ITwitter iTwitter = mock(ITwitter.class);

        Status status = mock(Status.class);
        when(status.getText()).thenReturn("dfsdggferr");
        when(iTwitter.publishTheTweet("dfsdggferr")).thenReturn(Optional.of(status));
        User user = mock(User.class);
        when(status.getUser()).thenReturn(user);

        TweetResource tweetResource = new TweetResource(iTwitter);

        Tweet tweet = new Tweet("dfsdggferr");
        Response response = tweetResource.postTweet(tweet);
        assertEquals(tweet.getTweet(), ((Tweet)response.getEntity()).getTweet());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        TwitterException twitterException = new TwitterException("{errors: {'message': 'twitterException'," +
                "'code': '777'}}", new Exception(), 400);
        when(iTwitter.publishTheTweet(tweet.getTweet())).thenThrow(twitterException);
        response = tweetResource.postTweet(tweet);
        String responseEntity = (String) response.getEntity();
        System.out.println(responseEntity);
        System.out.println(twitterException.getMessage());
        assertEquals(twitterException.getMessage(), responseEntity);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

    }
}
