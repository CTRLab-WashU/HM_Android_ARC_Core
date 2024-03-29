package com.healthymedium.arc.study;

import com.healthymedium.arc.time.TimeUtil;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class TestCycle {

    private int id;

    private DateTime scheduledStartDate;          // official start date of the TestCycle,
    private DateTime scheduledEndDate;            // official end date of the TestCycle,
    private DateTime actualStartDate;           // user-modified start date of the TestCycle,
    private DateTime actualEndDate;             // user-modified end date of the TestCycle,

    private List<TestDay> days;

    public TestCycle(int id, DateTime start, DateTime end) {
        this.id = id;
        scheduledStartDate = TimeUtil.setMidnight(start);
        scheduledEndDate = TimeUtil.setMidnight(end);
        days = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public DateTime getScheduledStartDate() {
        return scheduledStartDate;
    }

    public DateTime getScheduledEndDate() {
        return scheduledEndDate;
    }

    public DateTime getActualStartDate() {
        if(actualStartDate == null) {
            return scheduledStartDate;
        }
        return actualStartDate;
    }

    public void setActualStartDate(DateTime actualStartDate) {
        this.actualStartDate = TimeUtil.setMidnight(actualStartDate);
    }

    public DateTime getActualEndDate() {
        if(actualEndDate == null) {
            return scheduledEndDate;
        }
        return actualEndDate;
    }

    public void setActualEndDate(DateTime actualEndDate) {
        this.actualEndDate = TimeUtil.setMidnight(actualEndDate);
    }

    public List<TestDay> getTestDays() {
        return days;
    }

    public TestDay getTestDay(int dayIndex) {
        if(dayIndex>=days.size()) {
            return null;
        }
        return days.get(dayIndex);
    }

    public List<TestSession> getTestSessions() {
        List<TestSession> sessions = new ArrayList<>();
        for(TestDay day : days) {
            sessions.addAll(day.sessions);
        }
        return sessions;
    }

    public TestSession getTestSession(int index) {
        int pointer = 0;
        for(TestDay day : days) {
            for(TestSession session : day.sessions) {
                if(pointer==index) {
                    return session;
                }
                pointer++;
            }
        }
        return null;
    }

    public int getNumberOfTestDays() {
        return days.size();
    }

    public int getNumberOfTests() {
        int count = 0;
        for(TestDay day : days) {
            count += day.getNumberOfTests();
        }
        return count;
    }

    public int getNumberOfTestsLeft() {
        int count = 0;
        for(TestDay day : days) {
            count += day.getNumberOfTestsLeft();
        }
        return count;
    }

    public boolean isOver() {
        if(getActualEndDate().isBeforeNow()) {
            return true;
        }
        if(days.size()==0) {
            return true;
        }
        int last = days.size()-1;
        return days.get(last).isOver();
    }

    public boolean hasStarted() {
        if(days.size()==0) {
            return false;
        }
        return days.get(0).hasStarted();
    }

    public boolean hasThereBeenAFinishedTest() {
        for(TestDay day : days) {
            if(day.getNumberOfTestsFinished()>0) {
                return true;
            }
        }
        return false;
    }

    public int getProgress() {
        float progress = 0;
        int numEntries = days.size();
        for(TestDay day : days){
            progress += ((float)day.getProgress()/numEntries);
        }
        return (int) progress;
    }

    public void shiftSchedule(int numDays) {
        for(TestDay day : days) {
            day.setStartTime(day.getStartTime().plusDays(numDays));
            day.setEndTime(day.getEndTime().plusDays(numDays));

            List<TestSession> sessions = day.getTestSessions();
            for(TestSession session : sessions) {
                LocalDate date = session.getPrescribedTime().plusDays(numDays).toLocalDate();
                session.setScheduledDate(date);
            }
        }
        List<TestSession> sessions = getTestSessions();
        int last = sessions.size()-1;
        setActualStartDate(sessions.get(0).getScheduledTime());
        setActualEndDate(sessions.get(last).getScheduledTime().plusDays(1));
    }

   /**
    * 1 schedule was found in Production to have an invalid schedule a.k.a "corrupted".
    * The "corruption" was that the first test scheduled for each day had a start_date for the day before. 
    * The participant reported an issue with tests not starting despite getting notifications. 
    * Our study admin checked their test schedule and noticed that the test dates didn't line up right.
    *
    * To fix the issue, we first check if a schedule is in this invalid format.
    * Then, we re-create the user's schedule and upload it to the server.
    * 
    * This fix was validated by testing different paths with the "corrupted" schedule. 
    * Reinstall to mock user with bad data on two versions. One with the fix the other without. 
    * One corrected itself immediately, the other corrected itself after updating to the new version.
    */
    public boolean isScheduleCorrupted() {
        for(TestDay day : days) {
            if(day.isScheduleCorrupted()){
                return true;
            }
        }
        return false;
    }

}
