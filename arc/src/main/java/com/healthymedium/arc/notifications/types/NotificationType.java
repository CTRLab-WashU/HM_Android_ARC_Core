package com.healthymedium.arc.notifications.types;

import android.content.Context;
import android.support.annotation.RawRes;

import com.healthymedium.arc.notifications.NotificationNode;

public abstract class NotificationType {

    protected int id;
    protected int importance;
    protected String channelId;
    protected String channelName;
    protected String channelDesc;
    protected String extra;
    protected boolean proctored;

    @RawRes
    protected int soundResource = -1;

    public NotificationType(){

    }

    public int getId(){
        return id;
    }

    public String getChannelId(){
        return channelId;
    }

    public String getChannelName(){
        return channelName;
    }

    public String getChannelDesc(){
        return channelDesc;
    }

    public boolean hasExtra(){
        return extra!=null;
    }

    public String getExtra(){
        return extra;
    }

    public int getImportance(){
        return importance;
    }

    public boolean isProctored(){
        return proctored;
    }

    public int getSoundResource() {
        return soundResource;
    }

    // abstract methods ----------------------------------------------------------------------------

    public abstract String getContent(Context context);

    // the return value dictates whether or not the notification is shown to the user
    public abstract boolean onNotifyPending(NotificationNode node);

}