package com.example.adoptpet;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class Pet {
    private String name;
    private int age;
    private String kind;
    private String freeText;
    private Uri[] picturesUriArr;


    // general definitions
    private static final int PICTURES_URI_ARR_LENGTH = 3;
    private static final String TAG_LOG = "PetClass";


    //String definitions
    private static final String AGE_STRING = "age";
    private static final String KIND_STRING = "dogOrCat";
    private static final String FREE_TEXT_STRING = "freeText";
    private static final String NAME_STRING = "name";
    private static final String PIC_1_URI_STRING = "picture1Uri";
    private static final String PIC_2_URI_STRING = "picture2Uri";
    private static final String PIC_3_URI_STRING = "picture3Uri";




    public Pet(String name, int age, String kind, String FreeText, Uri [] PicturesUriArr) {
        this.name = name;
        this.age = age;
        this.kind = kind;
        this.freeText = FreeText;
        if(PicturesUriArr.length == 3)
            this.picturesUriArr = PicturesUriArr;
        else {
            Log.e(TAG_LOG, "Pet() — PicturesUriArr.length != 3 ");
            this.picturesUriArr = new Uri[3];
        }
        Log.d(TAG_LOG, "constructing a pet - " + this.toString());
    }

    public Pet (Map<String,Object> map)
    {
        this.age = (int) map.get(AGE_STRING);
        this.kind = (String) map.get(KIND_STRING);
        this.freeText = (String) map.get(FREE_TEXT_STRING);
        this.name = (String) map.get(NAME_STRING);

        this.picturesUriArr = new Uri[3];
        this.picturesUriArr[0] = (Uri) map.get(PIC_1_URI_STRING);
        this.picturesUriArr[1] = (Uri) map.get(PIC_2_URI_STRING);
        this.picturesUriArr[2] = (Uri) map.get(PIC_3_URI_STRING);
        Log.d(TAG_LOG, "constructing a pet from map - " + this.toString());

    }

    public Map<String,Object> createMap ()
    {
        Map<String,Object> map = new HashMap<>();

        map.put(AGE_STRING,this.age);
        map.put(KIND_STRING,this.kind);
        map.put(FREE_TEXT_STRING,this.freeText);
        map.put(NAME_STRING,this.age);
        map.put(PIC_1_URI_STRING,this.picturesUriArr[0]);
        map.put(PIC_2_URI_STRING,this.picturesUriArr[1]);
        map.put(PIC_3_URI_STRING,this.picturesUriArr[2]);

        Log.d(TAG_LOG, "createMap - " + this.toString());
        return map;
    }

    @NonNull
    @Override
    public String toString() {
        return "age = " + this.age + "dogOrCat = " + this.kind +
        "freeText = " + this.freeText + "name = " + this.name + "picturesUriArr[0] = " + this.picturesUriArr[0] +
                "picturesUriArr[1] = " + this.picturesUriArr[1] + "picturesUriArr[2] = " + this.picturesUriArr[2];
    }

    public String getFreeText() {
        return freeText;
    }

    public void setFreeText(String freeText) {
        this.freeText = freeText;
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

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Uri[] getPicturesUriArr() {
        return this.picturesUriArr;
    }

    public void setPicturesUriArr(Uri[] picturesUriArr) {
        this.picturesUriArr = picturesUriArr;
    }

}


