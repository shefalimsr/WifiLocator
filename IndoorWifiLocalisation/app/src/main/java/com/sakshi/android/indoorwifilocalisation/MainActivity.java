package com.sakshi.android.indoorwifilocalisation;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int defaultlevel = -120;
    private static final double q  = 2;
    private static final double alpha  = 0.5;
    public String day;

    WifiManager wifiManager;
    WifiScanReceiver wifiReciever;
    Button scan;
    TextView list;

    WifiInfo wifiInfo;
    ArrayList<AccessPoint> accessPointsList;
    ArrayList<APoint> aPointsList;
    ArrayList<ImportantAccessPoint> importantAccessPoints;
    ArrayList<ImpAccessPoint> impAccessPoints;
    ArrayList<CurrentAccessPoint> maccesspoints;
    ArrayList<String> wifi;
    double d[];
    double s[];
    double dw[];
    double sw[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
              maccesspoints = new ArrayList<CurrentAccessPoint>();
              importantAccessPoints = new ArrayList<ImportantAccessPoint>();
              accessPointsList = new ArrayList<AccessPoint>();
              impAccessPoints = new ArrayList<ImpAccessPoint>();
              aPointsList = new ArrayList<APoint>();


        list = (TextView) findViewById(R.id.list);
        scan = (Button) findViewById(R.id.scan);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start scan when user wants to get position

                wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifiReciever = new WifiScanReceiver();
                wifiInfo = wifiManager.getConnectionInfo();
                wifiManager.startScan();

            }
        });




    }


// gets data of unknown access point
    class WifiScanReceiver extends BroadcastReceiver {
        @SuppressLint("UseValueOf")
        public void onReceive(Context c, Intent intent) {

            List<ScanResult> wifiScanList = wifiManager.getScanResults();
            wifi = new ArrayList<String>();

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


/*---------------------- function gets most important access point of current location--------------------------*/
    private String mostImpAccessPoint(){
        String s =maccesspoints.get(0).getMac();
        double v = maccesspoints.get(0).getLevel();

        for(int j =0;j<maccesspoints.size() - 1;j++){


                 if(maccesspoints.get(j).getLevel()>v){
                     s=maccesspoints.get(j).getMac();
                     v = maccesspoints.get(j).getLevel();

                 }
        }
        return s;


    }

    /*--------------------function get hour of day---------------*/

    private int hourOfDay() {
       int s =0;
       int low = -1;
       int high = -1;
       int counts =0;
       int countlow =0;
       int counthigh =0;
       String s1= maccesspoints.get(0).getTime();

        String h = String.valueOf(s1).substring(0, 2);
        s  =Integer.valueOf(h);

        for(int j =1;j<maccesspoints.size();j++){


            String s2= maccesspoints.get(j).getTime();

            String h1 = String.valueOf(s2).substring(0, 3);
            int hnow = Integer.valueOf(h1);


                if(s==hnow){
                    counts++;

            }
            else if(hnow>s){
                    high = hnow;
                    counthigh++;
            }
            else {
                low = hnow;
                countlow++;
                }

        }


        if( counts>=counthigh && counts>=countlow )
            return s;

        else if( counthigh>=counts && counthigh>=countlow ){
            return  high;
        }


        else {
            return low;

        }

    }

/*----------adding all the rooms with same access points in same hour of day - change child name---------------*/
//filter hour of day , mac address of accesss point,day
    private void getRooms(){
        DatabaseReference reference;


        // edit child name

            reference = FirebaseDatabase.getInstance().getReference().child("Access Points");

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                    {


                    ImportantAccessPoint ap = dataSnapshot1.getValue(ImportantAccessPoint.class);

                   // wifi.add("ssid : " + ap.getName() + "\n" +"mac address : " + ap.getMac() + "\n" + "level : " + String.valueOf(ap.getLevel()) +  " dBm" + "\n" +  "Day of month :" + ap.getDay() + "\n" ); //append to the other data
//  //Log.d("wifi " , String.valueOf(wifi));
                        if(ap.getHourOfDay()==hourOfDay()&&ap.getMac().equals(mostImpAccessPoint())&&ap.getDay().equals(day)) {
                            importantAccessPoints.add(ap);
                        }




                }



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });


    }

 /*-------function - gets info all the access points in all the rooms with MIP -- change child name------------*/
    //filter room , hour of day ,day
    private void getAllAccessPoints(){
        DatabaseReference reference;


        // edit child name

//change child here
        reference = FirebaseDatabase.getInstance().getReference().child("Access Points");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {


                    AccessPoint ap = dataSnapshot1.getValue(AccessPoint.class);

                    // wifi.add("ssid : " + ap.getName() + "\n" +"mac address : " + ap.getMac() + "\n" + "level : " + String.valueOf(ap.getLevel()) +  " dBm" + "\n" +  "Day of month :" + ap.getDay() + "\n" ); //append to the other data
//  //Log.d("wifi " , String.valueOf(wifi));
                    for(int k =0;k<importantAccessPoints.size();k++){

                        if((importantAccessPoints.get(k).getRoom().equals(ap.getRoom()))&&hourOfDay()==ap.getHourOfDay()&&ap.getDay().equals(day)) {
                            accessPointsList.add(ap);
                        }
                    }






                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });




    }


    private void calculateDistance(){
        int length = importantAccessPoints.size();
        d = new double[length];
        s = new double[length];
        int nd =0;

// room no fetched from k

        for(int k=0;k<importantAccessPoints.size();k++){
            // all routers in the room
            for(int y=0;y<accessPointsList.size();y++){
                if(importantAccessPoints.get(k).getRoom().equals(accessPointsList.get(y).getRoom())){

                    // if router is there in unknown fingerprint
                    for(int z =0;z<maccesspoints.size();z++){

                        if(accessPointsList.get(y).getMac().equals(maccesspoints.get(z).getMac())){
                            nd++;
                            double d1 = maccesspoints.get(z).getLevel() - accessPointsList.get(y).getLevel();

                            d[k] = d[k] + Math.pow(d1,q);

                        }
                        else {

                            double d1 = maccesspoints.get(z).getLevel() - defaultlevel;



                            d[k] = d[k] + Math.pow(d1,q);
                        }


                    }



                }

            }

        }


        for(int k=0;k<importantAccessPoints.size();k++){
            d[k] = Math.abs(d[k]);
            double d2 = 1/q;
            d[k] = Math.pow(d[k],d2);
        }



        // similarity calculation

        for(int k=0;k<importantAccessPoints.size();k++){
            int ne = maccesspoints.size();
            double denom = alpha*ne/nd;
            double denominator = 1 - denom;
            s[k] = 1/(d[k]*denominator);


        }
        double max =s[0];
        int m=0;

        for(int k=1;k<importantAccessPoints.size();k++){

            if(s[k]>max){
                m=k;
                max = s[k];
            }

        }

        String roomt = importantAccessPoints.get(m).getRoom();
        Log.v("The room",roomt);
   list.setText(roomt);


    }



    private void getRoomWithoutTime(){
        DatabaseReference reference;


        // edit child name

        reference = FirebaseDatabase.getInstance().getReference().child("Access Points");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {


                    ImpAccessPoint ap = dataSnapshot1.getValue(ImpAccessPoint.class);

                    // wifi.add("ssid : " + ap.getName() + "\n" +"mac address : " + ap.getMac() + "\n" + "level : " + String.valueOf(ap.getLevel()) +  " dBm" + "\n" +  "Day of month :" + ap.getDay() + "\n" ); //append to the other data
//  //Log.d("wifi " , String.valueOf(wifi));
                    if(ap.getMac().equals(mostImpAccessPoint())&&ap.getDay().equals(day)) {
                        impAccessPoints.add(ap);
                    }




                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });





    }


    private void getAllAccessPointsWithoutTime(){
        DatabaseReference reference;


        // edit child name

//change child here
        reference = FirebaseDatabase.getInstance().getReference().child("Access Points");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {


                    APoint ap = dataSnapshot1.getValue(APoint.class);

                    // wifi.add("ssid : " + ap.getName() + "\n" +"mac address : " + ap.getMac() + "\n" + "level : " + String.valueOf(ap.getLevel()) +  " dBm" + "\n" +  "Day of month :" + ap.getDay() + "\n" ); //append to the other data
//  //Log.d("wifi " , String.valueOf(wifi));
                    for(int k =0;k<impAccessPoints.size();k++){

                        if((impAccessPoints.get(k).getRoom().equals(ap.getRoom()))&&ap.getDay().equals(day)) {
                            aPointsList.add(ap);
                        }
                    }






                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });




    }



    private void calculateDistanceWithoutTime(){

        int length = impAccessPoints.size();
        dw = new double[length];
        sw = new double[length];
        int nd =0;

// room no fetched from k

        for(int k=0;k<impAccessPoints.size();k++){
            // all routers in the room
            for(int y=0;y<aPointsList.size();y++){
                if(impAccessPoints.get(k).getRoom().equals(aPointsList.get(y).getRoom())){

                    // if router is there in unknown fingerprint
                    for(int z =0;z<maccesspoints.size();z++){

                        if(aPointsList.get(y).getMac().equals(maccesspoints.get(z).getMac())){
                            nd++;
                            double d1 = maccesspoints.get(z).getLevel() - aPointsList.get(y).getLevel();

                            dw[k] = dw[k] + Math.pow(d1,q);

                        }
                        else {

                            double d1 = maccesspoints.get(z).getLevel() - defaultlevel;



                            dw[k] = dw[k] + Math.pow(d1,q);
                        }


                    }



                }

            }

        }


        for(int k=0;k<impAccessPoints.size();k++){
            dw[k] = Math.abs(dw[k]);
            double d2 = 1/q;
            dw[k] = Math.pow(dw[k],d2);
        }



        // similarity calculation

        for(int k=0;k<impAccessPoints.size();k++){
            int ne = maccesspoints.size();
            double denom = alpha*ne/nd;
            double denominator = 1 - denom;
            sw[k] = 1/(dw[k]*denominator);


        }
        double max =sw[0];
        int m=0;

        for(int k=1;k<impAccessPoints.size();k++){

            if(sw[k]>max){
                m=k;
                max = sw[k];
            }

        }

        String roomt = impAccessPoints.get(m).getRoom();
        Log.v("The room",roomt);
        list.setText(roomt);



    }







}









