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

import android.widget.DatePicker;

import java.util.Date;

public class Medicine {
    // Encapsulation
    private Date dateStarted;
    private String medicineName;
    private Double doseAmount;
    private String doseUnit;
    private Integer dailyFrequency;

    public Medicine (Date dateStarted, String medicineName, Double doseAmount, String doseUnit, Integer dailyFrequency) {
        this.dateStarted = dateStarted;
        this.medicineName = medicineName;
        this.doseAmount = doseAmount;
        this.doseUnit = doseUnit;
        this.dailyFrequency = dailyFrequency;
    }

    public Date getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(Date dateStarted) {
        this.dateStarted = dateStarted;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public Double getDoseAmount() {
        return doseAmount;
    }

    public void setDoseAmount(Double doseAmount) {
        this.doseAmount = doseAmount;
    }

    public String getDoseUnit() {
        return doseUnit;
    }

    public void setDoseUnit(String doseUnit) {
        this.doseUnit = doseUnit;
    }

    public Integer getDailyFrequency() {
        return dailyFrequency;
    }

    public void setDailyFrequency(Integer dailyFrequency) {
        this.dailyFrequency = dailyFrequency;
    }
}
