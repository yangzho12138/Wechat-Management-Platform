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
    int subscribe = 1;
    String openid;
    String nickname;
    Integer sex;
    String language = "zh_CN";
    String city;
    String province;
    String country;
    String headimgurl;
    String subscribe_time;
    String phone;
    String password;
}
