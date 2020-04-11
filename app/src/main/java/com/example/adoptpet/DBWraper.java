package com.example.adoptpet;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import android.media.MediaDataSource;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Map;
import java.util.UUID;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DBWraper {
    //string definitions
    public static final String petsCollectionName     = "petsCollection";
    public static final String fireBaseCollectionName = "appCollection";
    final static FirebaseFirestore db = FirebaseFirestore.getInstance();
    static FirebaseStorage storage = FirebaseStorage.getInstance();
    static StorageReference storageReference = storage.getReference();

    public static final String DBWraperLogTag  = "DBWraper";
    public static final String collectionUsers = "Users";
    public static final String collectionPets  = "Pets";

    //TODO: implement the following functions (add a lot logs, pay attention to function failure)
    public static List<Pet> getAllPetsFiltered(Filters filters)
    {
        final List<Pet> petList = new ArrayList<>();
        final boolean ret[] = new boolean[1];


        Query collectionGroup = db.collectionGroup(collectionPets);
        Query query = filters.buildQuery(collectionGroup);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    for (QueryDocumentSnapshot document : task.getResult())
                    {

                        petList.add(new Pet (document.getData()));
                    }

                    Log.d(DBWraperLogTag," getMyPets - read complete" + petList.toString());
                    ret[0] = true;

                }
                else
                {
                Log.d(DBWraperLogTag, "getMyPets - Error getting documents: ", task.getException());
                ret[0] = false;
                }
            }
        });
        if(ret[0])
        {
            return petList;
        }
        else
        {
            return null;
        }
    }



    public static List<Pet> getMyPets(FirebaseFirestore db, String userId)
    {
        final List<Pet> petList = new ArrayList<>();
        final boolean ret[] = new boolean[1];

        db.collection(collectionUsers).document(userId).collection(collectionPets).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        petList.add(new Pet (document.getData()));
                    }
                    Log.d(DBWraperLogTag," getMyPets - read complete" + petList.toString());
                    ret[0] = true;
                } else {
                    Log.d(DBWraperLogTag, "getMyPets - Error getting documents: ", task.getException());
                    ret[0] = false;
                }
            }
        });


        if(ret[0])
        {
            return petList;
        }
        else
        {
            return null;
        }
    }


    // maybe can fail ==> return boolean
    public static boolean addNewPet(Pet pet, FirebaseUser user, final Context context)
    {
        final boolean[] ret = new boolean[1];
        Map<String,Object> map = pet.createMap();
        db.collection(collectionUsers).document(user.getUid()).collection(collectionPets).add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(context, "addNewPet - Added pet to db", Toast.LENGTH_LONG).show();
                ret[0] = true;
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(context, "addNewPet - Failed adding new pet" + e.getMessage(), Toast.LENGTH_LONG).show();
                        ret[0] = false;
                    }
                });
        return ret[0];
    }

    public static boolean editPet(DocumentReference documentReference, Pet editedPet, FirebaseUser user, final Context context)
    {
        boolean ret = false;
        //TODO: maybe retry when fail??
        if(removePet(documentReference))
        {
            if(addNewPet(editedPet, user, context))
            {
                Log.d(DBWraperLogTag, "editPet - pet successfully edited !");
                ret = true;
            }
            else
            {
                Log.e(DBWraperLogTag, "editPet - Error pet had been deleted but not added");
                ret = false;
            }
        }
        else
        {
            Log.e(DBWraperLogTag, "editPet - Error pet remove failed");
            ret = false;
        }

        return ret;
    }

    public static boolean removePet(DocumentReference documentReference)
    {
        //TODO : remove picture from storage
        final boolean[] ret = new boolean[1];
        documentReference.delete()
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(DBWraperLogTag, "removePet - pet successfully deleted!");
                ret[0] = true;
                //TODO: raise toast
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(DBWraperLogTag, "removePet - Error deleting document", e);
                ret[0] = false;
                //TODO: raise toast
            }
        });

        return ret[0];
    }

     public static Uri uploadPicture(Uri filePath , final Context context)
     {
        final Uri[] storageUri = new Uri[1];
        final boolean ret[] = new boolean[1];
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            UploadTask  uploadTask = ref.putFile(filePath);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,
                                Task<Uri>>() {
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
                        progressDialog.dismiss();
                        storageUri[0] = task.getResult();
                        Log.i(DBWraperLogTag, " uploadPicture - upload picture complete successfully " + storageUri[0].toString());
                        Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Log.w(DBWraperLogTag, " uploadPicture - upload picture failed  ");
                        Toast.makeText(context, "upload picture failed!", Toast.LENGTH_SHORT).show();
                    }
            }
        });
        }
        return storageUri[0];
    }


    public static void imageViewSetPictureFromStorage(ImageView imageView, String storagePath)
    {
        Picasso.get().load(storagePath).into(imageView);
    }


}
