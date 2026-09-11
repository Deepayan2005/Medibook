package com.coderdeepayan.hospital.medibook;

import java.util.List;

public class Patient {
    private String name, crNo, umid_id;
    private List<Appointment> appointmentList;
    public Patient(String name, String crNo, String umid_id) {
        this.name = name;
        this.crNo = crNo;
        this.umid_id = umid_id;
    }

    public Patient(String name, String crNo,List<Appointment> appointmentList){
        this.name=name;
        this.crNo=crNo;
        this.appointmentList=appointmentList;
    }

    public String getName() {
        return name;
    }

    public List<Appointment> getAppointmentList() {
        return appointmentList;
    }

    public String getCrNo() {
        return crNo;
    }
    public String getUmid_id() {
        return umid_id;
    }
}
