package com.openclassrooms.realestatemanager.models.geocodingAPI;

import com.google.gson.annotations.SerializedName;


public class Geometry {

    @SerializedName("location")
    private Location mLocation;
    @SerializedName("location_type")
    private String mLocationType;
    @SerializedName("viewport")
    private Viewport mViewport;

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location location) {
        mLocation = location;
    }

    public String getLocationType() {
        return mLocationType;
    }

    public void setLocationType(String locationType) {
        mLocationType = locationType;
    }

    public Viewport getViewport() {
        return mViewport;
    }

    public void setViewport(Viewport viewport) {
        mViewport = viewport;
    }

}