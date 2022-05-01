package management.service.Impl;

import entites.Group_Module.GroupInfo;
import management.mapper.GroupingMapper;
import management.service.GroupingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupingServiceImpl implements GroupingService {
    @Autowired
    GroupingMapper groupingMapper;

    public int ruleTagNum(){
        return groupingMapper.ruleTagNum();
    }
    public int localTagNum(){
        return groupingMapper.localTagNum();
    }
    public List<GroupInfo> tagList(Integer pageSize,Integer records){
        return groupingMapper.tagList(pageSize,records);
    }

    public boolean addGroup(GroupInfo groupInfo){
        int count = groupingMapper.addGroup(groupInfo);
        if(count==0)
            return false;
        return true;
    }

    public boolean deleteGroup(String tagId){
        int count = groupingMapper.deleteGroup(tagId);
        if(count==0)
            return false;
        return true;
    }

    public boolean updateGroup(String description,String tagName, Integer tagId, Integer tagType){
        int count = groupingMapper.updateGroup(description,tagName,tagId,tagType);
        if(count==0)
            return false;
        return true;
    }

    public GroupInfo tagDetail(Integer tagId){
        return groupingMapper.tagDetail(tagId);
    }
}
