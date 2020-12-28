package com.ff.api.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ff.common.base.BaseController;
import com.ff.common.model.ResponseData;
import com.ff.common.util.MD5Utils;
import com.ff.shop.model.ContentDetails;
import com.ff.shop.model.TextManagement;
import com.ff.shop.service.TextManagementService;
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

@Api(value = "制度规范办公流程",description = "制度规范办公流程")
@Controller
@RequestMapping("/api/textManagement")
public class ManagementTextController extends BaseController {

    @Reference
    private UsersService usersService;

    @Reference
    private TextManagementService textManagementService;

    @CrossOrigin(origins = "*", maxAge = 3600)
    @ApiOperation(value="制度规范办公流程",notes="制度规范办公流程")
    @RequestMapping(value="/getList",method= RequestMethod.POST)
    @ResponseBody
    public ResponseData getList(HttpServletRequest request, HttpServletResponse response,
                                @ApiParam(required=true,name="token",value="token")@RequestParam(value="token",required=false)String token,
                                @ApiParam(required=true,name="type",value="type")@RequestParam(value="type",required=false)Integer type,
                                @ApiParam(required=true,name="classification",value="classification")@RequestParam(value="classification",required=false)Integer classification
    ) {
        ResponseData result = new ResponseData();
        Page<TextManagement> page = getPage(request);

        if(StringUtils.isEmpty(type.toString())){
            result.setState(500);
            result.setMessage("参数为空");
            return result;
        }
        if(StringUtils.isEmpty(token)){
            result.setState(500);
            result.setMessage("参数为空");
            return result;
        }
        if(StringUtils.isEmpty(classification.toString())){
            result.setState(500);
            result.setMessage("参数为空");
            return result;
        }
        String id = MD5Utils.parseToken(token);
        Users users = usersService.findUserByToken(token);
        if(id==null|| users==null){
            result.setState(1000);
            result.setMessage("登入失效");
            return result;
        }
        EntityWrapper<TextManagement> ew=new EntityWrapper();
        ew.orderBy("id",false);
        ew.eq("type",type);
        ew.eq("classification",classification);
        Map<String,Object> data=new HashMap<>();


        Page<TextManagement> list=textManagementService.selectPage(page,ew);
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
    @RequestMapping(value="/getDetails",method= RequestMethod.POST)
    @ResponseBody
    public ResponseData getSingleDetails(HttpServletRequest request,
                                         @ApiParam(required=true,name="id",value="id")@RequestParam(value="id",required=false)Integer id,
                                         @ApiParam(required=true,name="token",value="token")@RequestParam(value="token",required=false)String token
    ) {
        ResponseData result = new ResponseData();
        if(id==null){
            result.setMessage("参数为空");
            result.setState(500);
            return result;
        }
        if(StringUtils.isEmpty(token)){
            result.setMessage("参数为空");
            result.setState(500);
            return result;
        }
        String ids = MD5Utils.parseToken(token);
        Users users = usersService.findUserByToken(token);
        if(ids==null|| users==null){
            result.setState(1000);
            result.setMessage("登入失效");
            return result;
        }
        TextManagement textManagement = textManagementService.findByPrimaryKey(id.toString());
        if(textManagement!=null){
            result.setState(200);
            result.setDatas(textManagement);
            result.setMessage("成功");
        }else{
            result.setState(500);
            result.setDatas(null);
            result.setMessage("数据为空");
        }


        return result;
    }



}
