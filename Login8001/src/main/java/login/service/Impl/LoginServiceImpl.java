package login.service.Impl;

import entites.Login_Module.ChangePassword;
import entites.Login_Module.LoginInfo;
import entites.Login_Module.SignUpInfo;
import login.mapper.LoginMapper;
import login.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    LoginMapper loginMapper;

    @Override
    public String login(String phone, String password) {
        String nickname = loginMapper.login(phone,password);
        return nickname;
    }

    public LoginInfo getLoginInfo(String phone){
        LoginInfo loginInfo = loginMapper.getLoginInfo(phone);
        return loginInfo;
    }

    public boolean updateLoginInfo(String phone,String nickname, String loginTime, String loginIp){
        int count = loginMapper.updateLoginInfo(phone,nickname,loginTime,loginIp);
        if(count==0)
            return false;
        return true;
    }

    public boolean signUp(SignUpInfo signUpInfo){
        int count = loginMapper.signUp(signUpInfo);
        if(count==0)
            return false;
        return true;
    }

    public boolean bind(SignUpInfo signUpInfo){
        int count = loginMapper.bind(signUpInfo);
        if(count==0)
            return false;
        return true;
    }

    public boolean changePassword(ChangePassword changePassword){
        int count = loginMapper.changePassword(changePassword);
        if(count==0)
            return false;
        return true;
    }

    public boolean check_phone(String phone){
        int count = loginMapper.check_phone(phone);
        if(count==0)
            return true;
        return false;
    }

    public boolean check_nickname(String nickname){
        int count = loginMapper.check_nickname(nickname);
        if(count==0)
            return true;
        return false;
    }
}
