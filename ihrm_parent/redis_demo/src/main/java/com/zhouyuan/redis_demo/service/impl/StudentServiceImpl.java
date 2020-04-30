package com.zhouyuan.redis_demo.service.impl;

//import com.zhouyuan.redis_demo.config.dbConfig.ReadDataSource;
//import com.zhouyuan.redis_demo.config.dbConfig.WriteDataSource;
import com.zhouyuan.redis_demo.config.mysql.DataSourceConfig;
import com.zhouyuan.redis_demo.config.mysql.TargetDateSource;
import com.zhouyuan.redis_demo.dao.StudentDao;
import com.zhouyuan.redis_demo.entity.Student;
import com.zhouyuan.redis_demo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @description:
 * @author: yuand
 * @create: 2020-04-29 17:42
 * @modifiedBy: yuand
 **/
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentDao studentDao;

    @Override
    @TargetDateSource(dataSource = DataSourceConfig.READ_DATASOURCE_KEY)
//    @ReadDataSource
    public List<Student> findAll() {
        return studentDao.findAll();
    }

    @Override
//    @ReadDataSource
    @TargetDateSource(dataSource = DataSourceConfig.READ_DATASOURCE_KEY)
    public Student findById(Integer id) {
        Optional<Student> students = studentDao.findById(id);
        if (students.isPresent() && students.get() != null) {
            return students.get();
        }
        return null;
    }

    @Override
    @Transactional
//    @WriteDataSource
    @TargetDateSource(dataSource = DataSourceConfig.WRITE_DATASOURCE_KEY)
    public Integer save(Student entity) throws Exception {
        if (entity.getId() != null) {
            Student perz = studentDao.saveAndFlush(entity);
            return perz.getId();
        }
        Student perz = studentDao.save(entity);
        return perz.getId();
    }
}
