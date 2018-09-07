package com.TwitterDemo;


import com.TwitterDemo.services.ITwitter;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.ws.rs.Path;
import java.util.Map;

public class TwitterDemoApplication extends Application<TwitterDemoConfiguration> {

    public static void main(String[] args) throws Exception {
        new TwitterDemoApplication().run(args);
    }

    @Override
    public void run(TwitterDemoConfiguration twitterDemoConfiguration, Environment environment) throws Exception {

        AnnotationConfigWebApplicationContext parent = new AnnotationConfigWebApplicationContext();
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();

        parent.refresh();
        parent.getBeanFactory().registerSingleton("configuration", twitterDemoConfiguration);
        parent.registerShutdownHook();
        parent.start();

        //the real main app context has a link to the parent context
        ctx.setParent(parent);
        ctx.register(MyAppSpringConfiguration.class);
        ctx.refresh();
        ctx.registerShutdownHook();
        ctx.start();

        //resources
        Map<String, Object> resources = ctx.getBeansWithAnnotation(Path.class);
        for(Map.Entry<String,Object> entry : resources.entrySet()) {
            environment.jersey().register(entry.getValue());
        }

        ITwitter iTwitter = ctx.getBean(ITwitter.class);
        iTwitter.getAccessToken(twitterDemoConfiguration);

    }
}
