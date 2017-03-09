package com.tovi.ddwork.work;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href='mailto:zhaotengfei9@gmail.com'>Tengfei Zhao</a>
 */
public class SynchronizationTest {
    @Test
    public void checkWeek() throws Exception {
        // 周日
        assertFalse(Synchronization.checkWeek(0));
        // 周六
        assertFalse(Synchronization.checkWeek(6));

        assertTrue(Synchronization.checkWeek(1));
        assertTrue(Synchronization.checkWeek(2));
        assertTrue(Synchronization.checkWeek(3));
        assertTrue(Synchronization.checkWeek(4));
        assertTrue(Synchronization.checkWeek(5));

    }

    @Test
    public void checkTimeFrame() throws Exception {
        long curTime;
        long onWorkTime = new Date(0, 0, 0, 7, 30, 0).getTime();
        long offWorkTime = new Date(0, 0, 0, 18, 30, 0).getTime();
        long frameSize = 1 * 60 * 60 * 1000;

        /*** onWork ==================================== ***/
        curTime = new Date(0, 0, 0, 6, 0, 0).getTime();
        assertFalse(Synchronization.checkTimeFrame(curTime, onWorkTime, offWorkTime, frameSize));
        curTime = new Date(0, 0, 0, 6, 29, 0).getTime();
        assertFalse(Synchronization.checkTimeFrame(curTime, onWorkTime, offWorkTime, frameSize));

        curTime = new Date(0, 0, 0, 6, 30, 0).getTime();
        assertTrue(Synchronization.checkTimeFrame(curTime, onWorkTime, offWorkTime, frameSize));
        curTime = new Date(0, 0, 0, 6, 31, 0).getTime();
        assertTrue(Synchronization.checkTimeFrame(curTime, onWorkTime, offWorkTime, frameSize));
        curTime = new Date(0, 0, 0, 7, 0, 0).getTime();
        assertTrue(Synchronization.checkTimeFrame(curTime, onWorkTime, offWorkTime, frameSize));
        curTime = new Date(0, 0, 0, 7, 29, 0).getTime();
        assertTrue(Synchronization.checkTimeFrame(curTime, onWorkTime, offWorkTime, frameSize));
        curTime = new Date(0, 0, 0, 7, 30, 0).getTime();
        assertTrue(Synchronization.checkTimeFrame(curTime, onWorkTime, offWorkTime, frameSize));

        curTime = new Date(0, 0, 0, 7, 31, 0).getTime();
        assertFalse(Synchronization.checkTimeFrame(curTime, onWorkTime, offWorkTime, frameSize));
        curTime = new Date(0, 0, 0, 8, 0, 0).getTime();
        assertFalse(Synchronization.checkTimeFrame(curTime, onWorkTime, offWorkTime, frameSize));

        /*** offWork ==================================== ***/
        curTime = new Date(0, 0, 0, 17, 0, 0).getTime();
        assertFalse(Synchronization.checkTimeFrame(curTime, onWorkTime, offWorkTime, frameSize));
        curTime = new Date(0, 0, 0, 17, 29, 0).getTime();
        assertFalse(Synchronization.checkTimeFrame(curTime, onWorkTime, offWorkTime, frameSize));

        curTime = new Date(0, 0, 0, 17, 30, 0).getTime();
        assertTrue(Synchronization.checkTimeFrame(curTime, onWorkTime, offWorkTime, frameSize));
        curTime = new Date(0, 0, 0, 17, 31, 0).getTime();
        assertTrue(Synchronization.checkTimeFrame(curTime, onWorkTime, offWorkTime, frameSize));
        curTime = new Date(0, 0, 0, 18, 0, 0).getTime();
        assertTrue(Synchronization.checkTimeFrame(curTime, onWorkTime, offWorkTime, frameSize));
        curTime = new Date(0, 0, 0, 18, 29, 0).getTime();
        assertTrue(Synchronization.checkTimeFrame(curTime, onWorkTime, offWorkTime, frameSize));
        curTime = new Date(0, 0, 0, 18, 30, 0).getTime();
        assertTrue(Synchronization.checkTimeFrame(curTime, onWorkTime, offWorkTime, frameSize));

        curTime = new Date(0, 0, 0, 18, 31, 0).getTime();
        assertFalse(Synchronization.checkTimeFrame(curTime, onWorkTime, offWorkTime, frameSize));
        curTime = new Date(0, 0, 0, 19, 0, 0).getTime();
        assertFalse(Synchronization.checkTimeFrame(curTime, onWorkTime, offWorkTime, frameSize));
    }

    @Test
    public void checkMinute() throws Exception {
        assertFalse(Synchronization.checkMinute(10, 0));
        assertTrue(Synchronization.checkMinute(0, 10));

        assertFalse(Synchronization.checkMinute(10, 0));
        assertTrue(Synchronization.checkMinute(10, 10));
        assertTrue(Synchronization.checkMinute(50, 10));


        assertFalse(Synchronization.checkMinute(1, 0));
        assertFalse(Synchronization.checkMinute(12, 0));
        assertFalse(Synchronization.checkMinute(59, 0));

        assertFalse(Synchronization.checkMinute(1, 10));
        assertFalse(Synchronization.checkMinute(12, 10));
        assertFalse(Synchronization.checkMinute(59, 10));
    }

    @Test
    public void isBefore() throws Exception {
        long curTime;
        long onWorkTime = new Date(0, 0, 0, 7, 30, 0).getTime();
        long offWorkTime = new Date(0, 0, 0, 18, 30, 0).getTime();
        long beforeSize = 2 * 60 * 1000;

        /*** onWork ==================================== ***/
        curTime = new Date(0, 0, 0, 7, 0, 0).getTime();
        assertFalse(Synchronization.isBefore(curTime, onWorkTime, offWorkTime, beforeSize));
        curTime = new Date(0, 0, 0, 7, 27, 0).getTime();
        assertFalse(Synchronization.isBefore(curTime, onWorkTime, offWorkTime, beforeSize));

        curTime = new Date(0, 0, 0, 7, 28, 0).getTime();
        assertTrue(Synchronization.isBefore(curTime, onWorkTime, offWorkTime, beforeSize));

        curTime = new Date(0, 0, 0, 7, 29, 0).getTime();
        assertFalse(Synchronization.isBefore(curTime, onWorkTime, offWorkTime, beforeSize));
        curTime = new Date(0, 0, 0, 7, 30, 0).getTime();
        assertFalse(Synchronization.isBefore(curTime, onWorkTime, offWorkTime, beforeSize));
        curTime = new Date(0, 0, 0, 8, 0, 0).getTime();
        assertFalse(Synchronization.isBefore(curTime, onWorkTime, offWorkTime, beforeSize));


        /*** onWork ==================================== ***/
        curTime = new Date(0, 0, 0, 18, 0, 0).getTime();
        assertFalse(Synchronization.isBefore(curTime, onWorkTime, offWorkTime, beforeSize));
        curTime = new Date(0, 0, 0, 18, 27, 0).getTime();
        assertFalse(Synchronization.isBefore(curTime, onWorkTime, offWorkTime, beforeSize));

        curTime = new Date(0, 0, 0, 18, 28, 0).getTime();
        assertTrue(Synchronization.isBefore(curTime, onWorkTime, offWorkTime, beforeSize));

        curTime = new Date(0, 0, 0, 18, 29, 0).getTime();
        assertFalse(Synchronization.isBefore(curTime, onWorkTime, offWorkTime, beforeSize));
        curTime = new Date(0, 0, 0, 18, 30, 0).getTime();
        assertFalse(Synchronization.isBefore(curTime, onWorkTime, offWorkTime, beforeSize));
        curTime = new Date(0, 0, 0, 19, 0, 0).getTime();
        assertFalse(Synchronization.isBefore(curTime, onWorkTime, offWorkTime, beforeSize));
    }
}