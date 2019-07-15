package com.healthymedium.arc.study;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TestSessionTest {

    @Test
    public void getConstructorArguments() {

        int id = 0;
        int index = 0;
        int dayIndex = 0;

        TestSession session = new TestSession(dayIndex,index,id);

        Assert.assertEquals(id,session.getId());
        Assert.assertEquals(index,session.getIndex());
        Assert.assertEquals(dayIndex,session.getDayIndex());
    }

    @Test
    public void getDateTimes() {

        DateTime prescribedTime = DateTime.parse("2019-06-26 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss"));

        TestSession session = new TestSession(0,0,0);
        session.setPrescribedTime(prescribedTime);
        Assert.assertEquals(prescribedTime,session.getPrescribedTime());
        Assert.assertEquals(prescribedTime, session.getScheduledTime());
        Assert.assertEquals(prescribedTime.plusHours(2),session.getExpirationTime());
    }

    @Test
    public void getUserModifiedDateTimes() {

        DateTime prescribedTime = DateTime.parse("2019-06-26 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss"));

        TestSession session = new TestSession(0,0,0);
        session.setPrescribedTime(prescribedTime);
        Assert.assertEquals(prescribedTime,session.getPrescribedTime());

        LocalDate scheduledDate = LocalDate.parse("2019-06-26");
        session.setScheduledDate(scheduledDate);

        DateTime scheduledTime = session.getScheduledTime();
        Assert.assertEquals(scheduledDate,scheduledTime.toLocalDate());
        Assert.assertEquals(prescribedTime.toLocalTime(),scheduledTime.toLocalTime());

        DateTime expirationTime = session.getExpirationTime();
        Assert.assertEquals(scheduledDate,expirationTime.toLocalDate());
        Assert.assertEquals(prescribedTime.plusHours(2).toLocalTime(),expirationTime.toLocalTime());

    }

    @Test
    public void getFlagsInit() {

        TestSession session = new TestSession(0,0,0);
        session.setPrescribedTime(DateTime.now().plusHours(1));

        Assert.assertFalse(session.isAvailable());

        Assert.assertFalse(session.wasFinished());
        Assert.assertFalse(session.isFinished());
        Assert.assertFalse(session.isOngoing());
        Assert.assertFalse(session.isOver());
        Assert.assertFalse(session.wasMissed());

    }


    @Test
    public void getFlagsStarted() {

        TestSession session = new TestSession(0,0,0);
        session.setPrescribedTime(DateTime.now().minusHours(1));

        Assert.assertTrue(session.isAvailable());

        session.markStarted();

        Assert.assertFalse(session.wasFinished());
        Assert.assertFalse(session.isFinished());

        Assert.assertTrue(session.isOngoing());
        Assert.assertFalse(session.isOver());
        Assert.assertFalse(session.wasMissed());

    }

    @Test
    public void getFlagsAbandoned() {

        TestSession session = new TestSession(0,0,0);
        session.setPrescribedTime(DateTime.now().minusHours(1));

        Assert.assertTrue(session.isAvailable());

        session.markStarted();
        session.markAbandoned();

        Assert.assertFalse(session.wasFinished());
        Assert.assertFalse(session.isFinished());

        Assert.assertFalse(session.isOngoing());
        Assert.assertTrue(session.isOver());
        Assert.assertFalse(session.wasMissed());

    }

    @Test
    public void getFlagsCompleted() {

        TestSession session = new TestSession(0,0,0);
        session.setPrescribedTime(DateTime.now().minusHours(1));

        Assert.assertTrue(session.isAvailable());

        session.markStarted();
        session.markCompleted();

        Assert.assertTrue(session.wasFinished());
        Assert.assertTrue(session.isFinished());

        Assert.assertFalse(session.isOngoing());
        Assert.assertTrue(session.isOver());
        Assert.assertFalse(session.wasMissed());

    }

    @Test
    public void getFlagsMissed() {

        TestSession session = new TestSession(0,0,0);
        session.setPrescribedTime(DateTime.now().plusHours(3));

        Assert.assertFalse(session.isAvailable());

        session.markMissed();

        Assert.assertFalse(session.wasFinished());
        Assert.assertFalse(session.isFinished());

        Assert.assertFalse(session.isOngoing());
        Assert.assertTrue(session.isOver());
        Assert.assertTrue(session.wasMissed());

    }

    @Test
    public void getFlagsInterrupted() {

        TestSession session = new TestSession(0,0,0);
        session.setPrescribedTime(DateTime.now());

        Assert.assertFalse(session.wasInterrupted());
        session.markInterrupted();
        Assert.assertTrue(session.wasInterrupted());

    }

}