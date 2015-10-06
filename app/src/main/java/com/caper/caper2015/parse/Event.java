package com.caper.caper2015.parse;

/**
 * Created by Lucas on 15/09/2015.
 */
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;
import java.util.List;

@ParseClassName("Event")
public class Event extends ParseObject {

    public Booth getBooth(){
        return (Booth)get("booth");
    }

    public List<Company> getCompanies(){
        return getList("companies");
    }

    public Date getDateEnd(){
        return getDate("date_end");
    }

    public Date getDateStart(){
        return getDate("date_start");
    }

    public String getDescription(){
        String str = getString("description");
        return str == null ? null : str;    }

    public List<Human> getHumans(){
        return getList("humans");
    }

    public String getTitle(){
        String str = getString("title");
        return str == null ? null : str;    }

    public String getType() {
        String str = getString("type");
        return str == null ? null : str;    }

    public static ParseQuery<Event> createQuery(){
        ParseQuery<Event> eventQuery = new ParseQuery(Event.class);
        return eventQuery.setLimit(1000).fromLocalDatastore();
    }

    public static List<Event> load() throws ParseException {
        return new ParseQuery(Event.class).setLimit(1000).find();
    }
}
