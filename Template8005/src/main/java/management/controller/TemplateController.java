package management.controller;

import entites.CommonResult;
import entites.Template_Module.TemplateInfo;
import management.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mp/internet/wechat/template")
public class TemplateController {
    @Autowired
    TemplateService templateService;

//    @RequestMapping("/list")
//    public CommonResult list(@RequestBody String listInfo){
//        JSONObject jsonObject = JSONObject.parseObject(listInfo);
//    }

    @RequestMapping("/add")
    public CommonResult add(@RequestBody TemplateInfo templateInfo){
        if(templateInfo==null)
            return new CommonResult(-1,"add template failed due to parameter error",null);
        boolean res = templateService.newTemplate(templateInfo);
        if(res==false)
            return new CommonResult(-1,"add template failed",null);
        return new CommonResult(-1,"add template succeed",null);
    }

}
