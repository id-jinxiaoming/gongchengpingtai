package com.ff.EngineeringManagement;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ff.common.base.BaseController;
import com.ff.shop.model.FinancialNgineering;
import com.ff.shop.model.FinancialNgineeringImages;
import com.ff.shop.service.FinancialNgineeringImagesService;
import com.ff.shop.service.FinancialNgineeringService;
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
@RequestMapping("/engineering/management")
public class EngineeringManagementController extends BaseController {

    @Reference
    private FinancialNgineeringImagesService financialNgineeringImagesService;

    @Reference
    private FinancialNgineeringService financialNgineeringService;

    @RequiresPermissions(value = "engineering:management:list")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(String key, ModelMap map) {

        key= StringEscapeUtils.unescapeHtml(key);
        try {
            key = new String(key.getBytes("iso-8859-1"), "utf-8");
        } catch (Exception e) {
            // TODO: handle exception
        }

        map.put("key", key);
        return new ModelAndView("engineering/management/list",map);
    }

    @RequiresPermissions(value = "engineering:management:list")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doList(HttpServletRequest request, String key) {
        Page<FinancialNgineering> page = getPage(request);
        EntityWrapper<FinancialNgineering> ew=new EntityWrapper();
        ew.like("telephone","%"+key+"%");
        ew.eq("status",2);
        Page<FinancialNgineering> data= financialNgineeringService.selectPage(page,ew);
        Map<String, Object> resultMap = new LinkedHashMap();
        resultMap.put("data",data);
        return resultMap;
    }

    @RequiresPermissions("engineering:management:delete")
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(String[] id) {
        financialNgineeringService.deleteByPrimaryKey(id);
        Map<String, Object> resultMap = new LinkedHashMap();
        resultMap.put("msg","删除成功");
        resultMap.put("status",200);
        return resultMap;
    }

    @RequiresPermissions("engineering:management:show")
    @RequestMapping(value="/show/{id}",method=RequestMethod.GET)
    public ModelAndView edit(@PathVariable("id")Integer id, ModelMap map){
        FinancialNgineeringImages financialNgineeringImages = new FinancialNgineeringImages();
        financialNgineeringImages.setFnId(id);
        List<FinancialNgineeringImages> data=financialNgineeringImagesService.selectByT(financialNgineeringImages);

        map.put("item", data);
        return new ModelAndView("/engineering/management/show",map);

    }
}
