package management.service;

import entites.Push_Module.TaskInfo;

import java.util.List;
import java.util.concurrent.Future;

// 同步方法想要调用异步方法必须单独写一个bean
public interface AsynService {
    Future<String> sendMsg(List<String> openidList, String templateId);
    void updateStatus(Future<String> future, TaskInfo taskInfo);
}
