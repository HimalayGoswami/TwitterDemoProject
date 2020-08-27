package com.TwitterDemo.api;

import com.TwitterDemo.Services.ITwitter;

import com.TwitterDemo.Resources.TimeLine;
import com.TwitterDemo.Resources.TimeLineResource;
import org.mockito.Mockito;
import twitter4j.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.*;

import javax.ws.rs.core.Response;
import java.util.*;

public class TimeLineResourceTest {

    @Test
    public void getTimeLine() throws TwitterException {

        List<String> statuses = new ArrayList<>();
        statuses.add("yjjsdfefsdffsdff");
        statuses.add("s343dfeererf");
        statuses.add("sdrer33drf");
        TimeLine timeLine = new TimeLine(statuses);

        ITwitter iTwitter = mock(ITwitter.class);

        Mockito.when(iTwitter.getHomeTimeline()).thenReturn(statuses);
        TimeLineResource timeLineResource = new TimeLineResource(iTwitter);
        Response response = timeLineResource.getTimeLine();
        TimeLine responseTimeline = ((TimeLine) response.getEntity());
        assertEquals(timeLine, responseTimeline);
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

}
