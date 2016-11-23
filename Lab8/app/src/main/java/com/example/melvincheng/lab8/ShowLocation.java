package com.example.melvincheng.lab8;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ShowLocation extends AppCompatActivity implements LocationListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_location);

        setupLocationServices();
    }

    public void onProviderEnabled(String provider) {
        Log.d("LocationSample", "onProviderEnabled(" + provider + ")");
    }

    public void onLocationChanged(Location location) {
        Log.d("LocationSample", "onLocationChanged(" + location + ")");

        showLocationName(location);
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("LocationSample", "onStatusChanged(" + provider + ", " + status + ", extras)");
    }

    public void onProviderDisabled(String provider) {
        Log.d("LocationSample", "onProviderDisabled(" + provider + ")");
    }

    private void setupLocationServices() {
        requestLocationPermissions();

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // request that the user install the GPS provider
            String locationConfig = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
            Intent enableGPS = new Intent(locationConfig);
            startActivity(enableGPS);
        } else {
            // determine the location
            updateLocation();
        }
    }

    /*
       Sample data:
         CN Tower:      43.6426, -79.3871
         Eiffel Tower:  48.8582,   2.2945
     */
    private void updateLocation() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

            // request an fine location provider
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setSpeedRequired(false);
            criteria.setCostAllowed(false);
            String recommended = locationManager.getBestProvider(criteria, true);

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);

            Location location = locationManager.getLastKnownLocation(recommended);
            if (location != null) {
                showLocationName(location);
            }
        } else {
            Log.d("LocationSample", "Location provider permission denied, perms: " + ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION));
        }
    }

    final int PERMISSIONS_REQUEST_ACCESS_LOCATION = 410020;
    private void requestLocationPermissions() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Explain to the user why we need to read the contacts
            }

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_LOCATION);

            return;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateLocation();
                } else {
                    // tell the user that the feature will not work
                }
                return;
            }
        }
    }

    private void showLocationName(Location location) {
        Log.d("LocationSample", "showLocationName("+location+")");
        // perform a reverse geocode to get the address
        if (Geocoder.isPresent()) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            try {
                // reverse geocode from current GPS position
                List<Address> results = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                if (results.size() > 0) {
                    EditText add1 = (EditText)findViewById(R.id.addressLine1_edit);
                    EditText add2 = (EditText)findViewById(R.id.addressLine2_edit);
                    EditText locality = (EditText)findViewById(R.id.locality_edit);
                    EditText adminArea = (EditText)findViewById(R.id.adminArea_edit);
                    EditText country = (EditText)findViewById(R.id.country_edit);
                    EditText postalCode = (EditText)findViewById(R.id.postalCode_edit);
                    EditText phone = (EditText)findViewById(R.id.phone_edit);
                    EditText url = (EditText)findViewById(R.id.url_edit);
                    Address address = results.get(0);
                    add1.setText(address.getAddressLine(0));
                    add2.setText(address.getAddressLine(1));
                    locality.setText(address.getLocality());
                    adminArea.setText(address.getAdminArea());
                    country.setText(address.getCountryName());
                    postalCode.setText(address.getPostalCode());
                    phone.setText(address.getPhone());
                    url.setText(address.getUrl());

                } else {
                    Log.d("LocationSample", "No results found while reverse geocoding GPS location");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("LocationSample", "No geocoder present");
        }
    }

    private Address geocodeLookup(String address) {
        if (Geocoder.isPresent()) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            try {
                // forward geocode from the provided address
                List<Address> results = geocoder.getFromLocationName(address, 1);

                if (results.size() > 0) {
                    return results.get(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
