package com.sakshi.android.wifilocation;

/**
 * Created by shivanshsharma on 25/12/17.
 */

public class AccessPoint {
    float avg_level;
    String day;
    int hour;
    String mac;
    long room;






    public AccessPoint()
    {

    }


    public AccessPoint(float l,String d, int h,String m,long r)
    {

        room=r;
        mac=m;
        avg_level=l;
        day=d;
       hour=h;
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

   public  int getHourOfDay(){return hour;}
}
