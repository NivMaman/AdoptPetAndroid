package com.example.adoptpet;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

public class ExtendedPetViewActivity extends AppCompatActivity {
    private ImageButton exitButton;
    private TextView txtAge;
    private TextView txtName;
    private TextView txtLocation;
    private TextView txtContact;
    private TextView txtPhoneNumber;
    private TextView txtFreeText;
    private TextView txtKind;
    private SliderView sliderView;
    private ImageView imgGender;


    // string constant definitions
    private final String logTagClass = "ExtendedPetViewActivity";
    private final String AGE_TXT_PREFIX = "age - ";
    private final String NAME_TXT_PREFIX = "name - ";
    private final String LOCATION_TXT_PREFIX = "location - ";
    private final String CONTACT_NAME_TXT_PREFIX = "location - ";
    private final String PHONE_NUM_TXT_PREFIX = "location - ";
    private final String FREE_TEXT_TXT_PREFIX = "location - ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.pet_extended_view);
        Log.i(logTagClass,"onCreate");
        txtName = (TextView)findViewById(R.id.ext_view_txt_name);
        txtAge = (TextView)findViewById(R.id.ext_view_txt_age);
        txtLocation = (TextView)findViewById(R.id.ext_view_txt_location);
        txtContact = (TextView)findViewById(R.id.ext_view_txt_contact_name);
        txtPhoneNumber = (TextView)findViewById(R.id.ext_view_txt_phone_number);
        txtFreeText = (TextView)findViewById(R.id.ext_view_txt_free_text);
        txtKind = (TextView)findViewById(R.id.ext_view_txt_kind);

        exitButton = (ImageButton)findViewById(R.id.exitButton);
        sliderView = (SliderView)findViewById(R.id.imageSlider);
        ImageSliderAdapter imageAdapter = new ImageSliderAdapter(this);
        sliderView.setSliderAdapter(imageAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);

        Pet pet = (Pet) getIntent().getSerializableExtra(PetViewHolder.petExtraKey);

        imageAdapter.renewItems(pet.getPicturesUriArr());
        DBWrapper.imageViewLoadUri(imgGender,pet.genderIconUri());
        txtName.setText(pet.getName());
        txtAge.setText("" + pet.getAge());
        txtLocation.setText(pet.getLocation());
        txtContact.setText(pet.getContactName());
        txtFreeText.setText(pet.getFreeText());
        txtKind.setText(pet.getKind());

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

}
