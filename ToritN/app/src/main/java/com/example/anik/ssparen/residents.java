package com.example.anik.ssparen;

/**
 * Created by Anik on 9/18/2017.
 */

public class residents {
    public String name;
    public String flat;
    public int usage;
    private String house;

    public residents(){

    }

    public residents(String name,String flat,int usage,String house) {
        this.name=name;
        this.flat = flat;
        this.usage=usage;
        this.house=house;
    }

    public String getName() {
        return name;
    }

    public String getFlat() {
        return flat;
    }

    public int getUsage() {
        return usage;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public void setUsage(int usage) {
        this.usage = usage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }


}
