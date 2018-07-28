package com.example.anik.ssparen;

/**
 * Created by Anik on 8/20/2017.
 */

public class userid {
    String userId;
    String flat;
    String size;
    String house;
    String area;

    public userid(){

    }

    public userid(String userId,String flat,String size,String house,String area) {
        this.userId = userId;
        this.flat=flat;
        this.size=size;
        this.house=house;
        this.area=area;
    }

    public String getUserId() {
        return userId;
    }

    public String getFlat() {
        return flat;
    }

    public String getSize() {
        return size;
    }

    public String getHouse() {
        return house;
    }

    public String getArea() {
        return area;
    }
}
