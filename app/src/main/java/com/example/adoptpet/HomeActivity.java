package com.example.adoptpet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

    }



    private void fetch() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("posts");

        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(query, new SnapshotParser<Model>() {
                            @NonNull
                            @Override
                            public Model parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new Model(snapshot.child("id").getValue().toString(),
                                        snapshot.child("title").getValue().toString(),
                                        snapshot.child("desc").getValue().toString());
                            }
                        })
                        .build();

        adapter = new FirestoreRecyclerAdapter<Pet, PetViewHolder>(options) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item, parent, false);

                return new PetViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(PetViewHolder holder, final int position, Model model) {
                //holder.setTxtTitle(model.getmTitle());
                //holder.setTxtDesc(model.getmDesc());

                holder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(HomeActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                    }
                });
            }

        };
        recyclerView.setAdapter(adapter);
    }
}
