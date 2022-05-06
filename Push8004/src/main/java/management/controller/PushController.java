package management.controller;

import com.alibaba.fastjson.JSONObject;
import entites.CommonResult;
import entites.Push_Module.TaskInfo;
import management.service.PushService;
import management.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utils.JwtUtil;
import management.utils.SendMessageUtil;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/mp/internet/wechat")
public class PushController {
    @Autowired
    RedisUtil redisUtil;

    @Autowired
    PushService pushService;

    @RequestMapping("/template/sendTemplateMsg")
    public CommonResult sendTemplateMsg(@RequestBody String info, HttpServletRequest request){
        // 分布式锁——每5min才能调用一次接口
        boolean lock = redisUtil.lock("sendTemplateMsg","1",1000*60*5);
        if(lock==false)
            return new CommonResult(-1,"the api is called in the last 5 min",null);
        JSONObject jsonObject = JSONObject.parseObject("info");
        String tagId = jsonObject.getString("tagId");
        String templateId = jsonObject.getString("templateId");
        if(tagId==null || templateId==null)
            return new CommonResult(-1,"send message failed due to parameter error",null);
        // 根据tagId去查需要发送信息的用户
        List<String> openid =pushService.targetFans(tagId);
        // 利用消息队列给Task8006推送消息
        boolean res = pushService.pushTasks(openid,templateId,request);
        if(res==false)
            return new CommonResult(-1,"failed",null);
        return new CommonResult(0,"succeed",null);
    }
}
