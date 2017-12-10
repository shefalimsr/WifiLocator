package com.shefali.wifilocator;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Scan extends Activity {

    WifiManager wifiManager;
    WifiScanReceiver wifiReciever;
    ListView list;
    String wifis[];
    WifiInfo wifiInfo;
    ArrayList<String> wifi;
    AccessPoint accessPoint;
    private DatabaseReference mDatabase;
    Bundle bundle=new Bundle();
    public String building,floor,room;
// ...


    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        bundle=getIntent().getExtras();

        Log.d("Start", String.valueOf(bundle));
         building = bundle.getString("Building");

        Log.d("building ",building);
         floor=bundle.getString("Floor");
         room=bundle.getString("Room");
        list = (ListView) findViewById(R.id.list);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiReciever = new WifiScanReceiver();
        wifiInfo = wifiManager.getConnectionInfo();
        wifiManager.startScan();
    }



    protected void onPause() {
        unregisterReceiver(wifiReciever);
        super.onPause();
    }

    protected void onResume() {
        registerReceiver(wifiReciever, new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    class WifiScanReceiver extends BroadcastReceiver {
        @SuppressLint("UseValueOf")
        public void onReceive(Context c, Intent intent) {
            mDatabase = FirebaseDatabase.getInstance().getReference();

            List<ScanResult> wifiScanList = wifiManager.getScanResults();
            wifi = new ArrayList<String>();
            wifis = new String[wifiScanList.size()];
            for (int i = 0; i < wifiScanList.size(); i++) {

                String ssid = wifiScanList.get(i).SSID;
                String bssid = wifiScanList.get(i).BSSID;
                Integer stren = wifiScanList.get(i).level;
                Integer frq=wifiScanList.get(i).frequency;
                Long timest = wifiScanList.get(i).timestamp;
                long epoch = System.currentTimeMillis() - SystemClock.elapsedRealtime() + (timest / 1000);
                Date timestamp = new Date(epoch);
               // String date = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (epoch*1000));
                Log.d("date ", String.valueOf(timestamp));

                String day1= String.valueOf(timestamp).substring(0,3);
String time=String.valueOf(timestamp).substring(11,19);
                Log.d("time ",time);

                Log.d("substring day ",day1);
                String day;

                switch (day1)
                {
                    case "Mon" : day="Monday";
                                break;
                    case "Tue" : day="Tueday";
                        break;
                    case "Wed" : day="Wednesday";
                        break;
                    case "Thu" : day="Thursday";
                        break;
                    case "Fri" : day="Friday";
                        break;
                    case "Sat" : day="Saturday";
                        break;
                    case "Sun" : day="Sunday";
                        break;
                    default: day="Thursday";
                }

//                double exp = (27.55 - (20 * Math.log10(frq)) + Math.abs(stren)) / 20.0;
//                double distanceM = Math.pow(10.0, exp);



String device= getDeviceName();


                accessPoint = new AccessPoint(building,floor,room,ssid,bssid,stren,time,day,getDeviceName());

                mDatabase.child("Access Points").push().setValue(accessPoint);



                //wifis[i] = ((wifiScanList.get(i)).toString());
            }

            DatabaseReference reference;

            reference = FirebaseDatabase.getInstance().getReference().child("Access Points");

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                    {


                    AccessPoint ap = dataSnapshot1.getValue(AccessPoint.class);

                    wifi.add("ssid : " + ap.getName() + "\n" +"mac address : " + ap.getMac() + "\n" + "level : " + String.valueOf(ap.getLevel()) +  " dBm" + "\n" +  "Day of month :" + ap.getDay() + "\n" ); //append to the other data
  //Log.d("wifi " , String.valueOf(wifi));

                }

                    list.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_list_item_1, wifi));

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });


        }
    }


    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
}

