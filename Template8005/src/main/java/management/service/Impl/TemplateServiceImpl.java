package management.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import entites.Template_Module.TemplateInfo;
import management.mapper.TemplateMapper;
import management.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TemplateServiceImpl implements TemplateService {
    @Autowired
    TemplateMapper templateMapper;

    // mybatis-plus分页查询
    public IPage<TemplateInfo> templateList(String templateType,String templateName,String templateId, String createBy, Integer page, Integer pageSize) {
        IPage<TemplateInfo> pagelimit = new Page<>(page,pageSize);
        QueryWrapper<TemplateInfo> qw = new QueryWrapper<>();
        // 实现动态查询
        if(templateType!=null && templateType.equals("")==false) // 这里不能像在mapper.xml中写成 !=""，必须用equals进行判断
            qw.eq("templateType",templateType);
        if(templateName!=null && templateName.equals("")==false)
            qw.eq("templateName",templateName);
        if(templateId!=null && templateId.equals("")==false)
            qw.eq("templateId",templateId);
        if(createBy!=null && createBy.equals("")==false)
            qw.eq("createBy",createBy);
        IPage<TemplateInfo> infoIPage = templateMapper.selectPage(pagelimit,qw);
        return infoIPage;
    }


    public boolean newTemplate(TemplateInfo templateInfo) {
        int res = templateMapper.insert(templateInfo);
        if(res==0)
            return false;
        return true;
    }
}

