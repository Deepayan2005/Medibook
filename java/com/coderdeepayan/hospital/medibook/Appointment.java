package com.coderdeepayan.hospital.medibook;

public class Appointment {
    private String deptUnitName,hospitalName,currentQueueNo,patientQueueNo;
    private boolean isDoctorAvailable;

    public Appointment(String deptUnitName, String hospitalName, String currentQueueNo,
                       String patientQueueNo, boolean isDoctorAvailable) {
        this.deptUnitName = deptUnitName;
        this.hospitalName = hospitalName;
        this.currentQueueNo = currentQueueNo;
        this.patientQueueNo = patientQueueNo;
        this.isDoctorAvailable = isDoctorAvailable;
    }

    public Appointment() {
    }

    public String getDeptUnitName() {
        return deptUnitName;
    }
    public String getHospitalName() {
        return hospitalName;
    }
    public String getCurrentQueueNo() {
        return currentQueueNo;
    }
    public String getPatientQueueNo() {
        return patientQueueNo;
    }
    public boolean isDoctorAvailable() {
        return isDoctorAvailable;
    }

    public void setDeptUnitName(String deptUnitName) {
        this.deptUnitName = deptUnitName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public void setCurrentQueueNo(String currentQueueNo) {
        this.currentQueueNo = currentQueueNo;
    }

    public void setPatientQueueNo(String patientQueueNo) {
        this.patientQueueNo = patientQueueNo;
    }

    public void setDoctorAvailable(boolean doctorAvailable) {
        isDoctorAvailable = doctorAvailable;
    }
}
