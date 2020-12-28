package com.ff.api.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ff.common.base.BaseController;
import com.ff.common.model.ResponseData;

import com.ff.common.util.DateUtils;
import com.ff.common.util.MD5Utils;
import com.ff.common.util.StringUtils;
import com.ff.shop.model.FinancialNgineering;
import com.ff.shop.model.FinancialNgineeringImages;
import com.ff.shop.service.FinancialNgineeringImagesService;
import com.ff.shop.service.FinancialNgineeringService;
import com.ff.users.model.Users;
import com.ff.users.service.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(value="添加财务手续工程管理",description="添加财务手续工程管理")
@Controller
@RequestMapping(value="/api/financial")
public class FinancialNgineeringApiController extends BaseController {

    @Reference
    private FinancialNgineeringService financialNgineeringService;

    @Reference
    private UsersService usersService;

    @Reference
    private FinancialNgineeringImagesService financialNgineeringImagesService;

    @CrossOrigin(origins = "*", maxAge = 3600)
    @ApiOperation(value = "添加财务手续工程管理",notes = "添加财务手续工程管理")
    @RequestMapping(value = "/insertInformation",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData insertInformation(HttpServletRequest request,
                                            @ApiParam(required=true,name="token",value="token")@RequestParam(value="token",required=false)String token,
                                            @ApiParam(required=true,name="financialNgineering",value="financialNgineering") FinancialNgineering financialNgineering,
                                            @ApiParam(required=true,name="img",value="img")@RequestParam(value="img",required=false)String[] img
    ){
        ResponseData result = new ResponseData();
        if(StringUtils.isEmpty(token)){
            result.setState(500);
            result.setMessage("token为空");
            return result;
        }
        String id = MD5Utils.parseToken(token);
        if(id==null) {
            result.setState(1000);
            result.setMessage("登入失效");
            return result;
        }
        Users model=usersService.findUserByToken(token);
        if(model==null)
        {
            result.setState(1000);
            result.setMessage("未登入");
            return result;
        }
        FinancialNgineering fn = new FinancialNgineering();
        fn.setUserId(model.getUserId());
        fn.setNickName(model.getNickname());
        fn.setStatus(financialNgineering.getStatus());
        fn.setTelephone(model.getUsername());
        fn.setCreateTime(DateUtils.getDateTime());
        Integer ids = financialNgineeringService.insert(fn);
        if(ids>0&&img!=null&&img.length!=0){

//            if(img!=null || (img==null && img.length!=0) ||img.length!=0){
                for(String images:img){
                    FinancialNgineeringImages financialNgineeringImages = new FinancialNgineeringImages();
                    financialNgineeringImages.setFnId(ids);
                    financialNgineeringImages.setImages(images);
                    financialNgineeringImagesService.insert(financialNgineeringImages);

                }
                result.setState(200);
                result.setMessage("上传成功");
            }else {
                result.setState(500);
                result.setMessage("上传失败，无材料图片");
            }
       /* }else{
            result.setState(500);
            result.setMessage("上传失败");*//*
        }*/
        return result;

    }
}
