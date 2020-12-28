package com.ff.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ff.common.base.BaseController;
import com.ff.shop.model.ImageText;
import com.ff.shop.model.JumpButton;
import com.ff.shop.model.JumpButtonTypes;
import com.ff.shop.model.PictureClassification;
import com.ff.shop.service.JumpButtonService;
import com.ff.shop.service.JumpButtonTypesService;
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
@RequestMapping(value = "/jump/button")
public class JumpButtonController extends BaseController {

    @Reference
    private JumpButtonService jumpButtonService;

    @Reference
    private JumpButtonTypesService jumpButtonTypesService;

    @RequiresPermissions(value = "jump:button:list")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(String key, ModelMap map) {

        key= StringEscapeUtils.unescapeHtml(key);
        try {
            key = new String(key.getBytes("iso-8859-1"), "utf-8");
        } catch (Exception e) {
            // TODO: handle exception
        }

        map.put("key", key);
        return new ModelAndView("jump/button/list",map);

    }


    @RequiresPermissions(value = "jump:button:list")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doList(String key, HttpServletRequest request) {
        Page<JumpButton> page = getPage(request);
        EntityWrapper<JumpButton> ew=new EntityWrapper();
        ew.orderBy("id",false);
        Page<JumpButton> data= jumpButtonService.selectPage(page,ew);
        Map<String, Object> resultMap = new LinkedHashMap();

        resultMap.put("data",data);
        return resultMap;
    }

    @RequiresPermissions("jump:button:delete")
    @RequestMapping(value ="/delete")
    @ResponseBody
    public Object delete(String[] id) {
        jumpButtonService.deleteByPrimaryKey(id);
        Map<String, Object> resultMap = new LinkedHashMap();
        resultMap.put("msg","删除成功");
        resultMap.put("status",200);
        return resultMap;
    }

    @RequiresPermissions("jump:button:add")
    @RequestMapping(value="/add",method=RequestMethod.GET)
    public ModelAndView add(ModelMap map){
        map.put("brand", jumpButtonTypesService.selectAll());
        return new ModelAndView("/jump/button/add",map);
    }

    @RequiresPermissions("jump:button:add")
    @RequestMapping(value="/add",method=RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> doAdd(JumpButton jumpButton,HttpServletRequest request) {
        Map<String, Object> resultMap = new LinkedHashMap();
        JumpButtonTypes jumpButtonTypes =jumpButtonTypesService.findByPrimaryKey(jumpButton.getJptId().toString());
        jumpButton.setStatus(jumpButtonTypes.getStatus());
        Integer id=jumpButtonService.insert(jumpButton);
        if(id>0){
            resultMap.put("status", 200);
            resultMap.put("message", "操作成功");
        }else{
            resultMap.put("status", 500);
            resultMap.put("message", "操作失败");
        }
        return resultMap;
    }

    @RequiresPermissions("jump:button:edit")
    @RequestMapping(value="/edit/{id}",method=RequestMethod.GET)
    public ModelAndView edit(@PathVariable("id")Integer id, ModelMap map){
        map.put("brand", jumpButtonTypesService.selectAll());
        map.put("jumpButton",jumpButtonService.findByPrimaryKey(id.toString()));
        return new  ModelAndView("/jump/button/edit",map);
    }

    @RequiresPermissions("jump:button:edit")
    @RequestMapping(value="/edit",method=RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> doEdit(JumpButton jumpButton,HttpServletRequest request) {
        Map<String, Object> resultMap = new LinkedHashMap();
        JumpButtonTypes jumpButtonTypes =jumpButtonTypesService.findByPrimaryKey(jumpButton.getJptId().toString());
        jumpButton.setStatus(jumpButtonTypes.getStatus());
        Integer id=jumpButtonService.updateByPrimaryKey(jumpButton);
        if(id>0){
            resultMap.put("status", 200);
            resultMap.put("message", "操作成功");
        }else{
            resultMap.put("status", 500);
            resultMap.put("message", "操作失败");
        }
        return resultMap;
    }
}
