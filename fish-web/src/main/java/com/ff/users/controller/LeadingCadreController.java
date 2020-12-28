package com.ff.users.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ff.common.base.BaseController;
import com.ff.shop.model.PerformanceDisplay;
import com.ff.users.model.LeadingCadre;
import com.ff.users.service.LeadingCadreService;
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
@RequestMapping("/leading/cadre")
public class LeadingCadreController extends BaseController {

    @Reference
    private LeadingCadreService leadingCadreService;

    @RequiresPermissions(value = "leading:cadre:list")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(String key, ModelMap map) {

        key = StringEscapeUtils.unescapeHtml(key);
        try {
            key = new String(key.getBytes("iso-8859-1"), "utf-8");
        } catch (Exception e) {
            // TODO: handle exception
        }

        map.put("key", key);
        return new ModelAndView("leading/cadre/list", map);

    }

    @RequiresPermissions(value = "leading:cadre:list")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doList(String key, HttpServletRequest request) {
        Page<LeadingCadre> page = getPage(request);
        EntityWrapper<LeadingCadre> ew = new EntityWrapper<>();
        ew.like("name", "%" + key + "%");
        Page<LeadingCadre> data = leadingCadreService.selectPage(page, ew);
        Map<String, Object> resultMap = new LinkedHashMap();

        resultMap.put("data", data);
        return resultMap;
    }

    @RequiresPermissions("leading:cadre:edit")
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable("id") Integer id, ModelMap map) {
        map.put("leadingCadre", leadingCadreService.findByPrimaryKey(id.toString()));
        return new ModelAndView("/leading/cadre/edit", map);
    }

    @RequiresPermissions("leading:cadre:edit")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doEdit(LeadingCadre model, HttpServletRequest request) {
        Map<String, Object> resultMap = new LinkedHashMap();
        Integer id = leadingCadreService.updateByPrimaryKey(model);
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
