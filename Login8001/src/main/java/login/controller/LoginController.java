package login.controller;

import entites.CommonResult;
import entites.Login_Module.*;
import login.service.LoginService;
import login.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: wechat_management_platform
 * @description: login-module
 * @author: Yang Zhou
 * @create: 2022-04-23
 **/

@Slf4j
@RestController
@RequestMapping("/mp/internet/wechat")
public class LoginController {
    @Autowired
    RedisUtil redisUtil;

    @Autowired
    LoginService loginService;

    @GetMapping("/getPublicKey")
    public CommonResult getPublicKey() throws Exception {
        PublicKey publicKey = RSAUtil.getPublicKey();
        if(publicKey == null)
            return new CommonResult(-1,"failed",null);
        Map<String,PublicKey> map = new HashMap<>();
        map.put("publicKey",publicKey);
        CommonResult commonResult = new CommonResult(0,"success",map);
        return commonResult;
    }

//    @GetMapping("/getPicCode")
//    public CommonResult getPicCode() throws Exception {
//
//    }
//
//    @PostMapping("/verifyPicCode")
//    public CommonResult verifyPicCode(){
//
//    }
//
//    @GetMapping("/sendCode")
//    public CommonResult sendCode(){
//
//    }

    @PostMapping("/login")
    public CommonResult login(@RequestBody UserTypo userTypo, HttpServletRequest request) throws Exception {
        // 解析请求体，获取参数
        String phone = RSAUtil.decrypt(userTypo.getPhone(),RSAUtil.getPrivateKey());
        String password = SHA256Util.getSha256Str(RSAUtil.decrypt(userTypo.getPassword(),RSAUtil.getPrivateKey()));
        log.info("phone:"+phone+" password:"+password);
        if(phone == null || password==null)
            return new CommonResult(-1,"login failed",null);

        CommonResult commonResult = new CommonResult();
        String nickname = new String();
        LoginInfo loginInfo = new LoginInfo();
        // find record from Redis
        List<Object> legalUsers = redisUtil.listRange("legalUsers",0,-1);
        if(legalUsers!=null && legalUsers.size()!=0){
            log.info("进入Redis缓存中查找数据");
            for(Object o:legalUsers){
                loginInfo = (LoginInfo) o;
                System.out.println(loginInfo);
                if(loginInfo.getStatus()==1 && phone.equals(loginInfo.getPhone()) && password.equals(loginInfo.getPassword())){
                    log.info("在Redis缓存中找到数据");
                    nickname = loginInfo.getNickname();
                }
            }
        }else{
            log.info("未在Redis缓存中找到数据");
            nickname = loginService.login(phone,password);
            System.out.println(phone+" "+password);
            if(nickname==null){
                log.info("未在数据库中找到数据");
                commonResult.setCode(-1);
                commonResult.setMessage("login failed");
                commonResult.setData(null);
                return commonResult;
            }else{
                log.info("在数据库中找到数据");
                // 将数据库中查询到的数据插入Redis缓存中
                loginInfo = loginService.getLoginInfo(phone);
                loginInfo.setPassword(password);
                redisUtil.leftPush("legalUsers",loginInfo); // add query results to redis
            }
        }
        // 将登陆数据插入wechat_fans_login_info表中
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time_string = sdf.format(date);
        String ip = IpUtil.getIpAddress(request);
        loginService.updateLoginInfo(phone,nickname,time_string,ip);

        String token = JwtUtil.sign(loginInfo.getNickname(),"user");
        commonResult.setCode(0);
        commonResult.setMessage("login success");
        Map<String,String> map = new HashMap<>();
        map.put("uid",loginInfo.getOpenid());
        map.put("token",token);
        map.put("phone",loginInfo.getPhone());
        map.put("loginTime", loginInfo.getLoginTime());
        map.put("status",Integer.toString(loginInfo.getStatus()));
        map.put("loginIp",loginInfo.getLoginIp());
        map.put("userName",loginInfo.getNickname());
        commonResult.setData(map);
        return commonResult;
    }

    @RequestMapping("/signUp")
    public CommonResult signUp(@RequestBody SignUpInfo signUpInfo) throws Exception {
        // set up the time
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time_string = sdf.format(date);
        signUpInfo.setSubscribe_time(time_string);
        // set up the openId
        String openId = IdGenerator.get();
        signUpInfo.setOpenid(openId);
        System.out.println(signUpInfo);
        // decrypt the phone
        String phone = RSAUtil.decrypt(signUpInfo.getPhone(),RSAUtil.getPrivateKey());
        signUpInfo.setPhone(phone);
        // encrypt the password
        String password = SHA256Util.getSha256Str(RSAUtil.decrypt(signUpInfo.getPassword(),RSAUtil.getPrivateKey()));
        signUpInfo.setPassword(password);
        System.out.println(signUpInfo);

        CommonResult commonResult = new CommonResult();
        // check whether the phone number/nickname was used
        boolean check1 = loginService.check_phone(phone);
        if(check1 == false){
            commonResult.setCode(-1);
            commonResult.setMessage("the phone number has been used");
            return commonResult;
        }
        boolean check2 = loginService.check_nickname(signUpInfo.getNickname());
        if(check2 == false){
            commonResult.setCode(-1);
            commonResult.setMessage("the nickname has been used");
            return commonResult;
        }

        boolean res = loginService.signUp(signUpInfo)&loginService.bind(signUpInfo);

        HashMap<String,String> map = new HashMap<>();
        if(res == false){
            commonResult.setCode(-1);
            commonResult.setMessage("sign up/bind failed");
        }else{
            commonResult.setCode(1);
            commonResult.setMessage("sign up/bind success");
            // sign up success, return the openid to user
            map.put("openid",openId);
            commonResult.setData(map);

        }
        return commonResult;
    }

    @RequestMapping("/passwordChange")
    public CommonResult passwordChange(@RequestBody ChangePassword changePassword) throws Exception {
        System.out.println(RSAUtil.decrypt(changePassword.getPassword(),RSAUtil.getPrivateKey()));
        String password = SHA256Util.getSha256Str(RSAUtil.decrypt(changePassword.getPassword(),RSAUtil.getPrivateKey()));
        changePassword.setPassword(password);
        String newPassword = SHA256Util.getSha256Str(RSAUtil.decrypt(changePassword.getNewPassword(),RSAUtil.getPrivateKey()));
        changePassword.setPassword(newPassword);
        String phone = RSAUtil.decrypt(changePassword.getPhone(),RSAUtil.getPrivateKey());
        changePassword.setPhone(phone);
        System.out.println(changePassword);
        boolean res = loginService.changePassword(changePassword);
        CommonResult commonResult = new CommonResult();
        if(res==false){
            commonResult.setCode(-1);
            commonResult.setMessage("change password failed");
        }else{
            commonResult.setCode(0);
            commonResult.setMessage("change password succeed");
        }
        return commonResult;
    }

}
