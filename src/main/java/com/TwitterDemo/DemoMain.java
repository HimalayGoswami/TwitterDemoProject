package com.TwitterDemo;
import com.TwitterDemo.Services.ITwitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.TwitterException;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.util.Properties;
import java.util.Scanner;

public class DemoMain {

    private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static void main(String[] args) {

        DemoMain demoMain = new DemoMain();
        demoMain.getAccessToken(args);

        boolean closeTheApp = false;
        while (closeTheApp != true){
           System.out.println("\nOperations: ");
           System.out.println("1. Retrieve Timeline.");
           System.out.println("2. Publish Tweet.");
           System.out.println("3. Close the Application.");
           System.out.print("Please enter operation Number: ");
           Scanner sc = new Scanner(System.in);
           int opNumber = sc.nextInt();
           switch (opNumber){
               case 1:
                   try {
                       RetrieveTimeline retrieveTimeline = new RetrieveTimeline(ITwitter.getInstance());
                       retrieveTimeline.retrieveTheTimeline();
                   } catch (TwitterException e) {
                       e.printStackTrace();
                       logger.error("Error while retrieving Timeline: ",e);
                       System.out.println("Failed to retrieve timeline: " + e.getMessage());
                   }
                   break;

               case 2:
                   try {
                       PublishTweet publishTweet = new PublishTweet(ITwitter.getInstance());
                       String tweet = publishTweet.getTweetInput();
                       publishTweet.publishTheTweet(tweet);
                   } catch (TwitterException te) {
                       te.printStackTrace();
                       logger.error("Error while publishing the tweet: ", te);
                       System.out.println("Failed to tweet: " + te.getMessage());
                   }
                   break;

               case 3:
                   closeTheApp = true;
                   break;
           }
           if(closeTheApp)
               break;
           }
        System.out.println("Exiting the Twitter Demo Application.");
        System.exit(0);
    }

    protected void getAccessToken(String[] args) {

        try {
            Properties prop = new Properties();

            File fileObject = new File("twitter4j.properties");

            populateExistingUserProperties(fileObject, prop, args);

            String oldConsumerKey = prop.getProperty("oauth.consumerKey");

            if (oldConsumerKey!=null && oldConsumerKey.equals(args[0])) {
                return;
            }

            prop.setProperty("oauth.consumerKey", args[0]);
            prop.setProperty("oauth.consumerSecret", args[1]);

            ITwitter iTwitter = ITwitter.getInstance();
            iTwitter.setOAuthConsumer(args[0], args[1]);

            iTwitter.populateAccessKeyToken(prop);

            FileOutputStream out = new FileOutputStream(fileObject);
            prop.store(out, "twitter4j.properties");
            out.close();
            System.out.println("ConsumerKey & consumerSecret Stored.");
            System.out.println("Successfully stored access token to twitter4j.properties.");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            logger.error("Error while acquiring Access Token: ", ioe);
            System.exit(-1);
        }

    }

    protected void populateExistingUserProperties(File fileObject, Properties prop, String[] args)
            throws IOException {
        try {
            InputStream inStream = new FileInputStream(fileObject);
            prop.load(inStream);
            inStream.close();
        } catch (FileNotFoundException e) {
            // consumer key/secret are not set in twitter4j.properties
            logger.info("twitter4j Properties file doest not exists in directory.");
            if(args.length<2){
                logger.info("Consumer Key & Secret not provided in Arguments.");
                System.out.println("Please pass the [consumer key] [consumer secret] arguments");
                System.exit(-1);
            }
        }
    }

}