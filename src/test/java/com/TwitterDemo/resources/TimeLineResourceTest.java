package com.TwitterDemo.resources;

import com.TwitterDemo.models.Tweet;
import com.TwitterDemo.services.CacheService;
import com.TwitterDemo.services.ITwitter;

import com.TwitterDemo.models.TimeLine;
import org.mockito.Mockito;
import twitter4j.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.*;

import javax.ws.rs.core.Response;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class TimeLineResourceTest {

    @Test
    public void getTimeLine() throws TwitterException {

        List<Tweet> statuses = new ArrayList<>();
        statuses.add(new Tweet("yjjsdfefsdffsdff"));
        statuses.add(new Tweet("s343dfeererf"));
        statuses.add(new Tweet("sdrer33drf"));
        TimeLine timeLine = new TimeLine(statuses);

        ITwitter iTwitter = mock(ITwitter.class);

        Mockito.when(iTwitter.getHomeTimeline()).thenReturn(Optional.of(statuses));
        TimeLineResource timeLineResource = new TimeLineResource(iTwitter, null);
        Response response = timeLineResource.getTimeLine();
        TimeLine responseTimeline = ((TimeLine) response.getEntity());
        assertEquals(timeLine, responseTimeline);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        Mockito.when(iTwitter.getHomeTimeline()).thenReturn(Optional.empty());
        response = timeLineResource.getTimeLine();
        responseTimeline = ((TimeLine) response.getEntity());
        assertTrue(responseTimeline == null);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        TwitterException twitterException = new TwitterException("{errors: {'message': 'twitterException'," +
                "'code': '777'}}", new Exception(), 400);
        Mockito.when(iTwitter.getHomeTimeline()).thenThrow(twitterException);
        response = timeLineResource.getTimeLine();
        String responseEntity = (String) response.getEntity();
        System.out.println(responseEntity);
        System.out.println(twitterException.getMessage());
        assertEquals(twitterException.getMessage(), responseEntity);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

    }

    @Test
    public void getFilteredUserTweets() throws TwitterException, ExecutionException {

        List<Tweet> statuses = new ArrayList<>();
        statuses.add(new Tweet("yjjsdfefsdffsdff"));

        String keyword = "ff";
        CacheService cacheService = mock(CacheService.class);
        Mockito.when(cacheService.get(keyword)).thenReturn(Optional.of(statuses));
        TimeLineResource timeLineResource = new TimeLineResource(null, cacheService);
        Response response = timeLineResource.getFilteredUserTweets(keyword);
        TimeLine responseTimeline = ((TimeLine) response.getEntity());
        assertTrue(responseTimeline.getStatuses().size() == 1);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        Mockito.when(cacheService.get(keyword)).thenReturn(Optional.empty());
        response = timeLineResource.getFilteredUserTweets(keyword);
        responseTimeline = ((TimeLine) response.getEntity());
        assertTrue(responseTimeline == null);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());


        TwitterException twitterException = new TwitterException("{errors: {'message': 'twitterException'," +
                "'code': '777'}}");
        ExecutionException executionException = new ExecutionException(twitterException);
        Mockito.when(cacheService.get(keyword)).thenThrow(executionException);
        response = timeLineResource.getFilteredUserTweets(keyword);
        String responseEntity = (String) response.getEntity();
        System.out.println(responseEntity);
        System.out.println(executionException.getMessage());
        assertEquals(executionException.getMessage(), responseEntity);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }


}
