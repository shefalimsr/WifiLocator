package com.sakshi.android.wifilocation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
    public String day;
    ArrayList<String> wifi;
    ArrayList<CurrentAccessPoint> maccesspoints;
    double d[];
    double s[];
    double dw[];
    double sw[];
    ArrayList<AccessPoint> accessPointsList;
    ArrayList<APoint> aPointsList;
    ArrayList<ImportantAccessPoint> importantAccessPoints;
    ArrayList<ImpAccessPoint> impAccessPoints;
    private static final int defaultlevel = -120;
    private static final double q  = 2;
    private static final double alpha  = 0.5;
    TextView list1;
    TextView list2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        importantAccessPoints = new ArrayList<ImportantAccessPoint>();
        accessPointsList = new ArrayList<AccessPoint>();
        impAccessPoints = new ArrayList<ImpAccessPoint>();
        aPointsList = new ArrayList<APoint>();

        list1 = (TextView) findViewById(R.id.list1);
        list2 = (TextView) findViewById(R.id.list2);

        Intent intent = getIntent();

        Bundle b = getIntent().getExtras();


        // get maccesspoints and day from bundles

        maccesspoints = new ArrayList<CurrentAccessPoint>();
        maccesspoints = b.getParcelableArrayList("mac");
        day = b.getString("day");

        String mostImpAccessPoint = mostImpAccessPoint();
        int hourOfDay = hourOfDay();


        if(maccesspoints.size()>0){
            getRooms();
            getRoomWithoutTime();

        }
        else{
            list1.setText("no access points recorded");
        }



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


        //Log.v("MOST imp ACCESS POINT :",s);
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

            String h1 = String.valueOf(s2).substring(0, 2);


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


        if( counts>=counthigh && counts>=countlow ){

            //Log.v("HOUR",String.valueOf(s));
            return s;

        }


        else if( counthigh>=counts && counthigh>=countlow ){
            //Log.v("HOUR",String.valueOf(high));
            return  high;
        }


        else {
            //Log.v("HOUR",String.valueOf(low));
            return low;

        }

    }

    /*----------adding all the rooms with same access points in same hour of day - change child name---------------*/
//filter hour of day , mac address of accesss point,day
    //done
    private void getRooms(){
        DatabaseReference reference;


        // edit child name

        reference = FirebaseDatabase.getInstance().getReference().child("4");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                Log.e("hello:",String.valueOf(dataSnapshot.getChildrenCount()));

                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())

                {
                    ImportantAccessPoint ap
                            = dataSnapshot1.getValue(ImportantAccessPoint.class);

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

        getAllAccessPoints();


    }

    /*-------function - gets info all the access points in all the rooms with MIP -- change child name------------*/
    //filter room , hour of day ,day
    //done
    private void getAllAccessPoints(){

        DatabaseReference reference;


        // edit child name

//change child here
        reference = FirebaseDatabase.getInstance().getReference().child("2");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.e("hello:",String.valueOf(dataSnapshot.getChildrenCount()));

                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {


                    AccessPoint ap = dataSnapshot1.getValue(AccessPoint.class);

                    // wifi.add("ssid : " + ap.getName() + "\n" +"mac address : " + ap.getMac() + "\n" + "level : " + String.valueOf(ap.getLevel()) +  " dBm" + "\n" +  "Day of month :" + ap.getDay() + "\n" ); //append to the other data
//  //Log.d("wifi " , String.valueOf(wifi));
                    for(int k =0;k<importantAccessPoints.size();k++){

                        if((importantAccessPoints.get(k).getRoom()==(ap.getRoom()))&&hourOfDay()==ap.getHourOfDay()&&ap.getDay().equals(day)) {
                            accessPointsList.add(ap);
                        }
                    }






                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

Log.v("getALl:",String.valueOf(importantAccessPoints.size()));

        calculateDistance();


    }

// done
    private void calculateDistance(){




        int length = importantAccessPoints.size();
        Log.e(" hello_size:", String.valueOf(length));
        if(length>0) {


            Log.v(" importantAPoint size:", String.valueOf(length));
            d = new double[length];
            s = new double[length];
            int nd = 0;

// room no fetched from k

            for (int k = 0; k < importantAccessPoints.size(); k++) {
                // all routers in the room
                for (int y = 0; y < accessPointsList.size(); y++) {
                    if (importantAccessPoints.get(k).getRoom()==(accessPointsList.get(y).getRoom())) {

                        // if router is there in unknown fingerprint
                        for (int z = 0; z < maccesspoints.size(); z++) {

                            if (accessPointsList.get(y).getMac().equals(maccesspoints.get(z).getMac())) {
                                nd++;
                                double d1 = maccesspoints.get(z).getLevel() - accessPointsList.get(y).getLevel();

                                d[k] = d[k] + Math.pow(d1, q);

                            } else {

                                double d1 = maccesspoints.get(z).getLevel() - defaultlevel;


                                d[k] = d[k] + Math.pow(d1, q);
                            }


                        }


                    }

                }

            }


            for (int k = 0; k < importantAccessPoints.size(); k++) {
                d[k] = Math.abs(d[k]);
                double d2 = 1 / q;
                d[k] = Math.pow(d[k], d2);
            }


            // similarity calculation

            for (int k = 0; k < importantAccessPoints.size(); k++) {
                int ne = maccesspoints.size();
                double denom = alpha * ne / nd;
                double denominator = 1 - denom;
                s[k] = 1 / (d[k] * denominator);


            }
            double max = 0;
            if (s.length > 0) {
                max = s[0];
            }

            int m = 0;

            for (int k = 1; k < importantAccessPoints.size(); k++) {

                if (s[k] > max) {
                    m = k;
                    max = s[k];
                }

            }

            Long roomt = importantAccessPoints.get(m).getRoom();
            Log.v("The room", String.valueOf(roomt));
            list1.setText(String.valueOf(roomt));

        }
    }



    private void getRoomWithoutTime(){
        DatabaseReference reference;


        // edit child name

        reference = FirebaseDatabase.getInstance().getReference().child("3");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Log.e("hello:",String.valueOf(dataSnapshot.getChildrenCount()));
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


        getAllAccessPointsWithoutTime();





    }

    // done
    private void getAllAccessPointsWithoutTime(){
        DatabaseReference reference;


        // edit child name

//change child here
        reference = FirebaseDatabase.getInstance().getReference().child("1");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("hello:",String.valueOf(dataSnapshot.getChildrenCount()));

                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {



                    APoint ap = dataSnapshot1.getValue(APoint.class);

                    // wifi.add("ssid : " + ap.getName() + "\n" +"mac address : " + ap.getMac() + "\n" + "level : " + String.valueOf(ap.getLevel()) +  " dBm" + "\n" +  "Day of month :" + ap.getDay() + "\n" ); //append to the other data
//  //Log.d("wifi " , String.valueOf(wifi));
                    for(int k =0;k<impAccessPoints.size();k++){

                        if((impAccessPoints.get(k).getRoom()==(ap.getRoom()))&&ap.getDay().equals(day)) {
                            aPointsList.add(ap);
                        }
                    }






                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



calculateDistanceWithoutTime();
    }


    private void calculateDistanceWithoutTime() {


        int length = impAccessPoints.size();
        Log.e(" hello:", String.valueOf(length));
        if (length > 0) {


            dw = new double[length];
            sw = new double[length];
            int nd = 0;

// room no fetched from k

            for (int k = 0; k < impAccessPoints.size(); k++) {
                // all routers in the room
                for (int y = 0; y < aPointsList.size(); y++) {
                    if (impAccessPoints.get(k).getRoom()==(aPointsList.get(y).getRoom())) {

                        // if router is there in unknown fingerprint
                        for (int z = 0; z < maccesspoints.size(); z++) {

                            if (aPointsList.get(y).getMac().equals(maccesspoints.get(z).getMac())) {
                                nd++;
                                double d1 = maccesspoints.get(z).getLevel() - aPointsList.get(y).getLevel();

                                dw[k] = dw[k] + Math.pow(d1, q);

                            } else {

                                double d1 = maccesspoints.get(z).getLevel() - defaultlevel;


                                dw[k] = dw[k] + Math.pow(d1, q);
                            }


                        }


                    }

                }

            }


            for (int k = 0; k < impAccessPoints.size(); k++) {
                dw[k] = Math.abs(dw[k]);
                double d2 = 1 / q;
                dw[k] = Math.pow(dw[k], d2);
            }


            // similarity calculation

            for (int k = 0; k < impAccessPoints.size(); k++) {
                int ne = maccesspoints.size();
                double denom = alpha * ne / nd;
                double denominator = 1 - denom;
                sw[k] = 1 / (dw[k] * denominator);


            }

            double max = 0;
            if (sw.length > 0)
                sw[0] = 0;
            int m = 0;

            for (int k = 1; k < impAccessPoints.size(); k++) {

                if (sw[k] > max) {
                    m = k;
                    max = sw[k];
                }

            }

            String roomt = String.valueOf(impAccessPoints.get(m).getRoom());
            Log.v("The room", roomt);
            list2.setText(roomt);


        }
    }


}
