//Copyright [2021] [Chee Tey]
//
//        Licensed under the Apache License, Version 2.0 (the "License");
//        you may not use this file except in compliance with the License.
//        You may obtain a copy of the License at
//
//        http://www.apache.org/licenses/LICENSE-2.0
//
//        Unless required by applicable law or agreed to in writing, software
//        distributed under the License is distributed on an "AS IS" BASIS,
//        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//        See the License for the specific language governing permissions and
//        limitations under the License.

package com.example.tey_medbook;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Date;

public class AddMedicineFragment extends DialogFragment {
    private EditText medName;
    private EditText doseAmount;
    private Spinner doseUnit;
    private EditText dailyFrequency;
    private DatePicker dateStarted;
    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void createMedicine(Medicine newCity);
        void editMedicine(Medicine newCity, int pos);
        void deleteMedicine(int pos);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    public AddMedicineFragment newInstance (Medicine med, int pos) {
        Bundle args = new Bundle();
        args.putSerializable("medName", med.getMedicineName());
        args.putSerializable("dateStarted", med.getDateStarted());
        args.putSerializable("doseAmount", med.getDoseAmount());
        args.putSerializable("doseUnit", med.getDoseUnit());
        args.putSerializable("dailyFrequency", med.getDailyFrequency());
        args.putSerializable("pos", pos);

        AddMedicineFragment fragment = new AddMedicineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // This method is used to find the position of a value in the spinner given a value("mg", "mcg", "drop")
    private int getIndexOfSpinner(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        // If the value isn't found in within the spinner, return 0 (first value in the spinner)
        return 0;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_med_fragment, null);
        medName = view.findViewById(R.id.medName_editText);
        doseAmount = view.findViewById(R.id.doseAmount_editText);
        doseUnit = view.findViewById(R.id.doseUnit);
        dailyFrequency = view.findViewById(R.id.dailyFrequency_editText);
        dateStarted = view.findViewById(R.id.dateStarted_editText);

        // Limit the selections of unit to "mg", "mcg", and "drop"
        String[] unitSelections = new String[] {"mg", "mcg", "drop"};
        Spinner spinner = view.findViewById(R.id.doseUnit);
        // Because we're in a fragment, "getActivity().getBaseContext()" is used in lieu of "this"
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (getActivity().getBaseContext(), android.R.layout.simple_spinner_item, unitSelections);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

        // If the user is updating an existing Medicine, set the cityName and provinceName fields to
        // its existing values
        if (getArguments() != null) {
            // Put the values into the field accordingly
            medName.setText((String) getArguments().getSerializable("medName"));

            int year = ((Date)getArguments().getSerializable("dateStarted")).getYear() + 1900;
            int month = ((Date)getArguments().getSerializable("dateStarted")).getMonth();
            int day = ((Date)getArguments().getSerializable("dateStarted")).getDate();
            dateStarted.updateDate(year, month, day);

            doseAmount.setText(getArguments().getSerializable("doseAmount").toString());
            doseUnit.setSelection(getIndexOfSpinner(doseUnit, (String) getArguments().getSerializable("doseUnit")));
            // doseUnit.setText((String) getArguments().getSerializable("doseUnit"));
            dailyFrequency.setText(getArguments().getSerializable("dailyFrequency").toString());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // Create a new Medicine
        if (getArguments() == null) {
            return builder
                    .setView(view)
                    .setTitle("Add medicine")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Ensure that the user doesn't submit invalid forms (i.e. empty medicine name, 0 for daily frequency, etc)
                            if (TextUtils.isEmpty(medName.getText().toString()))
                            {
                                Toast.makeText(getActivity().getBaseContext(),
                                        "Medicine name can't be empty!",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (TextUtils.isEmpty(doseAmount.getText().toString()) || (Double.parseDouble(doseAmount.getText().toString())== 0.0))
                            {
                                Toast.makeText(getActivity().getBaseContext(),
                                        "Dose amount can't be empty or zero!",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (TextUtils.isEmpty(dailyFrequency.getText().toString()) || (Integer.parseInt(dailyFrequency.getText().toString()) == 0))
                            {
                                Toast.makeText(getActivity().getBaseContext(),
                                        "Medicine daily frequency can't be empty or zero!",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String med_Name = medName.getText().toString();
                            Double dose_Amount = Double.parseDouble(doseAmount.getText().toString());
                            String dose_Unit = doseUnit.getSelectedItem().toString();
                            Integer daily_Frequency = Integer.parseInt(dailyFrequency.getText().toString());

                            // "A year y is represented by the integer y - 1900"
                            int year = dateStarted.getYear() - 1900;
                            int month = dateStarted.getMonth();
                            int day = dateStarted.getDayOfMonth();
                            Date date_started = new Date(year, month, day);

                            // Update Medicine if the user is trying to modify it, else create a new entry
                            if (getArguments() != null) {
                                listener.editMedicine(new Medicine(date_started, med_Name, dose_Amount, dose_Unit, daily_Frequency), (int) getArguments().getSerializable("pos"));
                            } else {
                                listener.createMedicine(new Medicine(date_started, med_Name, dose_Amount, dose_Unit, daily_Frequency));
                            }

                        }
                    }).create();

            // Edit/Delete an existing Medicine
        } else {
            return builder
                    .setView(view)
                    .setTitle("Edit medicine")
                    // Let the user delete this Medicine entry
                    .setNegativeButton("Delete", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Pass the position of the medicine in the customList to be deleted back to MainActivity
                            listener.deleteMedicine((int) getArguments().getSerializable("pos"));
                        }
                    })
                    .setNeutralButton("Cancel", null)

                    // Edit the existing Medicine entry
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Ensure that the user doesn't submit invalid forms (i.e. empty medicine name, 0 for daily frequency, etc)
                            if (TextUtils.isEmpty(medName.getText().toString()))
                            {
                                Toast.makeText(getActivity().getBaseContext(),
                                        "Medicine name can't be empty!",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (TextUtils.isEmpty(doseAmount.getText().toString()) || (Double.parseDouble(doseAmount.getText().toString())== 0.0))
                            {
                                Toast.makeText(getActivity().getBaseContext(),
                                        "Dose amount can't be empty or zero!",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (TextUtils.isEmpty(dailyFrequency.getText().toString()) || (Integer.parseInt(dailyFrequency.getText().toString()) == 0))
                            {
                                Toast.makeText(getActivity().getBaseContext(),
                                        "Medicine daily frequency can't be empty or zero!",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }

                            String med_Name = medName.getText().toString();
                            Double dose_Amount = Double.parseDouble(doseAmount.getText().toString());
                            String dose_Unit = doseUnit.getSelectedItem().toString();
                            Integer daily_Frequency = Integer.parseInt(dailyFrequency.getText().toString());

                            // "A year y is represented by the integer y - 1900"
                            int year = dateStarted.getYear() - 1900;
                            int month = dateStarted.getMonth();
                            int day = dateStarted.getDayOfMonth();
                            Date date_started = new Date(year, month, day);

                            // Update the medicine at the "pos" with the new Medicine object
                            listener.editMedicine(new Medicine(date_started, med_Name, dose_Amount, dose_Unit, daily_Frequency), (int) getArguments().getSerializable("pos"));
                        }
                    }).create();
        }

    }
}
