package com.ff.api.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ff.common.base.BaseController;
import com.ff.common.model.ResponseData;
import com.ff.common.util.StringUtils;
import com.ff.shop.model.PurchasingPartner;
import com.ff.shop.service.PurchasingPartnerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/purchasingPartner")
public class PurchasingPartnerApiController extends BaseController {

        @Reference
        private PurchasingPartnerService purchasingPartnerService;

        /**
         *
         * @param request
         * @param response
         * @param type 1:供货商列表
         * @return
         */

        @CrossOrigin(origins = "*", maxAge = 3600)
        @ApiOperation(value="供货商列表获取",notes="供货商列表获取")
        @RequestMapping(value="/getList",method= RequestMethod.POST)
        @ResponseBody
        public ResponseData getList(HttpServletRequest request, HttpServletResponse response,
                                    @ApiParam(required=true,name="type",value="type")@RequestParam(value="type",required=false)Integer type
        ) {
            ResponseData result = new ResponseData();
            Page<PurchasingPartner> page = getPage(request);
            if(StringUtils.isEmpty(type.toString())){
                result.setState(500);
                result.setMessage("参数为空");
                return result;
            }
            EntityWrapper<PurchasingPartner> ew=new EntityWrapper();
            ew.orderBy("id",false);
            Map<String,Object> data=new HashMap<>();

            Page<PurchasingPartner> list = purchasingPartnerService.selectPage(page,ew);
            data.put("pages", list.getPages());
            data.put("size", list.getSize());
            data.put("total", list.getTotal());
            data.put("data",list.getRecords());
            result.setDatas(data);
            result.setState(200);
            result.setMessage("成功");
            return result;
        }

        @CrossOrigin(origins = "*", maxAge = 3600)
        @ApiOperation(value="详情",notes="详情")
        @RequestMapping(value="/getSingleDetails",method= RequestMethod.POST)
        @ResponseBody
        public ResponseData getSingleDetails(HttpServletRequest request,
                                             @ApiParam(required=true,name="id",value="id")@RequestParam(value="id",required=false)Integer id
        ) {
            ResponseData result = new ResponseData();
            if(id==null){
                result.setMessage("参数为空");
                result.setState(500);
                return result;
            }
            PurchasingPartner purchasingPartner = purchasingPartnerService.findByPrimaryKey(id.toString());
            if(purchasingPartner!=null){
                result.setState(200);
                result.setDatas(purchasingPartner);
                result.setMessage("成功");
            }else{
                result.setState(200);
                result.setDatas(null);
                result.setMessage("数据为空");
            }
            return result;
        }


}
