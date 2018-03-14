package com.example.marko.tester;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location startingLocation;

    static final int PERMISSIONS_FINE_LOCATION = 1;
    static final int PERMISSIONS_INTERNET = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //for getting last known (current)location:
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Intent userAddress = getIntent();
        if(userAddress != null){
            //an address has been passed in
            this.getUserAddress(userAddress); //will extract the address from what is passed
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.locationOptions) {
            return true;
        }else if (id == R.id.locationGPS){
            this.currentLoc(); //get current location <- for now, slo shows the location
            return true;
        }else if (id == R.id.locationAddress){
            //check permission for internet first
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                    == PackageManager.PERMISSION_GRANTED){
                startActivity(new Intent(this, AddressActivity.class)); //go to a different screen to input the address
            }else{
                //ask permission to use internet
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PERMISSIONS_INTERNET);
            }
            return true;
        }else if(id == R.id.destination) {
            return true;
        }else if(id == R.id.destinationAddress){
            return true;
        }

        return super.onOptionsItemSelected(item);
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void currentLoc(){

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){

            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        // This is where to put whatever to do once the location has been acquired
                        startingLocation = location;
                        //this is to make sure the location is being retrieved
                        mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("You are here."));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                    }
                }
            });
        }else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
        }
    }

    public void getUserAddress(Intent userAddress){
        //this is what to do to get the address
        String address = userAddress.getStringExtra(AddressActivity.EXTRA_MESSAGE);
        //now, need to figure out what to do with the given address
        this.addressEntered(address);
    }

    public void addressEntered(String userAddress){
        //this method is called when an address is to be entered
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try{
            addresses = geocoder.getFromLocationName(userAddress, 1);
            LatLng foundIt = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
            //place marker & move camera
            mMap.addMarker(new MarkerOptions().position(foundIt).title("You are here."));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(foundIt));

        }catch(IllegalArgumentException e){
            //no address has been given
        }catch(IOException e){
            //network is unavailable, or really anything else to do with IO
        }



    }
}
