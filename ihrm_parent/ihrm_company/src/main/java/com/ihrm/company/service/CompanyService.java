package com.ihrm.company.service;

import com.ihrm.company.dao.CompanyDao;
import com.ihrm.domain.company.Company;
import com.zhouyuan.saas.ihrm.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CompanyService {

    @Autowired
    CompanyDao companyDao;
    @Autowired
    IdWorker idWorker;

    /**
     * 保存企业
     * @param company
     */
    public void add(Company company){
        //通过idwork生成id
        long id = idWorker.nextId();
        //基本属性的设置
        company.setId(String.valueOf(id));
        //默认的状态
        company.setState(1);
        company.setAuditState("0");
        company.setCreateTime(new Date());
        companyDao.save(company);
    }

    /**
     * 更新企业
     *  1.参数：Company
     *  2.根据id查询企业对象
     *  3.设置修改的属性
     *  4.调用dao完成更新
     */
    public void update(Company company){
        Company temp = companyDao.findById(company.getId()).get();
        temp.setName(company.getName());
        temp.setCompanyPhone(company.getCompanyPhone());
        companyDao.save(temp);
    }

    /**
     * 删除企业
     */
    public void deleteById(String id){
        companyDao.deleteById(id);
    }

    /**
     * 根据id查询企业
     */
    public Company findById(String id){
        return companyDao.findById(id).get();
    }

    /**
     * 查询企业列表
     */
    public List<Company> findAll(){
        return companyDao.findAll();
    }
}
