package management.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import entites.Template_Module.TemplateInfo;


public interface TemplateService {
    IPage<TemplateInfo> templateList(String templateType,String templateName,String templateId, Integer page, Integer pageSize);

    boolean newTemplate(TemplateInfo templateInfo);
}
