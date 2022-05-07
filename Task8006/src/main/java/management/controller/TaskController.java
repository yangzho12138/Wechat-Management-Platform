package management.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import entites.CommonResult;
import entites.Push_Module.TaskInfo;
import entites.Template_Module.TemplateInfo;
import management.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/mp/internet/wechat/task")
public class TaskController {
    @Autowired
    TaskService taskService;

    @RequestMapping("/getTaskList")
    public CommonResult getTaskList(@RequestBody String info){
        JSONObject jsonObject = JSONObject.parseObject(info);
        String creator = jsonObject.getString("creator");
        String startTime = jsonObject.getString("startTime");
        String endTime = jsonObject.getString("endTime");
        Integer page = Integer.parseInt(jsonObject.getString("page"));
        Integer pageSize = Integer.parseInt(jsonObject.getString("pageSize"));

        IPage<TaskInfo> res = taskService.taskList(creator,startTime,endTime,page,pageSize);
        if(res==null)
            return  new CommonResult(-1,"failed",null);

        long totalTask = res.getTotal();
        long totalPage = totalTask/pageSize;
        if(totalTask%pageSize!=0)
            totalPage = totalPage+1;
        List<TaskInfo> list = res.getRecords();
        HashMap<String,Object> data = new HashMap<>();
        data.put("count",totalTask);
        data.put("totalPage",totalPage);
        List<HashMap<String,Object>> taskList = new ArrayList<>();
        for(TaskInfo t:list){
            HashMap<String,Object> map = new HashMap<>();
            map.put("taskId",t.getTaskId());
            map.put("taskStatus",t.getTaskStatus());
            map.put("createTime",t.getCreateTime());
            map.put("creator",t.getCreator());
            map.put("finishTime",t.getFinishTime());
            taskList.add(map);
        }
        data.put("taskList",taskList);
        return new CommonResult(0,"success",data);
    }

    @RequestMapping("/updateStatus")
    // feign想要传参成功，调用的接口和被调用的controller的参数表一定要有@RequestBody
    public boolean updateStatus(@RequestBody TaskInfo taskInfo){
        return taskService.updateStatus(taskInfo);
    }
}
