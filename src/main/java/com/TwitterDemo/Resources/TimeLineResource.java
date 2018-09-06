package com.TwitterDemo.resources;

import com.TwitterDemo.models.TimeLine;
import com.TwitterDemo.models.Tweet;
import com.TwitterDemo.services.ITwitter;
import com.TwitterDemo.RetrieveTimeline;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import twitter4j.TwitterException;

import javax.swing.text.html.Option;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Path("/api/1.0/")
@Produces(MediaType.APPLICATION_JSON)
public class TimeLineResource {

    @Autowired
    private RetrieveTimeline retrieveTimeline;

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public TimeLineResource(ITwitter iTwitter) {
        retrieveTimeline = new RetrieveTimeline(iTwitter);
    }

    @GET
    @Timed
    @Path("/TimeLine")
    public Response getTimeLine() {
        TimeLine timeLine = null;
        try {
            Optional<List<Tweet>> tweets = retrieveTimeline.getHomeTimeLine();
            if (tweets.isPresent())
                timeLine = new TimeLine(tweets.get());
        } catch (TwitterException e) {
            e.printStackTrace();
            logger.error("Error while getting timeline resource.", e);
            return Response.status(Response.Status.fromStatusCode(e.getStatusCode())).type(MediaType.APPLICATION_JSON)
                    .entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.OK).entity(timeLine).build();
    }

    @GET
    @Timed
    @Path("/tweet/filter/{keyword}")
    public Response getFilteredUserTweets(@PathParam("keyword") String keyword) {
        TimeLine timeLine = null;
        try {
            Optional<List<Tweet>> tweets = retrieveTimeline.getUserTimeLine();
            if (tweets.isPresent()){
                List<Tweet> filteredTweets = tweets.get().stream()
                        .filter(tweet -> tweet.getTweet().contains(keyword))
                        .collect(Collectors.toList());
                timeLine = new TimeLine(filteredTweets);
            }
        } catch (TwitterException e) {
            e.printStackTrace();
            logger.error("Error while getting timeline resource filtered on keyword: {}.", keyword, e);
            return Response.status(Response.Status.fromStatusCode(e.getStatusCode())).type(MediaType.APPLICATION_JSON)
                    .entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.OK).entity(timeLine).build();
    }
}
