package com.zhouyuan.redis_demo.service;

import com.zhouyuan.redis_demo.entity.Student;

import java.util.List;

/**
 * @description:
 * @author: yuand
 * @create: 2020-04-29 17:42
 * @modifiedBy: yuand
 **/
public interface StudentService {
    List<Student> findAll();

    Student findById(Integer id);

    Integer save(Student entity) throws Exception;

}
