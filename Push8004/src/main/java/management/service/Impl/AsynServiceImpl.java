package management.service.Impl;

import entites.Push_Module.TaskInfo;
import lombok.extern.slf4j.Slf4j;
import management.service.AsynService;
import management.service.PushUpdateService;
import management.utils.SendMessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

@Service
@Async //所有方法都为异步方法
@Slf4j

public class AsynServiceImpl implements AsynService {
    @Autowired
    PushUpdateService pushUpdateService;

    public Future<String> sendMsg(List<String> openidList, String templateId){
        SendMessageUtil.sendMessageToWeChat(openidList,templateId);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new AsyncResult<>("sendMsg执行结束");
    }

    public void updateStatus(Future<String> future, TaskInfo taskInfo) {
        while(future.isDone()==false){
            log.info("任务还未完成");
        }
        log.info("任务已完成");
        Date date_end = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time_string_end = sdf.format(date_end);
        taskInfo.setFinishTime(time_string_end);
        taskInfo.setTaskStatus("已完成");
        // 一个jvm程序只能有一个生产者/消费者实例
        pushUpdateService.updateStatus(taskInfo);
    }
}
