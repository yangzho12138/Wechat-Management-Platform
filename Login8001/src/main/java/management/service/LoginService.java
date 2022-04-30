package management.service;

import entites.Login_Module.ChangePassword;
import entites.Login_Module.LoginInfo;
import entites.Login_Module.SignUpInfo;

import java.util.List;

public interface LoginService {
    String login(String phone, String password);
    LoginInfo getLoginInfo(String phone);
    boolean updateLoginInfo(String phone,String nickname, String loginTime, String loginIp);

    boolean signUp(SignUpInfo signUpInfo);
    boolean bind(SignUpInfo signUpInfo);
    boolean check_phone(String phone);
    boolean check_nickname(String nickname);

    boolean changePassword(ChangePassword changePassword);
    boolean changePasswordHistory(String password, String changeTime, String phone);
    List<String> checkChangePassword(String phone);
}
