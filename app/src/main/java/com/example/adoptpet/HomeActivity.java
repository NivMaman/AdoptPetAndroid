package com.example.adoptpet;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FirebaseAuth auth;
    private PetAdapter petAdapter;
    private BottomNavigationView bottomNavigationView;
    private  ImageButton filterButton;
    private Toolbar toolBar;
    private Button logOutButton;
    private final int GET_FILTERS = 72;
    private boolean isFiltered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        auth = FirebaseAuth.getInstance();
        toolBar = (Toolbar) findViewById(R.id.products_toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        filterButton = (ImageButton) findViewById(R.id.toolbar_button);
        logOutButton = (Button) findViewById(R.id.log_out);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFiltered){
                    startActivityForResult(new Intent(getApplicationContext(), FiltersActivity.class), 72);
                }
                else{
                    Filters filters = new Filters();
                    petAdapter.readPetsFromDbByFilter(filters);
                    isFiltered = !isFiltered;
                    filterButton.setImageResource(R.drawable.ic_filter_list_black_24dp);
                }
            }
        });
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigationBar);
        bottomNavigationView.setSelectedItemId(R.id.home);
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

        // set up the RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        petAdapter = new PetAdapter(this);
        petAdapter.readPetsFromDbByFilter(new Filters());
        recyclerView.setAdapter(petAdapter);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GET_FILTERS && resultCode == RESULT_OK && data != null){
            Filters filters = (Filters) data.getSerializableExtra("Filters");
            petAdapter.readPetsFromDbByFilter(filters);
            filterButton.setImageResource(R.drawable.ic_delete_forever_black_24dp);
            isFiltered = !isFiltered;
        }
    }
    public void swipeRight(){bottomNavigationView.setSelectedItemId(R.id.add_pet);}
}
