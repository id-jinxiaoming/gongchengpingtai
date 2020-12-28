package com.ff.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ff.common.base.BaseController;
import com.ff.common.util.DateUtils;
import com.ff.shop.model.SupplierRepresentative;
import com.ff.shop.service.SupplierRepresentativeService;
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

@RequestMapping("/supplier/representative")
@Controller
public class SupplierRepresentativeController extends BaseController {

    @Reference
    private SupplierRepresentativeService supplierRepresentativeService;

    @RequiresPermissions(value = "supplier:representative:list")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(String key, ModelMap map) {

        key = StringEscapeUtils.unescapeHtml(key);
        try {
            key = new String(key.getBytes("iso-8859-1"), "utf-8");
        } catch (Exception e) {
            // TODO: handle exception
        }

        map.put("key", key);
        return new ModelAndView("supplier/representative/list", map);

    }

    @RequiresPermissions(value = "supplier:representative:list")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doList(String key, HttpServletRequest request) {
        Page<SupplierRepresentative> page = getPage(request);
        EntityWrapper<SupplierRepresentative> ew = new EntityWrapper<>();
        ew.like("company_name", "%" + key + "%");
        Page<SupplierRepresentative> data = supplierRepresentativeService.selectPage(page, ew);
        Map<String, Object> resultMap = new LinkedHashMap<>();

        resultMap.put("data", data);
        return resultMap;
    }

    @RequiresPermissions("supplier:representative:delete")
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(String[] id) {
        supplierRepresentativeService.deleteByPrimaryKey(id);
        Map<String, Object> resultMap = new LinkedHashMap();
        resultMap.put("msg", "删除成功");
        resultMap.put("status", 200);
        return resultMap;
    }

    @RequiresPermissions("supplier:representative:add")
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add() {

        return "/supplier/representative/add";
    }

    @RequiresPermissions("supplier:representative:add")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doAdd(SupplierRepresentative model) {
        Map<String, Object> resultMap = new LinkedHashMap();
        model.setDetails(StringEscapeUtils.unescapeHtml(model.getDetails()));
        model.setBandName(StringEscapeUtils.unescapeHtml(model.getBandName()));
        model.setCompanyName(StringEscapeUtils.unescapeHtml(model.getCompanyName()));
        model.setMainProducts(StringEscapeUtils.unescapeHtml(model.getMainProducts()));
        model.setAddTime(DateUtils.getDate());
        Integer id = supplierRepresentativeService.insert(model);
        if (id > 0) {
            resultMap.put("status", 200);
            resultMap.put("message", "操作成功");
        } else {
            resultMap.put("status", 500);
            resultMap.put("message", "操作失败");
        }
        return resultMap;
    }

    @RequiresPermissions("supplier:representative:edit")
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable("id") Integer id, ModelMap map) {
        SupplierRepresentative supplierRepresentative = supplierRepresentativeService.findByPrimaryKey(id.toString());
        map.put("supplierRepresentative",supplierRepresentative);
        return new ModelAndView("/supplier/representative/edit", map);
    }

    @RequiresPermissions("supplier:representative:edit")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doEdit(SupplierRepresentative model, HttpServletRequest request) {
        Map<String, Object> resultMap = new LinkedHashMap();
        model.setBandName(StringEscapeUtils.unescapeHtml(model.getBandName()));
        model.setCompanyName(StringEscapeUtils.unescapeHtml(model.getCompanyName()));
        model.setMainProducts(StringEscapeUtils.unescapeHtml(model.getMainProducts()));
        model.setDetails(StringEscapeUtils.unescapeHtml(model.getDetails()));
        Integer id = supplierRepresentativeService.updateByPrimaryKey(model);
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
