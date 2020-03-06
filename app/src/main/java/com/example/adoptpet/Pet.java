package com.example.adoptpet;

import android.net.Uri;

public class Pet {
    private String name;
    private int age;
    private boolean dogOrCat; // True = Dog, False = Cat
    private String freeText;
    private Uri [] picturesUriArr;
    private String ownerId;

    public Pet(String name, int age, boolean dogOrCat, String FreeText, Uri [] PicturesUriArr) {
        this.name = name;
        this.age = age;
        this.dogOrCat = dogOrCat;
        this.freeText = FreeText;
        this.picturesUriArr = PicturesUriArr;
    }

    public String getFreeText() {
        return freeText;
    }

    public void setFreeText(String freeText) {
        freeText = freeText;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isDogOrCat() {
        return dogOrCat;
    }

    public void setDogOrCat(boolean dogOrCat) {
        this.dogOrCat = dogOrCat;
    }

    public Uri[] getPicturesUriArr() {
        return this.picturesUriArr;
    }

    public void setPicturesUriArr(Uri[] picturesUriArr) {
        this.picturesUriArr = picturesUriArr;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}
