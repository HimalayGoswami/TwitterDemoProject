package com.TwitterDemo.api;

import com.TwitterDemo.ITwitter;
import org.junit.Test;
import twitter4j.TwitterException;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TweetResourceTest {

    @Test
    public void publishTheTweet() throws TwitterException {
        ITwitter iTwitter = mock(ITwitter.class);

        when(iTwitter.publishTheTweet("dfsdggferr")).thenReturn("dfsdggferr");

        TweetResource tweetResource = new TweetResource(iTwitter);

        Tweet tweet = new Tweet("dfsdggferr");
        Response response = tweetResource.postTweet(tweet);
        assertEquals(tweet, response.getEntity());
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
