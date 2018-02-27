package com.sakshi.android.wifilocation;

/**
 * Created by shivanshsharma on 26/12/17.
 */

public class APoint {
    float avg_level;
    String day;
    String mac;
    long room;



    public APoint()
    {

    }


    public APoint(float l, String d,String m,long r)
    {

        room=r;
        mac=m;
        avg_level=l;
        day=d;

    }



    public long getRoom()
    {
        return room;
    }



    public String getMac()
    {
        return mac;
    }

    public float getLevel()
    {
        return avg_level;
    }

    public String getDay()
    {
        return day;
    }


}
