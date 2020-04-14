package com.example.adoptpet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class AddPetActivity extends AppCompatActivity {

    private EditText fullName, age, kind;
    private FirebaseUser user;
    private Button addToDb;
    private ImageButton pic1;
    private ArrayList<Uri> imagePathList;
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
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });


        addToDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = fullName.getText().toString();
                int petAge = Integer.parseInt(age.getText().toString());
                String petKind = kind.getText().toString();
                String freeText = "free test";
                ArrayList<String> picturesArray =  new ArrayList<String>();
                Pet pet = new Pet(name, petAge, petKind, freeText, picturesArray);
                DBWraper.addNewPet(pet, user, imagePathList, AddPetActivity.this);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null)
        {
            imagePathList = new ArrayList<>();

            if(data.getClipData() != null){
                Log.i("in clip data", "");
                int count = data.getClipData().getItemCount();
                for (int i=0; i<count; i++){

                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    imagePathList.add(imageUri);
                }
            }
            else if(data.getData() != null){
                Log.i("in clip data", "");
                Uri imgUri = data.getData();
                imagePathList.add(imgUri);
            }
            else Log.i("in clip data error", "");
        }
        }

}
