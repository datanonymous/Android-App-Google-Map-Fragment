package com.example.alex.javagooglemapapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener { // extends FragmentActivity  AppCompatActivity

    public GoogleMap mMap; //maybe make this public?

    public JSONObject data = null;


//    public String address = "";
//
//    public double latJSON;
//    public double lonJSON;

    LocationManager locationManager;
    LocationListener locationListener;




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            }
        } else{//see if this works
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }//see if this works
    }



//https://stackoverflow.com/questions/41844194/gps-setting-issue-in-android-studio
//https://stackoverflow.com/questions/12044552/android-activate-gps-with-alertdialog-how-to-wait-for-the-user-to-take-action
//    public void isGPSEnable(){
//        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
//        boolean enabled = service
//                .isProviderEnabled(LocationManager.GPS_PROVIDER);
//        if (!enabled) {
//            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            startActivity(intent);
//        }
//    }




    public void updateLocationInfo(Location location) { //this is all new and has to do with updating the TextViews

        TextView latitudeTextView = (TextView) findViewById(R.id.latitudeTextView);
        TextView longitudeTextView = (TextView) findViewById(R.id.longitudeTextView);
        TextView altitudeTextView = (TextView) findViewById(R.id.altitudeTextView);
        TextView accuracyTextView = (TextView) findViewById(R.id.accuracyTextView);

        latitudeTextView.setText("Latitude: " + location.getLatitude());
        longitudeTextView.setText("Longitude: " + location.getLongitude());
        altitudeTextView.setText("Altitude: " + location.getAltitude());
        accuracyTextView.setText("Accuracy: " + location.getAccuracy());

        String latJSON = String.valueOf(location.getLatitude());
        String lonJSON = String.valueOf(location.getLongitude());
        getJSON(latJSON, lonJSON);

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            String address = "Could not find address";
            List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (listAddresses != null && listAddresses.size() > 0) {
                address = "";
                if (listAddresses.get(0).getSubThoroughfare() != null) {
                    address += listAddresses.get(0).getSubThoroughfare() + " ";
                }
                if (listAddresses.get(0).getThoroughfare() != null) {
                    address += listAddresses.get(0).getThoroughfare() + "\n";
                }
                if (listAddresses.get(0).getLocality() != null) {
                    address += listAddresses.get(0).getLocality() + "\n";
                    //getJSON(listAddresses.get(0).getLocality());  //getJSON("Durham"); // THIS WORKS!!!!!!!!!!
                }
                if (listAddresses.get(0).getPostalCode() != null) {
                    address += listAddresses.get(0).getPostalCode() + "\n";
                    //getJSON(listAddresses.get(0).getPostalCode());  //getJSON("Durham"); // THIS WORKS!!!!!!!!!!
                }
                if (listAddresses.get(0).getCountryName() != null) {
                    address += listAddresses.get(0).getCountryName() + "\n";
                }
            }
            TextView addressTextView = (TextView) findViewById(R.id.addressTextView);
            addressTextView.setText(address);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //testing 9/29/2017
    @Override
    public void onMapLongClick(LatLng latLng){
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        String address = "";
        try{
            List<Address> listAddresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if(listAddresses != null && listAddresses.size() > 0){
                if(listAddresses.get(0).getThoroughfare() != null){
                    if(listAddresses.get(0).getSubThoroughfare() != null){
                        address += listAddresses.get(0).getSubThoroughfare() + " ";
                    }
                    address += listAddresses.get(0).getThoroughfare();
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }

        if(address == ""){
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm [MM/dd/yyyy]");
            address = sdf.format(new Date());
        }

        mMap.addMarker(new MarkerOptions().position(latLng).title(address).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {  //Location location
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE); //to try and add the title for settings
        setTitle("Alex Ko's Location App");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        //openweathermap.org api data
//        DownloadTask task = new DownloadTask();
//        //https://stackoverflow.com/questions/34321728/using-openweathermap-api-key
//        //task.execute("http://api.openweathermap.org/data/2.5/weather?lat="+location.getLatitude()+"&lon="+location.getLongitude()+"&APPID=285d3b277a04868e1db63531a4832c78");
//        task.execute("http://api.openweathermap.org/data/2.5/weather?q="+27703+"&APPID=285d3b277a04868e1db63531a4832c78"); // this works!!!!!!!!!!

        //getJSON("Durham");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

//    public boolean onOptionsItemSelected(MenuItem item){
//        super.onOptionsItemSelected(item);
//
//        switch(item.getItemId()){
//            case R.id.notes:
//                //Log.i("Menu item selected", "Notes");
//                Intent intentNotes = new Intent(getApplicationContext(), NoteActivity.class);
//                startActivity(intentNotes);
//            case R.id.money:
//                //Log.i("Menu item selected", "Help");
//                Intent intentMoney = new Intent(getApplicationContext(), MainActivity2.class);
//                startActivity(intentMoney);
//                //return true;
//            default:
//                return false;
//        }
//    }


    // test this to see if it works... previous iteration used switch and case, but clicking on notes tab would bring user to money tab first then notes
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.notes){
            Intent intentNotes = new Intent(getApplicationContext(), NoteActivity.class);
            startActivity(intentNotes);
        }

        if(item.getItemId() == R.id.money){
            Intent intentMoney = new Intent(getApplicationContext(), MainActivity2.class);
            startActivity(intentMoney);
        }

        return super.onOptionsItemSelected(item);
    }




    //TESTING
    //https://stackoverflow.com/questions/34321728/using-openweathermap-api-key
    public void getJSON(final String lat, final String lon) { //final String city   //final String postalCode

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    URL url = new URL("http://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&APPID=ea574594b9d36ab688642d5fbeab847e"); //my api 285d3b277a04868e1db63531a4832c78   //postalCode     //other api = ea574594b9d36ab688642d5fbeab847e
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    StringBuffer json = new StringBuffer(1024);
                    String tmp = "";

                    while((tmp = reader.readLine()) != null)
                        json.append(tmp).append("\n");
                    reader.close();

                    data = new JSONObject(json.toString());

                    if(data.getInt("cod") != 200) {
                        System.out.println("Cancelled");
                        return null;
                    }
                } catch (Exception e) {
                    System.out.println("Exception "+ e.getMessage());
                    return null;
                }
                return null;
            }



            @Override  //THIS WORKS
            protected void onPostExecute(Void Void) { //Void Void - THIS WORKS
                //super.onPostExecute(data);
                if(data!=null){ //THIS WORKS
                    Log.d("my weather received",data.toString()); //THIS WORKS

                    TextView mainTextView = (TextView) findViewById(R.id.mainTextView);
                    TextView descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);




                    try{ //udemy code

                        Log.d("WEATHER RECEIVED",data.toString()); // DOES THE CODE EVER ENTER THE TRY CATCH?!?!?!?!


                        JSONObject jsonObject = new JSONObject(data.toString());

                        String weatherInfo = jsonObject.getString("weather");
                        Log.i("Weather content", weatherInfo); //udemy code

                        JSONArray arr = new JSONArray(weatherInfo); //udemy code    //data.toString()
                        for(int i=0;i<arr.length();i++){ //udemy code
                            JSONObject jsonPart = arr.getJSONObject(i); //udemy code
                            Log.i("main",jsonPart.getString("main")); //udemy code
                            Log.i("description",jsonPart.getString("description")); //udemy code
                            mainTextView.setText(jsonPart.getString("main"));
                            descriptionTextView.setText(jsonPart.getString("description"));
                        } //udemy code
                    } catch(JSONException e){ //udemy code
                        e.printStackTrace(); //udemy code
                    }  //udemy code
                } //THIS WORKS
            } //THIS WORKS



//            @Override //udemy code
//            public void onPostExecute(String result){ //udemy code
//                super.onPostExecute(result); //udemy code
//                try{ //udemy code
//                    JSONObject jsonObject = new JSONObject(result); //udemy code
//                    String weatherInfo = jsonObject.getString("weather"); //udemy code
//                    Log.i("Weather content", weatherInfo); //udemy code
//                    JSONArray arr = new JSONArray(weatherInfo); //udemy code
//                    for(int i=0;i<arr.length();i++){ //udemy code
//                        JSONObject jsonPart = arr.getJSONObject(i); //udemy code
//                        Log.i("main",jsonPart.getString("main")); //udemy code
//                        Log.i("description",jsonPart.getString("description")); //udemy code
//                    } //udemy code
//                } catch(JSONException e){ //udemy code
//                    e.printStackTrace(); //udemy code
//                }  //udemy code
//            } //udemy code

        }.execute();
    }


    //END TESTING

//    public class DownloadTask extends AsyncTask<String, Void, String>{
//        @Override
//        protected String doInBackground(String... urls) { // doInBackground method cannot interact with the UI at all
//            String result = "";
//            URL url;
//            HttpURLConnection urlConnection = null;
//            try {
//                url = new URL(urls[0]); // can fail, needs try and catch
//                urlConnection = (HttpURLConnection) url.openConnection(); //can fail, needs try and catch
//                InputStream in = urlConnection.getInputStream();
//                InputStreamReader reader = new InputStreamReader(in);
//                int data = reader.read();
//                while(data != -1){
//                    char current = (char) data;
//                    result += current;
//                    data = reader.read();
//                }
//                return result;
//            }catch(MalformedURLException e){
//                e.printStackTrace();
//            }catch(IOException e){
//                e.printStackTrace();
//            }
//            return null;
//        }
//        @Override
//        protected void onPostExecute(String result) { //can interact with UI
//            super.onPostExecute(result);
//            Log.i("Website content", result); // "Website content" is the identify of the log, and result is the actual result printed
//        }
//    }

//    //old button for switching to money screen (before I learned how to make menus!)
//    public void onClick(View view) {
//        //Toast.makeText(MapsActivity.this,"Button Pressed",Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
//        startActivity(intent);
//    }

    //http://www.viralandroid.com/2016/04/google-maps-android-api-adding-search-bar-part-3.html
    public void buttonClearFunction(View view) {
        mMap.clear();
        //Toast.makeText(MapsActivity.this, "Map cleared of markers", Toast.LENGTH_SHORT).show();
    }

    public void centerCameraFunction(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1); //permission is not granted so ask for permission
        } else { //permission has been granted
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE); //declare locationManager
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener); //request location updates from location manager
            Location newLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER); //get new location from location manager
            LatLng myLocation = new LatLng(newLocation.getLatitude(), newLocation.getLongitude()); //get lat long from new location
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 18)); //move camera to zoom level 18/20
            //Toast.makeText(MapsActivity.this, "Map centered", Toast.LENGTH_SHORT).show(); //show toast

//            String latJSON = String.valueOf(newLocation.getLatitude());
//            String lonJSON = String.valueOf(newLocation.getLongitude());
//            getJSON(latJSON, lonJSON);
        }
    }

    public void markLocation(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1); //permission is not granted so ask for permission
        } else { //permission has been granted
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE); //declare locationManager
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener); //request location updates from location manager
            Location newLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER); //get new location from location manager
            LatLng myLocation = new LatLng(newLocation.getLatitude(), newLocation.getLongitude()); //get lat long from new location

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm [MM/dd/yyyy]"); //try to add location information 9/29/2017
            String currentDateandTime = sdf.format(new Date());

            mMap.addMarker(new MarkerOptions().position(myLocation).title(currentDateandTime).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
            //Toast.makeText(MapsActivity.this, "Location Marked", Toast.LENGTH_SHORT).show();






//            https://stackoverflow.com/questions/42890570/android-get-location-in-button-click
//            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
//            String address = "";
//            LatLng latLng = (LatLng) new latLng;
//
//            try {
//                List<Address> listAddresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
//                if(listAddresses!=null && listAddresses.size()>0){
//                    if(listAddresses.get(0).getThoroughfare()!=null){
//                        if(listAddresses.get(0).getSubThoroughfare()!=null){
//                            address+=listAddresses.get(0).getSubThoroughfare() + " ";
//                        }
//                        address+=listAddresses.get(0).getThoroughfare();
//                    }
//                }
//            } catch(IOException e){
//                e.printStackTrace();
//            }
//
//            if(address==""){
//                SimpleDateFormat sdf = new SimpleDateFormat("mm:HH yyyyMMdd");
//                address = sdf.format(new Date());
//            }
//
//            mMap.addMarker(new MarkerOptions().position(myLocation).title(address).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));

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
        mMap.setOnMapLongClickListener(this);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID); //this may not be necessary

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                //LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());

                updateLocationInfo(location);

                //mMap.clear();
                //mMap.addMarker(new MarkerOptions().position(myLocation).title("My Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
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


        if (Build.VERSION.SDK_INT < 23) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1); //permission is not granted so ask for permission
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS); //testing 8/20/2017
                startActivity(intent); //testing 8/20/2017
            } else { //permission has been granted
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                //app is launched AND we can get the users location
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (lastKnownLocation != null) { //this calls to the updateLocationInfo function and updates TextViews
                    updateLocationInfo(lastKnownLocation);
                }

                LatLng myLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());

                //mMap.clear();
                //mMap.addMarker(new MarkerOptions().position(myLocation).title("My Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 18));

//                String latJSON = String.valueOf(lastKnownLocation.getLatitude());
//                String lonJSON = String.valueOf(lastKnownLocation.getLongitude());
//                getJSON(latJSON, lonJSON);
            }
        }

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }
}

