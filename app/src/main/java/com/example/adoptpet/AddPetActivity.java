package com.example.adoptpet;

import com.example.adoptpet.R;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class AddPetActivity extends AppCompatActivity {

    protected EditText fullNameEditText, ageEditText, freeTextEditText, contactNumberEditText, contactNameEditText
            ,petKindEditText;
    protected BottomNavigationView bottomNavigationView;
    protected RadioGroup kindGroup, sexGroup;
    protected RadioButton kindButton, sexButton;
    protected FirebaseUser user;
    protected Button addToDb;
    protected ImageButton pic1;
    protected ArrayList<Uri> imagePathList;
    protected ImageView[] picturesView;
    protected AutoCompleteTextView locationTextView;


    protected final int PICK_IMAGE_REQUEST = 71;
    protected static final String TAG = "AddPetActivity";

    protected final static  int MAX_NUMBER_OF_IMAGES = 3;


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
        fullNameEditText = (EditText)findViewById(R.id.name);
        ageEditText = (EditText)findViewById(R.id.age);
        kindGroup = (RadioGroup) findViewById(R.id.pet_kind);
        sexGroup = (RadioGroup) findViewById(R.id.pet_sex);
        contactNameEditText = (EditText)findViewById(R.id.contact_name);
        contactNumberEditText = (EditText)findViewById(R.id.contact_number);
        freeTextEditText = (EditText)findViewById(R.id.free_text);
        addToDb = (Button)findViewById(R.id.add_db);
        pic1 = (ImageButton) findViewById(R.id.add_pic);
        picturesView = new ImageView[MAX_NUMBER_OF_IMAGES];
        picturesView[0] = (ImageView) findViewById(R.id.pictureView1);
        picturesView[1] = (ImageView) findViewById(R.id.pictureView2);
        picturesView[2] = (ImageView) findViewById(R.id.pictureView3);
        petKindEditText = (EditText) findViewById(R.id.pet_kind_edit_txt);

        pic1.setOnClickListener(getSelectPictureListener());
        addToDb.setOnClickListener(getAddToDbListener());
        freeTextEditText.setOnKeyListener(getFreeTextOnKeyListener());


        // Get a reference to the AutoCompleteTextView in the layout
        locationTextView = (AutoCompleteTextView) findViewById(R.id.location);
        // Get the string array
        String[] cities = getResources().getStringArray(R.array.cities_array);
        // Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cities);
        locationTextView.setAdapter(adapter);


    }

    private  View.OnKeyListener getFreeTextOnKeyListener(){
        final int MAX_NUMBER_OF_LINES = 5;
        return new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {


                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {

                    if (((EditText) v).getLineCount() >= MAX_NUMBER_OF_LINES)
                        return true;
                }

                return false;
            }
        };
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
    protected Pet CollectPetInfo()
    {
        kindButton=(RadioButton)findViewById(kindGroup.getCheckedRadioButtonId());
        sexButton=(RadioButton)findViewById(sexGroup.getCheckedRadioButtonId());
        String name = fullNameEditText.getText().toString();
        String sex = sexButton.getText().toString();
        String dogOrCat = kindButton.getText().toString();
        String number = contactNumberEditText.getText().toString();
        String contact_name = contactNameEditText.getText().toString();
        double age = Double.parseDouble(ageEditText.getText().toString());
        String freeText = freeTextEditText.getText().toString();
        String location = locationTextView.getText().toString();
        String petKind  = petKindEditText.getText().toString();
        return new Pet(name, age, dogOrCat, sex, petKind,freeText, location, number, contact_name);
    }
    private View.OnClickListener getAddToDbListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    DBWrapper.addNewPet(CollectPetInfo(), user, imagePathList, AddPetActivity.this);
                }catch(Exception e){
                    Log.e(TAG, "Error Adding pet to DB! " + e.getMessage());
                }
            }
        };
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.i(TAG, "In onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null)
        {
            imagePathList = new ArrayList<>();
            if(data.getClipData() != null)
            {
                Log.i(TAG, "Get pictures selected by user.");
                int numOfPics = data.getClipData().getItemCount();
                if(numOfPics>MAX_NUMBER_OF_IMAGES)
                {
                    Toast.makeText(getApplication(), "only 3 pictures are allowed", Toast.LENGTH_SHORT).show();
                }
                for (int i=0; i<numOfPics && i < MAX_NUMBER_OF_IMAGES; i++){
                    try {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        imagePathList.add(imageUri);
                    }
                    catch(Exception e){
                        Log.e(TAG, e.getMessage());
                    }
                }
            }
            else if(data.getData() != null)
            {
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

        this.RefreshPicturesView();// this will show the image

        }
    protected void RefreshPicturesView()
    {
        Uri uri;
        for (int i=0;i<MAX_NUMBER_OF_IMAGES;i++)
        {
            if(i<imagePathList.size())
            {
                uri = imagePathList.get(i);
                DBWrapper.imageViewLoadUri(picturesView[i],uri);
            }
            else
            {
                picturesView[i].setImageResource(android.R.color.transparent);
            }

        }
    }
    public void navigateToHomeActivity()
    {
        bottomNavigationView.setSelectedItemId(R.id.home);
    }
    public void swipeLeft(){navigateToHomeActivity();}
    public void swipeRight(){bottomNavigationView.setSelectedItemId(R.id.my_pets);}
}



