package com.openclassrooms.realestatemanager.models.geocodingAPI;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;


public class Result implements Serializable {

    @SerializedName("address_components")
    private List<AddressComponent> mAddressComponents;
    @SerializedName("formatted_address")
    private String mFormattedAddress;
    @SerializedName("geometry")
    private Geometry mGeometry;
    @SerializedName("place_id")
    private String mPlaceId;
    @SerializedName("plus_code")
    private PlusCode mPlusCode;
    @SerializedName("types")
    private List<String> mTypes;

    public List<AddressComponent> getAddressComponents() {
        return mAddressComponents;
    }

    public void setAddressComponents(List<AddressComponent> addressComponents) {
        mAddressComponents = addressComponents;
    }

    public String getFormattedAddress() {
        return mFormattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        mFormattedAddress = formattedAddress;
    }

    public Geometry getGeometry() {
        return mGeometry;
    }

    public void setGeometry(Geometry geometry) {
        mGeometry = geometry;
    }

    public String getPlaceId() {
        return mPlaceId;
    }

    public void setPlaceId(String placeId) {
        mPlaceId = placeId;
    }

    public PlusCode getPlusCode() {
        return mPlusCode;
    }

    public void setPlusCode(PlusCode plusCode) {
        mPlusCode = plusCode;
    }

    public List<String> getTypes() {
        return mTypes;
    }

    public void setTypes(List<String> types) {
        mTypes = types;
    }

}