package com.example.tey_medbook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements AddMedicineFragment.OnFragmentInteractionListener {
    // Declare the variables so that we can reference it later.
    ListView medList;
    ArrayAdapter<Medicine> medAdapter;
    ArrayList<Medicine> medDataList;
    TextView totalDailyDoses;
    Integer dosesCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        medList = findViewById(R.id.medicine_list);
        totalDailyDoses = findViewById(R.id.textView);

        // Display an empty list of medicines on app startup
        medDataList = new ArrayList<>();
        medAdapter = new CustomList(this, medDataList);
        medList.setAdapter(medAdapter);
        displayDailyDoses(dosesCounter);

        // Let the user add a new medicine
        final FloatingActionButton addCityButton = findViewById(R.id.add_medicine_button);
        addCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddMedicineFragment().show(getSupportFragmentManager(), "ADD_CITY");
            }
        });

        // Let the user modify the medicine details
        medList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Medicine selectedMedicine = (Medicine) medList.getItemAtPosition(i);
                int position = i;
                new AddMedicineFragment().newInstance(selectedMedicine, i).show(getSupportFragmentManager(), "EDIT_CITY");
            }
        });
    }

    // Display the daily doses of medicines
    public void displayDailyDoses(Integer dosesCounter) {
        totalDailyDoses.setText("Doses per day: " + dosesCounter.toString());
    }

    // Create a new medicine entry and increment the number of doses accordingly
    @Override
    public void createMedicine(Medicine newMedicine) {
        dosesCounter += newMedicine.getDailyFrequency();
        displayDailyDoses(dosesCounter);
        medAdapter.add(newMedicine);
    }

    // Edit the medicine entry
    @Override
    public void editMedicine(Medicine newMedicine, int pos) {
        Medicine item = medAdapter.getItem(pos);

        // Replace the old dailyFrequency with the new dailyFrequency
        dosesCounter -= item.getDailyFrequency();
        dosesCounter += newMedicine.getDailyFrequency();
        item.setMedicineName(newMedicine.getMedicineName());
        item.setDateStarted(newMedicine.getDateStarted());
        item.setDoseAmount(newMedicine.getDoseAmount());
        item.setDoseUnit(newMedicine.getDoseUnit());
        item.setDailyFrequency(newMedicine.getDailyFrequency());
        medAdapter.notifyDataSetChanged();
        displayDailyDoses(dosesCounter);
    }

    // Remove the medicine entry and decrement the number of doses accordingly
    @Override
    public void deleteMedicine(int pos) {
        Medicine item = medAdapter.getItem(pos);
        medAdapter.remove(item);
        medAdapter.notifyDataSetChanged();
        dosesCounter -= item.getDailyFrequency();
        displayDailyDoses(dosesCounter);
    }
}