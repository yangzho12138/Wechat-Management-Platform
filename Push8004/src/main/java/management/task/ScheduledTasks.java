package management.task;

import entites.CommonResult;
import lombok.extern.slf4j.Slf4j;
import management.service.PushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ScheduledTasks {
    @Autowired
    PushService pushService;
    // 定时推送
    @Scheduled(fixedRate = 1000*60*30) // 每半小时发送一次
    public CommonResult sendAtTime(){
        log.info("定时任务开始执行");
        String templateId = "WX_PH_WZ_20200819005050299";
        List<String> openid = pushService.newFans();
        boolean res = pushService.pushTasks(openid,templateId,null);
        if(res==false)
            return new CommonResult(-1,"send at time failed",null);
        return new CommonResult(0,"send at time succeed",null);
    }
}
