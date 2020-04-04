package com.example.adoptpet;

public class Filters {

    private int minAge;
    private int maxAge;
    private String textContain;
    private String nameContain;
    private boolean dogsOnly;
    private boolean catsOnly;



    public Filters()
    {
        this.dogsOnly = false;
        this.catsOnly = false;
        this.textContain = null;
        this.nameContain = null;
        this.minAge = Integer.MIN_VALUE;
        this.maxAge = Integer.MAX_VALUE;
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

    public String getNameContain() {
        return nameContain;
    }

    public void setNameContain(String nameContain) {
        this.nameContain = nameContain;
    }

    public String getTextContain() {
        return textContain;
    }

    public void setTextContain(String textContain) {
        this.textContain = textContain;
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


    /*
    TODO: how to build  a query correctly ??????
    public query buildQuery()
    {

    }*/



}
