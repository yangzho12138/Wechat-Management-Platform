package management.service;

import entites.Group_Module.GroupInfo;
import entites.User;

import java.util.List;

public interface FansService {

    List<String> queryFansInfo(Integer sex, String subscribeScene, String subscribeTimeEnd, String subscribeTimeStart, Integer bindStatus);

    boolean tagBindRule(GroupInfo groupInfo, List<String> openid, int size);
}
