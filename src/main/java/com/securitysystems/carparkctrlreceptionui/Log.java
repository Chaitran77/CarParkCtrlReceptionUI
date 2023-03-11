package com.securitysystems.carparkctrlreceptionui;

import java.io.IOException;
import java.util.Date;

public class Log {
    // using the Integer class rather than int data type (for numerical attrs.) to permit possible null values received: https://stackoverflow.com/a/2254463/7169383

    public Integer EventID;
    public Integer CameraID;
    public Integer VehicleID;
    public String Numberplate;
    public Date EntryTimestamp;
    public Date ExitTimestamp;
    public String EntryImageBase64;
    public String ExitImageBase64;
    public boolean KnownVehicle;
    public Tenant tenant;

    Log(Integer EventID, Integer CameraID, Integer VehicleID, String Numberplate, Date EntryTimestamp, Date ExitTimestamp, String EntryImageBase64, String ExitImageBase64, boolean KnownVehicle) {
        this.EventID = EventID;
        this.CameraID = CameraID;
        this.VehicleID = VehicleID;
        this.Numberplate = Numberplate;
        this.EntryTimestamp = EntryTimestamp;
        this.ExitTimestamp = ExitTimestamp;
        this.EntryImageBase64 = EntryImageBase64;
        this.ExitImageBase64 = ExitImageBase64;
        this.KnownVehicle = KnownVehicle;
    }

    @Override
    public String toString() {
        return "Log{" +
//                "EventID=" + EventID +
//                ", CameraID=" + CameraID +
//                ", VehicleID=" + VehicleID +
                ", Numberplate='" + Numberplate + '\'' +
//                ", EntryTimestamp=" + EntryTimestamp +
//                ", ExitTimestamp=" + ExitTimestamp +
//                ", EntryImageBase64='" + " EntryImageBase64 " + '\'' +
//                ", ExitImageBase64='" + " ExitImageBase64 " + '\'' +
//                ", KnownVehicle=" + KnownVehicle +
                '}';
    }

    public void setTenant() throws IOException {
        this.tenant = HttpRequester.getTenantDataFromLogID(this.EventID);
    }

    public static int compareNumberplate(Log a, Log b) {
        return a.Numberplate.compareToIgnoreCase(b.Numberplate);
    }
}
