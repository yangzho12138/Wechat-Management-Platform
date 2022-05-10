package entites.Login_Module;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

// The information needed when user sign up this system
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SignUpInfo {
    private int subscribe = 1;
    private String openid;
    private String nickname;
    private Integer sex;
    private String language = "zh_CN";
    private String city;
    private String province;
    private String country;
    private String headimgurl;
    private String subscribe_time;
    private String phone;
    private String password;
}
