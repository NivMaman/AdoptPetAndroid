package com.example.adoptpet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class AddPetActivity extends AppCompatActivity {

    private EditText fullName, age, freeText, contactNumber, contactName;
    private BottomNavigationView bottomNavigationView;
    private RadioGroup kindGroup, sexGroup;
    private RadioButton kindButton, sexButton;
    private FirebaseUser user;
    private Button addToDb;
    private ImageButton pic1;
    private ArrayList<Uri> imagePathList;
    private final int PICK_IMAGE_REQUEST = 71;
    private static final String TAG = "AddPetActivity";
    private String cities[] = {"Acre",
            "Afula",
            "Arad",
            "Ariel",
            "Ashdod",
            "Ashkelon",
            "Bat Yam",
            "Beersheba",
            "Beit Shean",
            "Beit Shemesh",
            "Beitar Illit",
            "Bnei Brak",
            "Dimona",
            "Eilat",
            "Elad",
            "Givat Shmuel",
            "Givatayim",
            "Hadera",
            "Haifa",
            "Herzliya",
            "Hod HaSharon",
            "Holon",
            "Jerusalem",
            "Karmiel",
            "Kiryat Ata",
            "Kiryat Bialik",
            "Kiryat Gat",
            "Kiryat Malakhi",
            "Kiryat Motzkin",
            "Kiryat Ono",
            "Kiryat Shmona",
            "Kiryat Yam",
            "Kfar Saba",
            "Lod",
            "Maale Adumim",
            "Maalot-Tarshiha",
            "Migdal HaEmek",
            "Modiin-Maccabim-Reut",
            "Modiin Illit",
            "Nahariya",
            "Nazareth Illit ",
            "Nesher",
            "Ness Ziona",
            "Netanya",
            "Netivot",
            "Ofakim",
            "Or Akiva",
            "Or Yehuda",
            "Petah Tikva",
            "Raanana",
            "Ramat Gan",
            "Ramat HaSharon",
            "Ramla",
            "Rehovot",
            "Rishon LeZion",
            "Rosh HaAyin",
            "Safed",
            "Sderot",
            "Tel Aviv-Yaffo",
            "Tiberias",
            "Tirat Carmel",
            "Yavne",
            "Yehud-Monosson",
            "Yokneam"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();
        setContentView(R.layout.activity_add_pet);
        bottomNavigationView = findViewById(R.id.navigationBar);
        bottomNavigationView.setSelectedItemId(R.id.add_pet);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.add_pet:
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.my_pets:
                        startActivity(new Intent(getApplicationContext(), MyPetsActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                }
                return false;
            }
        });
        fullName = (EditText)findViewById(R.id.name);
        age = (EditText)findViewById(R.id.age);
        kindGroup = (RadioGroup) findViewById(R.id.pet_kind);
        sexGroup = (RadioGroup) findViewById(R.id.pet_sex);
        contactName = (EditText)findViewById(R.id.contact_name);
        contactNumber = (EditText)findViewById(R.id.contact_number);
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
                kindButton=(RadioButton)findViewById(kindGroup.getCheckedRadioButtonId());
                sexButton=(RadioButton)findViewById(sexGroup.getCheckedRadioButtonId());
                String name = fullName.getText().toString();
                String sex = sexButton.getText().toString();
                String kind = kindButton.getText().toString();
                String number = contactNumber.getText().toString();
                String contact_name = contactName.getText().toString();
                int petAge = Integer.parseInt(age.getText().toString());
                String description = freeText.getText().toString();
                String location = "Haifa";
                Pet pet = new Pet(name, petAge, kind, sex, location, number, contact_name);
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
