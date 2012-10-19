package com.bean;


import java.util.List;
import java.util.Map;

public class PolicyBO {
    private PolicyBVO policyBVO;

    private String    field1;

    private String    field2;

    private Map map;
    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    private PolicyCustomerBVO customerBVO;
    
    private List<InsuredBO> insuredBOList;
    
    
    public PolicyCustomerBVO getCustomerBVO() {
        return customerBVO;
    }

    public void setCustomerBVO(PolicyCustomerBVO customerBVO) {
        this.customerBVO = customerBVO;
    }

    public List<InsuredBO> getInsuredBOList() {
        return insuredBOList;
    }

    public void setInsuredBOList(List<InsuredBO> insuredBOList) {
        this.insuredBOList = insuredBOList;
    }

    public PolicyBVO getPolicyBVO() {
        return policyBVO;
    }

    public void setPolicyBVO(PolicyBVO policyBVO) {
        this.policyBVO = policyBVO;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

}
