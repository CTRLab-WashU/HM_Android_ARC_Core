package com.healthymedium.arc.api.models;

public class DeviceRegistration {

    public String device_id;           // the unique id for this device
    public String participant_id;      // the user's participant id
    public String device_info;         // format "OS name|device model|OS version", ie "iOS|iPhone8,4|10.1.1"
    public String app_version;         // version of the app
    public String authorization_code;  // the authorization code entered by the user
    public Boolean override;           // (optional) set to true if re-registering an existing user

}
