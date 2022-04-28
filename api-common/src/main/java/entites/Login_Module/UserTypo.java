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
    String phone;
    String password;
    String code;
    String timestamp;
    String picCode;
}
