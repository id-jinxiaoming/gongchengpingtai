package com.ff.api.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ff.common.base.BaseController;
import com.ff.common.model.ResponseData;
import com.ff.common.util.MD5Utils;
import com.ff.shop.model.ContentDetails;
import com.ff.shop.model.SupplierRepresentative;
import com.ff.shop.service.ContentDetailsService;
import com.ff.shop.service.SupplierRepresentativeService;
import com.ff.users.model.Users;
import com.ff.users.service.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Api(value = "供货商列表",description = "供货商列表")
@Controller
@RequestMapping("/api/supplierRepresentative")
public class SupplierRepresentativeApiController extends BaseController {

    @Reference
    private SupplierRepresentativeService supplierRepresentativeService;

    @Reference
    private UsersService usersService;

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
        Page<SupplierRepresentative> page = getPage(request);
        if(StringUtils.isEmpty(type.toString())){
            result.setState(500);
            result.setMessage("参数为空");
            return result;
        }
        EntityWrapper<SupplierRepresentative> ew=new EntityWrapper();
        ew.orderBy("id",false);
        Map<String,Object> data=new HashMap<>();


        Page<SupplierRepresentative> list=supplierRepresentativeService.selectPage(page,ew);
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
        SupplierRepresentative supplierRepresentative = supplierRepresentativeService.findByPrimaryKey(id.toString());
        if(supplierRepresentative!=null){
            result.setState(200);
            result.setDatas(supplierRepresentative);
            result.setMessage("成功");
        }else{
            result.setState(200);
            result.setDatas(null);
            result.setMessage("数据为空");
        }
        return result;
    }


}
