package com.example.adoptpet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

public class AddPetActivity extends AppCompatActivity {

    private EditText fullName, age, kind;
    private FirebaseUser user;
    private Button addToDb;
    private ImageButton pic1;
    private Uri filePath1;
    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();
        setContentView(R.layout.activity_add_pet);
        fullName = (EditText)findViewById(R.id.name);
        age = (EditText)findViewById(R.id.age);
        kind = (EditText)findViewById(R.id.pet_kind);
        addToDb = (Button)findViewById(R.id.add_db);
        pic1 = (ImageButton) findViewById(R.id.add_pic);

        pic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });


        addToDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = fullName.getText().toString();
                int petAge = Integer.parseInt(age.getText().toString());
                String petKind = kind.getText().toString();
                String freeText = "free test";
                Uri storagePath1 = DBWraper.uploadPicture(filePath1,AddPetActivity.this);
                Uri[] picturesArray =  {storagePath1};
                Pet pet = new Pet(name, petAge, petKind, freeText, picturesArray);
                DBWraper.addNewPet(pet, user, AddPetActivity.this);
            }
        });
    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath1 = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath1);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}
