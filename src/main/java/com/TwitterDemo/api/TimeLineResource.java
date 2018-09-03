package com.TwitterDemo.api;

import com.TwitterDemo.ITwitter;
import com.TwitterDemo.RetrieveTimeline;
import com.codahale.metrics.annotation.Timed;
import twitter4j.TwitterException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/TimeLine")
@Produces(MediaType.APPLICATION_JSON)
public class TimeLineResource {

    private RetrieveTimeline retrieveTimeline;

    public TimeLineResource(ITwitter iTwitter) {
        retrieveTimeline = new RetrieveTimeline(iTwitter);
    }

    @GET
    @Timed
    public Response getTimeLine() {
        TimeLine timeLine = null;
        try {

            timeLine = new TimeLine(retrieveTimeline.getHomeTimeLine());
        } catch (TwitterException e) {
            e.printStackTrace();
            return Response.status(Response.Status.fromStatusCode(e.getStatusCode())).type(MediaType.APPLICATION_JSON)
                    .entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.OK).entity(timeLine).build();
    }
}
