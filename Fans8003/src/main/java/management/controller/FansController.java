package management.controller;

import com.alibaba.fastjson.JSONObject;
import entites.CommonResult;
import entites.Group_Module.GroupInfo;
import lombok.extern.slf4j.Slf4j;
import management.service.FansService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utils.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mp/internet/wechat")
public class FansController {
    @Autowired
    FansService fansService;

    @RequestMapping("/fans/option")
    public CommonResult option(){
        HashMap<String, Object> data = new HashMap<>();
        // sex setting
        List<HashMap<String,Object>> sex = new ArrayList<>();
        HashMap<String,Object> unknown = new HashMap<>();
        unknown.put("value","未知");
        unknown.put("code",0);
        HashMap<String,Object> man = new HashMap<>();
        man.put("value","男");
        man.put("code",1);
        HashMap<String,Object> woman = new HashMap<>();
        woman.put("value","女");
        woman.put("code",2);
        sex.add(unknown);
        sex.add(man);
        sex.add(woman);

        // bindStatus setting
        List<HashMap<String,Object>> bindStatus = new ArrayList<>();
        HashMap<String,Object> no = new HashMap<>();
        no.put("value","未绑定");
        no.put("code",0);
        HashMap<String,Object> yes = new HashMap<>();
        yes.put("value","绑定");
        yes.put("code",1);
        bindStatus.add(no);
        bindStatus.add(yes);

        // subscribeScene setting
        List<HashMap<String,Object>> subscribeScene = new ArrayList<>();
        HashMap<String,Object> s1 = new HashMap<>();
        s1.put("value","公众号搜索");
        s1.put("code","ADD_SCENE_SEARCH");
        HashMap<String,Object> s2 = new HashMap<>();
        s2.put("value","公众号迁移");
        s2.put("code","ADD_SCENE_ACCOUNT_MIGRATION");
        HashMap<String,Object> s3 = new HashMap<>();
        s3.put("value","名片分享");
        s3.put("code","ADD_SCENE_PROFILE_CARD");
        HashMap<String,Object> s4 = new HashMap<>();
        s4.put("value","扫描二维码");
        s4.put("code","ADD_SCENE_QR_CODE");
        HashMap<String,Object> s5 = new HashMap<>();
        s5.put("value","图文页内名称点击");
        s5.put("code","ADD_SCENE_PROFILE_LINK");
        HashMap<String,Object> s6 = new HashMap<>();
        s6.put("value","图文页右上角菜单");
        s6.put("code","ADD_SCENE_PROFILE_ITEM");
        HashMap<String,Object> s7 = new HashMap<>();
        s7.put("value","支付后关注");
        s7.put("code","ADD_SCENE_PAID");
        HashMap<String,Object> s8 = new HashMap<>();
        s8.put("value","其他");
        s8.put("code","AADD_SCENE_OTHERS");
        subscribeScene.add(s1);
        subscribeScene.add(s2);
        subscribeScene.add(s3);
        subscribeScene.add(s4);
        subscribeScene.add(s5);
        subscribeScene.add(s6);
        subscribeScene.add(s7);
        subscribeScene.add(s8);

        data.put("sex",sex);
        data.put("bindStatus",bindStatus);
        data.put("subscribeScene",subscribeScene);

        return new CommonResult(0,"success",data);
    }

    @RequestMapping("/fans/queryFansInfo")
    public CommonResult queryFansInfo(@RequestBody String restriction) {
        JSONObject jsonObject = JSONObject.parseObject(restriction);
        Integer sex = Integer.parseInt(jsonObject.getString("sex"));
        String subscribeScene = jsonObject.getString("subscribeScene");
        String subscribeTimeEnd = jsonObject.getString("subscribeTimeEnd");
        String subscribeTimeStart = jsonObject.getString("subscribeTimeStart");
        String bindStatus = jsonObject.getString("bindStatus");
        log.info(sex+" "+subscribeScene+" "+subscribeTimeEnd+" "+subscribeTimeStart+" "+bindStatus);
        List<String> users_openid = fansService.queryFansInfo(sex, subscribeScene, subscribeTimeEnd, subscribeTimeStart, bindStatus);
        log.info(users_openid.toString());
        HashMap<String, Object> data = new HashMap<>();
        data.put("fansCount", users_openid.size());
        return new CommonResult(0, "success", data);
    }

    @RequestMapping("/tagBindingRule")
    public CommonResult tagBindRule(@RequestBody String bindRule, HttpServletRequest request){
        JSONObject jsonObject = JSONObject.parseObject(bindRule);
        Integer tagId = Integer.parseInt(jsonObject.getString("tagId"));
        Integer fansCount = Integer.parseInt(jsonObject.getString("fansCount"));
        String rule = jsonObject.getString("rule"); // json字符串

        // 查询符合rule的用户
        JSONObject jsonObject1 = JSONObject.parseObject(rule);
        Integer sex = Integer.parseInt(jsonObject1.getString("sex"));
        String subscribeScene = jsonObject1.getString("subscribeScene");
        String subscribeTimeEnd = jsonObject1.getString("subscribeTimeEnd");
        String subscribeTimeStart = jsonObject1.getString("subscribeTimeStart");
        String bindStatus = jsonObject1.getString("bindStatus");
        List<String> users_openid = fansService.queryFansInfo(sex, subscribeScene, subscribeTimeEnd, subscribeTimeStart, bindStatus);
        log.info(users_openid.toString());

        // 将这些wechat_fans_info中的tagid_list字段添加新的tagId,gourpid更新为2(规则分组）并在wechat_group_info中添加新的规则分组内容
        // 获取日期和用户
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time_string = sdf.format(date);
        String token = request.getParameter("token");
        String createBy = JwtUtil.getUserName(token);

        GroupInfo groupInfo = new GroupInfo(time_string,tagId,createBy,2,null,fansCount,"规则分组"+tagId,rule);
        boolean res = fansService.tagBindRule(groupInfo,users_openid,users_openid.size());
        if(res==false)
            return new CommonResult(-1,"failed",null);
        return new CommonResult(0,"success",null);
    }
}
