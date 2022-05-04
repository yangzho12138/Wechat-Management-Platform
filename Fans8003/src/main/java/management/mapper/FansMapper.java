package management.mapper;

import entites.Group_Module.GroupInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FansMapper {
    List<String> queryFansInfo(Integer sex, String subscribeScene, String subscribeTimeEnd, String subscribeTimeStart, String bindStatus);

    int updateGroupInfo(GroupInfo groupInfo);
    int updateTagIdList(List<String> openid, String tagId);
}
