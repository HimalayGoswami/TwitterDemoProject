package com.TwitterDemo.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import twitter4j.ResponseList;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.List;

public class TimeLine {
    private ResponseList<Status> statuses;

    public TimeLine(ResponseList<Status> statuses) {
        this.statuses = statuses;
    }

    @JsonProperty
    public List<String> getStatuses() {
        List<String> statusTexts = new ArrayList<>();
        statuses.forEach(status -> statusTexts.add(status.getText()));
        return statusTexts;
    }
}
