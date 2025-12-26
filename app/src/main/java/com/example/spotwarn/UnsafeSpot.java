package com.example.spotwarn;

public class UnsafeSpot {
    public String locationName;
    public String reason;
    public double latitude;
    public double longitude;
    public String severity;

    public UnsafeSpot() {}

    public UnsafeSpot(String locationName, String reason, double latitude, double longitude, String severity) {
        this.locationName = locationName;
        this.reason = reason;
        this.latitude = latitude;
        this.longitude = longitude;
        this.severity = severity;
    }
}
