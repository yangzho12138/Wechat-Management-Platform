package management.service;

import com.alibaba.fastjson.JSON;
import entites.Push_Module.TaskInfo;
import lombok.extern.slf4j.Slf4j;
import management.mapper.TaskMapper;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(topic = "wechat_task", consumerGroup = "wechat-push")
public class CreateTask implements RocketMQListener<String> {
    @Autowired
    TaskMapper taskMapper;

    public void onMessage(String s) {
        TaskInfo taskInfo = JSON.parseObject(s,TaskInfo.class);
        taskMapper.insert(taskInfo);
    }
}
