package com.sakshi.android.indoorwifilocalisation;

/**
 * Created by shivanshsharma on 25/12/17.
 */

public class AccessPoint {

    String room;
    String mac;
    int level;
    String day;
    int hourOfDay;


    public AccessPoint()
    {

    }


    public AccessPoint(String r,String m, int l,String d,int h)
    {

        room=r;
        mac=m;
        level=l;
        day=d;
       hourOfDay=h;
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

   public  int getHourOfDay(){return hourOfDay;}
}
