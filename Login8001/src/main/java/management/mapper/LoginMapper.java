package management.mapper;

import entites.Login_Module.ChangePassword;
import entites.Login_Module.LoginInfo;
import entites.Login_Module.SignUpInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LoginMapper {
    // query with phone and password
    String login(String phone, String password);
    LoginInfo getLoginInfo(String phone);
    int updateLoginInfo(String phone,String nickname, String loginTime, String loginIp);

    // signUp the user info and bind the phone number
    int signUp(SignUpInfo signUpInfo);
    int bind(SignUpInfo signUpInfo);
    int check_phone(String phone);
    int check_nickname(String nickname);

    // change Password
    int changePassword(ChangePassword changePassword);
    int changePasswordHistory(String password, String changeTime, String phone);
    List<String> checkChangePassword(String phone);

}
