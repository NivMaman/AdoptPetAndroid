package com.example.adoptpet;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DBWrapper {
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
                Log.e(DBWraperLogTag, "getMyPets - Error getting documents: ", task.getException());
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
    public static void addNewPet(final Pet pet, final FirebaseUser user, ArrayList<Uri> imagePathList, final Context context)
    {
        final ProgressDialog progressDialog = getProgressDialog(context);
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Pictures");
        final List<Uri> clonedImageList = new ArrayList<>(imagePathList);

        imagePathList.clear();
        final int imageListSize = clonedImageList.size();
        List<Task<Uri>> uploadedImageUrlTasks = new ArrayList<>(imageListSize);
        for (Uri imageUri : clonedImageList) {
            final String imageFilename = UUID.randomUUID().toString();
            Log.d(DBWraperLogTag, "Starting upload for \"" + imageFilename + "\"...");
            final StorageReference imageRef = storageReference.child(imageFilename);
            final UploadTask currentUploadTask = imageRef.putFile(imageUri);
            Task<Uri> currentUrlTask = currentUploadTask
                    .continueWithTask(getContinuation(imageFilename, imageRef)).addOnCompleteListener(getTaskOnCompleteListener(pet));
            uploadedImageUrlTasks.add(currentUrlTask);
        }
        Tasks.whenAllSuccess(uploadedImageUrlTasks)
                .addOnCompleteListener(addPetCompleteListener(pet, user, context, progressDialog, imageListSize));
    }

    private static OnCompleteListener<List<Object>> addPetCompleteListener(final Pet pet, final FirebaseUser user, final Context context, final ProgressDialog progressDialog, final int imageListSize) {
        return new OnCompleteListener<List<Object>>() {
            @Override
            public void onComplete(@NonNull Task<List<Object>> task) {
                if (task.getResult().size() == imageListSize){
                    Log.d(DBWraperLogTag, "All Tasks uploaded succesfully");
                    Map<String,Object> map = pet.createMap();
                    db.collection(collectionUsers).document(user.getUid()).collection(collectionPets).add(map)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(context, "addNewPet - Added pet to db", Toast.LENGTH_LONG).show();
                                    Log.i(DBWraperLogTag, "Add new pet to db succesfully");
                                    progressDialog.dismiss();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(Exception e) {
                                    Toast.makeText(context, "addNewPet - Failed adding new pet" + e.getMessage(), Toast.LENGTH_LONG).show();
                                    Log.e(DBWraperLogTag, "Add new pet to db faild" + e.getMessage());
                                    e.printStackTrace();
                                }
                            });
            }
        }};
    }

    private static OnCompleteListener<Uri> getTaskOnCompleteListener(final Pet pet) {
        return new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    String pathReference = task.getResult().toString();
                    pet.addUriReference(pathReference);
                    Log.i(DBWraperLogTag, "Added path reference to pet");
                } else {
                    Log.e(DBWraperLogTag, "Upload task failed!  \"" + task.getException());
                }
            }
        };
    }

    private static Continuation<UploadTask.TaskSnapshot, Task<Uri>> getContinuation(final String imageFilename, final StorageReference imageRef) {
        return new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    Log.e(DBWraperLogTag, "Upload for \"" + imageFilename + "\" failed!");
                    throw task.getException();
                }

                Log.d(DBWraperLogTag, "Upload for \"" + imageFilename + "\" finished. Fetching download URL...");
                return imageRef.getDownloadUrl();
            }
        };
    }

    private static ProgressDialog getProgressDialog(Context context) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Uploading .... ");
        progressDialog.show();
        return progressDialog;
    }
/*
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


    public static void imageViewSetPictureFromStorage(ImageView imageView, String storagePath)
    {
        Picasso.get().load(storagePath).into(imageView);
    }

*/
}
