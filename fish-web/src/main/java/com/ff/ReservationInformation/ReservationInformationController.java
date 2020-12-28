package com.ff.ReservationInformation;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ff.common.base.BaseController;
import com.ff.shop.model.ReservationInformation;
import com.ff.shop.service.ReservationInformationService;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/reservation/information")
public class ReservationInformationController extends BaseController {

    @Reference
    private ReservationInformationService reservationInformationService;



    @RequiresPermissions(value = "reservation:information:list")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(String key, ModelMap map) {

        key= StringEscapeUtils.unescapeHtml(key);
        try {
            key = new String(key.getBytes("iso-8859-1"), "utf-8");
        } catch (Exception e) {
            // TODO: handle exception
        }

        map.put("key", key);
        return new ModelAndView("reservation/information/list",map);

    }

    @RequiresPermissions(value = "reservation:information:list")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doList(HttpServletRequest request, String key) {
        Page<ReservationInformation> page = getPage(request);
        EntityWrapper<ReservationInformation> ew=new EntityWrapper();
        ew.like("phone_number","%"+key+"%");
        Page<ReservationInformation> data= reservationInformationService.selectPage(page,ew);
        Map<String, Object> resultMap = new LinkedHashMap();
        resultMap.put("data",data);
        return resultMap;
    }

    @RequiresPermissions("reservation:information:delete")
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(String[] id) {
        reservationInformationService.deleteByPrimaryKey(id);
        Map<String, Object> resultMap = new LinkedHashMap();
        resultMap.put("msg","删除成功");
        resultMap.put("status",200);
        return resultMap;
    }


}
