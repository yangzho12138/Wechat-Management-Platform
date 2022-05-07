package management.service;

import entites.Push_Module.TaskInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "nacos-task")
public interface PushUpdateService {
    // feign传参一定要有@RequestBody，参数才能成功传到被调用的模块中
    @RequestMapping("/mp/internet/wechat/task/updateStatus")
    public boolean updateStatus(@RequestBody TaskInfo taskInfo);

}
