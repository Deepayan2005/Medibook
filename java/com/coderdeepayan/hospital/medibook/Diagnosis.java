package com.coderdeepayan.hospital.medibook;

import java.util.List;

public class Diagnosis {
    private String testName,patientName,crNo,hospitalCode,deptName,labName,requestNo,requestDate;
    private boolean isReportMade;
    private List<DiagnosticResult> diagnosticResultList;

    public Diagnosis(String testName, String patientName, String crNo, String hospitalCode,
                     String deptName, String labName,
                     String requestNo, String requestDate, boolean isReportMade,
                     List<DiagnosticResult> diagnosticResultList) {
        this.testName = testName;
        this.patientName = patientName;
        this.crNo = crNo;
        this.hospitalCode = hospitalCode;
        this.deptName = deptName;
        this.labName = labName;
        this.requestNo = requestNo;
        this.requestDate = requestDate;
        this.isReportMade = isReportMade;
        this.diagnosticResultList = diagnosticResultList;
    }

    public String getPatientName() {
        return patientName;
    }

    public List<DiagnosticResult> getDiagnosticResultList() {
        return diagnosticResultList;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public String getCrNo() {
        return crNo;
    }

    public boolean isReportMade() {
        return isReportMade;
    }

    public String getTestName() {
        return testName;
    }

    public String getHospitalCode() {
        return hospitalCode;
    }

    public String getDeptName() {
        return deptName;
    }

    public String getLabName() {
        return labName;
    }

    public String getRequestNo() {
        return requestNo;
    }
}
