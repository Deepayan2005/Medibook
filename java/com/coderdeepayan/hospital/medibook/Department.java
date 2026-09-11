package com.coderdeepayan.hospital.medibook;

public class Department {
    private String departmentName, opdName,doctorName,departmentCode, deptUnitCode;

    public Department(String departmentName, String opdName, String doctorName,
                      String departmentCode, String deptUnitCode) {
        this.departmentName = departmentName;
        this.opdName = opdName;
        this.doctorName = doctorName;
        this.departmentCode = departmentCode;
        this.deptUnitCode = deptUnitCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public String getOpdName() {
        return opdName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public String getDeptUnitCode() {
        return deptUnitCode;
    }
}
