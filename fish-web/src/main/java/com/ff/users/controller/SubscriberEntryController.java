package com.ff.users.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ff.common.base.BaseController;
import com.ff.users.model.SubscriberEntry;
import com.ff.users.service.SubscriberEntryService;
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
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/subscriber/entry")
public class SubscriberEntryController extends BaseController {

    @Reference
    private SubscriberEntryService subscriberEntryService;

    @RequiresPermissions(value = "subscriber:entry:list")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(String key, ModelMap map) {

        key = StringEscapeUtils.unescapeHtml(key);
        try {
            key = new String(key.getBytes("iso-8859-1"), "utf-8");
        } catch (Exception e) {
            // TODO: handle exception
        }

        map.put("key", key);
        return new ModelAndView("subscriber/entry/list", map);

    }

    @RequiresPermissions(value = "subscriber:entry:list")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doList(String key, HttpServletRequest request) {
        Page<SubscriberEntry> page = getPage(request);
        EntityWrapper<SubscriberEntry> ew = new EntityWrapper<>();
        ew.like("name", "%" + key + "%");
        Page<SubscriberEntry> data = subscriberEntryService.selectPage(page, ew);
        Map<String, Object> resultMap = new LinkedHashMap();

        resultMap.put("data", data);
        return resultMap;
    }

    @RequiresPermissions("subscriber:entry:delete")
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(String[] id) {
        subscriberEntryService.deleteByPrimaryKey(id);
        Map<String, Object> resultMap = new LinkedHashMap();
        resultMap.put("msg", "删除成功");
        resultMap.put("status", 200);
        return resultMap;
    }

    @RequiresPermissions("subscriber:entry:add")
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add() {

        return "/subscriber/entry/add";
    }

    @RequiresPermissions("subscriber:entry:add")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doAdd(SubscriberEntry model) {
        Map<String, Object> resultMap = new LinkedHashMap();
        SubscriberEntry subscriberEntry = new SubscriberEntry();
        subscriberEntry.setName(model.getName());
        subscriberEntry.setTelephone(model.getTelephone());
        List<SubscriberEntry> list =subscriberEntryService.selectByT(subscriberEntry);
        if(list.size()>0){
            resultMap.put("status", 500);
            resultMap.put("message", "操作失败：已有此用户");
        }else {
            Integer id = subscriberEntryService.insert(model);
            if (id > 0) {
                resultMap.put("status", 200);
                resultMap.put("message", "操作成功");
            } else {
                resultMap.put("status", 500);
                resultMap.put("message", "操作失败");
            }
        }
        return resultMap;
    }


    @RequiresPermissions("subscriber:entry:edit")
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable("id") Integer id, ModelMap map) {
        map.put("subscriber", subscriberEntryService.findByPrimaryKey(id.toString()));
        return new ModelAndView("/subscriber/entry/edit", map);
    }

    @RequiresPermissions("subscriber:entry:edit")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doEdit(SubscriberEntry model, HttpServletRequest request) {
        Map<String, Object> resultMap = new LinkedHashMap();
        Integer id = subscriberEntryService.updateByPrimaryKey(model);
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
