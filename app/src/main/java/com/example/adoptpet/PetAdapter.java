package com.example.adoptpet;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PetAdapter extends RecyclerView.Adapter<PetViewHolder>{


    private List<Pet> petList;
    private Context context;

    public PetAdapter(Context context) {
        this.petList = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.pet_card_holder,parent,false);
        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
        Pet pet = petList.get(position);
        holder.bindNewPet(pet,position,this);
        int currentColor;
        if(pet.isCat())
            currentColor = ContextCompat.getColor(context, R.color.recycler_item_cat_bg);
        else
            currentColor = ContextCompat.getColor(context, R.color.recycler_item_dog_bg);

        holder.getLayout().setBackgroundColor(currentColor);
    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    public List<Pet> getPetList() {
        return petList;
    }

    public void setPetList(List<Pet> petList)
    {
        this.petList = petList;
        this.notifyDataSetChanged();
    }

    public void readPetsFromDbByFilter(Filters filters)
    {
       DBWrapper.getAllPetsFiltered(filters,this);
        this.notifyDataSetChanged();
    }
    public void readMyPetsFromDb(String userId)
    {
        DBWrapper.getMyPets(this, userId);
        this.notifyDataSetChanged();
    }

}
