package com.example.dell.bean;

public class City {
    private String province;
    private String city;
    private String number;
    private String firstpy;
    private String allpy;
    private String allFirstpy;

    public City(String province,String city,String number,String firstpy,
                String allpy,String allFirstpy){
        this.province = province;
        this.city = city;
        this.number=number;
        this.allFirstpy = allFirstpy;
        this.firstpy = firstpy;
        this.allpy=allpy;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getNumber() {
        return number;
    }

    public String getFirstpy() {
        return firstpy;
    }

    public String getAllpy() {
        return allpy;
    }

    public String getAllFirstpy() {
        return allFirstpy;
    }


}
