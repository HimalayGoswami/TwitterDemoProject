package com.TwitterDemo.models;

import com.TwitterDemo.models.TimeLine;
import com.TwitterDemo.models.Tweet;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TimeLineTest {

    @Test
    public void equals(){

        List<Tweet> statuses = new ArrayList<>();
        statuses.add(new Tweet("yjjsdfefsdffsdff"));
        statuses.add(new Tweet("s343dfeererf"));
        statuses.add(new Tweet("sdrer33drf"));
        TimeLine timeLine = new TimeLine(statuses);

        List<Tweet> statuses2 = new ArrayList<>();
        statuses2.add(new Tweet("yjjsdfefsdffsdff"));
        statuses2.add(new Tweet("s343dfeererf"));
        statuses2.add(new Tweet("sdrer33drf"));
        TimeLine timeLine2 = new TimeLine(statuses2);

        assertTrue(timeLine.equals(timeLine2));

        assertTrue(timeLine.equals(timeLine));

        statuses2.remove(0);
        assertFalse(timeLine.equals(timeLine2));

        statuses2.add(new Tweet("dsfsd"));
        assertFalse(timeLine.equals(timeLine2));

        timeLine2 = null;
        assertFalse(timeLine.equals(timeLine2));

        Object obj = new Object();
        assertFalse(timeLine.equals(obj));
    }
}
