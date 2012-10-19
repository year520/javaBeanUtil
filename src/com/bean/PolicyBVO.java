package com.bean;

import java.util.List;


public class PolicyBVO {
    private String policyId;

    private String policyNo;

    private String expDate;

    private String effDate;
    private List<ClassificationBO>        classificationBOList;
    
    public List<ClassificationBO> getClassificationBOList() {
		return classificationBOList;
	}

	public void setClassificationBOList(List<ClassificationBO> classificationBOList) {
		this.classificationBOList = classificationBOList;
	}

	public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getEffDate() {
        return effDate;
    }

    public void setEffDate(String effDate) {
        this.effDate = effDate;
    }

}
