package com.TwitterDemo;


import com.TwitterDemo.Resources.TimeLineResource;
import com.TwitterDemo.Resources.TweetResource;
import com.TwitterDemo.Services.ITwitter;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class TwitterDemoApplication extends Application<TwitterDemoConfiguration> {

    public static void main(String[] args) throws Exception {
        new TwitterDemoApplication().run(args);
    }

    @Override
    public void run(TwitterDemoConfiguration twitterDemoConfiguration, Environment environment) throws Exception {

        ITwitter iTwitter = ITwitter.getInstance();
        iTwitter.getAccessToken(twitterDemoConfiguration);

        final TimeLineResource timelinesResource = new TimeLineResource(ITwitter.getInstance());
        environment.jersey().register(timelinesResource);

        final TweetResource tweetResource = new TweetResource(ITwitter.getInstance());
        environment.jersey().register(tweetResource);
    }
}
