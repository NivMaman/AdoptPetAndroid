package com.example.adoptpet;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

public class DBWraper {
    //string definitions
    public static final String petsCollectionName     = "petsCollection";
    public static final String fireBaseCollectionName = "appCollection";

    /*
    TODO: implement the following functions (add a lot logs, pay attention to function failure)
    public static List<Pet> getAllPetsFiltered(Filters filters)
    {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query petsCollectionRef  = fireBaseDb.collectionGroup(petsCollectionName);
        TODO: use filters.buildQuery

        return List<Pet> ;
    }


    public static List<Pet> getMyPets(FirebaseFirestore db)
    {
      final FirebaseFirestore db = FirebaseFirestore.getInstance();

    }

    // maybe can fail ==> return boolean
    public static boolean addNewPet(Pet pet)
    {

    }
    // maybe can fail ==> return boolean
    public static boolean editPet(not sure how)
    {

    }

    // maybe can fail ==> return boolean
    public static boolean removePet(not sure how)
    {

    }

     // maybe can fail ==> return boolean
     // set storagePath after upload
    public static boolean uploadPicture(String filePath, StringBuilder storagePath)
    {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imagesRef = storageRef.child("images");

    }

    // maybe can fail ==> return boolean
    public static boolean imageViewSetPictureFromStorage(ImageView imageView, String storagePath)
    {

        // Download directly from StorageReference using Glide
        // (See MyAppGlideModule for Loader registration)
        GlideApp.with(this context )
        .load(storageReference)
        .into(imageView);
    }

    // to use when we failing to get to DB
    public static void raiseToast(String string)
    {

    }

    */

}
