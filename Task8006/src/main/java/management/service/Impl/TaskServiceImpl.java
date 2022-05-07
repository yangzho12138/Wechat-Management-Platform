package management.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import entites.Push_Module.TaskInfo;
import management.mapper.TaskMapper;
import management.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    TaskMapper taskMapper;

    public IPage<TaskInfo> taskList(String creator, String startTime, String endTime, Integer page, Integer pageSize){
        IPage<TaskInfo> pagelimit = new Page<>(page,pageSize);
        QueryWrapper<TaskInfo> qw = new QueryWrapper<>();
        if(creator!=null &&  creator.equals("")==false)
            qw.eq("creator",creator);
        if(startTime!=null && startTime.equals("")==false && endTime!=null && endTime.equals("")==false)
            qw.between("createTime",startTime,endTime);
        IPage<TaskInfo> taskInfo = taskMapper.selectPage(pagelimit, qw);
        return taskInfo;
    }

    public boolean updateStatus(TaskInfo taskInfo){
        System.out.println("成功调用feign "+taskInfo.toString());
        String taskId = taskInfo.getTaskId();
        QueryWrapper<TaskInfo> qw = new QueryWrapper<>();
        if(taskId!=null && taskId.equals("")==false)
            qw.eq("taskId",taskId);
        int count = taskMapper.update(taskInfo,qw);
        if(count==0)
            return false;
        return true;
    }

}
