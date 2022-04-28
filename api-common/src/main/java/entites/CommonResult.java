package entites;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResult<T> {
    public Integer code;
    public String message;
    public T data; // 范型——实体类

    // 请求失败时，只传2个参数
    public CommonResult(Integer code, String message){
        this.code=code;
        this.message=message;
        this.data=null;
    }
}
