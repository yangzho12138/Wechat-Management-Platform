package entites;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;


@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
    int subscribe = 0;
    String openid;
    String nickname;
    int sex;
    String city;
    String country;
    String province;
    String language;
    String headimgurl;
    Date subscribe_time;
    String unionid;
    String remark;
    Integer groupid;
    String tagid_list;
    String subscribe_scene;
}
