package com.ff.api.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ff.common.base.BaseController;
import com.ff.common.model.ResponseData;
import com.ff.common.util.MD5Utils;
import com.ff.shop.model.ContentDetails;
import com.ff.shop.model.Goods;
import com.ff.shop.service.ContentDetailsService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "图文内容获取",description = "图文内容获取")
@Controller
@RequestMapping("/api/detailsContent")
public class DetailsContentController extends BaseController {

    @Reference
    private ContentDetailsService contentDetailsService;

    @Reference
    private UsersService usersService;

    /**
     *
     * @param request
     * @param response
     * @param type 1:新闻快讯2：微课堂
     * @return
     */

    @CrossOrigin(origins = "*", maxAge = 3600)
    @ApiOperation(value="图文内容获取",notes="图文内容获取")
    @RequestMapping(value="/getList",method= RequestMethod.POST)
    @ResponseBody
    public ResponseData getList(HttpServletRequest request, HttpServletResponse response,
                                @ApiParam(required=true,name="type",value="type")@RequestParam(value="type",required=false)Integer type
    ) {
        ResponseData result = new ResponseData();
        Page<ContentDetails> page = getPage(request);
        if(StringUtils.isEmpty(type.toString())){
            result.setState(500);
            result.setMessage("参数为空");
            return result;
        }
        EntityWrapper<ContentDetails> ew=new EntityWrapper();
        ew.orderBy("id",false);
        ew.eq("type",type);
        Map<String,Object> data=new HashMap<>();


        Page<ContentDetails> list=contentDetailsService.selectPage(page,ew);
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
        ContentDetails contentDetails = contentDetailsService.findByPrimaryKey(id.toString());
        if(contentDetails!=null){
            result.setState(200);
            result.setDatas(contentDetails);
            result.setMessage("成功");
        }else{
            result.setState(200);
            result.setDatas(null);
            result.setMessage("数据为空");
        }
        return result;
    }

    /**
     *
     * @param request
     * @param response
     * @param type 3:内部公告
     * @return
     */

    @CrossOrigin(origins = "*", maxAge = 3600)
    @ApiOperation(value="图文内容获取",notes="图文内容获取")
    @RequestMapping(value="/getListed",method= RequestMethod.POST)
    @ResponseBody
    public ResponseData getListed(HttpServletRequest request, HttpServletResponse response,
                                  @ApiParam(required=true,name="type",value="type")@RequestParam(value="type",required=false)Integer type,
                                  @ApiParam(required=true,name="token",value="token")@RequestParam(value="token",required=false)String token

    ) {
        ResponseData result = new ResponseData();
        Page<ContentDetails> page = getPage(request);
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
        String ids = MD5Utils.parseToken(token);
        Users users = usersService.findUserByToken(token);
        if(ids==null|| users==null){
            result.setState(1000);
            result.setMessage("登入失效");
            return result;
        }
        EntityWrapper<ContentDetails> ew=new EntityWrapper();
        ew.orderBy("id",false);
        ew.eq("type",type);
        Map<String,Object> data=new HashMap<>();


        Page<ContentDetails> list=contentDetailsService.selectPage(page,ew);
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
    @RequestMapping(value="/getSingleDetailsThree",method= RequestMethod.POST)
    @ResponseBody
    public ResponseData getSingleDetailsThree(HttpServletRequest request,
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

        ContentDetails contentDetails = contentDetailsService.findByPrimaryKey(id.toString());
        if(contentDetails!=null){
            result.setState(200);
            result.setDatas(contentDetails);
            result.setMessage("成功");
        }else{
            result.setState(200);
            result.setDatas(null);
            result.setMessage("数据为空");
        }
        return result;
    }


}
