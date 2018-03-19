package com.example.marko.tester;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddressActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.TESTER.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
    }

    public void setDestination(View view){
        Intent intent = getIntent();
        String origin = intent.getStringExtra(MapsActivity.EXTRA_LOCATION);
        EditText editText = (EditText) findViewById(R.id.editText);
        String loc = editText.getText().toString();
        Uri eng = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=" + origin + "&destination=" + "43.657731, -79.377236" +"&travelmode=walking");

        if(loc.equals("eng")|| loc.equals("engineering") || loc.equals("george vari engineering")) {
            getRoute(view, eng);
        }

    }

    public void getRoute(View view, Uri uri){
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
