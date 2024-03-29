package com.healthymedium.arc.study;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

// the distinction between cycles and rhythms is that a ry
public class CircadianRhythm {

    private LocalTime bed;
    private LocalTime wake;
    private String weekday;
    private long lastBedUpdate;
    private long lastWakeUpdate;

    CircadianRhythm(String weekday){
        this.weekday = weekday;
        bed = LocalTime.MIDNIGHT;
        wake = LocalTime.MIDNIGHT;

        long now = DateTime.now().getMillis();
        lastBedUpdate = now;
        lastWakeUpdate = now;
    }

    public void setTimes(String wake, String bed) {
        this.wake = LocalTime.parse(wake);
        lastWakeUpdate = DateTime.now().getMillis();

        this.bed = LocalTime.parse(bed);
        lastBedUpdate = DateTime.now().getMillis();
    }

    public void setTimes(LocalTime wake, LocalTime bed) {
        this.wake = wake;
        lastWakeUpdate = DateTime.now().getMillis();

        this.bed = bed;
        lastBedUpdate = DateTime.now().getMillis();
    }

    public LocalTime getBedTime() {
        return bed;
    }

    public void setBedTime(LocalTime bed) {
        this.bed = bed;
        lastBedUpdate = DateTime.now().getMillis();
    }

    public LocalTime getWakeTime() {
        return wake;
    }

    public void setWakeTime(LocalTime wake) {
        this.wake = wake;
        lastWakeUpdate = DateTime.now().getMillis();
    }

    public String getWeekday() {
        return weekday;
    }

    public boolean isNocturnal(){
        return (wake.isAfter(bed));
    }

    public DateTime lastUpdatedWakedOn(){
        return new DateTime(lastWakeUpdate);
    }

    public DateTime lastUpdatedBedOn(){
        return new DateTime(lastBedUpdate);
    }

}
