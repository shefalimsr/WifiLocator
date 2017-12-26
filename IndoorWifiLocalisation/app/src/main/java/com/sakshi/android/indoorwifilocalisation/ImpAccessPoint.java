package com.sakshi.android.indoorwifilocalisation;

/**
 * Created by shivanshsharma on 26/12/17.
 */

public class ImpAccessPoint {

    String room;
    String mac;

    String day;

    public ImpAccessPoint()
    {

    }


    public ImpAccessPoint(String r,String m,String d)
    {


        room=r;
        mac=m;

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
    public String getDay(){return day;}
}
