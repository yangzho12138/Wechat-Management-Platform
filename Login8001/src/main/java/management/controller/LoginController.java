package management.controller;

import com.alibaba.fastjson.JSONObject;
import entites.CommonResult;
import entites.Login_Module.*;
import management.service.LoginService;
import management.utils.*;
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
import java.util.concurrent.TimeUnit;

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

    @RequestMapping("/getPicCode")
    public CommonResult getPicCode() throws Exception {
        CommonResult commonResult = new CommonResult();
        // 调用接口获取图形验证码（省略）
        String code = "a049"; // 验证码
        commonResult.setCode(0);
        commonResult.setMessage("success get piccode");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time_string = sdf.format(date);
        HashMap<String, String> map = new HashMap<>();
        map.put("img","imgurl");
        map.put("timestamp",time_string);
        // 将验证码和时间戳放入redis缓存中
        System.out.println(time_string);
        redisUtil.setForTimeMIN(code,time_string,15);
        return commonResult;
    }

    @RequestMapping("/verifyPicCode")
    public CommonResult verifyPicCode(@RequestBody String picCode){
        // 去redis中查找
        JSONObject jsonObject = JSONObject.parseObject(picCode);
        String code = (String)jsonObject.get("picCode");
        String timestamp = (String)jsonObject.get("timestamp");
        System.out.println(code+" "+timestamp);
        String redis_time = redisUtil.get(code);
        if(redis_time!=null && redis_time.equals(timestamp)==true){
            redisUtil.expire(code,0,TimeUnit.SECONDS); // 验证成功立即失效
            return new CommonResult(0,"verify success",null);
        }
        return new CommonResult(-1,"verify failed",null);
    }

    @PostMapping("/sendCode")
    public CommonResult sendCode(@RequestBody String phone) throws Exception {
        String code = codeGenerator.getlinkNo();
        // 字符串转json
        JSONObject jsonObject = JSONObject.parseObject(phone);
        phone = (String) jsonObject.get("phone");
        phone = RSAUtil.decrypt(phone,RSAUtil.getPrivateKey());
        log.info(phone);
        HashMap<String,String> map = new HashMap<>();
        map.put("code",code);
        redisUtil.set(phone,code);
        redisUtil.expire(phone,120,TimeUnit.SECONDS);
        return new CommonResult(0,"send code success",map);
    }

    @PostMapping("/login")
    public CommonResult login(@RequestBody UserTypo userTypo, HttpServletRequest request) throws Exception {
        // 解析请求体，获取参数
        String phone = RSAUtil.decrypt(userTypo.getPhone(),RSAUtil.getPrivateKey());
        String password = SHA256Util.getSha256Str(RSAUtil.decrypt(userTypo.getPassword(),RSAUtil.getPrivateKey()));
        log.info("phone:"+phone+" password:"+password);
        if(phone == null || password==null)
            return new CommonResult(-1,"login failed",null);
        // 对手机验证码进行校验
        String code = redisUtil.get(phone);
        if(code==null || code.equals(userTypo.getCode())==false){
            log.info(code+" "+userTypo.getCode());
            return new CommonResult(-1,"login failed",null);
        }
        redisUtil.expire(phone,0,TimeUnit.SECONDS); // 验证成功立刻失效

        CommonResult commonResult = new CommonResult();

        String nickname = loginService.login(phone,password);
        System.out.println(phone+" "+password);
        if(nickname==null){
            log.info("未在数据库中找到数据");
            commonResult.setCode(-1);
            commonResult.setMessage("login failed");
            commonResult.setData(null);
            return commonResult;
        }
        log.info("在数据库中找到数据");
        // 将登陆数据插入wechat_fans_login_info表中
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time_string = sdf.format(date);
        String ip = IpUtil.getIpAddress(request);
        loginService.updateLoginInfo(phone,nickname,time_string,ip);
        // 获取返回数据
        commonResult.setCode(0);
        commonResult.setMessage("login success");
        LoginInfo loginInfo = loginService.getLoginInfo(phone);
        String token = JwtUtil.sign(loginInfo.getNickname(),phone);
        // 将token放入redis

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
            // 注册成功后，将数据插入历史密码数据库中
            loginService.changePasswordHistory(password,time_string,phone);
            // sign up success, return the openid to user
            map.put("openid",openId);
            commonResult.setData(map);

        }
        return commonResult;
    }

    // 密码的正则匹配模式
    public static final String PW_Pattern = "";
    @RequestMapping("/passwordChange")
    public CommonResult passwordChange(@RequestBody ChangePassword changePassword) throws Exception {
        System.out.println(RSAUtil.decrypt(changePassword.getPassword(),RSAUtil.getPrivateKey()));
        String password = SHA256Util.getSha256Str(RSAUtil.decrypt(changePassword.getPassword(),RSAUtil.getPrivateKey()));
        changePassword.setPassword(password);
        // 对新设置密码的合理性进行检验
        String newPassword = RSAUtil.decrypt(changePassword.getNewPassword(),RSAUtil.getPrivateKey());
        // 密码格式验证
        if(newPassword.matches(PW_Pattern)==false)
            return new CommonResult(-1,"password is illegal");
        // 新设置的密码不能与最近5次的设置相同
        newPassword = SHA256Util.getSha256Str(newPassword);
        String phone = RSAUtil.decrypt(changePassword.getPhone(),RSAUtil.getPrivateKey());
        List<String> histroyPassword = loginService.checkChangePassword(phone);
        for(Object o:histroyPassword){
            String hp = (String) o;
            if(newPassword.equals(hp)==true)
                return new CommonResult(-1,"password used in last five times");
        }
        changePassword.setPassword(newPassword);
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
            // 修改成功后向数据库中插入一次历史修改记录
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time_string = sdf.format(date);
            loginService.changePasswordHistory(newPassword,time_string,phone);
        }
        return commonResult;
    }

//    @RequestMapping("/changeBindPhone")
//    public CommonResult changeBindPhone(){
//
//    }
//
//    @RequestMapping("/logoff")
//    public CommonResult logoff(){
//
//    }

}
