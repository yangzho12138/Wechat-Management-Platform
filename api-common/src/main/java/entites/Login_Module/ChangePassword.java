package entites.Login_Module;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

// Change Password
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChangePassword {
    String phone;
    String password;
    String newPassword;
}
