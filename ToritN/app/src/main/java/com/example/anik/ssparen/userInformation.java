package com.example.anik.ssparen;

/**
 * Created by Anik on 8/18/2017.
 */

public class userInformation {
    public String userId;
    public String name;
    public String age;
    public String flat;
    public String area;
    public String size;
    public String family;
    public String consume;
    String house;

    public userInformation(){

    }

    public userInformation(String userId,String name,String age,String flat,String area , String size,String family,String consume,String house){
        this.userId=userId;
        this.name=name;
        this.age=age;
        this.flat=flat;
        this.area=area;
        this.size=size;
        this.family=family;
        this.consume=consume;
        this.house=house;
    }

    public String getConsume() {
        return consume;
    }

    public void setConsume(String consume) {
        this.consume = consume;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getFlat() {
        return flat;
    }

    public String getArea() {
        return area;
    }

    public String getSize() {
        return size;
    }

    public String getHouse() {
        return house;
    }

    public String getFamily() {
        return family;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public void setFamily(String family) {
        this.family = family;
    }
}
