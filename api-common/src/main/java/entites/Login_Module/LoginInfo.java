package entites.Login_Module;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

// find whether a user is valid
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LoginInfo implements Serializable {
    String openid;
    String phone;
    String loginTime;
    int status;
    String loginIp;
    String nickname;
    String password;
}
