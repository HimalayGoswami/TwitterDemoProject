package com.TwitterDemo;


import com.TwitterDemo.api.TimeLineResource;
import com.TwitterDemo.api.TweetResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

import java.util.Arrays;

public class TwitterDemoApplication extends Application<TwitterDemoConfiguration> {

    public static void main(String[] args) throws Exception {
        DemoMain.getAccessToken(args);
        String[] strArgs = Arrays.stream(args).skip(2).toArray(String[]::new);
        new TwitterDemoApplication().run(strArgs);
    }

    @Override
    public void run(TwitterDemoConfiguration twitterDemoConfiguration, Environment environment) throws Exception {
        final TimeLineResource timelinesResource = new TimeLineResource();
        environment.jersey().register(timelinesResource);

        final TweetResource tweetResource = new TweetResource();
        environment.jersey().register(tweetResource);
    }
}
