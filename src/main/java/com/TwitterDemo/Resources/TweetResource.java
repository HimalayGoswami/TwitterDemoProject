package com.TwitterDemo.resources;

import com.TwitterDemo.models.Tweet;
import com.TwitterDemo.services.ITwitter;
import com.TwitterDemo.PublishTweet;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import twitter4j.TwitterException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.invoke.MethodHandles;
import java.util.Optional;

@Service
@Path("/Tweet")
public class TweetResource {

    @Autowired
    private PublishTweet publishTweet;

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public TweetResource(ITwitter iTwitter){
        publishTweet = new PublishTweet(iTwitter);
    }

    @POST
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postTweet(Tweet tweet) {
        Optional<Tweet> status = null;
        try {
            status = publishTweet.publishTheTweet(tweet.getTweet());
        } catch (TwitterException te) {
            te.printStackTrace();
            logger.error("Error while Tweeting.", te);
            return Response.status(Response.Status.fromStatusCode(te.getStatusCode())).type(MediaType.APPLICATION_JSON)
                        .entity(te.getMessage()).build();
        }
        return Response.status(Response.Status.OK).entity(status.get()).build();
    }
}
