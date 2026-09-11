package com.coderdeepayan.hospital.medibook;

public class Queue {
    private String name, departmentUnitName, queueNumber;

    public Queue(String name, String departmentUnitName, String queueNumber) {
        this.name = name;
        this.departmentUnitName = departmentUnitName;
        this.queueNumber = queueNumber;
    }

    public String getName() {
        return name;
    }

    public String getDepartmentUnitName() {
        return departmentUnitName;
    }

    public String getQueueNumber() {
        return queueNumber;
    }
}
