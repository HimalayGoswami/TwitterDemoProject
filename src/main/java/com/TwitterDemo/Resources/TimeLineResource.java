package com.TwitterDemo.resources;

import com.TwitterDemo.models.TimeLine;
import com.TwitterDemo.services.ITwitter;
import com.TwitterDemo.RetrieveTimeline;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.LoggerFactory;
import twitter4j.TwitterException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.invoke.MethodHandles;

@Path("/TimeLine")
@Produces(MediaType.APPLICATION_JSON)
public class TimeLineResource {

    private RetrieveTimeline retrieveTimeline;

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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
            logger.error("Error while getting timeline resource.", e);
            return Response.status(Response.Status.fromStatusCode(e.getStatusCode())).type(MediaType.APPLICATION_JSON)
                    .entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.OK).entity(timeLine).build();
    }
}
