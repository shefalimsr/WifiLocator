package com.shefali.wifilocator;

/**
 * Created by shefali on 10/12/17.
 */

public class AccessPoint {



    String building;
    String floor;
    String room;
    String name;
    String mac;
    int level;
    String day;
    String time;
    String device;


    public AccessPoint()
    {

    }


    public AccessPoint(String b,String f,String r,String n ,String m, int l,String t,String d,String de)
    {
        building=b;
        floor=f;
        room=r;
        name=n;
        mac=m;
        level=l;
        time=t;
        day=d;
        device=de;
    }

    public String getBuilding()
    {
        return building;
    }

    public String getFloor()
    {
        return floor;
    }

    public String getRoom()
    {
        return room;
    }

    public String getName()
    {
        return name;
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

    public String getTime()
    {
        return time;
    }
}
