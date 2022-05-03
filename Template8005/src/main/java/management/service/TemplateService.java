package management.service;

import entites.Template_Module.TemplateInfo;

import java.util.List;

public interface TemplateService {
    int templateNum();
    List<TemplateInfo> templateList(Integer pageSize, Integer records);

    boolean newTemplate(TemplateInfo templateInfo);
}
