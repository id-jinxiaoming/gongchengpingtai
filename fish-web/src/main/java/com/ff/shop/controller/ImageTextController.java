package com.ff.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ff.common.base.BaseController;
import com.ff.common.model.JsonTreeData;
import com.ff.shop.model.Goods;
import com.ff.shop.model.GoodsCate;
import com.ff.shop.model.ImageText;
import com.ff.shop.model.PictureClassification;
import com.ff.shop.service.ImageTextService;
import com.ff.shop.service.PictureClassificationService;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/image/text")
public class ImageTextController extends BaseController {

    @Reference
    private PictureClassificationService pictureClassificationService;

    @Reference
    private ImageTextService imageTextService;

    @RequiresPermissions(value = "image:text:list")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(String key, ModelMap map) {

        key= StringEscapeUtils.unescapeHtml(key);
        try {
            key = new String(key.getBytes("iso-8859-1"), "utf-8");
        } catch (Exception e) {
            // TODO: handle exception
        }

        map.put("key", key);
        return new ModelAndView("image/text/list",map);

    }


    @RequiresPermissions(value = "image:text:list")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doList(String key, HttpServletRequest request) {
        Page<ImageText> page = getPage(request);
        EntityWrapper<ImageText> ew=new EntityWrapper();
        ew.orderBy("id",false);
        Page<ImageText> data= imageTextService.selectPage(page,ew);
        Map<String, Object> resultMap = new LinkedHashMap();

        resultMap.put("data",data);
        return resultMap;
    }

    @RequiresPermissions("image:text:delete")
    @RequestMapping(value ="/delete")
    @ResponseBody
    public Object delete(String[] id) {
        imageTextService.deleteByPrimaryKey(id);
        Map<String, Object> resultMap = new LinkedHashMap();
        resultMap.put("msg","删除成功");
        resultMap.put("status",200);
        return resultMap;
    }

    @RequiresPermissions("image:text:add")
    @RequestMapping(value="/add",method=RequestMethod.GET)
    public ModelAndView add(ModelMap map){
        map.put("brand", pictureClassificationService.selectAll());
        return new ModelAndView("/image/text/add",map);
    }

    @RequiresPermissions("image:text:add")
    @RequestMapping(value="/add",method=RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> doAdd(ImageText model,HttpServletRequest request) {
        Map<String, Object> resultMap = new LinkedHashMap();
        PictureClassification pictureClassification=pictureClassificationService.findByPrimaryKey(model.getPcId().toString());
        model.setStatus(pictureClassification.getStatus());
        Integer id=imageTextService.insert(model);
        if(id>0){
            resultMap.put("status", 200);
            resultMap.put("message", "操作成功");
        }else{
            resultMap.put("status", 500);
            resultMap.put("message", "操作失败");
        }
        return resultMap;
    }

    @RequiresPermissions("image:text:edit")
    @RequestMapping(value="/edit/{id}",method=RequestMethod.GET)
    public ModelAndView edit(@PathVariable("id")Integer id, ModelMap map){
        map.put("brand", pictureClassificationService.selectAll());
        map.put("imageText",imageTextService.findByPrimaryKey(id.toString()));
        return new  ModelAndView("/image/text/edit",map);
    }

    @RequiresPermissions("image:text:edit")
    @RequestMapping(value="/edit",method=RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> doEdit(ImageText imageText,HttpServletRequest request) {
        Map<String, Object> resultMap = new LinkedHashMap();
        PictureClassification pictureClassification=pictureClassificationService.findByPrimaryKey(imageText.getPcId().toString());
        imageText.setStatus(pictureClassification.getStatus());
        Integer id=imageTextService.updateByPrimaryKey(imageText);
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
