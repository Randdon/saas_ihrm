package com.ihrm.employee.dao;

import com.ihrm.domain.employee.UserCompanyPersonal;
import com.ihrm.domain.employee.response.EmployeeReportResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 数据访问接口
 */
public interface UserCompanyPersonalDao extends JpaRepository<UserCompanyPersonal, String>, JpaSpecificationExecutor<UserCompanyPersonal> {
    UserCompanyPersonal findByUserId(String userId);

    /**
     * 获取构成表单的数据
     * @param month
     * @param companyId
     * @return
     */
    @Query(value = "select new com.ihrm.domain.employee.response.EmployeeReportResult(a,b) from UserCompanyPersonal a " +
            "left join EmployeeResignation b on a.userId = b.userId " +
            "where a.companyId = ?2 and a.timeOfEntry like ?1 or (b.resignationTime like ?1)")
    List<EmployeeReportResult> findEmployeeReport(String month, String companyId);
}