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
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

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
    public static void getAllPetsFiltered(Filters filters, final PetAdapter petAdapter)
    {
        Query collectionGroup = db.collectionGroup(collectionPets);
        Query query = filters.buildQuery(collectionGroup);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    List<Pet> petList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult())
                    {
                        petList.add(new Pet (document.getData()));
                    }
                    Log.d(DBWraperLogTag," getMyPets - read complete" + petList.toString());
                    petAdapter.setPetList(petList);

                }
                else
                {
                    //TODO - raise toast
                    Log.e(DBWraperLogTag, "getMyPets - Error getting documents: ", task.getException());
                }
            }
        });
    }



    public static void getMyPets(final PetAdapter petAdapter, String userId)
    {
        List<Pet> petList = new ArrayList<>();

        db.collection(collectionUsers).document(userId).collection(collectionPets).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Pet> petList = new ArrayList<>();
                    Pet pet;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        pet = new Pet (document.getData());
                        pet.setDocumentReferenceStr(document.getReference().getPath());
                        petList.add(pet);

                    }
                    petAdapter.setPetList(petList);
                    Log.d(DBWraperLogTag," getMyPets - read complete" + petList.toString());
                } else {
                    Log.d(DBWraperLogTag, "getMyPets - Error getting documents: ", task.getException());
                }
            }
        });
    }


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

    public static void editPet(DocumentReference documentReference, final Pet editedPet,final FirebaseUser user,final ArrayList<Uri> imagePathList ,final Context context)
    {

        documentReference.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(DBWraperLogTag, "editPet - pet successfully deleted!");
                        addNewPet(editedPet, user,imagePathList, context);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(DBWraperLogTag, "editPet - Error deleting document", e);
                        Toast.makeText(context, "Edit Pet Failure", Toast.LENGTH_LONG).show();
                    }
                });
    }



    public static void removePet(DocumentReference documentReference)
    {
        //TODO : remove picture from storage
        documentReference.delete()
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(DBWraperLogTag, "removePet - pet successfully deleted!");
                //TODO: raise toast
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(DBWraperLogTag, "removePet - Error deleting document", e);
                //TODO: raise toast
            }
        });
    }


    public static void imageViewLoadUri(ImageView imageView, Uri uri)
    {
        final int radius = 5;
        final int margin = 5;
        final Transformation transformation = new RoundedCornersTransformation(radius, margin);
        Picasso.get().load(uri).transform(transformation).fit().into(imageView);
    }


}
