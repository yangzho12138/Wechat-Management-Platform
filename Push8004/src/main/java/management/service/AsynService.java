package management.service;

import java.util.List;
import java.util.concurrent.Future;

// 同步方法想要调用异步方法必须单独写一个bean
public interface AsynService {
    Future<String> sendMsg(List<String> openidList, String templateId);
}
