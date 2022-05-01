package management.mapper;

import entites.Group_Module.GroupInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GroupingMapper {
    int ruleTagNum();
    int localTagNum();
    List<GroupInfo> tagList(Integer pageSize,Integer records);

    int addGroup(GroupInfo groupInfo);

    int deleteGroup(String tagId);

    int updateGroup(String description,String tagName, Integer tagId, Integer tagType);

    GroupInfo tagDetail(Integer tagId);
}
