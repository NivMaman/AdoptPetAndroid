package com.example.adoptpet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Printer;
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
    //private ImageButton exitButton;
    private TextView txtAge;
    private TextView txtName;
    private TextView txtLocation;
    private TextView txtContact;
    private TextView txtPhoneNumber;
    private TextView txtFreeText;
    private TextView txtKind;
    private SliderView sliderView;
    private ImageView imgGender;
    private ImageButton callButton;
    private ImageButton whatsappButton;


    // string constant definitions
    private final String logTagClass = "ExtendedPetViewActivity";
    private final String AGE_TXT_PREFIX = "Age - ";
    private final String NAME_TXT_PREFIX = "Name - ";
    private final String LOCATION_TXT_PREFIX = "Location - ";
    private final String CONTACT_NAME_TXT_PREFIX = "Contact name - ";
    private final String PHONE_NUM_TXT_PREFIX = "Phone number - ";
    private final String FREE_TEXT_TXT_PREFIX = "Description - ";
    private final String KIND_TXT_PREFIX = "Kind - ";



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
        txtFreeText = (TextView)findViewById(R.id.ext_view_txt_free_text);
        txtKind = (TextView)findViewById(R.id.ext_view_txt_kind);
        imgGender = (ImageView) findViewById(R.id.ext_view_img_gender);
        callButton = (ImageButton)findViewById(R.id.ext_view_button_call);
        whatsappButton = (ImageButton)findViewById(R.id.ext_view_button_whats);
        DBWrapper.imageViewLoadUri(whatsappButton, Uri.parse("android.resource://com.example.adoptpet/" + R.drawable.whatsapp_logo));
        //exitButton = (ImageButton)findViewById(R.id.exitButton);
        sliderView = (SliderView)findViewById(R.id.imageSlider);
        ImageSliderAdapter imageAdapter = new ImageSliderAdapter(this);
        sliderView.setSliderAdapter(imageAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);

        final Pet pet = (Pet) getIntent().getSerializableExtra(PetViewHolder.petExtraKey);

        imageAdapter.renewItems(pet.getPicturesUriArr());
        imgGender.setImageURI(pet.genderIconUri());
        //DBWrapper.imageViewLoadUri(imgGender,pet.genderIconUri());
        txtName.setText(NAME_TXT_PREFIX +pet.getName());
        txtAge.setText(AGE_TXT_PREFIX + pet.getAge());
        txtLocation.setText(LOCATION_TXT_PREFIX + pet.getLocation());
        txtContact.setText(CONTACT_NAME_TXT_PREFIX + pet.getContactName());
        txtFreeText.setText(FREE_TEXT_TXT_PREFIX + pet.getFreeText());//TODO: check on null
        txtKind.setText(KIND_TXT_PREFIX + pet.getKind());





        /*exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/

        whatsappButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(
                                "https://wa.me/972"+pet.getPhoneNumber()+"?text=Hello,%20interested%20in%20your%20pet")));
            }
        });
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + pet.getPhoneNumber()));
                v.getContext().startActivity(intent);
            }
        });




    }

}
