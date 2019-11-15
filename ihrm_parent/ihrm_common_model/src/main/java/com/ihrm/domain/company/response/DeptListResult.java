package com.ihrm.domain.company.response;

import com.ihrm.domain.company.Company;
import com.ihrm.domain.company.Department;

import java.util.List;

public class DeptListResult {

    private String companyId;
    private String companyName;
    private String companyManage;//公司联系人
    private List<Department> depts;

    public DeptListResult(Company company, List depts) {
        this.companyId = company.getId();
        this.companyName = company.getName();
        this.companyManage = company.getLegalRepresentative();//公司联系人
        this.depts = depts;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyManage() {
        return companyManage;
    }

    public void setCompanyManage(String companyManage) {
        this.companyManage = companyManage;
    }

    public List<Department> getDepts() {
        return depts;
    }

    public void setDepts(List<Department> depts) {
        this.depts = depts;
    }
}
