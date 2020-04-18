package com.example.adoptpet;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Pet implements Serializable {
    private String  name; //Must
    private long    age; //Must
    private boolean dogOrCat; //Must true = cat, false = dog
    private boolean isMale; //Must
    private String  location;//Must
    private String  phoneNumber;//Must
    private String  contactName;//Must
    private String  kind;// not a Must -- use set if needed
    private String  freeText; // not a Must -- use set if needed

    private ArrayList<String> picturesUriStringList;// not a Must -- use add if needed
    private DocumentReference documentReference;


    // general definitions
    private static final int PICTURES_URI_ARR_LENGTH = 3;
    private static final String TAG_LOG = "PetClass";


    //String definitions
    private static final String NAME_MAP_KEY = "name";
    private static final String AGE_MAP_KEY = "age";
    private static final String DOG_OR_CAT_MAP_KEY = "dogOrCat";
    private static final String IS_MALE_MAP_KEY = "freeText";
    private static final String LOCATION_MAP_KEY= "picturesList";
    private static final String PHONE_NUMBER_MAP_KEY= "phoneNumber";
    private static final String CONTACT_NAME_MAP_KEY= "contactName";
    private static final String KIND_MAP_KEY= "kind";
    private static final String FREE_TEXT_MAP_KEY = "freeText";
    private static final String PIC_LIST_MAP_KEY= "picturesList";


    public Pet(String name, long age, boolean dogOrCat, boolean isMale, String location, String phoneNumber, String contactName) {
        this.name = name;
        this.age = age;
        this.dogOrCat = dogOrCat;
        this.isMale = isMale;
        this.location = location;
        this.phoneNumber = phoneNumber;
        this.contactName = contactName;

        this.picturesUriStringList = new ArrayList<>();

        Log.d(TAG_LOG, "constructing a pet -" + this.toString());

    }


    public Pet (Map<String,Object> map)
    {
        this.name            = (String)  map.get(NAME_MAP_KEY);
        this.age             = (long)    map.get(AGE_MAP_KEY);
        this.dogOrCat        = (boolean) map.get(DOG_OR_CAT_MAP_KEY);
        this.isMale          = (boolean) map.get(IS_MALE_MAP_KEY);
        this.location        = (String)  map.get(LOCATION_MAP_KEY);
        this.phoneNumber     = (String)  map.get(PHONE_NUMBER_MAP_KEY);
        this.contactName     = (String)  map.get(CONTACT_NAME_MAP_KEY);
        this.kind            = (String)  map.get(KIND_MAP_KEY);
        this.freeText        = (String)  map.get(FREE_TEXT_MAP_KEY);
        this.picturesUriStringList = ((ArrayList<String>)map.get(PIC_LIST_MAP_KEY));

        Log.d(TAG_LOG, "constructing a pet from map - " + this.toString());

    }

    public Map<String,Object> createMap ()
    {
        Map<String,Object> map = new HashMap<>();

        map.put(NAME_MAP_KEY        ,this.name);
        map.put(AGE_MAP_KEY         ,this.age);
        map.put(DOG_OR_CAT_MAP_KEY  ,this.dogOrCat);
        map.put(IS_MALE_MAP_KEY     ,this.isMale);
        map.put(LOCATION_MAP_KEY    ,this.location);
        map.put(PHONE_NUMBER_MAP_KEY,this.phoneNumber);
        map.put(CONTACT_NAME_MAP_KEY,this.contactName);
        map.put(KIND_MAP_KEY        ,this.kind);
        map.put(FREE_TEXT_MAP_KEY   ,this.freeText);
        map.put(PIC_LIST_MAP_KEY    ,this.picturesUriStringList);

        Log.d(TAG_LOG, "createMap - " + this.toString());
        return map;
    }

    @NonNull
    @Override
    public String toString() {
        return " age = " + this.age + " dogOrCat = " + this.dogOrCat +
                " isMale = " + this.isMale + " location = " + this.location +" phoneNumber = "+ this.phoneNumber + " contactName = " + this.contactName
                + " freeText = " + this.freeText + " name = " + this.name +" picturesUriStringList = "+ this.picturesUriStringList + " freeText = " + this.freeText;
    }



    public Uri getMainPictureUri()
    {
        if(this.picturesUriStringList != null && this.picturesUriStringList.size()!= 0 )
        {
            return Uri.parse(picturesUriStringList.get(0));
        }
        else
        {
            //defaults pictures
            int drawable;
            if(dogOrCat)
            {
                //dog
                drawable = R.drawable.dog_default;
            }
            else
            {
                //cat
                drawable = R.drawable.cat_default;
            }
            return Uri.parse("android.resource://com.example.adoptpet/" + drawable);
        }
    }

    public ArrayList<Uri> getPicturesUriArr()
    {
        ArrayList<Uri> picturesUriConverter = new ArrayList<>();
        Uri converter;
        for (String uri : this.picturesUriStringList)
        {
            converter = Uri.parse(uri);
            picturesUriConverter.add(converter);
        }
        return picturesUriConverter;
    }

    public void addUriReference(String uri)
    {
        if (uri != null){
            this.picturesUriStringList.add (uri);
        }
    }

    public boolean isCat() {
        return dogOrCat;
    }
    public boolean isDog() { return !dogOrCat; }

    public DocumentReference getDocumentReference() {
        return documentReference;
    }

    public void setDocumentReference(DocumentReference documentReference) {
        this.documentReference = documentReference;
    }

    public boolean isDogOrCat() {
        return dogOrCat;
    }

    public void setDogOrCat(boolean dogOrCat) {
        this.dogOrCat = dogOrCat;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        this.isMale = male;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
    public String getFreeText() {
        return this.freeText;
    }

    public void setFreeText(String freeText) {
        this.freeText = freeText;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAge() {
        return this.age;
    }

    public void setAge(long age) {
        this.age = age;
    }

    public String getKind() {
        return this.kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }
}



