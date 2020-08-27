package com.TwitterDemo.api;

import com.TwitterDemo.PublishTweet;
import com.codahale.metrics.annotation.Timed;
import twitter4j.Status;
import twitter4j.TwitterException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/Tweet")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TweetResource {
    @POST
    @Timed
    public Response postTweet(Tweet tweet) {
        Status status = null;
        try {
            status = PublishTweet.publishTheTweet(tweet.getTweet());
        } catch (TwitterException e) {
            e.printStackTrace();
            return Response.status(Response.Status.fromStatusCode(e.getStatusCode())).type(MediaType.APPLICATION_JSON)
                        .entity(e.getErrorMessage()).build();
        }
        return Response.status(Response.Status.OK).entity(status).build();
    }
}
