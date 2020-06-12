package com.example.adoptpet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;

public class FiltersActivity extends AppCompatActivity {
    private int minAge = 0, maxAge = 50;
    private CrystalRangeSeekbar rangeSeekbar;
    private TextView min, max;
    private RadioGroup kindGroup, sexGroup;
    private RadioButton kindButton, sexButton;
    private Button applyFilters;
    private AutoCompleteTextView locationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        min = (TextView) findViewById(R.id.min);
        max = (TextView) findViewById(R.id.max);
        kindGroup = (RadioGroup) findViewById(R.id.pet_kind);
        sexGroup = (RadioGroup) findViewById(R.id.pet_sex);
        locationTextView = (AutoCompleteTextView) findViewById(R.id.location);
        rangeSeekbar = (CrystalRangeSeekbar) findViewById(R.id.rangeSeekbar);

// set listener
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                min.setText(String.valueOf(minValue));
                max.setText(String.valueOf(maxValue));
            }
        });

// set final value listener
        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                minAge = Integer.parseInt(minValue.toString());
                maxAge = Integer.parseInt(maxValue.toString());
            }
        });

        // Get the string array
        String[] cities = getResources().getStringArray(R.array.cities_array);
        // Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cities);
        locationTextView.setAdapter(adapter);
        applyFilters = findViewById(R.id.run_filter);
        applyFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filters filters = new Filters();
                if (kindGroup.getCheckedRadioButtonId() != -1){
                    kindButton=(RadioButton)findViewById(kindGroup.getCheckedRadioButtonId());
                    String kind = kindButton.getText().toString();
                    if (kind.equals(Pet.PET_DOG_STRING)){
                        filters.setDogsOnly(true);
                    }
                    else if (kind.equals(Pet.PET_CAT_STRING)){
                        filters.setCatsOnly(true);
                    }
                }
                if (sexGroup.getCheckedRadioButtonId() != -1){
                    sexButton=(RadioButton)findViewById(sexGroup.getCheckedRadioButtonId());
                    String gender = sexButton.getText().toString();
                    if (gender.equals("Male")){
                        filters.setGender("Male");
                    }
                    else if(gender.equals("Female")){
                        filters.setGender("Female");
                    }
                }
                String location = locationTextView.getText().toString();
                if (!location.equals("")){
                    filters.setLocation(location);
                }
                filters.setMaxAge(maxAge);
                filters.setMinAge(minAge);
                Intent intent = new Intent();
                intent.putExtra("Filters", filters);
                setResult(FiltersActivity.RESULT_OK, intent);
                finish();

            }
        });

    }
}
