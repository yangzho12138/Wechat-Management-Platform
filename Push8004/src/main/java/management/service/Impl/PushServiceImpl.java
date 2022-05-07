package management.service.Impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import entites.FansInfo;
import entites.Push_Module.TaskInfo;
import lombok.extern.slf4j.Slf4j;
import management.mapper.PushMapper;
import management.service.AsynService;
import management.service.PushService;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import utils.IdGenerator;
import utils.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

@Service
@Slf4j
public class PushServiceImpl implements PushService {
    @Autowired
    PushMapper pushMapper;

    @Autowired
    AsynService asynService;

    @Autowired
    RocketMQTemplate rocketMQTemplate;

    public List<String> targetFans(String tagId){
        QueryWrapper<FansInfo> qw = new QueryWrapper<>();
        qw.like("tagid_list",tagId);
        List<FansInfo> fansInfos = pushMapper.selectList(qw);
        if(fansInfos==null || fansInfos.size()==0)
            return null;
        List<String> openid = new ArrayList<>();
        for(FansInfo f:fansInfos){
            openid.add(f.getOpenid());
        }
        return openid;
    }

    public List<String> newFans(){
        QueryWrapper<FansInfo> qw = new QueryWrapper<>();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time_string_end = sdf.format(date);
        // 获取48h前的时间
        Long time = System.currentTimeMillis();
        time -= 1000*60*60*48;
        String time_string_begin = sdf.format(time);
        log.info(time_string_begin+" "+time_string_end);
        qw.between("subscribe_time",time_string_begin,time_string_end);
        List<FansInfo> fansInfos = pushMapper.selectList(qw);
        if(fansInfos==null || fansInfos.size()==0)
            return null;
        List<String> openid = new ArrayList<>();
        for(FansInfo f:fansInfos){
            openid.add(f.getOpenid());
        }
        return openid;
    }


    public boolean pushTasks(List<String> openidList, String templateId, HttpServletRequest request){
        Date date_begin = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time_string_begin = sdf.format(date_begin);
        String creator = "系统";
        if(request!=null){
            String token = request.getParameter("token");
            creator = JwtUtil.getUserName(token);
        }
        Future<String> future = asynService.sendMsg(openidList, templateId);
        log.info("开始执行异步任务");
        String time_string_end = null;
        String taskStatus = "未完成";
        if(future.isDone()==true){
            Date date_end = new Date();
            time_string_end = sdf.format(date_end);
            taskStatus = "已完成";
        }
        TaskInfo taskInfo = new TaskInfo(IdGenerator.get(),creator,taskStatus,time_string_begin,time_string_end);
        Message msg = MessageBuilder.withPayload(JSON.toJSONString(taskInfo)).build();
        SendResult sendResult = rocketMQTemplate.syncSend("wechat_task",msg,5000);
        // 信息发送后，一直检测未完成的任务是否完成，若完成则发消息更新任务状态
        

        log.info("sendResult"+sendResult);
        if(sendResult!=null)
            return true;
        return false;
    }
}
