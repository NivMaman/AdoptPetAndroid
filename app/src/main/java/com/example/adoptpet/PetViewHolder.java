package com.example.adoptpet;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class PetViewHolder extends RecyclerView.ViewHolder {
    public TextView txtName;
    public TextView txtAge;
    public TextView txtKind;
    public TextView txtDogOrCat;
    public ImageView imageView;
    public LinearLayout layout;


    public PetViewHolder(@NonNull View itemView) {
        super(itemView);
        txtName = itemView.findViewById(R.id.pet_name);
        txtAge = itemView.findViewById(R.id.pet_age);
        txtKind = itemView.findViewById(R.id.pet_kind);
        txtDogOrCat = itemView.findViewById(R.id.pet_dog_or_cat);
        imageView = itemView.findViewById(R.id.pet_image);
        layout = itemView.findViewById(R.id.pet_layout);
    }


    public void setTxtName(String string) {
        txtName.setText(string);
    }
    public void setTxtAge(String string) {
        txtAge.setText(string);
    }
    public void setTxtKind(String string) {
        txtKind.setText(string);
    }
    public void setTxtDogOrCat(String string) {
        txtDogOrCat.setText(string);
    }
}
