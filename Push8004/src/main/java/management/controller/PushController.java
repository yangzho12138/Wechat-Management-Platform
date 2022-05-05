package management.controller;

import com.alibaba.fastjson.JSONObject;
import entites.CommonResult;
import management.service.PushService;
import management.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utils.SendMessageUtil;

import java.util.List;

@RestController
@RequestMapping("/mp/internet/wechat")
public class PushController {
    @Autowired
    RedisUtil redisUtil;

    @Autowired
    PushService pushService;

    @RequestMapping("/template/sendTemplateMsg")
    public CommonResult sendTemplateMsg(@RequestBody String info){
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
        // 模拟调用接口发送信息（模拟生产者）


        // 利用feign调用Task8006的接口更新任务列表

    }

    // 定时推送
}
