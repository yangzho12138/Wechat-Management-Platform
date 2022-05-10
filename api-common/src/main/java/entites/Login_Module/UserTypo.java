package entites.Login_Module;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

// the information needed when user try to login the system
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserTypo {
    private String phone;
    private String password;
    private String code;
    private String timestamp;
    private String picCode;
}
