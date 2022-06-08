package com.example.fmsclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

public class NewSettings extends AppCompatActivity {
    Switch familyLine, spouseLine, storyLine, maleFilter, femaleFilter, momFilter, dadFilter;
    Button logoutButton;


    public static DataCache dataCache = DataCache.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_settings);

        //setting the settings views
        familyLine = findViewById(R.id.familyLines);
        spouseLine = findViewById(R.id.spouseLines);
        storyLine = findViewById(R.id.storyLines);
        maleFilter = findViewById(R.id.maleEvents);
        femaleFilter = findViewById(R.id.femaleEvents);
        momFilter = findViewById(R.id.motherSide);
        dadFilter = findViewById(R.id.fatherSide);
        logoutButton = findViewById(R.id.logoutButton);

        familyLine.setChecked(dataCache.familyBool);
        spouseLine.setChecked(dataCache.spouseBool);
        storyLine.setChecked(dataCache.storyBool);
        maleFilter.setChecked(dataCache.maleBool);
        femaleFilter.setChecked(dataCache.femaleBool);
        momFilter.setChecked(dataCache.maternalBool);
        dadFilter.setChecked(dataCache.paternalBool);

        familyLine.setOnClickListener(view -> dataCache.setFamilyBool(familyLine.isChecked()));
        spouseLine.setOnClickListener(view -> dataCache.setSpouseBool(spouseLine.isChecked()));
        storyLine.setOnClickListener(view -> dataCache.setStoryBool(storyLine.isChecked()));
        maleFilter.setOnClickListener(view -> dataCache.setMaleBool(maleFilter.isChecked()));
        femaleFilter.setOnClickListener(view -> dataCache.setFemaleBool(femaleFilter.isChecked()));
        momFilter.setOnClickListener(view -> dataCache.setMaternalBool(momFilter.isChecked()));
        dadFilter.setOnClickListener(view -> dataCache.setPaternalBool(dadFilter.isChecked()));


        logoutButton.setOnClickListener(view ->{

            dataCache.basePerson = null;
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

    }

        @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        if(dataCache.settingsChanged){
            dataCache.settingsChanged = false;
            return false;
        }
        if(menuItem.getItemId() == android.R.id.home){
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }
}