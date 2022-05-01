package management.mapper;

import entites.Group_Module.GroupInfo;

import java.util.List;

public interface GroupingMapper {
    int ruleTagNum();
    int localTagNum();
    List<GroupInfo> tagList(String page,String pageSize);

    int addGroup(GroupInfo groupInfo);

    int deleteGroup(String tagId);

    int updateGroup(String description,String tagName, Integer tagId, Integer tagType);

    GroupInfo tagDetail(String tagId);
}
