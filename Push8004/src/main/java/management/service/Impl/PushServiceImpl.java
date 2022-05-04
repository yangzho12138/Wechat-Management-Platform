package management.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import entites.FansInfo;
import management.mapper.PushMapper;
import management.service.PushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PushServiceImpl implements PushService {
    @Autowired
    PushMapper pushMapper;

    public List<String> targetFans(String tagId){
        QueryWrapper<FansInfo> qw = new QueryWrapper<>();
        qw.like("tagid_list",tagId);
        List<FansInfo> fansInfos = pushMapper.selectList(qw);
        if(fansInfos==null || fansInfos.size()==0)
            return null;
        List<String> openid = new ArrayList<>();
        for(FansInfo f:fansInfos){
            openid.add(f.getOpenid());
        }
        return openid;
    }
}
