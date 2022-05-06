package management.service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PushService {
    List<String> targetFans(String tagId);
    List<String> newFans();

    boolean pushTasks(List<String> openidList, String templateId, HttpServletRequest request);
}
