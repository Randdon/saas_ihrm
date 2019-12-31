package com.ihrm.company.controller;

import com.ihrm.company.service.CompanyService;
import com.ihrm.company.service.DepartmentService;
import com.ihrm.domain.company.Company;
import com.ihrm.domain.company.Department;
import com.ihrm.domain.company.response.DeptListResult;
import com.zhouyuan.saas.ihrm.controller.BaseController;
import com.zhouyuan.saas.ihrm.entity.Result;
import com.zhouyuan.saas.ihrm.entity.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

//1.解决跨域
@CrossOrigin
//2.声明restContoller
@RestController
//3.设置父路径
@RequestMapping(value="/company")   //  company/deparment
public class DepartmentController extends BaseController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private CompanyService companyService;

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentController.class);

    /**
     * 保存
     */
    @RequestMapping(value="/department",method = RequestMethod.POST)
    public Result save(@RequestBody Department department) {
        //1.设置保存的企业id
        /**
         * String companyId = "1";
         * 因为该Controller继承了BaseController，所以在执行该controller的每个方法前都会先执行BaseController里@ModelAttribute注解的方法
         * 所以不用在每个方法里再新构造companyId了，直接用BaseController里的companyId即可
         */

        /**
         * 企业id：目前使用固定值1，以后会解决
         */

        LOGGER.info("测试BaseController里的setRequestAndResponse方法是否能获取到该save方法的reques参数：{}",request.getParameter("name"));
        department.setCompanyId(companyId);
        department.setCreateTime(new Date());
        //2.调用service完成保存企业
        departmentService.save(department);
        //3.构造返回结果
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 查询企业的部门列表
     * 指定企业id
     */
    @RequestMapping(value="/department",method = RequestMethod.GET)
    public Result findAll() {
        //1.指定企业id
        //String companyId = "1";

        Company company = companyService.findById(companyId);
        //2.完成查询
        List<Department> list = departmentService.findAll(companyId);
        //3.构造返回结果
        DeptListResult deptListResult = new DeptListResult(company,list);
        return new Result(ResultCode.SUCCESS,deptListResult);
    }

    /**
     * 根据ID查询department
     */
    @RequestMapping(value="/department/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable(value="id") String id) {
        Department department = departmentService.findById(id);
        return new Result(ResultCode.SUCCESS,department);
    }

    /**
     * 修改Department
     */
    @RequestMapping(value="/department/{id}",method = RequestMethod.PUT)
    public Result update(@PathVariable(value="id") String id, @RequestBody Department department) {
        //1.设置修改的部门id
        department.setId(id);
        //2.调用service更新
        departmentService.update(department);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据id删除
     */
    @RequestMapping(value="/department/{id}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable(value="id") String id) {
        departmentService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据code查询department
     * @return
     */
    @RequestMapping(value="/department/findByCode",method = RequestMethod.GET)
    public Department findByCode(@RequestParam(value="code") String departCode) {
        Department department = departmentService.findByCompanyIdAndCode(departCode,companyId);
        return department;
    }
}
