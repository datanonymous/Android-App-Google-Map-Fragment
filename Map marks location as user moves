package com.example.alex.javagooglemapapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public GoogleMap mMap; //maybe make this public?

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==1){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                }
            }
        }
    }

    public void updateLocationInfo(Location location){ //this is all new and has to do with updating the TextViews

        TextView latitudeTextView = findViewById(R.id.latitudeTextView);
        TextView longitudeTextView = findViewById(R.id.longitudeTextView);
        TextView altitudeTextView = findViewById(R.id.altitudeTextView);
        TextView accuracyTextView = findViewById(R.id.accuracyTextView);

        latitudeTextView.setText("Latitude: " + location.getLatitude());
        longitudeTextView.setText("Longitude: " + location.getLongitude());
        altitudeTextView.setText("Altitude: " + location.getAltitude());
        accuracyTextView.setText("Accuracy: " + location.getAccuracy());

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            String address = "Could not find address";
            List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if(listAddresses!=null && listAddresses.size()>0){
                address="";
                if(listAddresses.get(0).getSubThoroughfare()!=null){
                    address += listAddresses.get(0).getSubThoroughfare() + " ";
                }
                if(listAddresses.get(0).getThoroughfare()!=null){
                    address += listAddresses.get(0).getThoroughfare() + "\n";
                }
                if(listAddresses.get(0).getLocality()!=null){
                    address += listAddresses.get(0).getLocality() + "\n";
                }
                if(listAddresses.get(0).getPostalCode()!=null){
                    address += listAddresses.get(0).getPostalCode() + "\n";
                }
                if(listAddresses.get(0).getCountryName()!=null){
                    address += listAddresses.get(0).getCountryName() + "\n";
                }
            }
            TextView addressTextView = (TextView) findViewById(R.id.addressTextView);
            addressTextView.setText(address);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void onClick(View view){
        Toast.makeText(MapsActivity.this,"Button Pressed",Toast.LENGTH_SHORT).show();
    }

    //http://www.viralandroid.com/2016/04/google-maps-android-api-adding-search-bar-part-3.html
    public void buttonClearFunction(View view){
        mMap.clear();
        Toast.makeText(MapsActivity.this,"Map cleared of markers",Toast.LENGTH_SHORT).show();
    }

    public void centerCameraFunction(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1); //permission is not granted so ask for permission
        } else { //permission has been granted
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE); //declare locationManager
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener); //request location updates from location manager
            Location newLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER); //get new location from location manager
            LatLng myLocation = new LatLng(newLocation.getLatitude(), newLocation.getLongitude()); //get lat long from new location
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 18)); //move camera to zoom level 18/20
            Toast.makeText(MapsActivity.this, "Map centered", Toast.LENGTH_SHORT).show(); //show toast
        }
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
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID); //this may not be necessary

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());

                updateLocationInfo(location);

                //mMap.clear();
                mMap.addMarker(new MarkerOptions().position(myLocation).title("My Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation,18));
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if(Build.VERSION.SDK_INT < 23){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        }else{
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1); //permission is not granted so ask for permission
            }else{ //permission has been granted
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

                //app is launched AND we can get the users location
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if(lastKnownLocation!=null){ //this calls to the updateLocationInfo function and updates TextViews
                    updateLocationInfo(lastKnownLocation);
                }

                LatLng myLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());

                //mMap.clear();
                mMap.addMarker(new MarkerOptions().position(myLocation).title("My Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation,18));
            }
        }
    }
}
