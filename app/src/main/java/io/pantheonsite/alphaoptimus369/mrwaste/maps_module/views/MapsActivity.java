package io.pantheonsite.alphaoptimus369.mrwaste.maps_module.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.pantheonsite.alphaoptimus369.mrwaste.R;
import io.pantheonsite.alphaoptimus369.mrwaste.commons.data.Constants;
import io.pantheonsite.alphaoptimus369.mrwaste.commons.utils.GpsUtils;
import io.pantheonsite.alphaoptimus369.mrwaste.databinding.ActivityMapsBinding;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private ActivityMapsBinding binding;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;

    private double latitude = 0.0, longitude = 0.0;
    private Address locationAddress = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_maps);
        binding.setLifecycleOwner(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        initUi();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE_GPS) {
            if (resultCode == RESULT_OK)
                getLocationPermissionForCurrentPosition();

            else
                Toast.makeText(MapsActivity.this, R.string.gps_permission_denied,
                        Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constants.REQUEST_CODE_LOCATION_PERMISSION) {
            int min = Math.min(permissions.length, grantResults.length);
            boolean allGranted = true;

            for (int i = 0; i < min; ++i) {
                if (permissions[i].equals(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        allGranted = false;
                        break;
                    }
                }
            }

            if (allGranted) {
                getCurrentUserLocation();

            } else {
                Toast.makeText(this, R.string.location_permission_denied, Toast.LENGTH_LONG).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                if (isGPSEnable)
                    getLocationPermissionForCurrentPosition();
                else
                    Toast.makeText(MapsActivity.this, R.string.gps_permission_denied,
                            Toast.LENGTH_LONG).show();
            }
        });


    }


    private void initUi()
    {
        binding.imageViewMarker.setImageResource(R.drawable.ic_unknown_location_colorprimary_24dp);
    }

    private void getLocationPermissionForCurrentPosition()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[] {
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    Constants.REQUEST_CODE_LOCATION_PERMISSION
            );

        } else {
            getCurrentUserLocation();
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentUserLocation()
    {
        fusedLocationClient.getLastLocation().addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Location result = task.getResult();

                if (result != null) {
                    latitude = result.getLatitude();
                    longitude = result.getLongitude();

                } else {
                    Toast.makeText(MapsActivity.this, R.string.failed_to_get_location,
                            Toast.LENGTH_LONG).show();
                    Log.e(Constants.LOG_TAG, "onComplete: task result is null");
                }

            } else {
                Toast.makeText(MapsActivity.this, R.string.failed_to_get_location,
                        Toast.LENGTH_LONG).show();
                Log.e(Constants.LOG_TAG, "onComplete: task failed successfully!");
            }

            geocodeLocation(true, true);
            setListenerToUpdateLocationAfterMove();
        });
    }

    private void updateLatLngWithCameraLocation(boolean mapAutoZoom)
    {
        LatLng target = mMap.getCameraPosition().target;

        latitude = target.latitude;
        longitude = target.longitude;

        geocodeLocation(mapAutoZoom, false);
    }

    private void geocodeLocation(boolean mapAutoZoom, boolean moveCamera)
    {
        try {
            Geocoder geo = new Geocoder(this.getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(latitude, longitude, 1);

            locationAddress = addresses.isEmpty() ? null : addresses.get(0);
            updateUi(mapAutoZoom, moveCamera);

        } catch (IOException e) {
            Log.e(Constants.LOG_TAG, "markLocationOnMap: ", e);
        }
    }

    private void updateUi(boolean mapAutoZoom, boolean moveCamera)
    {
        String addressLine = null, featureName = "---", locality = "---";

        if (locationAddress != null) {
            Log.w(Constants.LOG_TAG, "updateUi: lat = " + locationAddress.getLatitude() + ", lng = " + locationAddress.getLongitude());
            addressLine = locationAddress.getAddressLine(0);
            featureName = locationAddress.getFeatureName();
            locality = locationAddress.getLocality();
        }

        if (TextUtils.isEmpty(addressLine))
            addressLine = String.format(Locale.getDefault(), "%s, %s", featureName, locality);

        binding.textViewCurrentLocation.setText(addressLine);

        if (mMap != null) {
            LatLng latLng = new LatLng(latitude, longitude);
            // mMap.addMarker(new MarkerOptions().position(latLng).title(locality));
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            if (moveCamera) {
                if (!mapAutoZoom)
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                else
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f));

            } else {
                if (mapAutoZoom)
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(18f));
            }
        }
    }

    private void setListenerToUpdateLocationAfterMove()
    {
        if (mMap == null) {
            Toast.makeText(this, R.string.no_map_found, Toast.LENGTH_LONG).show();
            return;
        }

        mMap.setOnCameraMoveListener(() ->
                binding.imageViewMarker.setImageResource(R.drawable.ic_unknown_location_colorprimary_24dp)
        );

        mMap.setOnCameraIdleListener(() -> {
            binding.imageViewMarker.setImageResource(R.drawable.ic_place_red_24dp);
            updateLatLngWithCameraLocation(false);
        });
    }

}
