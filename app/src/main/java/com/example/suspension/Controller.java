package com.example.suspension;

import android.annotation.SuppressLint;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


/*
* Cette class a pour but d'orchestrer les différents éléments qui définissent la logique de l'application
* */
public class Controller {

    private static Controller INSTANCE = null;

    public Aire[] areas = null;
    public Point[] points = null;

    public double mapW = 0;
    public double mapH = 0;

    public double mapOffX;
    public double mapOffY;

    private Point initalPos;

    private Controller(InputStream data, TextView t, LocationManager l, MapPosView m){
        loadAreas(data);
        m.init(mapW, mapH);
        initLocation(t, l, m);
    }

    public static Controller getInstance(){
        if (INSTANCE == null) {
            throw new RuntimeException("Controller Not Initialized");
        }
        return INSTANCE;
    }

    public static Controller getInstance(InputStream data, TextView t, LocationManager l, MapPosView m){
        if (INSTANCE == null) {
            INSTANCE = new Controller(data, t, l, m);
        }
        return INSTANCE;
    }

    public void loadAreas(InputStream data){

        Gson gson = new Gson();
        Data res = gson.fromJson(new InputStreamReader(data), Data.class);

        points = new Point[res.points.length];
        areas = new Aire[res.areas.length];

        mapH = Math.abs(res.la2 - res.la1);
        mapW = Math.abs(res.lo2 - res.lo1);

        mapOffX = res.lo1;
        mapOffY = res.la1;

        for(DPoint p : res.points){
            points[p.id - 1] = new Point(p.lo - res.lo1, - p.la + res.la1);
        }

        int i = 0;
        for(DArea a : res.areas) {
            Point[] areaPoints = new Point[a.ids.length];
            for (int j = 0; j < areaPoints.length; j++) {
                areaPoints[j] = points[a.ids[j] - 1];
            }

            areas[i] = new Aire(a.id, areaPoints, a.lvl);
            i++;
        }
    }

    @SuppressLint("MissingPermission") //Permission are already checked
    public void initLocation(TextView textView, LocationManager locationManager, MapPosView m){
        LocationListener locationListener = location -> {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude() ;
            textView.setText(String.format("lo : %s\nla : %s\nlvl : %s", longitude, latitude, getLevel(new Point(longitude - mapOffX, - mapH - latitude + mapOffY))));
            m.setPos(new Point(longitude, latitude));
        };


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
    }

    public int getLevel(Point p){

        for(Aire area : areas){
            if(area.contains(p)) return area.SuspensionLevel;
        }
        return 0;
    }

    //classes pour déserialiser
    class DPoint{
        @SerializedName("id")
        int id;
        @SerializedName("lo")
        double lo;
        @SerializedName("la")
        double la;
    }

    class DArea{
        @SerializedName("id")
        int id;
        @SerializedName("lvl")
        int lvl;
        @SerializedName("ids")
        Integer[] ids;
    }
    class Data{
        @SerializedName("lo1")
        double lo1;
        @SerializedName("la1")
        double la1;
        @SerializedName("lo2")
        double lo2;
        @SerializedName("la2")
        double la2 ;
        @SerializedName("points")
        DPoint[] points;
        @SerializedName("areas")
        DArea[] areas;
    }
}
