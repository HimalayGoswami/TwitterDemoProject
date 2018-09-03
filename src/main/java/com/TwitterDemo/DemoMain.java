package com.TwitterDemo;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.io.*;
import java.util.Properties;
import java.util.Scanner;

public class DemoMain {

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

            populateAccessKeyToken(prop);

            FileOutputStream out = new FileOutputStream(fileObject);
            prop.store(out, "twitter4j.properties");
            out.close();
            System.out.println("ConsumerKey & consumerSecret Stored.");
            System.out.println("Successfully stored access token to twitter4j.properties.");
        } catch (IOException ioe) {
            ioe.printStackTrace();
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
            if(args.length<2){
                System.out.println("Please pass the [consumer key] [consumer secret] arguments");
                System.exit(-1);
            }
        }
    }

    protected void populateAccessKeyToken(Properties prop) throws IOException {
        try {
            ITwitter iTwitter = ITwitter.getInstance();
            RequestToken requestToken = iTwitter.getOAuthRequestToken();
            System.out.println("Got request token.");
            AccessToken accessToken = null;

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while (null == accessToken) {
                System.out.println("Open the following URL and grant access to your account:");
                System.out.println(requestToken.getAuthorizationURL());

                System.out.print("Enter the PIN(if available) and hit enter after you granted access.[PIN]:");
                String pin = br.readLine();
                try {
                    if (pin.length() > 0) {
                        accessToken = iTwitter.getOAuthAccessToken(requestToken, pin);
                    } else {
                        System.out.println("Incorrect PIN, Please try again.");
                    }
                } catch (TwitterException te) {
                    te.printStackTrace();
                }
            }
            System.out.println("Got access token.");
            System.out.println("Access token: " + accessToken.getToken());
            System.out.println("Access token secret: " + accessToken.getTokenSecret());

            prop.setProperty("oauth.accessToken", accessToken.getToken());
            prop.setProperty("oauth.accessTokenSecret", accessToken.getTokenSecret());

            br.close();
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get accessToken: " + te.getMessage());
            System.exit(-1);
        }
    }

    public void getAccessToken(TwitterDemoConfiguration twitterDemoConfiguration) throws IOException {
        ITwitter iTwitter = ITwitter.getInstance();
        iTwitter.setOAuthConsumer(twitterDemoConfiguration.getConsumerKey(), twitterDemoConfiguration.getConsumerSecret());

        Properties prop = new Properties();
        populateAccessKeyToken(prop);
    }
}