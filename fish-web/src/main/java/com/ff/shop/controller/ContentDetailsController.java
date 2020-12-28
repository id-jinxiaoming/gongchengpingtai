package com.ff.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ff.common.base.BaseController;

import com.ff.common.util.DateUtils;
import com.ff.shop.model.ContentDetails;
import com.ff.shop.service.ContentDetailsService;
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
@RequestMapping("/content/details")
public class ContentDetailsController extends BaseController {


    @Reference
    private ContentDetailsService contentDetailsService;

    @RequiresPermissions(value = "content:details:list")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(String key, ModelMap map) {

        key = StringEscapeUtils.unescapeHtml(key);
        try {
            key = new String(key.getBytes("iso-8859-1"), "utf-8");
        } catch (Exception e) {
            // TODO: handle exception
        }

        map.put("key", key);
        return new ModelAndView("content/details/list", map);

    }

    @RequiresPermissions(value = "content:details:list")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doList(String key, HttpServletRequest request) {
        Page<ContentDetails> page = getPage(request);
        EntityWrapper<ContentDetails> ew = new EntityWrapper<>();
        ew.like("title", "%" + key + "%");
        Page<ContentDetails> data = contentDetailsService.selectPage(page, ew);
        Map<String, Object> resultMap = new LinkedHashMap();

        resultMap.put("data", data);
        return resultMap;
    }

    @RequiresPermissions("content:details:delete")
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(String[] id) {
        contentDetailsService.deleteByPrimaryKey(id);
        Map<String, Object> resultMap = new LinkedHashMap();
        resultMap.put("msg", "删除成功");
        resultMap.put("status", 200);
        return resultMap;
    }

    @RequiresPermissions("content:details:add")
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add() {

        return "/content/details/add";
    }

    @RequiresPermissions("content:details:add")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doAdd(ContentDetails model) {
        Map<String, Object> resultMap = new LinkedHashMap();
        model.setDetails(StringEscapeUtils.unescapeHtml(model.getDetails()));
        model.setTitle(StringEscapeUtils.unescapeHtml(model.getTitle()));
        model.setAddTime(DateUtils.getDate());
        Integer id = contentDetailsService.insert(model);
        if (id > 0) {
            resultMap.put("status", 200);
            resultMap.put("message", "操作成功");
        } else {
            resultMap.put("status", 500);
            resultMap.put("message", "操作失败");
        }
        return resultMap;
    }

    @RequiresPermissions("content:details:edit")
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable("id") Integer id, ModelMap map) {
        ContentDetails contentDetails = contentDetailsService.findByPrimaryKey(id.toString());
        map.put("contentDetails",contentDetails);
        map.put("titles",StringEscapeUtils.escapeHtml(contentDetails.getTitle()));
        return new ModelAndView("/content/details/edit", map);
    }

    @RequiresPermissions("content:details:edit")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doEdit(ContentDetails model, HttpServletRequest request) {
        Map<String, Object> resultMap = new LinkedHashMap();
        model.setDetails(StringEscapeUtils.unescapeHtml(model.getDetails()));
        model.setTitle(StringEscapeUtils.unescapeHtml(model.getTitle()));
        Integer id = contentDetailsService.updateByPrimaryKey(model);
        if (id > 0) {
            resultMap.put("status", 200);
            resultMap.put("message", "操作成功");
        } else {
            resultMap.put("status", 500);
            resultMap.put("message", "操作失败");
        }
        return resultMap;
    }

}