package com.coderdeepayan.hospital.medibook;

public class Prescription {
    private String hospitalCode,crno, episodeCode, visitNo, entryDate,departmentUnitName,hospitalName,formattedDate,patient_name;

    public Prescription(String hospitalCode, String crno, String episodeCode, String visitNo,
                        String entryDate, String departmentUnitName, String hospitalName,
                        String formattedDate, String patient_name) {
        this.hospitalCode = hospitalCode;
        this.crno = crno;
        this.episodeCode = episodeCode;
        this.visitNo = visitNo;
        this.entryDate = entryDate;
        this.departmentUnitName = departmentUnitName;
        this.hospitalName = hospitalName;
        this.formattedDate = formattedDate;
        this.patient_name = patient_name;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public String getDepartmentUnitName() {
        return departmentUnitName;
    }

    public String getHospitalCode() {
        return hospitalCode;
    }

    public String getCrno() {
        return crno;
    }

    public String getEpisodeCode() {
        return episodeCode;
    }

    public String getVisitNo() {
        return visitNo;
    }

    public String getEntryDate() {
        return entryDate;
    }


}
