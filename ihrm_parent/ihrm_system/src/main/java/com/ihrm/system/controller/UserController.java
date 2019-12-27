package com.ihrm.system.controller;

import com.ihrm.domain.system.User;
import com.ihrm.domain.system.response.ProfileResult;
import com.ihrm.domain.system.response.UserResult;
import com.ihrm.system.service.PermissionService;
import com.ihrm.system.service.UserService;
import com.zhouyuan.saas.ihrm.controller.BaseController;
import com.zhouyuan.saas.ihrm.entity.PageResult;
import com.zhouyuan.saas.ihrm.entity.Result;
import com.zhouyuan.saas.ihrm.entity.ResultCode;
import com.zhouyuan.saas.ihrm.utils.JwtUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

//1.解决跨域
@CrossOrigin
//2.声明restContoller
@RestController
//3.设置父路径
@RequestMapping(value="/sys")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private JwtUtils jwtUtils;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    /**
     * 分配角色
     * @param map
     * @return
     */
    @RequestMapping(value = "/user/assignRoles",method = RequestMethod.PUT)
    public Result assignRoles(@RequestBody Map<String,Object> map){
        //1.获取被分配的用户id
        String userId = map.get("id").toString();
        //2.获取到角色的id列表
        List<String> roleIds = (List<String>) map.get("roleIds");
        //3.调用service完成角色分配
        userService.assignRoles(userId,roleIds);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 保存
     */
    @RequestMapping(value="/user",method = RequestMethod.POST)
    public Result save(@RequestBody User user) {
        //1.设置保存的企业id

        user.setCompanyId(companyId);
        user.setCompanyName(companyName);
        user.setCreateTime(new Date());
        //2.调用service完成保存企业
        userService.add(user);
        //3.构造返回结果
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 查询企业的用户列表
     * 指定企业id
     */
    @RequestMapping(value="/user",method = RequestMethod.GET)
    public Result findAll(int size, int page, @RequestParam Map map) {
        map.put("companyId",companyId);
        Page users = userService.findAll(map,page,size);
        //构造返回结果
        PageResult pageResult = new PageResult(users.getTotalElements(),users.getContent());
        return new Result(ResultCode.SUCCESS,pageResult);
    }

    /**
     * 根据ID查询user
     */
    @RequestMapping(value="/user/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable(value="id") String id) {
        User user = userService.findById(id);
        //在回显用户时返回该用户已有的角色id组合（前台也要改）
        UserResult userResult = new UserResult(user);
        return new Result(ResultCode.SUCCESS,userResult);
    }

    /**
     * 修改User
     */
    @RequestMapping(value="/user/{id}",method = RequestMethod.PUT)
    public Result update(@PathVariable(value="id") String id, @RequestBody User user) {
        //1.设置修改的部门id
        user.setId(id);
        //2.调用service更新
        userService.update(user);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据id删除
     */
    @RequiresPermissions(value = "API-USER-DELETE")
    @RequestMapping(value="/user/{id}",method = RequestMethod.DELETE, name = "API-USER-DELETE")
    public Result delete(@PathVariable(value="id") String id) {
        userService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 用户登录获取token
     * @param map
     * @return
     */
/*    @RequestMapping(value = "login",method = RequestMethod.POST)
    public Result login(@RequestBody Map<String,String> map){
        String mobile = map.get("mobile");
        String password = map.get("password");
        User user = userService.findByMobile(mobile);
        if (null == user || !password.equals(user.getPassword())){
            return new Result(ResultCode.MOBILEORPASSWORDERROR);
        } else {
            //查询该用户拥有的所有api权限并写入到token中
            StringBuilder sb = new StringBuilder(16);
            user.getRoles().stream()
                    .forEach(role -> role.getPermissions().stream()
                            .filter(permission -> permission.getType().equals(PermissionConstants.PY_API))
                            .forEach(permission -> sb.append(permission.getCode()).append(",")));
            Map<String,Object> params = new HashMap<>(2);
            params.put("apis",sb.toString());

            params.put("companyId",user.getCompanyId());
            params.put("companyName",user.getCompanyName());
            //构造token
            String token = jwtUtils.createJwt(user.getId(), user.getUsername(), params);
            return new Result(ResultCode.SUCCESS,token);
        }
    }*/

    /**
     * 用户登录
     * @param map
     * @return
     */
    @RequestMapping(value = "login",method = RequestMethod.POST)
    public Result login(@RequestBody Map<String,String> map){
        String sessionId;
        try {
            String mobile = map.get("mobile");
            String password = map.get("password");
            //加密密码，构造器参数列表：密码，盐，加密次数
            password = new Md5Hash(password,mobile,3).toString();
            //1.构造登录令牌 UsernamePasswordToken
            UsernamePasswordToken upToken = new UsernamePasswordToken(mobile, password);
            //2.获取subject
            Subject subject = SecurityUtils.getSubject();
            //3.调用login方法，进入realm完成认证
            subject.login(upToken);
            //4.获取sessionId
            sessionId = subject.getSession().getId().toString();
        } catch (AuthenticationException e) {
            LOGGER.error("登录失败!{}",e);
            return new Result(ResultCode.UNAUTHENTICATED);
        }
        //5.构造返回结果
        return new Result(ResultCode.SUCCESS,sessionId);
    }

    /**
     * 登陆后紧接着获取用户信息（包含该用户所具有的权限信息）
     * 前后端约定：前端请求微服务时需要添加头信息Authorization ,内容为Bearer+空格+token
     * @return
     */
/*    @RequestMapping(value = "profile",method = RequestMethod.POST)
    public Result profile(){
*//*
        //从请求头中获取token
        String authorization = request.getHeader("Authorization");
        String token = authorization.replace("Bearer ","");
        //解析token获取claims
        Claims claims = jwtUtils.parseJwt(token);
*//*
        //从claims中获取userId
        String userId = claims.getId();
        User user = userService.findById(userId);

        //根据User构造ProfileResult
        ProfileResult result = null;
        if ("user".equals(user.getLevel())){
            result = new ProfileResult(user);
        }else {
            Map<String,Object> map = new HashMap<>(1);
            if ("coAdmin".equals(user.getLevel())){
                map.put("enVisible",1);
            }
            List<Permission> permissions = permissionService.findAll(map);
            result = new ProfileResult(user,permissions);
        }
        return new Result(ResultCode.SUCCESS, result);
    }*/

    @RequestMapping(value = "profile",method = RequestMethod.POST)
    public Result profile(){
        Subject subject = SecurityUtils.getSubject();
        PrincipalCollection principals = subject.getPrincipals();
        ProfileResult result = (ProfileResult) principals.getPrimaryPrincipal();
        return new Result(ResultCode.SUCCESS, result);
    }
}
