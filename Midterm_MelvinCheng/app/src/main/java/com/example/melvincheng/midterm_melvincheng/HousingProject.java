package com.example.melvincheng.midterm_melvincheng;

public class HousingProject {
    private float latitude;
    private float longitude;
    private String address;
    private String municipality;
    private int numUnits;

    public HousingProject(float latitude, float longitude, String address, String municipality, int numUnits) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.municipality = municipality;
        this.numUnits = numUnits;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public int getNumUnits() {
        return numUnits;
    }

    public void setNumUnits(int numUnits) {
        this.numUnits = numUnits;
    }

    public String getShortSummary() {
        return address + ", " + municipality;
    }

    public String toString() {
        return "" + numUnits + " units at " + address + ", " + municipality + " (" + latitude + ", " + longitude + ")";
    }
}
