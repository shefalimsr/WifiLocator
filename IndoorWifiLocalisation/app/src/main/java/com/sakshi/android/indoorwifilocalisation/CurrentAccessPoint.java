package com.sakshi.android.indoorwifilocalisation;

/**
 * Created by shivanshsharma on 25/12/17.
 */

public class CurrentAccessPoint {

    String mac;
    int level;
    String time;



    public CurrentAccessPoint()
    {

    }


    public CurrentAccessPoint(String m, int l,String t)
    {

        mac=m;
        level=l;
        time=t;

    }





    public String getMac()
    {
        return mac;
    }

    public int getLevel()
    {
        return level;
    }

    public String getTime()
    {
        return time;
    }
}

