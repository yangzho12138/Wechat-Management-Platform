package management.service.Impl;

import management.service.AsynService;
import management.utils.SendMessageUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Future;

@Service
@Async //所有方法都为异步方法
public class AsynServiceImpl implements AsynService {
    public Future<String> sendMsg(List<String> openidList, String templateId){
        SendMessageUtil.sendMessageToWeChat(openidList,templateId);
        return new AsyncResult<>("sendMsg执行结束");
    }
}
