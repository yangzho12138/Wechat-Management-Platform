package management.service.Impl;

import entites.Group_Module.GroupInfo;
import management.mapper.FansMapper;
import management.service.FansService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FansServiceImpl implements FansService {
    @Autowired
    FansMapper fansMapper;

    public List<String> queryFansInfo(Integer sex, String subscribeScene, String subscribeTimeEnd, String subscribeTimeStart, String bindStatus) {
        return fansMapper.queryFansInfo(sex,subscribeScene,subscribeTimeEnd,subscribeTimeStart,bindStatus);
    }

    @Transactional
    public boolean tagBindRule(GroupInfo groupInfo, List<String> openid,int size){
        int count1 = fansMapper.updateGroupInfo(groupInfo);
        int count2 = fansMapper.updateTagIdList(openid,Integer.toString(groupInfo.getTagId()));
        if(count1==0 || count2!=size)
            return false;
        return true;
    }
}
