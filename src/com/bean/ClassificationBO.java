package com.bean;


import java.util.Map;

public class ClassificationBO {
    private String classificationId;

    @SuppressWarnings("rawtypes")
    private Map    fieldMap;

    public String getClassificationId() {
        return classificationId;
    }

    public void setClassificationId(String classificationId) {
        this.classificationId = classificationId;
    }

    @SuppressWarnings("rawtypes")
    public Map getFieldMap() {
        return fieldMap;
    }

    public void setFieldMap(@SuppressWarnings("rawtypes") Map fieldMap) {
        this.fieldMap = fieldMap;
    }
    
}
