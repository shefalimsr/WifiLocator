package com.shefali.wifilocator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Home extends AppCompatActivity {
    Bundle bundle=new Bundle();
    EditText building,floor,room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        floor=(EditText)findViewById(R.id.floor);
        room=(EditText)findViewById(R.id.room);
        building=(EditText)findViewById(R.id.building);



        Button scan=(Button)findViewById(R.id.scan);





        scan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(),Scan.class);

                bundle.putString("Building", String.valueOf(building.getText()));
                bundle.putString("Floor",String.valueOf(floor.getText()));
                bundle.putString("Room",String.valueOf(room.getText()));

                i.putExtras(bundle);
Log.d("scanned ", String.valueOf(building.getText()));
                startActivity(i);
            }
        });
    }
}
