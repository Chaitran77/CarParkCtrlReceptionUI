package com.securitysystems.carparkctrlreceptionui;

import java.util.Date;

public class Log {
    public int EventID;
    public int CameraID;
    public int VehicleID;
    public String Numberplate;
    public Date EntryTimestamp;
    public Date ExitTimestamp;
    public String EntryImageBase64;
    public String ExitImageBase64;

    public boolean Acknowledged;
    public boolean KnownVehicle;
    // https://stackoverflow.com/a/42884828 to store dates/times
    // client.query will return a timestamp String in the promise result rows

    Log(int EventID, int CameraID, int VehicleID, String Numberplate, Date EntryTimestamp, Date ExitTimestamp, String EntryImageBase64, String ExitImageBase64, boolean Acknowledged, boolean KnownVehicle) {
        this.EventID = EventID;
        this.CameraID = CameraID;
        this.VehicleID = VehicleID;
        this.Numberplate = Numberplate;
        this.EntryTimestamp = EntryTimestamp;
        this.ExitTimestamp = ExitTimestamp;
        this.EntryImageBase64 = EntryImageBase64;
        this.ExitImageBase64 = ExitImageBase64;
        this.Acknowledged = Acknowledged;
        this.KnownVehicle = KnownVehicle;
    }
}
