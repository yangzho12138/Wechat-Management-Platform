package management.task;

import lombok.extern.slf4j.Slf4j;
import management.service.PushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@Async
public class ScheduledTasks {
    @Autowired
    PushService pushService;

    // 定时推送
    // pushTasks只是将任务信息推送给Task8006模块，将数据加入数据库是在Task8006模块中的任务
    @Scheduled(fixedRate = 1000*60*30) // 每半小时发送一次
    @Retryable
    public void sendAtTime(){
        log.info("定时任务开始执行");
        String templateId = "WX_PH_WZ_20200819005050299";
        List<String> openid = pushService.newFans();
        boolean res = pushService.pushTasks(openid,templateId,null);
    }
}
