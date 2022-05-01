package management.controller;

import com.alibaba.fastjson.JSONObject;
import entites.CommonResult;
import entites.Group_Module.GroupInfo;
import management.service.GroupingService;
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


@RestController
@RequestMapping("/mp/internet/wechat")
public class GroupingController {
    @Autowired
    GroupingService groupingService;

    @RequestMapping("/tag/queryTagInfoList")
    public CommonResult queryTagInfoList(@RequestBody String queryInfo){
        // 解析json
        JSONObject jsonObject = JSONObject.parseObject(queryInfo);
        String page = jsonObject.getString("page");
        String pageSize = jsonObject.getString("pageSize");
        if(page==null || pageSize==null)
            return new CommonResult(-1,"query failed due to parameter error",null);

        HashMap<String,Object> data = new HashMap<>();
        int ruleTag = groupingService.ruleTagNum();
        int localTag = groupingService.localTagNum();
        int totalPage = (ruleTag+localTag)/Integer.parseInt(pageSize) + 1;
        List<HashMap<String,Object>> tagList = new ArrayList<>();

        List<GroupInfo> list = groupingService.tagList(page,pageSize);
        if(list!=null && list.size()!=0){
            for(Object o:list){
                GroupInfo groupInfo = (GroupInfo) o;
                HashMap<String,Object> tags = new HashMap<>();
                tags.put("dateCreated",groupInfo.getDateCreated());
                tags.put("tagId",groupInfo.getTagId());
                tags.put("createBy",groupInfo.getCreateBy());
                tags.put("tagType",groupInfo.getTagType());
                tags.put("description",groupInfo.getDescription());
                tags.put("fansCount",groupInfo.getFansCount());
                tags.put("tagName",groupInfo.getTagName());
                tagList.add(tags);
            }
        }

        data.put("ruleTag",ruleTag);
        data.put("localTag",localTag);
        data.put("totalPage",totalPage);
        data.put("tagList",tagList);

        return new CommonResult(0,"query succeed",data);
    }

    @RequestMapping("/addTag")
    public CommonResult addTag(@RequestBody String newGroupInfo, HttpServletRequest request){
        JSONObject jsonObject = JSONObject.parseObject(newGroupInfo);
        String description = jsonObject.getString("description");
        String tagName = jsonObject.getString("tagName");
        Integer tagId = Integer.parseInt(jsonObject.getString("tagID"));
        Integer tagType = Integer.parseInt(jsonObject.getString("type"));
        // tagId,tagType,tagName必填，description选填
        if(tagName==null || tagId==null || tagType==null)
            return new CommonResult(-1,"add tag failed due to parameter error", null);

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time_string = sdf.format(date);
        // 从token中获取当前用户的nickname
        String token = request.getParameter("token");
        String createBy = JwtUtil.getUserName(token);

        GroupInfo groupInfo = new GroupInfo(time_string,tagId,createBy,tagType,description,0,tagName);
        boolean res = groupingService.addGroup(groupInfo);
        if(res==false)
            return new CommonResult(-1,"add tag failed", null);
        return new CommonResult(0,"add tag succeed",null);
    }

    @RequestMapping("/deleteTag")
    public CommonResult deleteTag(@RequestBody String tag){
        JSONObject jsonObject = JSONObject.parseObject(tag);
        String tagId = jsonObject.getString("tagId");

        if(tagId==null)
            return new CommonResult(-1,"delete tag failed due to parameter error", null);

        boolean res = groupingService.deleteGroup(tagId);
        if(res==false)
            return new CommonResult(-1,"delete tag failed", null);
        return new CommonResult(0,"delete tag succeed",null);
    }

    @RequestMapping("/updateTag")
    public CommonResult updateTag(@RequestBody String updateInfo){
        JSONObject jsonObject = JSONObject.parseObject(updateInfo);
        String description = jsonObject.getString("description");
        String tagName = jsonObject.getString("tagName");
        Integer tagId = Integer.parseInt(jsonObject.getString("tagId"));
        Integer tagType = Integer.parseInt(jsonObject.getString("type"));

        // 根据tagId,tagType 对description和tagName进行修改
        // 动态更新，不填就是不变，description和tagName必须填至少一个
        if(description==null && tagName==null || tagId==null || tagType==null)
            return new CommonResult(-1,"update tag failed due to parameter error", null);

        boolean res = groupingService.updateGroup(description,tagName,tagId,tagType);
        if(res==false)
            return new CommonResult(-1,"update tag failed", null);
        return new CommonResult(0,"update tag succeed",null);
    }

    @RequestMapping("/ruleTagDetail")
    public CommonResult ruleTagDetail(@RequestBody String tag){
        JSONObject jsonObject = JSONObject.parseObject(tag);
        String tagId = jsonObject.getString("tagId");

        if(tagId == null)
            return new CommonResult(-1,"rule tag detail failed due to parameter error", null);

        GroupInfo groupInfo = groupingService.tagDetail(tagId);
        if(groupInfo==null){
            return new CommonResult(-1,"rule tag detail failed",null);
        }
        HashMap<String,Object> data = new HashMap<>();
        data.put("description",groupInfo.getDescription());
        data.put("tagName",groupInfo.getTagName());
        data.put("tagId",groupInfo.getTagId());
        data.put("type",groupInfo.getTagType());
        data.put("fansCount",groupInfo.getFansCount());
        return new CommonResult(0,"rule tag detail succeed",data);
    }
}
