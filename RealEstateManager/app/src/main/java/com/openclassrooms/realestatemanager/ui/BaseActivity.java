package com.openclassrooms.realestatemanager.ui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.GoogleMap;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.Estate;

import java.util.List;
import java.util.Objects;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public abstract class BaseActivity extends AppCompatActivity {


    private static final int RC_CAMERA_AND_STORAGE_COARSELOCATION_FINELOCATION = 100;
    private static final String[] CAM_AND_READ_EXTERNAL_STORAGE =
            {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    protected ActionBar ab;
    private LocationManager locationManager;
    private GoogleMap googleMap;


    /**
     * For toolbar
     */
    protected void configureToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * For up arrow in toolbar
     */
    protected void configureUpButton() {
        ab = getSupportActionBar();
        if (ab != null) ab.setDisplayHomeAsUpEnabled(true);
    }

    /**
     * For permissions camera and read external storage
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

    }

    /**
     * For permissions
     */
    @AfterPermissionGranted(RC_CAMERA_AND_STORAGE_COARSELOCATION_FINELOCATION)
    protected void methodRequiresTwoPermission() {

        if (EasyPermissions.hasPermissions(this, CAM_AND_READ_EXTERNAL_STORAGE)) {

            Log.d("Permissions", "Permissions granted");
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "This application need permissions to access",
                    RC_CAMERA_AND_STORAGE_COARSELOCATION_FINELOCATION, CAM_AND_READ_EXTERNAL_STORAGE);
        }
    }


}