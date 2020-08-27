package com.TwitterDemo.api;

import com.TwitterDemo.Resources.TimeLine;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TimeLineTest {

    @Test
    public void equals(){

        List<String> statuses = new ArrayList<>();
        statuses.add("yjjsdfefsdffsdff");
        statuses.add("s343dfeererf");
        statuses.add("sdrer33drf");
        TimeLine timeLine = new TimeLine(statuses);

        List<String> statuses2 = new ArrayList<>();
        statuses2.add("yjjsdfefsdffsdff");
        statuses2.add("s343dfeererf");
        statuses2.add("sdrer33drf");
        TimeLine timeLine2 = new TimeLine(statuses2);

        assertTrue(timeLine.equals(timeLine2));

        assertTrue(timeLine.equals(timeLine));

        statuses2.remove(0);
        assertFalse(timeLine.equals(timeLine2));

        statuses2.add("dsfsd");
        assertFalse(timeLine.equals(timeLine2));

        timeLine2 = null;
        assertFalse(timeLine.equals(timeLine2));

        Object obj = new Object();
        assertFalse(timeLine.equals(obj));
    }
}
