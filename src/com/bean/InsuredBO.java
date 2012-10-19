package com.bean;


import java.util.List;

public class InsuredBO {

    private String                  insuredId;

    private String                  insuredNo;

    
    private ClassificationBO        classificationBO;

    private List<PolicyCustomerBVO> policyCustmerBVOList;

    public String getInsuredId() {
        return insuredId;
    }

    public void setInsuredId(String insuredId) {
        this.insuredId = insuredId;
    }

    public String getInsuredNo() {
        return insuredNo;
    }

    public void setInsuredNo(String insuredNo) {
        this.insuredNo = insuredNo;
    }

    public ClassificationBO getClassificationBO() {
        return classificationBO;
    }

    public void setClassificationBO(ClassificationBO classificationBO) {
        this.classificationBO = classificationBO;
    }

    public List<PolicyCustomerBVO> getPolicyCustmerBVOList() {
        return policyCustmerBVOList;
    }

    public void setPolicyCustmerBVOList(List<PolicyCustomerBVO> policyCustmerBVOList) {
        this.policyCustmerBVOList = policyCustmerBVOList;
    }


}
