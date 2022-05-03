package management.service.Impl;

import entites.Template_Module.TemplateInfo;
import management.mapper.TemplateMapper;
import management.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TemplateServiceImpl implements TemplateService {
    @Autowired
    TemplateMapper templateMapper;

    public int templateNum() {
        return 0;
    }

    public List<TemplateInfo> templateList(Integer pageSize, Integer records) {
        return null;
    }


    public boolean newTemplate(TemplateInfo templateInfo) {
        int res = templateMapper.insert(templateInfo);
        if(res==0)
            return false;
        return true;
    }
}

