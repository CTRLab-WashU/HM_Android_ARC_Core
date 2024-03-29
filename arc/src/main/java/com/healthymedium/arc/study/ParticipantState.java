package com.healthymedium.arc.study;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class ParticipantState {

    public String id;

    public DateTime studyStartDate;

    public List<TestCycle> testCycles;
    public int currentTestSession;
    public int currentTestCycle;
    public int currentTestDay;

    public Earnings earnings;

    public CircadianClock circadianClock;
    public boolean hasValidSchedule;
    public boolean isStudyRunning;
    public int hasCommittedToStudy;
    public boolean hasBeenShownNotificationOverview;
    public boolean hasBeenShownBatteryOptimizationOverview;

    // These are variables only used during runtime
    public DateTime lastPauseTime;

    public ParticipantState(){
        circadianClock = new CircadianClock();
        lastPauseTime = new DateTime();
        testCycles = new ArrayList<>();
        earnings = new Earnings();
        hasCommittedToStudy = -1;
    }

}
