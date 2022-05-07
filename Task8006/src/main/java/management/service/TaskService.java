package management.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import entites.Push_Module.TaskInfo;

public interface TaskService {
    IPage<TaskInfo> taskList(String creator, String startTime, String endTime, Integer page, Integer pageSize);

    boolean updateStatus(TaskInfo taskInfo);
}
