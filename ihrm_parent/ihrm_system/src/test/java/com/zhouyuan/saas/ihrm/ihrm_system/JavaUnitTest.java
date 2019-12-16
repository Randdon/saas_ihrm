package com.zhouyuan.saas.ihrm.ihrm_system;

import com.ihrm.domain.system.Role;
import com.ihrm.domain.system.User;
import com.ihrm.domain.system.response.RoleResult;
import com.ihrm.system.dao.RoleDao;
import com.ihrm.system.dao.UserDao;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: Integer的值在-128到127时，Integer对象是在IntegerCache.cache产生，会复用已有对象，也就是说，
 * 这个区间的Integer可以直接用等号进行判断。
 * Integer的值在-128到127之外时，Integer对象在堆上产生，不会复用已有对象，用等号会返回false。
 * 建议老老实实用equals()方法来比较Integer对象。
 * @author: yuand
 * @create: 2019-11-29 11:24
 **/
public class JavaUnitTest extends IhrmSystemApplicationTests{

    @Autowired
    UserDao userDao;
    @Autowired
    RoleDao roleDao;

    private static final Logger LOGGER = LoggerFactory.getLogger(JavaUnitTest.class);
    @Test
    public void test(){
        integerTest(129);
        System.out.println("**************************************");
        integerTest(127);
    }

    /**
     * Integer的值在-128到127时，Integer对象是在IntegerCache.cache产生，会复用已有对象，也就是说，这个区间的Integer可以直接用等号进行判断。
     * Integer的值在-128到127之外时，Integer对象在堆上产生，不会复用已有对象，用等号会返回false。
     * 建议老老实实用equals()方法来比较Integer对象。
     * @param test
     */
    public void integerTest(int test){
        Integer integer = test;
        Integer integer1 = test;
        System.out.println(integer == test);
        System.out.println(integer == integer1);
        System.out.println(integer.intValue() == integer1.intValue());
        System.out.println(integer.equals(integer1));
    }

    @Test
    public void jpaTest(){
        List<String> ids = new ArrayList<>(3);
        ids.add("1063705482939731968");
        Optional<User> byId = userDao.findById("1063705482939731968");
        System.out.println(byId.get());
        //List<String> ids = Arrays.asList("1063705482939731968", "1075383135459430400", "1075383135371350016");
        List<User> list = userDao.findAllById(ids);
        System.out.println(list.size());
    }

    /**
     * jpa的多对多关系分为维护端和被维护端，删除维护端的记录会影响关联表中的记录
     * 但是删除被维护端的记录不会影响关联表中的记录
     */
    @Test
    //@Transactional
    public void jpaMany2ManyTest(){
        /**
         * 删除用户的同时也会删除用户_角色关联表
         */
        //userDao.deleteById("1066370498633486336");
        /**
         * 删除角色的时候不会删除用户_角色关联表
         * 但会删除角色_权限关联表
         */
        //roleDao.deleteById("1062944989845262336");

        /**
         * 在用户和角色关联的时候，比如用户（a）关联了两个角色（i和ii），那么在根据id查用户的时候，去看User实体里的roles角色Set集合，
         * 里面会报错：Unable to evaluate the expression Method threw 'org.hibernate.LazyInitializationException' exception.
         * 解决方案：加上    @Transactional注解
         * 问题原因：见 https://blog.csdn.net/Randon_Renhai/article/details/103506030
         */
/*
        User user = userDao.findById("1063705482939731968").get();
        System.out.println(user.getCompanyId());
        //System.out.println(JSON.toJSONString(user));
        System.out.println(user.getRoles().size());//懒加载——在getRole的时候才会去查role表
        for (Role role:
             user.getRoles()) {
            System.out.println(role);
            System.out.println(role.getName());
            System.out.println(role.getUsers().size());
        }
        System.out.println(user.getRoles().iterator().next());
        System.out.println(user.getRoles().size());
*/
        roleDao.deleteById("1064098829009293312");
/*
        Role byId = roleDao.findById("1064098829009293312").get();
        User user = userDao.findById("1063705482939731968").get();
        Set<Role> roles = new HashSet<>(1);

        roles.add(byId);
        user.setRoles(roles);
        userDao.save(user);
        System.out.println(byId.getUsers().size());
*/


    }

    @Test
    public void timeStampTest(){
        System.out.println(Instant.now().getEpochSecond());//秒
        System.out.println(System.currentTimeMillis());//毫秒
        System.out.println(Instant.now().getNano());
        long timestamp = LocalDateTime.now().toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        System.out.println(timestamp);
        long timeStamp = System.currentTimeMillis();
        UUID uuid = UUID.randomUUID();
        System.out.println(uuid.timestamp());
        System.out.println(timeStamp);

    }

    @Test
    public void FileName(){
        File file = new File("D:\\projects\\dcc\\code\\dp-project-dcc\\pom.xml");
        System.out.println(file.getName());
    }

    public int download(String fileId, boolean isEnclosure, HttpServletRequest request, HttpServletResponse response, String userId){

        //根据fileID查询出文件路径
        String filePath = "";
        //TODO 需确认如何获取到文件系统的File对象
        File file = new File(System.getenv("user.dir")+filePath);
        BufferedInputStream bufferedInputStream = null;
        OutputStream outputStream = null;
        String fileName = file.getName();
        try {
            //处理文件名中文乱码和空格问题
            if(request.getHeader("User-Agent").toUpperCase().indexOf("MSIE")>0){
                fileName = URLEncoder.encode(fileName, "UTF-8");
            }else {
                fileName = new String((fileName.getBytes("UTF-8")), "ISO8859-1");
            }
            //fileName=fileName.replaceAll("\\+","%20");    //处理空格转为加号的问题
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=\""
                    + fileName
                    + "\"");

            FileInputStream fileInputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            byte[] buff = new byte[1024];
            int readResult = bufferedInputStream.read(buff);
            outputStream = response.getOutputStream();
            while (readResult != -1){
                outputStream.write(buff,0,readResult);
                outputStream.flush();
                readResult = bufferedInputStream.read(buff);
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("文件下载时出错：{}",e);
        } catch (IOException e) {
            LOGGER.error("文件下载时出错：{}",e);
        } finally {
            if (null != bufferedInputStream){
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    LOGGER.error("输入流关闭时发生异常：{}",e);
                }
            }
            if (null != outputStream){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    LOGGER.error("输出流关闭时异常：{}",e);
                }
            }
        }
        return 0;
    }

    @Test
    public void findEmpty(){
        List<Role> all = roleDao.findAll();
        System.out.println(all.stream().findAny());
    }

    @Test
    public void jpaFindTest(){
        /**
         * 在此处会报懒加载异常，但是在com.ihrm.system.controller.RoleController#findById(java.lang.String)不会报
         */
        Role role = roleDao.findById("1062944989845262336").get();
        RoleResult roleResult = new RoleResult(role);
        roleResult.getPermIds().forEach(s -> System.out.println(s));
    }

    @Test
    @Transactional
    public void jpaUpdateTest(){
        /**
         * 单元测试这里无法修改中间表，但是com.ihrm.system.controller.RoleController#assignRoles(java.util.Map)可以
         */
        //1.根据id查询用户
        User user = userDao.findById("1063705989926227968").get();
        String[] roleIds = new String[]{"1064098935443951616","1064099035536822272"};
        //2.设置用户的角色集合
        Set<Role> roles = roleDao.findAllById(Arrays.asList(roleIds)).stream().collect(Collectors.toSet());
        //设置用户和角色集合的关系
        user.setRoles(roles);
        //3.更新用户：如果userid是表中没有的，则会入库bs_user和pe_user_role两张表，如果userid已存在，则只入库pe_user_role表
        userDao.save(user);
        System.out.println("===============");
    }
}
