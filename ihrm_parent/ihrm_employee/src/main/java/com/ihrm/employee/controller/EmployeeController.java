package com.ihrm.employee.controller;

import com.ihrm.domain.employee.*;
import com.ihrm.domain.employee.response.EmployeeReportResult;
import com.ihrm.employee.service.*;
import com.zhouyuan.saas.ihrm.controller.BaseController;
import com.zhouyuan.saas.ihrm.entity.PageResult;
import com.zhouyuan.saas.ihrm.entity.Result;
import com.zhouyuan.saas.ihrm.entity.ResultCode;
import com.zhouyuan.saas.ihrm.poi.ExcelExportUtil;
import com.zhouyuan.saas.ihrm.utils.BeanMapUtils;
import com.zhouyuan.saas.ihrm.utils.DownloadUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/employees")
public class EmployeeController extends BaseController {
    @Autowired
    private UserCompanyPersonalService userCompanyPersonalService;
    @Autowired
    private UserCompanyJobsService userCompanyJobsService;
    @Autowired
    private ResignationService resignationService;
    @Autowired
    private TransferPositionService transferPositionService;
    @Autowired
    private PositiveService positiveService;
    @Autowired
    private ArchiveService archiveService;


    /**
     * 员工个人信息保存
     */
    @RequestMapping(value = "/{id}/personalInfo", method = RequestMethod.PUT)
    public Result savePersonalInfo(@PathVariable(name = "id") String uid, @RequestBody Map map) throws Exception {
        UserCompanyPersonal sourceInfo = BeanMapUtils.mapToBean(map, UserCompanyPersonal.class);
        if (sourceInfo == null) {
            sourceInfo = new UserCompanyPersonal();
        }
        sourceInfo.setUserId(uid);
        sourceInfo.setCompanyId(super.companyId);
        userCompanyPersonalService.save(sourceInfo);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 员工个人信息读取
     */
    @RequestMapping(value = "/{id}/personalInfo", method = RequestMethod.GET)
    public Result findPersonalInfo(@PathVariable(name = "id") String uid) throws Exception {
        UserCompanyPersonal info = userCompanyPersonalService.findById(uid);
        if(info == null) {
            info = new UserCompanyPersonal();
            info.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS,info);
    }

    /**
     * 员工岗位信息保存
     */
    @RequestMapping(value = "/{id}/jobs", method = RequestMethod.PUT)
    public Result saveJobsInfo(@PathVariable(name = "id") String uid, @RequestBody UserCompanyJobs sourceInfo) throws Exception {
        //更新员工岗位信息
        if (sourceInfo == null) {
            sourceInfo = new UserCompanyJobs();
            sourceInfo.setUserId(uid);
            sourceInfo.setCompanyId(super.companyId);
        }
        userCompanyJobsService.save(sourceInfo);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 员工岗位信息读取
     */
    @RequestMapping(value = "/{id}/jobs", method = RequestMethod.GET)
    public Result findJobsInfo(@PathVariable(name = "id") String uid) throws Exception {
        //UserCompanyJobs info = userCompanyJobsService.findById(super.userId);
        UserCompanyJobs info = userCompanyJobsService.findById(uid);

        if(info == null) {
            info = new UserCompanyJobs();
            info.setUserId(uid);
            info.setCompanyId(companyId);
        }
        return new Result(ResultCode.SUCCESS,info);
    }

    /**
     * 离职表单保存
     */
    @RequestMapping(value = "/{id}/leave", method = RequestMethod.PUT)
    public Result saveLeave(@PathVariable(name = "id") String uid, @RequestBody EmployeeResignation resignation) throws Exception {
        resignation.setUserId(uid);
        resignationService.save(resignation);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 离职表单读取
     */
    @RequestMapping(value = "/{id}/leave", method = RequestMethod.GET)
    public Result findLeave(@PathVariable(name = "id") String uid) throws Exception {
        EmployeeResignation resignation = resignationService.findById(uid);
        if(resignation == null) {
            resignation = new EmployeeResignation();
            resignation.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS,resignation);
    }

    /**
     * 导入员工
     */
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public Result importDatas(@RequestParam(name = "file") MultipartFile attachment) throws Exception {
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 调岗表单保存
     */
    @RequestMapping(value = "/{id}/transferPosition", method = RequestMethod.PUT)
    public Result saveTransferPosition(@PathVariable(name = "id") String uid, @RequestBody EmployeeTransferPosition transferPosition) throws Exception {
        transferPosition.setUserId(uid);
        transferPositionService.save(transferPosition);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 调岗表单读取
     */
    @RequestMapping(value = "/{id}/transferPosition", method = RequestMethod.GET)
    public Result findTransferPosition(@PathVariable(name = "id") String uid) throws Exception {
        UserCompanyJobs jobsInfo = userCompanyJobsService.findById(uid);
        if(jobsInfo == null) {
            jobsInfo = new UserCompanyJobs();
            jobsInfo.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS,jobsInfo);
    }

    /**
     * 转正表单保存
     */
    @RequestMapping(value = "/{id}/positive", method = RequestMethod.PUT)
    public Result savePositive(@PathVariable(name = "id") String uid, @RequestBody EmployeePositive positive) throws Exception {
        positiveService.save(positive);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 转正表单读取
     */
    @RequestMapping(value = "/{id}/positive", method = RequestMethod.GET)
    public Result findPositive(@PathVariable(name = "id") String uid) throws Exception {
        EmployeePositive positive = positiveService.findById(uid);
        if(positive == null) {
            positive = new EmployeePositive();
            positive.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS,positive);
    }

    /**
     * 历史归档详情列表
     */
    @RequestMapping(value = "/archives/{month}", method = RequestMethod.GET)
    public Result archives(@PathVariable(name = "month") String month, @RequestParam(name = "type") Integer type) throws Exception {
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 归档更新
     */
    @RequestMapping(value = "/archives/{month}", method = RequestMethod.PUT)
    public Result saveArchives(@PathVariable(name = "month") String month) throws Exception {
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 历史归档列表
     */
    @RequestMapping(value = "/archives", method = RequestMethod.GET)
    public Result findArchives(@RequestParam(name = "pagesize") Integer pagesize, @RequestParam(name = "page") Integer page, @RequestParam(name = "year") String year) throws Exception {
        Map map = new HashMap();
        map.put("year",year);
        map.put("companyId",companyId);
        Page<EmployeeArchive> searchPage = archiveService.findSearch(map, page, pagesize);
        PageResult<EmployeeArchive> pr = new PageResult(searchPage.getTotalElements(),searchPage.getContent());
        return new Result(ResultCode.SUCCESS,pr);
    }

    /**
     * excel表单导出
     * @param month 月份 格式：2018-02
     * @throws IOException
     */
/*    @RequestMapping(value = "/export/{month}", method = RequestMethod.GET)
    public void export(@PathVariable(name = "month") String month) throws Exception {
        //从db中获取构成表单的数据
        List<EmployeeReportResult> employeeReports = userCompanyPersonalService.findEmployeeReport(month+"%",companyId);

        //获取模板资源
        ClassPathResource classPathResource = new ClassPathResource("excel_template/hr-demo.xlsx");
        //构造excel导出工具类并通过工具类下载excel报表
        ExcelExportUtil excelExportUtil = new ExcelExportUtil(EmployeeReportResult.class, 2, 2);
        excelExportUtil.export(response,classPathResource.getInputStream(),employeeReports,month+"人事报表.xlsx");
    }*/

    /**
     * 百万数据excel表单导出，SXSSFWorkbook无法应用模板样式
     * @param month 月份 格式：2018-02
     * @throws IOException
     */
    @RequestMapping(value = "/export/{month}", method = RequestMethod.GET)
    public void export(@PathVariable(name = "month") String month) throws IOException {
        //从db中获取构成表单的数据
        List<EmployeeReportResult> employeeReports = userCompanyPersonalService.findEmployeeReport(month+"%",companyId);

        /**
         * SXSSFWorkbook可以处理有百万条数量级的excel报表导出，其原理是当内存中创建的对象超过阈值时（此处设置的是200），
         * 便会将内存中的对象以xml的形式写入到磁盘临时空间（windows下可用win+r，输入%temp%来看生成的临时xml）中，
         * 以减轻内存的负担，防止出现oom，但是磁盘io的速度远低于内存，
         * 所以如果当数据量更大以至于写入磁盘的速度跟不上内存中对象增加的速度并且超过了内存大小的时候，仍然有可能会报oom错误
         * 所以使用SXSSFWorkbook时尽量要少创建对象，不要样式和字体，减少对象开销
         */
        SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook(200);
        //获取模板表单
        SXSSFSheet sheet = sxssfWorkbook.createSheet();
        SXSSFRow row;
        SXSSFCell cell ;
        //插入数据到表单，每个employeeReport对象即是表单中一行数据，employeeReports对象中的每个字段即是每一行中的每个单元格的数据值
        for (int j = 0; j < 10000; j++) {
            for (int i = 0; i < employeeReports.size(); i++) {
                //创建行
                row = sheet.createRow(j*employeeReports.size() + i);
                // 编号
                cell = row.createCell(0);
                cell.setCellValue(employeeReports.get(i).getUserId());
                // 姓名
                cell = row.createCell(1);
                cell.setCellValue(employeeReports.get(i).getUsername());
                // 手机
                cell = row.createCell(2);
                cell.setCellValue(employeeReports.get(i).getMobile());
                // 最高学历
                cell = row.createCell(3);
                cell.setCellValue(employeeReports.get(i).getTheHighestDegreeOfEducation());
                // 国家地区
                cell = row.createCell(4);
                cell.setCellValue(employeeReports.get(i).getNationalArea());
                // 护照号
                cell = row.createCell(5);
                cell.setCellValue(employeeReports.get(i).getPassportNo());
                // 籍贯
                cell = row.createCell(6);
                cell.setCellValue(employeeReports.get(i).getNativePlace());
                // 生日
                cell = row.createCell(7);
                cell.setCellValue(employeeReports.get(i).getBirthday());
                // 属相
                cell = row.createCell(8);
                cell.setCellValue(employeeReports.get(i).getZodiac());
                // 入职时间
                cell = row.createCell(9);
                cell.setCellValue(employeeReports.get(i).getTimeOfEntry());
                // 离职类型
                cell = row.createCell(10);
                cell.setCellValue(employeeReports.get(i).getTypeOfTurnover());
                // 离职原因
                cell = row.createCell(11);
                cell.setCellValue(employeeReports.get(i).getReasonsForLeaving());
                // 离职时间
                cell = row.createCell(12);
                cell.setCellValue(employeeReports.get(i).getResignationTime());
            }

        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        sxssfWorkbook.write(outputStream);
        //下载报表
        DownloadUtils.download(outputStream,response,month+"月人事报表.xlsx");
    }
}
