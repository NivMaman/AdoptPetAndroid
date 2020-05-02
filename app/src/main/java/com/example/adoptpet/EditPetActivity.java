package com.example.adoptpet;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class EditPetActivity extends AddPetActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.bottomNavigationView.setVisibility(View.GONE);


        final Pet originalPet = (Pet) getIntent().getSerializableExtra(PetViewHolder.petExtraKey);
        super.fullNameEditText.setText("" + originalPet.getName());
        super.ageEditText.setText("" + originalPet.getAge());
        super.freeTextEditText.setText("" + originalPet.getFreeText());
        super.contactNumberEditText.setText("" + originalPet.getPhoneNumber());
        super.contactNameEditText.setText("" + originalPet.getContactName());
        super.petKindEditText.setText("" + originalPet.getKind());

        if(originalPet.isMale())
        {
            ((RadioButton) findViewById(R.id.male)).setChecked(true);
        }
        else
        {
            ((RadioButton) findViewById(R.id.female)).setChecked(true);
        }

        if(originalPet.isDog())
        {
            ((RadioButton) findViewById(R.id.dog)).setChecked(true);
        }
        else
        {
            ((RadioButton) findViewById(R.id.cat)).setChecked(true);
        }

        super.imagePathList = originalPet.getPicturesUriArr();
        RefreshPicturesView();

        super.locationTextView.setText(originalPet.getLocation());

        super.addToDb.setText("Edit Pet");
        super.addToDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DBWrapper.editPet(originalPet.getDocumentReference(),CollectPetInfo(), user, imagePathList, EditPetActivity.this);
            }
        });




    }
}
