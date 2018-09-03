package com.TwitterDemo.api;

import com.TwitterDemo.ITwitter;
import com.TwitterDemo.PublishTweet;
import com.codahale.metrics.annotation.Timed;
import twitter4j.TwitterException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/Tweet")
public class TweetResource {

    private PublishTweet publishTweet;

    public TweetResource(ITwitter iTwitter){
        publishTweet = new PublishTweet(iTwitter);
    }

    @POST
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postTweet(Tweet tweet) {
        Tweet status = null;
        try {
            status = publishTweet.publishTheTweet(tweet.getTweet());
        } catch (TwitterException e) {
            e.printStackTrace();
            return Response.status(Response.Status.fromStatusCode(e.getStatusCode())).type(MediaType.APPLICATION_JSON)
                        .entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.OK).entity(status).build();
    }
}
