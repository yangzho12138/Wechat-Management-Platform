package management.service;

import entites.Group_Module.GroupInfo;

import java.util.List;

public interface GroupingService {
    // 分组信息查询
    int ruleTagNum();
    int localTagNum();
    List<GroupInfo> tagList(Integer pageSize, Integer records);

    boolean addGroup(GroupInfo groupInfo);

    boolean deleteGroup(String tagId);

    boolean updateGroup(String description,String tagName, Integer tagId, Integer tagType);

    GroupInfo tagDetail(Integer tagId);
}
