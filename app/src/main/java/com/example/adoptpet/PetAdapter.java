package com.example.adoptpet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.StorageReference;

import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetViewHolder>{
    private List<Pet> petList;
    private Context context;

    public PetAdapter(List<Pet> petList, Context context) {
        this.petList = petList;
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
        holder.txtAge.setText( petList.get(position).getAge() );
        holder.txtDogOrCat.setText( petList.get(position).getAge() );
        holder.txtKind.setText( petList.get(position).getAge() );
        holder.txtName.setText( petList.get(position).getAge() );
        //DBWraper.imageViewSetPictureFromStorage(holder.imageView, petList.get(position).getPicturesUriArr()[0] );

    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    public List<Pet> getPetList() {
        return petList;
    }

    public void setPetList(List<Pet> petList) {
        this.petList = petList;
        this.notifyDataSetChanged();
    }
}
