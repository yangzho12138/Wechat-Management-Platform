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
    private String openid;
    private String phone;
    private String loginTime;
    private int status;
    private String loginIp;
    private String nickname;
    private String password;
}
