package com.ff.ProjectCooperation;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ff.common.base.BaseController;
import com.ff.shop.model.ProjectCooperation;
import com.ff.shop.service.ProjectCooperationService;
import com.ff.users.model.Users;
import com.ff.users.service.UsersService;
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
@RequestMapping("/project/cooperation")
public class ProjectCooperationController extends BaseController {

    @Reference
    private ProjectCooperationService projectCooperationService;

    @Reference
    private UsersService usersService;

    @RequiresPermissions(value = "project:cooperation:list")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(String key, ModelMap map) {

        key= StringEscapeUtils.unescapeHtml(key);
        try {
            key = new String(key.getBytes("iso-8859-1"), "utf-8");
        } catch (Exception e) {
            // TODO: handle exception
        }

        map.put("key", key);
        return new ModelAndView("project/cooperation/list",map);
    }

    @RequiresPermissions(value = "project:cooperation:list")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doList(HttpServletRequest request, String key) {
        Page<ProjectCooperation> page = getPage(request);
        EntityWrapper<ProjectCooperation> ew=new EntityWrapper();
        ew.like("entry_name","%"+key+"%");
        Page<ProjectCooperation> data= projectCooperationService.selectPage(page,ew);
        Map<String, Object> resultMap = new LinkedHashMap();
        resultMap.put("data",data);
        return resultMap;
    }


    @RequiresPermissions("project:cooperation:delete")
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(String[] id) {
        projectCooperationService.deleteByPrimaryKey(id);
        Map<String, Object> resultMap = new LinkedHashMap();
        resultMap.put("msg","删除成功");
        resultMap.put("status",200);
        return resultMap;
    }

    @RequiresPermissions("project:cooperation:show")
    @RequestMapping(value="/show/{id}",method=RequestMethod.GET)
    public ModelAndView edit(@PathVariable("id")String id, ModelMap map){

        Users model = usersService.findByPrimaryKey(id);
        map.put("item", model);
        return new ModelAndView("/project/cooperation/show",map);

    }


}
