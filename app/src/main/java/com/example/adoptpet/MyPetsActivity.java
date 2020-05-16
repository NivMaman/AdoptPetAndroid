package com.example.adoptpet;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MyPetsActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    private PetAdapter petAdapter;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pets);

        user = FirebaseAuth.getInstance().getCurrentUser();

        // set up the RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        petAdapter = new PetAdapter(this);
        petAdapter.readMyPetsFromDb(user.getUid());
        recyclerView.setAdapter(petAdapter);


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigationBar);
        bottomNavigationView.setSelectedItemId(R.id.my_pets);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.add_pet:
                        startActivity(new Intent(getApplicationContext(), AddPetActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.my_pets:
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        RefreshMyPetsList();
    }

    public void RefreshMyPetsList()
    {
        petAdapter.readMyPetsFromDb(user.getUid());
    }
}
