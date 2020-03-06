package com.example.adoptpet;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PetViewHolder extends RecyclerView.ViewHolder {
    public LinearLayout root;
    public TextView txtTitle;
    public TextView txtDesc;

    public PetViewHolder(@NonNull View itemView) {
        super(itemView);
        //root = itemView.findViewById(R.id.list_root);
        //txtTitle = itemView.findViewById(R.id.list_title);
        //txtDesc = itemView.findViewById(R.id.list_desc);
    }


    public void setTxtTitle(String string) {
        txtTitle.setText(string);
    }


    public void setTxtDesc(String string) {
        txtDesc.setText(string);
    }
}
