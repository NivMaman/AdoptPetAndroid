package com.example.adoptpet;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddPetActivity extends AppCompatActivity {
    private EditText fullName, age, kind;
    private FirebaseUser user;
    private Button addToDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        setContentView(R.layout.activity_home);
        fullName = (EditText)findViewById(R.id.name);
        age = (EditText)findViewById(R.id.age);
        kind = (EditText)findViewById(R.id.pet_kind);
        addToDb = (Button)findViewById(R.id.add_db_botton);

        addToDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = fullName.getText().toString();
                int petAge = Integer.parseInt(age.getText().toString());
                String petKind = kind.getText().toString();

                Pet pet = new Pet(name, petAge, petKind);

                db.collection("Users").add(pet).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AddPetActivity.this, "Added to db", Toast.LENGTH_LONG).show();

                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(AddPetActivity.this, "Error In Saving Details: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

            }
        });
    }
}
