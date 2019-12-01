package com.example.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapsActivity extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final String TAG = "MapsActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    //Garages
    private static final LatLng SPIRIT = new LatLng(30.443521, -84.305285);
    private static final LatLng PENSACOLA = new LatLng(30.438014, -84.299836);
    private static final LatLng CALL_STREET = new LatLng(30.444050, -84.289161);
    private static final LatLng SAINT_AUGUSTINE = new LatLng(30.437705, -84.290161);
    private static final LatLng TRADITIONS = new LatLng(30.441931, -84.297074);
    private static final LatLng WOODWARD = new LatLng(30.444836, -84.298962);

    //Markers
    private Marker mSpirit;
    private Marker mPensacola;
    private Marker mCall_St;
    private Marker mSt_Aug;
    private Marker mTraditions;
    private Marker mWoodward;

    //variables
    private MapViewModel mapViewModel;
    private GoogleMap mMap;
    private boolean LocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        mapViewModel = ViewModelProviders.of(this).get(MapViewModel.class);
        View root = inflater.inflate(R.layout.activity_maps, container, false);
        getLocationPermission();
        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(getActivity(), "Map is ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;

        if(LocationPermissionGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
        }
        //initialize markers on map fragment
        //Customize map markers with image of garage and name a the top of marker
        mSpirit = mMap.addMarker(new MarkerOptions()
                .position(SPIRIT)
                .title("Spirit Garage")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.spirittag)));
        mSpirit.setTag(0);

        mCall_St = mMap.addMarker(new MarkerOptions()
                .position(CALL_STREET)
                .title("Call Street Garage")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.callstreettag)));

        mCall_St.setTag(0);

        mPensacola = mMap.addMarker(new MarkerOptions()
                .position(PENSACOLA)
                .title("West Pensacola Street Garage")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pensacolatag)));

        mPensacola.setTag(0);

        mSt_Aug = mMap.addMarker(new MarkerOptions()
                .position(SAINT_AUGUSTINE)
                .title("St Augustine Garage")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.staugtag))); //175px

        mSt_Aug.setTag(0);

        mTraditions = mMap.addMarker(new MarkerOptions()
                .position(TRADITIONS)
                .title("Traditions Way Garage")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.traditionstag)));
        mTraditions.setTag(0);

        mWoodward = mMap.addMarker(new MarkerOptions()
                .position(WOODWARD)
                .title("Woodward Garage")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.woodwardtag)));
        mWoodward.setTag(0);
    }

    private void getDeviceLocation(){
    Log.d(TAG, "getDeviceLocation: getting the devices current location");
    mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        try{
            if(LocationPermissionGranted){
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location");
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM, "My Location");

                        } else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(getActivity(),"unable to get current location",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e) {
            Log.d(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }
    private void moveCamera(LatLng latlng, float zoom, String title){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latlng.latitude + ", lng: " + latlng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));
    }

    private void initMap() {
        Log.d(TAG, "initMap: initialization of map");
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void getLocationPermission()
    {
        Log.d(TAG, "getLocationPermission: getting location permission");
        String[] permissions = {FINE_LOCATION, COURSE_LOCATION};
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationPermissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(getActivity(), permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(), permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called");
        LocationPermissionGranted = false;

        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            LocationPermissionGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    LocationPermissionGranted = true;
                    initMap();
                }
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
