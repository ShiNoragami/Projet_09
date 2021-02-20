package com.openclassrooms.realestatemanager.utils;

import android.location.Address;

import com.openclassrooms.realestatemanager.models.geocodingAPI.Geocoding;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class EstateManagerStream {

    /**
     * Create stream for Geocoding
     * @param address
     * @return
     */
    public static Observable<Geocoding> streamFetchGeocode (String address) {
        EstateManagerService estateManagerService = EstateManagerRetrofitObject.retrofit.create(EstateManagerService.class);
        return estateManagerService.getGeocode(address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }
}