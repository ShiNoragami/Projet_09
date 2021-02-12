package com.openclassrooms.realestatemanager.models.geocodingAPI;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class Geocoding implements Serializable {

    @SerializedName("results")
    private List<Result> mResults;
    @SerializedName("status")
    private String mStatus;

    public List<Result> getResults() {
        return mResults;
    }

    public void setResults(List<Result> results) {
        mResults = results;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}