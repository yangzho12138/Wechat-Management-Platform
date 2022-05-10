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
public class FansInfo {
    private int subscribe = 0;
    private String openid;
    private String nickname;
    private int sex;
    private String city;
    private String country;
    private String province;
    private String language;
    private String headimgurl;
    private Date subscribe_time;
    private String remark;
    private Integer groupid;
    private String tagid_list;
    private String subscribe_scene;
}
