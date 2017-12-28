package com.sakshi.android.indoorwifilocalisation;

/**
 * Created by shivanshsharma on 25/12/17.
 */

public class ImportantAccessPoint {

    String room;
    String mac;
    int hourOfDay;
    String day;

    public ImportantAccessPoint()
    {

    }


    public ImportantAccessPoint(String r,String m,int h ,String d)
    {


        room=r;
        mac=m;
        hourOfDay =h;
        day =d;

    }



    public String getRoom()
    {
        return room;
    }


    public String getMac()
    {
        return mac;
    }
    public int getHourOfDay(){return  hourOfDay;}
    public String getDay(){return day;}

}
