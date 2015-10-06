package com.caper.caper2015.parse;

/**
 * Created by Lucas on 15/09/2015.
 */

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

@ParseClassName("Building")
public class Building extends ParseObject{

    public ParseFile getFloorPlan(){
        return getParseFile("floorplan");
    }

    public String getName(){
        String str = getString("name");
        return str == null ? null : str;    }

    public static ParseQuery<Building> createQuery(){
        ParseQuery<Building> buildingQuery = new ParseQuery(Building.class);
        return buildingQuery.setLimit(1000).fromLocalDatastore();
    }

    public static List<Building> load() throws ParseException {
        return new ParseQuery(Building.class).setLimit(1000).find();
    }
}
