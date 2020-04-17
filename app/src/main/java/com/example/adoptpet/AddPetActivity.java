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

    private EditText fullName, age, kind, freeText;
    private FirebaseUser user;
    private Button addToDb;
    private ImageButton pic1;
    private ArrayList<Uri> imagePathList;
    private final int PICK_IMAGE_REQUEST = 71;
    private static final String TAG = "AddPetActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();
        setContentView(R.layout.activity_add_pet);
        fullName = (EditText)findViewById(R.id.name);
        age = (EditText)findViewById(R.id.age);
        kind = (EditText)findViewById(R.id.pet_kind);
        freeText = (EditText)findViewById(R.id.free_text);
        addToDb = (Button)findViewById(R.id.add_db);
        pic1 = (ImageButton) findViewById(R.id.add_pic);
        pic1.setOnClickListener(getSelectPictureListener());
        addToDb.setOnClickListener(getAddToDbListener());
    }

    private View.OnClickListener getSelectPictureListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        };
    }

    private View.OnClickListener getAddToDbListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = fullName.getText().toString();
                int petAge = Integer.parseInt(age.getText().toString());
                String petKind = kind.getText().toString();
                String description = freeText.getText().toString();
                ArrayList<String> picturesArray = new ArrayList<>();
                Pet pet = new Pet(name, petAge, petKind, description, picturesArray);
                try{
                    DBWrapper.addNewPet(pet, user, imagePathList, AddPetActivity.this);
                }catch(Exception e){
                    Log.e(TAG, "Error Adding pet to DB! " + e.getMessage());
                }
            }
        };
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "In onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null)
        {
            imagePathList = new ArrayList<>();
            if(data.getClipData() != null){
                Log.i(TAG, "Get pictures selected by user.");
                int count = data.getClipData().getItemCount();
                for (int i=0; i<count; i++){
                    try {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        imagePathList.add(imageUri);
                    }
                    catch(Exception e){
                        Log.e(TAG, e.getMessage());
                    }
                }
            }
            else if(data.getData() != null){
                Log.i(TAG, "Get one picture selected by user.");
                try{
                    Uri imgUri = data.getData();
                    imagePathList.add(imgUri);
                }
                catch(Exception e) {
                    Log.e(TAG, e.getMessage());
                }

            }
        }
        else Log.i(TAG, "No pictures selected by user.");
        }

}
