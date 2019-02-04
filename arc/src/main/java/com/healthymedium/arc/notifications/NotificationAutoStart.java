package com.healthymedium.arc.notifications;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PersistableBundle;
import android.util.Log;

import com.healthymedium.arc.utilities.PreferencesManager;

public class NotificationAutoStart extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.d("NotificationAutoStart","onReceive");
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            ComponentName serviceComponent = new ComponentName(context, NotificationScheduleJob.class);
            JobInfo.Builder builder = new JobInfo.Builder(50000, serviceComponent);
            builder.setRequiresDeviceIdle(false); // device should be idle
            builder.setRequiresCharging(false); // we don't care if the device is charging or not
            builder.setPersisted(false); // set persistant across reboots

            builder.setMinimumLatency(1);
            builder.setOverrideDeadline(1);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder.setRequiresBatteryNotLow(false);
                builder.setRequiresStorageNotLow(false);
            }

            JobScheduler jobScheduler = (JobScheduler)context.getSystemService(context.JOB_SCHEDULER_SERVICE);
            jobScheduler.schedule(builder.build());
        }
    }
}