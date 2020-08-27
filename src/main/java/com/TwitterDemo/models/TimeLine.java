package com.TwitterDemo.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TimeLine {

    private List<Tweet> tweets;

    public TimeLine(List<Tweet> statuses) {
        this.tweets = statuses;
    }

    @JsonProperty
    public List<Tweet> getStatuses() {
        return tweets;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        } else if (this == obj) {
            return true;
        } else if(obj instanceof TimeLine) {
            TimeLine timeLine = (TimeLine)obj;
            if(this.tweets.size() == timeLine.getStatuses().size()){
                for(int i=0; i<tweets.size(); i++){
                    if(!tweets.get(i).equals(timeLine.getStatuses().get(i))){
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}
