package com.zhouyuan.redis_demo.controller;

import com.zhouyuan.redis_demo.entity.Student;
import com.zhouyuan.redis_demo.service.StudentService;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description:
 * @author: yuand
 * @create: 2020-04-29 17:45
 * @modifiedBy: yuand
 **/
@RestController
@RequestMapping("/student/*")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @RequestMapping("findAll")
    public List<Student> findAll() {
        return studentService.findAll();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Student findById(@PathVariable("id") Integer id) {
        return studentService.findById(id);
    }

    @ApiImplicitParam(name = "entity", value = "学生实体类", required = true, dataType = "Student")
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public Integer save(@RequestBody Student entity) throws Exception {
        return studentService.save(entity);
    }

}
