package management.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import entites.CommonResult;
import entites.Template_Module.TemplateInfo;
import management.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utils.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/mp/internet/wechat/template")
public class TemplateController {
    @Autowired
    TemplateService templateService;

    @RequestMapping("/list")
    public CommonResult list(@RequestBody String listInfo){
        JSONObject jsonObject = JSONObject.parseObject(listInfo);
        String templateType = jsonObject.getString("templateType");
        String templateName = jsonObject.getString("templateName");
        String templateId = jsonObject.getString("templateId");
        String createBy = jsonObject.getString("createBy");
        Integer page = Integer.parseInt(jsonObject.getString("page"));
        Integer pageSize = Integer.parseInt(jsonObject.getString("pageSize"));

        IPage<TemplateInfo> res = templateService.templateList(templateType,templateName,templateId,createBy,page,pageSize);
        if(res==null)
            return new CommonResult(-1,"failed",null);

        long totalTemplate = res.getTotal();
        long totalPage = totalTemplate/pageSize;
        if(totalTemplate%pageSize!=0)
            totalPage = totalPage+1;
        List<TemplateInfo> list = res.getRecords();
        HashMap<String,Object> data = new HashMap<>();
        data.put("totalTemplate",totalTemplate);
        data.put("totalPage",totalPage);
        List<HashMap<String,Object>> templateList = new ArrayList<>();
        for(TemplateInfo t:list){
            HashMap<String,Object> map = new HashMap<>();
            map.put("templateType",t.getTemplateType());
            map.put("templateName",t.getTemplateName());
            map.put("templateId",t.getTemplateId());
            map.put("createBy",t.getCreateBy());
            templateList.add(map);
        }
        data.put("templateList",templateList);
        return new CommonResult(0,"success",data);

    }

    @RequestMapping("/add")
    public CommonResult add(@RequestBody TemplateInfo templateInfo, HttpServletRequest request){
        if(templateInfo==null)
            return new CommonResult(-1,"add template failed due to parameter error",null);
        String token = request.getParameter("token");
        String createBy = JwtUtil.getUserName(token);
        templateInfo.setCreateBy(createBy);
        boolean res = templateService.newTemplate(templateInfo);
        if(res==false)
            return new CommonResult(-1,"add template failed",null);
        return new CommonResult(-1,"add template succeed",null);
    }

}
