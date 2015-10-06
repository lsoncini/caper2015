package com.caper.caper2015.parse;


/**
 * Created by Lucas on 13/09/2015.
 */
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

@ParseClassName("Booth")
public class Booth extends ParseObject{
    public String getCode(){
        String str = getString("code");
        return str == null ? null : str;
    }

    public Building getBuilding(){
        return (Building)getParseObject("building");
    }

    public int getLocationX(){
        return getInt("location_x");
    }

    public int getLocationY(){
        return getInt("location_y");
    }

    public static ParseQuery<Booth> createQuery(){
        ParseQuery<Booth> boothQuery = new ParseQuery(Booth.class);
        return boothQuery.setLimit(1000).fromLocalDatastore();
    }

    public static List<Booth> load() throws ParseException {
        return new ParseQuery(Booth.class).setLimit(1000).find();
    }
}