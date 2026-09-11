package com.coderdeepayan.hospital.medibook;

public class DiagnosticResult {
    private String parameterName,value,standardValue;
    private boolean isNormal;

    public DiagnosticResult(String parameterName, String value, String standardValue, boolean isNormal) {
        this.parameterName = parameterName;
        this.value = value;
        this.standardValue = standardValue;
        this.isNormal = isNormal;
    }

    public String getParameterName() {
        return parameterName;
    }

    public String getValue() {
        return value;
    }

    public String getStandardValue() {
        return standardValue;
    }

    public boolean isNormal() {
        return isNormal;
    }
}
