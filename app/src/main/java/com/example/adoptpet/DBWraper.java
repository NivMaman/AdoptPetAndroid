package com.example.adoptpet;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;
import java.util.Map;

public class DBWraper {
    //string definitions
    public static final String petsCollectionName     = "petsCollection";
    public static final String fireBaseCollectionName = "appCollection";
    final static FirebaseFirestore db = FirebaseFirestore.getInstance();
    static FirebaseStorage storage = FirebaseStorage.getInstance();
    static StorageReference storageReference = storage.getReference();


    //TODO: implement the following functions (add a lot logs, pay attention to function failure)
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
    public static void addNewPet(Pet pet, FirebaseUser user, final Context context)
    {
        Map<String,Object> map = pet.createMap();
        db.collection("Users").document(user.getUid()).collection("Pets").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(context, "Added pet to db", Toast.LENGTH_LONG).show();

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(context, "Error In Saving Details: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
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
    public static Uri uploadPicture(Uri filePath , final Context context)
    {
        final Uri[] storageUri = new Uri[1];
        final StorageReference ref = storageReference.child("images/");
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            UploadTask uploadTask= ref.putFile(filePath);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        storageUri[0] = task.getResult();
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
        }

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

}
