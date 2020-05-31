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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.pantheonsite.alphaoptimus369.mrwaste.R;
import io.pantheonsite.alphaoptimus369.mrwaste.commons.data.ConstantsAndStaticData;
import io.pantheonsite.alphaoptimus369.mrwaste.commons.models.UserItem;
import io.pantheonsite.alphaoptimus369.mrwaste.commons.utils.ActivityStarter;
import io.pantheonsite.alphaoptimus369.mrwaste.commons.utils.GpsUtils;
import io.pantheonsite.alphaoptimus369.mrwaste.commons.utils.SharedPreferencesManager;
import io.pantheonsite.alphaoptimus369.mrwaste.commons.views.BaseActivity;
import io.pantheonsite.alphaoptimus369.mrwaste.databinding.ActivityMapsBinding;


public class MapsActivity extends BaseActivity implements OnMapReadyCallback
{

    private ActivityMapsBinding binding;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;

    private double latitude = 0.0, longitude = 0.0;
    private Address locationAddress = null;

    private String userEmail, userContactNo, userType, userPassword, addressLine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_maps);
        binding.setLifecycleOwner(this);

        getExtras();
        initComponents();
        initUi();
        initListeners();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ConstantsAndStaticData.REQUEST_CODE_GPS) {
            if (resultCode == RESULT_OK)
                getLocationPermissionForCurrentPosition();

            else
                Toast.makeText(MapsActivity.this, R.string.gps_permission_denied,
                        Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ConstantsAndStaticData.REQUEST_CODE_LOCATION_PERMISSION) {
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


    private void getExtras()
    {
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            userEmail = extras.getString(ConstantsAndStaticData.EXTRA_EMAIL, "");
            userContactNo = extras.getString(ConstantsAndStaticData.EXTRA_CONTACT_NO, "");
            userType = extras.getString(ConstantsAndStaticData.EXTRA_USER_TYPE, "");
            userPassword = extras.getString(ConstantsAndStaticData.EXTRA_PASSWORD, "");
        }
    }

    private void initComponents()
    {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void initUi()
    {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        binding.imageViewMarker.setImageResource(R.drawable.ic_unknown_location_colorprimary_24dp);
    }

    private void initListeners()
    {
        binding.buttonSave.setOnClickListener(v -> registerInFirebase());
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
                    ConstantsAndStaticData.REQUEST_CODE_LOCATION_PERMISSION
            );

        } else {
            getCurrentUserLocation();
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentUserLocation()
    {
        showProgressDialog();
        fusedLocationClient.getLastLocation().addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Location result = task.getResult();

                if (result != null) {
                    latitude = result.getLatitude();
                    longitude = result.getLongitude();

                } else {
                    Toast.makeText(MapsActivity.this, R.string.failed_to_get_location,
                            Toast.LENGTH_LONG).show();
                    Log.e(ConstantsAndStaticData.LOG_TAG, "onComplete: task result is null");
                }

                geocodeLocation(true, true);

            } else {
                Toast.makeText(MapsActivity.this, R.string.failed_to_get_location,
                        Toast.LENGTH_LONG).show();
                Log.e(ConstantsAndStaticData.LOG_TAG, "onComplete: task failed successfully!");

                latitude = 41.8;
                longitude = -71.4;

                geocodeLocation(true, true);
            }

            hideProgressDialog();
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
            Geocoder geo = new Geocoder(this.getApplicationContext(), Locale.ENGLISH);
            List<Address> addresses = geo.getFromLocation(latitude, longitude, 1);

            locationAddress = addresses.isEmpty() ? null : addresses.get(0);
            updateUi(mapAutoZoom, moveCamera);

        } catch (IOException e) {
            Log.e(ConstantsAndStaticData.LOG_TAG, "markLocationOnMap: ", e);
        }
    }

    private void updateUi(boolean mapAutoZoom, boolean moveCamera)
    {
        String featureName = "---", locality = "---";

        if (locationAddress != null) {
            Log.w(ConstantsAndStaticData.LOG_TAG, "updateUi: lat = " + locationAddress.getLatitude() + ", lng = " + locationAddress.getLongitude());
            addressLine = locationAddress.getAddressLine(0);
            featureName = locationAddress.getFeatureName();
            locality = locationAddress.getLocality();
        }

        if (TextUtils.isEmpty(addressLine))
            addressLine = String.format(Locale.ENGLISH, "%s, %s", featureName, locality);

        binding.textViewCurrentLocation.setText(addressLine);

        if (mMap != null) {
            LatLng latLng = new LatLng(latitude, longitude);
            // mMap.addMarker(new MarkerOptions().position(latLng).title(locality));
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            if (moveCamera) {
                if (!mapAutoZoom)
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                else
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 2f));

            } else {
                if (mapAutoZoom)
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(2f));
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

    private void registerInFirebase()
    {
        showProgressDialog();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(ConstantsAndStaticData.LOG_TAG, "createUserWithEmail:success");
                            storeInfoInFirestore();

                        } else {
                            Exception exception = task.getException();
                            ConstantsAndStaticData.currentUser = null;
                            new SharedPreferencesManager().saveCurrentUser(ConstantsAndStaticData.currentUser);

                            // If sign in fails, display a message to the user.
                            Log.w(ConstantsAndStaticData.LOG_TAG, "createUserWithEmail:failure", exception);
                            Toast.makeText(MapsActivity.this, R.string.auth_failed_no_net_reg,
                                    Toast.LENGTH_LONG).show();
                            hideProgressDialog();
                        }
                    }
                });
    }

    private void storeInfoInFirestore()
    {
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("email", userEmail);
        user.put("contact", userContactNo);
        user.put("type", userType);
        user.put("latitude", latitude);
        user.put("longitude", longitude);
        user.put("address", addressLine);
        user.put("addressCountry", locationAddress.getCountryName());
        user.put("addressLocality", locationAddress.getLocality());
        user.put("addressFeature", locationAddress.getFeatureName());
        user.put("addressThoroughfare", locationAddress.getThoroughfare());
        user.put("addressAdminArea", locationAddress.getAdminArea());
        user.put("addressCountryCode", locationAddress.getCountryCode());

        String documentId = userEmail;

        // Add a new document with a generated ID
        db.collection("users")
                .document(documentId)
                .set(user)
                .addOnSuccessListener(this, aVoid -> {
                    Log.d(ConstantsAndStaticData.LOG_TAG, "DocumentSnapshot added with ID: " + documentId);
                    hideProgressDialog();

                    ConstantsAndStaticData.currentUser = new UserItem(
                            userEmail,
                            userContactNo,
                            userType,
                            addressLine,
                            latitude,
                            longitude
                    );
                    new SharedPreferencesManager().saveCurrentUser(ConstantsAndStaticData.currentUser);

                    ActivityStarter.startHomeActivity(MapsActivity.this, true);
                })
                .addOnFailureListener(this, e -> {
                    Log.w(ConstantsAndStaticData.LOG_TAG, "Error adding document", e);
                    hideProgressDialog();

                    ConstantsAndStaticData.currentUser = new UserItem(
                            userEmail,
                            userContactNo,
                            userType,
                            addressLine,
                            latitude,
                            longitude
                    );
                    new SharedPreferencesManager().saveCurrentUser(ConstantsAndStaticData.currentUser);

                    ActivityStarter.startHomeActivity(MapsActivity.this, true);
                });
    }

}
