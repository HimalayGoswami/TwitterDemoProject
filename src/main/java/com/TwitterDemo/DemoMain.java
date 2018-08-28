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

       getAccessToken(args);

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
                       RetrieveTimeline.retrieveTheTimeline();
                   } catch (TwitterException e) {
                       e.printStackTrace();
                       System.out.println("Failed to retrieve timeline: " + e.getMessage());
                   }
                   break;

               case 2:
                   try {
                       String tweet = PublishTweet.getTweetInput();
                       PublishTweet.publishTheTweet(tweet);
                   } catch (TwitterException te) {
                       te.printStackTrace();
                       System.out.println("Failed to tweet: " + te.getMessage());
                   } catch (Exception e) {
                       e.printStackTrace();
                       System.out.println("Failed to tweet: " + e.getMessage());
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

    protected static void getAccessToken(String[] args) {

        Properties prop = new Properties();
        try {

            try {
                File fileObject = new File("twitter4j.properties");
                InputStream inStream = new FileInputStream(fileObject);
                prop.load(inStream);
                inStream.close();
            } catch (FileNotFoundException e) {
                // consumer key/secret are not set in twitter4j.properties
                System.out.println("Please pass the [consumer key] [consumer secret] arguments");
                System.exit(-1);
            }

            if (null == prop.getProperty("oauth.consumerKey")
                    && null == prop.getProperty("oauth.consumerSecret")) {
                // consumer key/secret are not set in twitter4j.properties
                System.out.println("Please pass the [consumer key] [consumer secret] arguments");
                System.exit(-1);
            }
            InputStream inStream = DemoMain.class.getClassLoader().getResourceAsStream("twitter4j.properties");
            prop.load(inStream);
            inStream.close();
            String oldConsumerKey = prop.getProperty("oauth.consumerKey");
            String oldConsumerSecret = prop.getProperty("oauth.consumerSecret");
            prop.setProperty("oauth.consumerKey", args[0]);
            prop.setProperty("oauth.consumerSecret", args[1]);

            if (oldConsumerKey == null || !oldConsumerKey.equals(args[0])
                    || oldConsumerSecret == null || !oldConsumerSecret.equals(args[1])) {
                prop.remove("oauth.accessToken");
                prop.remove("oauth.accessTokenSecret");
            }

            File fileObject = new File("twitter4j.properties");
            FileOutputStream out = new FileOutputStream(fileObject);
            prop.store(out, "twitter4j.properties");
            out.close();
            System.out.println("ConsumerKey & consumerSecret Stored.");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(-1);
        }
        if (null != prop.getProperty("oauth.accessToken")
                && null != prop.getProperty("oauth.accessTokenSecret")) {
            System.out.println("Access Token & access Token Secret are available.");
            return;
        }

        try {
            Twitter twitter = new TwitterFactory().getInstance();
            RequestToken requestToken = twitter.getOAuthRequestToken();
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
                        accessToken = twitter.getOAuthAccessToken(requestToken, pin);
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
            File fileObject = new File("twitter4j.properties");
            FileOutputStream out = new FileOutputStream(fileObject);
            prop.store(out, "twitter4j.properties");
            out.close();
            System.out.println("Successfully stored access token to twitter4j.properties.");
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get accessToken: " + te.getMessage());
            System.exit(-1);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Failed to read the system input.");
            System.exit(-1);
        }
    }

}