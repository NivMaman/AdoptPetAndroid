package com.example.adoptpet;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class Pet {
    private String name;
    private int age;
    private boolean dogOrCat; // True = Dog, False = Cat
    private String freeText;
    private String [] picturesUriArr;


    // general definitions
    private static final int PICTURES_URI_ARR_LENGTH = 3;
    private static final String TAG_LOG = "PetClass";


    //String definitions
    private static final String AGE_STRING = "age";
    private static final String DOG_OR_CAT_STRING = "dogOrCat";
    private static final String FREE_TEXT_STRING = "freeText";
    private static final String NAME_STRING = "name";
    private static final String PIC_1_URI_STRING = "picture1Uri";
    private static final String PIC_2_URI_STRING = "picture2Uri";
    private static final String PIC_3_URI_STRING = "picture3Uri";




    public Pet(String name, int age, boolean dogOrCat, String FreeText, String [] PicturesUriArr) {
        this.name = name;
        this.age = age;
        this.dogOrCat = dogOrCat;
        this.freeText = FreeText;
        if(PicturesUriArr.length == 3)
            this.picturesUriArr = PicturesUriArr;
        else {
            Log.e(this.TAG_LOG, "Pet() — PicturesUriArr.length != 3 ");
            this.picturesUriArr = new String[3];
        }
        Log.d(this.TAG_LOG, "constructing a pet - " + this.toString());
    }

    public Pet(Map<String,Object> map)
    {
        this.age = (int) map.get(this.AGE_STRING);
        this.dogOrCat = (Boolean) map.get(this.DOG_OR_CAT_STRING);
        this.freeText = (String) map.get(this.FREE_TEXT_STRING);
        this.name = (String) map.get(this.NAME_STRING);

        String[] array = { new String("str1"), new String("str2") };
        this.picturesUriArr = new String[3];
        this.picturesUriArr[0] = (String) map.get(this.PIC_1_URI_STRING);
        this.picturesUriArr[1] = (String) map.get(this.PIC_2_URI_STRING);
        this.picturesUriArr[2] = (String) map.get(this.PIC_3_URI_STRING);
        Log.d(this.TAG_LOG, "constructing a pet from map - " + this.toString());

    }

    public Map<String,Object> createMap ()
    {
        Map<String,Object> map = new HashMap<>();

        map.put(this.AGE_STRING,this.age);
        map.put(this.DOG_OR_CAT_STRING,this.age);
        map.put(this.FREE_TEXT_STRING,this.age);
        map.put(this.NAME_STRING,this.age);

        map.put(this.PIC_1_URI_STRING,this.age);
        map.put(this.PIC_2_URI_STRING,this.age);
        map.put(this.PIC_3_URI_STRING,this.age);

        Log.d(this.TAG_LOG, "createMap - " + this.toString());

        return map;
    }

    @NonNull
    @Override
    public String toString() {
        return "age = " + this.age + "dogOrCat = " + this.dogOrCat +
        "freeText = " + this.freeText + "name = " + this.name + "picturesUriArr[0] = " + this.picturesUriArr[0] +
                "picturesUriArr[1] = " + this.picturesUriArr[1] + "picturesUriArr[2] = " + this.picturesUriArr[2];
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

    public String[] getPicturesUriArr() {
        return this.picturesUriArr;
    }

    public void setPicturesUriArr(String[] picturesUriArr) {
        this.picturesUriArr = picturesUriArr;
    }

}
