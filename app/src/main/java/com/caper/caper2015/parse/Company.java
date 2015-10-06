package com.caper.caper2015.parse;

/**
 * Created by Lucas on 15/09/2015.
 */
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

@ParseClassName("Company")
public class Company extends ParseObject{
    public List<Booth> getBooths(){
        return getList("booths");
    }

    public String getDescription(){
        String str = getString("description");
        return str==null? null : str;
    }

    public String getEmail(){
        String str = getString("email");
        return str == null ? null : str;
    }

    public List<Human> getHumans(){
        return getList("humans");
    }

    public String getImage(){
        String str = getString("image");
        return str == null ? null : str;    }

    public String getName(){
        String str = getString("name");
        return str == null ? null : str;
    }

    public String getPhone(){
        String str = getString("phone");
        return str == null ? null : str;
    }

    public String getWeb(){
        String str = getString("web");
        return str == null ? null : str;
    }

    public static ParseQuery<Company> createQuery(){
        ParseQuery<Company> companyQuery = new ParseQuery(Company.class);
        return companyQuery.setLimit(1000).fromLocalDatastore();
    }

    public static List<Company> load() throws ParseException {
        return new ParseQuery(Company.class).setLimit(1000).find();
    }
}
