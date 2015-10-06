package com.caper.caper2015.parse;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;

/**
 * Created by Lucas on 04/10/2015.
 */
@ParseClassName("Config")
public class Config extends ParseObject{
    public static Date getLastUpdated() throws ParseException{
        Config config = (Config)new ParseQuery(Config.class).getFirst();
        return config.getDate("lastUpdatedDate");
    }
}
