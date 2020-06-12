package com.example.adoptpet;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import java.util.ArrayList;

public class EditPetActivity extends AddPetActivity {

    private final String logTagClass = "EditPetActivity";

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
                Pet pet = CollectPetInfo();
                // Iterate over imagePathList find already uploaded to DB images
                // Those images are removed from the list and added to pet instance directly
                for(int i=0;i<imagePathList.size();i++)
                {
                    String uri = imagePathList.get(i).toString();
                    Log.i(logTagClass,"uri " + uri);
                    if(uri.contains("https://"))
                    {
                        pet.addUriReference(uri);
                        imagePathList.remove(i);

                    }

                }
                Log.i(logTagClass,"imagePathList.size() = " + imagePathList.size());
                DBWrapper.editPet(originalPet.getDocumentReference(),pet, user, imagePathList, EditPetActivity.this);
            }
        });




    }
}
