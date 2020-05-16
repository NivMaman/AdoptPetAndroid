package com.example.adoptpet;

import com.google.firebase.firestore.Query;

import java.io.Serializable;

public class Filters implements Serializable {

    private int minAge;
    private int maxAge;
    private boolean dogsOnly;
    private boolean catsOnly;
    private  String location;
    private String gender;



    public Filters()
    {
        this.dogsOnly = false;
        this.catsOnly = false;
        this.minAge = Integer.MIN_VALUE;
        this.maxAge = Integer.MAX_VALUE;
        this.location = "";
        this.gender = "";
    }

    public boolean isCatsOnly() {
        return catsOnly;
    }

    public void setCatsOnly(boolean catsOnly) {
        this.catsOnly = catsOnly;
    }

    public boolean isDogsOnly() {
        return dogsOnly;
    }

    public void setDogsOnly(boolean dogsOnly) {
        this.dogsOnly = dogsOnly;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }

    public String getGender() { return gender; }

    public void setGender(String gender) { this.gender = gender; }

    public Query buildQuery(Query query) {
        //TODO: replace with defines of fields!
        //TODO: build more fields queries
        query = query.whereLessThan("age", this.maxAge).whereGreaterThan("age", this.minAge);
        if (!this.location.equals("")) {
            query.whereEqualTo("location", this.location);
        }
        if(this.dogsOnly){
            query.whereEqualTo("dogOrCat","Dog");
        }
        else if (this.catsOnly){
            query.whereEqualTo("dogOrCat","Cat");
        }
        if(!this.gender.equals("")){
            query.whereEqualTo("sex", this.gender);
        }
        return query;
    }



}
