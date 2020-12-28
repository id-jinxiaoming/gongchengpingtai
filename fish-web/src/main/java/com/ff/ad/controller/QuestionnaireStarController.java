package com.ff.ad.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ff.ad.model.Ad;
import com.ff.ad.model.QuestionnaireStar;
import com.ff.ad.service.QuestionnaireStarService;
import com.ff.common.base.BaseController;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/questionnaire/star")
public class QuestionnaireStarController extends BaseController {

    @Reference
    private QuestionnaireStarService questionnaireStarService;

    @RequiresPermissions(value = "questionnaire:star:list")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(String key, ModelMap map) {


        key= StringEscapeUtils.unescapeHtml(key);
        try {
            key = new String(key.getBytes("iso-8859-1"), "utf-8");
        } catch (Exception e) {
            // TODO: handle exception
        }

        map.put("key", key);
        return new ModelAndView("questionnaire/star/list",map);

    }


    @RequiresPermissions(value = "questionnaire:star:list")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doList(HttpServletRequest request, String key) {

        Page<QuestionnaireStar> page = getPage(request);
        EntityWrapper<QuestionnaireStar> ew=new EntityWrapper();
        ew.like("name","%"+key+"%");
        Page<QuestionnaireStar> data= questionnaireStarService.selectPage(page,ew);
        Map<String, Object> resultMap = new LinkedHashMap();
        resultMap.put("data",data);
        return resultMap;
    }

    @RequiresPermissions("questionnaire:star:edit")
    @RequestMapping(value="/edit/{id}",method=RequestMethod.GET)
    public ModelAndView edit(@PathVariable("id")String id, ModelMap map){
        QuestionnaireStar model = questionnaireStarService.findByPrimaryKey(id);
        map.put("questionnaireStar", model);
        return new ModelAndView("/questionnaire/star/edit",map);

    }
    @RequiresPermissions("questionnaire:star:edit")
    @RequestMapping(value="/edit",method=RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> doEdit(QuestionnaireStar model){
        Map<String, Object> resultMap = new LinkedHashMap();
        if(questionnaireStarService.updateByPrimaryKey(model)!=0){
            resultMap.put("status", 200);
            resultMap.put("message", "操作成功");
        }
        else
        {
            resultMap.put("status", 500);
            resultMap.put("message", "操作失败");
        }
        return resultMap;
    }
}
