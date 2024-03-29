package com.healthymedium.arc.study;

import com.healthymedium.arc.notifications.NotificationManager;
import com.healthymedium.arc.notifications.NotificationNode;
import com.healthymedium.arc.notifications.NotificationTypes;
import android.util.Log;

import com.healthymedium.arc.api.tests.BaseTest;
import com.healthymedium.arc.utilities.PreferencesManager;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.healthymedium.arc.notifications.types.TestMissed.TAG_TEST_MISSED_COUNT;

public class TestSession {

    static private final String tag = "TestSession";

    private int dayIndex;               // The day in the cycle
    private int index;                  // Which test this is on a given day
    private int id;
    private LocalDate scheduledDate;    // The user-modified scheduled date
    private DateTime prescribedTime;    // The original scheduled time

    private DateTime startTime;
    private DateTime completeTime;

    private List<Object> testData = new ArrayList<>();
    private List<Integer> testPercentages = new ArrayList<>();

    private boolean finishedSession;
    private boolean missedSession;
    private int interrupted;

    public TestSession(int dayIndex, int index, int id) {
        this.dayIndex = dayIndex;
        this.index = index;
        this.id = id;
        this.interrupted = -99;
    }

    public int getDayIndex() {
        return dayIndex;
    }

    public int getIndex() {
        return index;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean isFinished() {
        return finishedSession;
    }

    public void markCompleted() {
        completeTime = DateTime.now();
        finishedSession = true;
        Log.i(tag,"markCompleted()");
    }

    public void markAbandoned() {
        completeTime = DateTime.now();
        finishedSession = false;
        Log.i(tag,"markAbandoned()");
    }

    public void markMissed() {
        missedSession = true;
        finishedSession = false;
        Log.i(tag,"markMissed()");
    }

    public int getProgress() {
        float progress = 0;
        int numEntries = testPercentages.size();
        for(Integer percentage : testPercentages){
            progress += ((float)percentage/numEntries);
        }
//        return (progress>0) ? 100:0;
        return (int) progress;
    }

    public void setProgress(int progress) {
        testPercentages.clear();
        testPercentages.add(progress);
        Log.i(tag,"setProgress("+progress+")");
    }

    public DateTime getExpirationTime() {
        if(scheduledDate!=null) {
            return getPrescribedTime().withDate(scheduledDate).plusHours(2);
        }
        return getPrescribedTime().plusHours(2);
    }

    public DateTime getScheduledTime() {
        if(scheduledDate!=null) {
            return getPrescribedTime().withDate(scheduledDate);
        }
        return getPrescribedTime();
    }

    public DateTime getCompletedTime() {
        return completeTime;
    }

    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
        Log.i(tag,"setScheduledDate()");
    }

    public DateTime getPrescribedTime() {
        return prescribedTime;
    }

    public void setPrescribedTime(DateTime prescribedTime) {
        this.prescribedTime = prescribedTime;
        Log.i(tag,"setPrescribedTime()");

    }

    public DateTime getStartTime() {
        return startTime;
    }

    public void markStarted() {
        this.startTime = DateTime.now();

        // this null check lets unit tests work properly
        if(PreferencesManager.getInstance()!=null) {
            int notifyId = NotificationNode.getNotifyId(id, NotificationTypes.TestTake.getId());
            NotificationManager.getInstance().removeUserNotification(notifyId);

            PreferencesManager.getInstance().putInt(TAG_TEST_MISSED_COUNT, 0);
        }
        Log.i(tag,"markStarted()");

    }

    public void addTestData(BaseTest data) {
        Log.i(tag,"addTestData("+data.getClass().getName()+")");
        testData.add(data);
        testPercentages.add(data.getProgress(finishedSession));
    }

    public boolean isOver() {
        return (completeTime!=null || wasMissed());
    }

    public boolean isOngoing() {
        return startTime!=null && completeTime==null && !missedSession;
    }

    public boolean isAvailableNow() {
        DateTime now = DateTime.now();
        return (getScheduledTime().isBefore(now) && getExpirationTime().isAfter(now));
    }

    public List<Object> getTestData() {
        return testData;
    }

    public List<Object> getCopyOfTestData(){
        return Arrays.asList(Arrays.copyOf(testData.toArray(), testData.size()));
    }

    public int wasInterrupted() {
        return interrupted;
    }

    public boolean wasFinished() {
        return finishedSession;
    }

    public boolean wasMissed() {
        return missedSession;
    }

    public void markInterrupted(boolean interrupted) {
        this.interrupted = interrupted?1:0;
        Log.i(tag,"markInterrupted("+interrupted+")");
    }

    public void purgeData(){
        testData.clear();
        interrupted = -99;
        Log.i(tag,"purgeData()");
    }

}
