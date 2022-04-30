package management.service.Impl;

import entites.Login_Module.ChangePassword;
import entites.Login_Module.LoginInfo;
import entites.Login_Module.SignUpInfo;
import management.mapper.LoginMapper;
import management.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public boolean changePasswordHistory(String password, String changeTime, String phone){
        int count = loginMapper.changePasswordHistory(password,changeTime,phone);
        if(count==0)
            return false;
        return true;
    }

    public List<String> checkChangePassword(String phone){
        return loginMapper.checkChangePassword(phone);
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
