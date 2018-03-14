package com.example.marko.tester;

import android.content.Intent;
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

    public void addressEntered(View view){
        //do something when the button is pushed
        //gather the address from the msg box
        Intent intent = new Intent(this, MapsActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String address = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, address);

        //go back to the maps page
        startActivity(intent);
    }
}
