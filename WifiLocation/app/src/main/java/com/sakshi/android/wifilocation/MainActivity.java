package com.sakshi.android.wifilocation;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public String day = "Thursday";

    WifiManager wifiManager;
    WifiScanReceiver wifiReciever;
    Button scan;
    TextView list;

    WifiInfo wifiInfo;

    ArrayList<CurrentAccessPoint> maccesspoints;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
              maccesspoints = new ArrayList<CurrentAccessPoint>();


        //list = (TextView) findViewById(R.id.list);
        scan = (Button) findViewById(R.id.scan);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiReciever = new WifiScanReceiver();
        wifiInfo = wifiManager.getConnectionInfo();
        wifiManager.startScan();

        scan.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                // start scan when user wants to get position
                if(maccesspoints.size()>0) {


                    for (int k = 0; k < maccesspoints.size(); k++) {
                        Log.v("MAC_address", maccesspoints.get(k).getMac());
                        Log.v("Strength:", String.valueOf(maccesspoints.get(k).getLevel()));
                    }

                    Log.v("Day", day);

                    Bundle b = new Bundle();
                    b.putParcelableArrayList("mac", maccesspoints);
                    b.putString("day", day);
                    Intent i = new Intent(MainActivity.this, Main2Activity.class);
                    i.putExtras(b);
                    startActivity(i);
                }



            }
        });




    }


// gets data of unknown access point
    class WifiScanReceiver extends BroadcastReceiver {
        @SuppressLint("UseValueOf")
        public void onReceive(Context c, Intent intent) {

            List<ScanResult> wifiScanList = wifiManager.getScanResults();
            //wifi = new ArrayList<String>();

            for (int i = 0; i < wifiScanList.size(); i++) {


                String ssid = wifiScanList.get(i).SSID;
                String bssid = wifiScanList.get(i).BSSID;
                Integer stren = wifiScanList.get(i).level;
                Integer frq = wifiScanList.get(i).frequency;
                Long timest = wifiScanList.get(i).timestamp;


                long epoch = System.currentTimeMillis() - SystemClock.elapsedRealtime() + (timest / 1000);  // converting the time to epoch time
                Date timestamp = new Date(epoch); // obtaining time from epoch in form of  " Sun Dec 10 18:26:37 GMT+05:30 2017 "

                Log.d("date ", String.valueOf(timestamp));

                String day1 = String.valueOf(timestamp).substring(0, 3);   // obtaining day from timestamp
                String time = String.valueOf(timestamp).substring(11, 19);  // obtaining time from timestamp
                Log.d("time ", time);

                Log.d("substring day ", day1);
                //String day;

                switch (day1) {
                    case "Mon":
                        day = "Monday";
                        break;
                    case "Tue":
                        day = "Tueday";
                        break;
                    case "Wed":
                        day = "Wednesday";
                        break;
                    case "Thu":
                        day = "Thursday";
                        break;
                    case "Fri":
                        day = "Friday";
                        break;
                    case "Sat":
                        day = "Saturday";
                        break;
                    case "Sun":
                        day = "Sunday";
                        break;
                    default:
                        day = "Thursday";
                }

                if(ssid.equals("IIITV1") || ssid.equals("IIITV2") || ssid.equals("IIITV-Staff"))
                {
                    // check whether the router strength has already been taken or not
                    Boolean b = true;
                    for(int j =0;j<maccesspoints.size();j++){
                        String m = maccesspoints.get(j).getMac();
                        if(m.equals(bssid))
                        {
                            b=false;
                            break;
                        }


                    }
                    if(b){
                        CurrentAccessPoint cp = new CurrentAccessPoint(bssid,stren,time);
                        maccesspoints.add(cp);
                    }



                }



            }
        }
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
    
}









