package com.caper.caper2015.parse;

/**
 * Created by Lucas on 15/09/2015.
 */
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

@ParseClassName("Human")
public class Human extends ParseObject {
    public String getDescription(){
        String str = getString("description");
        return str == null ? null : str;
    }

    public String getEmail(){
        String str = getString("email");
        return str == null ? null : str;
    }

    public String getImage(){
        String str = getString("image");
        return str == null ? null : str;
    }

    public String getName(){
        String str = getString("name");
        return str == null ? null : str;
    }

    public String getPhone(){
        String str = getString("phone");
        return str == null ? null : str;
    }

    public static List<Human> load() throws ParseException {
        return new ParseQuery(Human.class).setLimit(1000).find();
    }
}