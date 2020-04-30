package com.zhouyuan.redis_demo.dao;

import com.zhouyuan.redis_demo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @description:
 * @author: yuand
 * @create: 2020-04-29 17:44
 * @modifiedBy: yuand
 **/
public interface StudentDao extends JpaRepository<Student,Integer>, JpaSpecificationExecutor<Student> {
}
