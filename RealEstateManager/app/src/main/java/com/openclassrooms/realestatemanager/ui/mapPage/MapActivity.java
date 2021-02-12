package com.openclassrooms.realestatemanager.ui.mapPage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.Estate;
import com.openclassrooms.realestatemanager.models.geocodingAPI.Geocoding;
import com.openclassrooms.realestatemanager.ui.BaseActivity;
import com.openclassrooms.realestatemanager.ui.EstateViewModel;
import com.openclassrooms.realestatemanager.ui.detailDescription.DetailActivity;
import com.openclassrooms.realestatemanager.utils.EstateManagerStream;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class MapActivity extends BaseActivity implements OnMapReadyCallback, LocationListener, Serializable {

    protected static final int PERMS_CALL_ID = 200;
    private GoogleMap map;
    private String mPosition;
    private LocationManager locationManager;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private EstateViewModel estateViewModel;
    private String completeAddress;
    private List<Estate> estateList = new ArrayList<>();
    private List<String> adressList = new ArrayList<>();
    private List<Long> idList = new ArrayList<>();
    private String estateType;
    private Long id;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        configureViewModel();
        //For map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        Objects.requireNonNull(mapFragment).getMapAsync(this);
        //For adress of estate
        this.estateViewModel.getEstates().observe(this, this::createStringForAddress);
    }

    /**
     * For Configure ViewModel
     */
    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.estateViewModel = ViewModelProviders.of(this, viewModelFactory).get(EstateViewModel.class);

    }

    /**
     * For permissions to position access
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, PERMS_CALL_ID);
            return;
        }
        locationManager = (LocationManager) (getSystemService(Context.LOCATION_SERVICE));

        if (Objects.requireNonNull(locationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 15000, 10, this);
            Log.e("GPSProvider", "testGPS");
        } else if (locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
            locationManager.requestLocationUpdates(
                    LocationManager.PASSIVE_PROVIDER, 15000, 10, this);
            Log.e("PassiveProvider", "testPassive");
        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 15000, 10, this);
            Log.e("NetWorkProvider", "testNetwork");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMS_CALL_ID) {
            checkPermissions();
        }
    }

    /**
     * For update location
     */
    @Override
    public void onPause() {
        super.onPause();
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }

    }

    public void onProviderDisabled(String provider) {
    }

    public void onProviderEnabled(String provider) {
        Log.d("LocationProject", "Provider Enabled");
    }


    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    /**
     * Retrieve User location
     *
     * @param location
     */
    public void onLocationChanged(Location location) {
        double mLatitude = location.getLatitude();
        double mLongitude = location.getLongitude();

        if (map != null) {
            LatLng googleLocation = new LatLng(mLatitude, mLongitude);
            map.moveCamera(CameraUpdateFactory.newLatLng(googleLocation));
            mPosition = mLatitude + "," + mLongitude;
            Log.d("TestLatLng", mPosition);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onResume() {
        super.onResume();
        checkPermissions();
    }

    /**
     * For map display
     *
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (Utils.isInternetAvailable(Objects.requireNonNull(this))) {
            map = googleMap;

            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.moveCamera(CameraUpdateFactory.zoomBy(15));

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, PERMS_CALL_ID);
                return;
            }

            googleMap.setMyLocationEnabled(true);
            //For click on infoWindow and retrieve estate detail
            map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                @Override
                public void onInfoWindowClick(Marker marker) {
                    long estateId = Long.parseLong(Objects.requireNonNull(marker.getTag()).toString());

                    for (Estate estate : estateList)
                        if (estate.getMandateNumberID() == estateId) {

                            Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                            intent.putExtra("estate", estate);
                            startActivity(intent);
                        }
                }
            });

        } else {
            Snackbar.make(findViewById(R.id.map), "No internet avalaible", Snackbar.LENGTH_SHORT).show();

        }
    }

    public void createStringForAddress(List<Estate> estateList) {
        this.estateList = estateList;
        if (!Objects.requireNonNull(estateList).isEmpty()) {
            for (Estate est : estateList) {
                id = est.getMandateNumberID();
                estateType = est.getEstateType();
                String address = est.getAddress();
                String postalCode = String.valueOf(est.getPostalCode());
                String city = est.getCity();
                completeAddress = address + "," + postalCode + "," + city;

                adressList.addAll(Collections.singleton(completeAddress));
                idList.add(id);

                Log.d("adressList", "adressList" + adressList);

                Log.d("createString", "createString" + completeAddress);

            }
            if (Utils.isInternetAvailable(Objects.requireNonNull(this))) {
                executeHttpRequestWithRetrofit();
            } else {
                Snackbar.make(findViewById(R.id.map), "No internet available", Snackbar.LENGTH_SHORT).show();

            }
        }
    }

    //http request for geocoding
    private void executeHttpRequestWithRetrofit() {
        map.clear();
        for (String address : adressList) {
            Disposable d = EstateManagerStream.streamFetchGeocode(address)
                    .subscribeWith(new DisposableObserver<Geocoding>() {
                        @Override
                        public void onNext(Geocoding geocoding) {

                            LatLng latLng = new LatLng(geocoding.getResults().get(0).getGeometry()
                                    .getLocation().getLat(), geocoding.getResults().get(0).getGeometry()
                                    .getLocation().getLng());

                            Marker marker = map.addMarker(new MarkerOptions().position(latLng)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                                    .title(geocoding.getResults().get(0).getFormattedAddress()));

                            marker.setTag(idList.get(adressList.indexOf(address)));

                        }

                        @Override
                        public void onComplete() {

                        }

                        @Override
                        public void onError(@NonNull Throwable e) {

                        }
                    });
            mCompositeDisposable.add(d);
        }
    }

    /**
     * Dispose subscription
     */
    private void disposeWhenDestroy() {
        mCompositeDisposable.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }
}