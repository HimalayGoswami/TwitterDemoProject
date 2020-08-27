package com.TwitterDemo.Resources;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TimeLine {

    private List<String> statuses;

    public TimeLine(List<String> statuses) {
        this.statuses = statuses;
    }

    @JsonProperty
    public List<String> getStatuses() {
        return statuses;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        } else if (this == obj) {
            return true;
        } else if(obj instanceof TimeLine) {
            TimeLine timeLine = (TimeLine)obj;
            if(this.statuses.size() == timeLine.getStatuses().size()){
                for(int i=0; i<statuses.size(); i++){
                    if(!statuses.get(i).equals(timeLine.getStatuses().get(i))){
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}
