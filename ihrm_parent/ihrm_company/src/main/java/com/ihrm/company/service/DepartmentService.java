package com.ihrm.company.service;

import com.ihrm.company.dao.DepartmentDao;
import com.ihrm.domain.company.Department;
import com.zhouyuan.saas.ihrm.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 1.保存部门
     */
    public void save(Department department) {
        //设置主键的值
        String id = idWorker.nextId()+"";
        department.setId(id);
        //调用dao保存部门
        departmentDao.save(department);
    }

    /**
     * 2.更新部门
     */
    public void update(Department department) {
        //1.根据id查询部门
        Department dept = departmentDao.findById(department.getId()).get();
        //2.设置部门属性
        dept.setCode(department.getCode());
        dept.setIntroduce(department.getIntroduce());
        dept.setName(department.getName());
        //3.更新部门
        departmentDao.save(dept);
    }

    /**
     * 3.根据id查询部门
     */
    public Department findById(String id) {
        return departmentDao.findById(id).get();
    }

    /**
     * 4.查询全部部门列表
     */
    public List<Department> findAll(String companyId) {
        Specification<Department> specification = new Specification<Department>() {
            /**
             * 用户构造查询条件
             *      root   ：包含了所有的对象数据
             *      cq     ：高级查询，一般不用
             *      cb     ：构造查询条件
             */
            @Override
            public Predicate toPredicate(Root<Department> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("companyId").as(String.class),companyId);
            }
        };
        return departmentDao.findAll(specification);
    }

    /**
     * 5.根据id删除部门
     */
    public void deleteById(String id) {
        departmentDao.deleteById(id);
    }
}
