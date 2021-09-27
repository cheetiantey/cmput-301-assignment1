package com.example.tey_medbook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CustomList extends ArrayAdapter<Medicine> {
    private ArrayList<Medicine> meds;
    private Context context;


    public CustomList(Context context, ArrayList<Medicine> med) {
        super(context, 0, med);
        this.meds = med;
        this.context = context;
    }

    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {
        // Ensure that the date follows (yyyy-mm-dd)
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content, parent,false);
        }

        // Connect the fields in content.xml to the variables below accordingly
        Medicine med = meds.get(position);
        TextView medName = view.findViewById(R.id.medName);
        TextView doseAmount = view.findViewById(R.id.dosage);
        TextView dateStarted = view.findViewById(R.id.dateStarted);
        TextView dailyFrequency = view.findViewById(R.id.dailyFrequency);

        // Put in the values accordingly
        medName.setText(med.getMedicineName());
        doseAmount.setText("Dosage:" + med.getDoseAmount().toString() + med.getDoseUnit());
        dateStarted.setText("From:" + formatter.format(med.getDateStarted()));
        dailyFrequency.setText(med.getDailyFrequency().toString() + " daily");
        return view;
    }
}
