package com.example.adoptpet;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


public class PetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView txtName;
    private ImageView imagePetMain;
    private TextView txtAge;
    private ImageView imageGender;
    private TextView txtLocation;



    private ConstraintLayout layout;
    private Pet pet;



    private final String logTagClass = "PetViewHolder";
    private final String AGE_TXT_PREFIX = "age - ";
    private final String NAME_TXT_PREFIX = "name - ";
    private final String LOCATION_TXT_PREFIX = "location - ";

    public static final String petExtraKey = "pet";


    public PetViewHolder(@NonNull View itemView) {
        super(itemView);
        imagePetMain = itemView.findViewById(R.id.view_holder_img_pet);
        imageGender  = itemView.findViewById(R.id.view_holder_img_gender);
        txtLocation  = itemView.findViewById(R.id.view_holder_txt_location);
        txtAge  = itemView.findViewById(R.id.view_holder_txt_age);
        txtName = itemView.findViewById(R.id.view_holder_txt_name);
        layout = itemView.findViewById(R.id.pet_layout);
        itemView.setOnClickListener(this);
    }


    public void setTxtName(String string) {
        txtName.setText(string);
    }
    public void bindNewPet(Pet pet)
    {
        this.pet = pet;
        // set all view to show the new pet
        txtName.setText( NAME_TXT_PREFIX + pet.getName());
        txtAge.setText(AGE_TXT_PREFIX + pet.getAge());
        txtLocation.setText(LOCATION_TXT_PREFIX + pet.getLocation());

        DBWrapper.imageViewLoadUri(this.imagePetMain, pet.getMainPictureUri());
        DBWrapper.imageViewLoadUri(this.imageGender , pet.genderIconUri());
    }

    @Override
    public void onClick(View v) {
        Context myContext = v.getContext();
        Intent intent = new Intent(myContext,ExtendedPetViewActivity.class);
        intent.putExtra(petExtraKey, this.pet);
        myContext.startActivity(intent);
    }

    public ConstraintLayout getLayout() {
        return layout;
    }

    public Pet getPet() {
        return pet;
    }
}
