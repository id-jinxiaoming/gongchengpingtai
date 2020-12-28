package com.ff.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ff.common.base.BaseController;

import com.ff.shop.model.PerformanceDisplay;
import com.ff.shop.service.PerformanceDisplayService;
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
@RequestMapping("/performance/display")
public class PerformanceDisplayController extends BaseController {


    @Reference
    private PerformanceDisplayService performanceDisplayService;


    @RequiresPermissions(value = "performance:display:list")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(String key, ModelMap map) {

        key = StringEscapeUtils.unescapeHtml(key);
        try {
            key = new String(key.getBytes("iso-8859-1"), "utf-8");
        } catch (Exception e) {
            // TODO: handle exception
        }

        map.put("key", key);
        return new ModelAndView("performance/display/list", map);

    }

    @RequiresPermissions(value = "performance:display:list")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doList(String key, HttpServletRequest request) {
        Page<PerformanceDisplay> page = getPage(request);
        EntityWrapper<PerformanceDisplay> ew = new EntityWrapper<>();
        ew.like("title", "%" + key + "%");
        Page<PerformanceDisplay> data = performanceDisplayService.selectPage(page, ew);
        Map<String, Object> resultMap = new LinkedHashMap();

        resultMap.put("data", data);
        return resultMap;
    }

    @RequiresPermissions("performance:display:delete")
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(String[] id) {
        performanceDisplayService.deleteByPrimaryKey(id);
        Map<String, Object> resultMap = new LinkedHashMap();
        resultMap.put("msg", "删除成功");
        resultMap.put("status", 200);
        return resultMap;
    }

    @RequiresPermissions("performance:display:add")
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add() {

        return "/performance/display/add";
    }

    @RequiresPermissions("performance:display:add")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doAdd(PerformanceDisplay model) {
        Map<String, Object> resultMap = new LinkedHashMap();
        model.setDescribe(StringEscapeUtils.unescapeHtml(model.getDescribe()));
        model.setDetails(StringEscapeUtils.unescapeHtml(model.getDetails()));
        Integer id = performanceDisplayService.insert(model);
        if (id > 0) {
            resultMap.put("status", 200);
            resultMap.put("message", "操作成功");
        } else {
            resultMap.put("status", 500);
            resultMap.put("message", "操作失败");
        }
        return resultMap;
    }

    @RequiresPermissions("performance:display:edit")
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable("id") Integer id, ModelMap map) {
        map.put("performanceDisplay", performanceDisplayService.findByPrimaryKey(id.toString()));
        return new ModelAndView("/performance/display/edit", map);
    }

    @RequiresPermissions("performance:display:edit")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doEdit(PerformanceDisplay model, HttpServletRequest request) {
        Map<String, Object> resultMap = new LinkedHashMap();
        model.setDescribe(StringEscapeUtils.unescapeHtml(model.getDescribe()));
        model.setDetails(StringEscapeUtils.unescapeHtml(model.getDetails()));
        Integer id = performanceDisplayService.updateByPrimaryKey(model);
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
