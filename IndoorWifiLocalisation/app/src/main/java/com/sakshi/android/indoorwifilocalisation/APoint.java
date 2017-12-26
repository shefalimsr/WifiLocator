package com.sakshi.android.indoorwifilocalisation;

/**
 * Created by shivanshsharma on 26/12/17.
 */

public class APoint {
    String room;
    String mac;
    int level;
    String day;



    public APoint()
    {

    }


    public APoint(String r,String m, int l,String d)
    {

        room=r;
        mac=m;
        level=l;
        day=d;

    }



    public String getRoom()
    {
        return room;
    }



    public String getMac()
    {
        return mac;
    }

    public int getLevel()
    {
        return level;
    }

    public String getDay()
    {
        return day;
    }


}
