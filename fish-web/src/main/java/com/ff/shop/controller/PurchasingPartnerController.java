package com.ff.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ff.common.base.BaseController;
import com.ff.common.util.DateUtils;
import com.ff.shop.model.PurchasingPartner;
import com.ff.shop.service.PurchasingPartnerService;
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
@RequestMapping("/purchasing/partner")
public class PurchasingPartnerController extends BaseController {

    @Reference
    private PurchasingPartnerService purchasingPartnerService;

    @RequiresPermissions(value = "purchasing:partner:list")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(String key, ModelMap map) {

        key = StringEscapeUtils.unescapeHtml(key);
        try {
            key = new String(key.getBytes("iso-8859-1"), "utf-8");
        } catch (Exception e) {
            // TODO: handle exception
        }

        map.put("key", key);
        return new ModelAndView("purchasing/partner/list", map);

    }

    @RequiresPermissions(value = "purchasing:partner:list")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doList(String key, HttpServletRequest request) {
        Page<PurchasingPartner> page = getPage(request);
        EntityWrapper<PurchasingPartner> ew = new EntityWrapper<>();
        ew.like("business_name", "%" + key + "%");
        Page<PurchasingPartner> data = purchasingPartnerService.selectPage(page, ew);
        Map<String, Object> resultMap = new LinkedHashMap<>();

        resultMap.put("data", data);
        return resultMap;
    }

    @RequiresPermissions("purchasing:partner:delete")
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(String[] id) {
        purchasingPartnerService.deleteByPrimaryKey(id);
        Map<String, Object> resultMap = new LinkedHashMap();
        resultMap.put("msg", "删除成功");
        resultMap.put("status", 200);
        return resultMap;
    }

    @RequiresPermissions("purchasing:partner:add")
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add() {

        return "/purchasing/partner/add";
    }

    @RequiresPermissions("purchasing:partner:add")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doAdd(PurchasingPartner model) {
        Map<String, Object> resultMap = new LinkedHashMap();
        model.setDetails(StringEscapeUtils.unescapeHtml(model.getDetails()));
        model.setBusinessName(StringEscapeUtils.unescapeHtml(model.getBusinessName()));
        model.setCompanyProfile(StringEscapeUtils.unescapeHtml(model.getCompanyProfile()));
        model.setCreateDate(DateUtils.getDate());
        Integer id = purchasingPartnerService.insert(model);
        if (id > 0) {
            resultMap.put("status", 200);
            resultMap.put("message", "操作成功");
        } else {
            resultMap.put("status", 500);
            resultMap.put("message", "操作失败");
        }
        return resultMap;
    }

    @RequiresPermissions("purchasing:partner:edit")
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable("id") Integer id, ModelMap map) {
        PurchasingPartner purchasingPartner = purchasingPartnerService.findByPrimaryKey(id.toString());
        map.put("purchasingPartner",purchasingPartner);
        return new ModelAndView("/purchasing/partner/edit", map);
    }

    @RequiresPermissions("purchasing:partner:edit")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doEdit(PurchasingPartner model, HttpServletRequest request) {
        Map<String, Object> resultMap = new LinkedHashMap();
        model.setBusinessName(StringEscapeUtils.unescapeHtml(model.getBusinessName()));
        model.setCompanyProfile(StringEscapeUtils.unescapeHtml(model.getCompanyProfile()));
        model.setDetails(StringEscapeUtils.unescapeHtml(model.getDetails()));
        Integer id = purchasingPartnerService.updateByPrimaryKey(model);
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
