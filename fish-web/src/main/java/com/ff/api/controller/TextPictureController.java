package com.ff.api.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ff.common.base.BaseController;
import com.ff.common.model.ResponseData;
import com.ff.shop.model.ImageText;
import com.ff.shop.service.ImageTextService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(value="栏位图文获取接口",description="栏位图文获取接口")
@Controller
@RequestMapping(value="/api/imageText")
public class TextPictureController extends BaseController {


    @Reference
    private ImageTextService imageTextService;


    @CrossOrigin(origins = "*", maxAge = 3600)
    @ApiOperation(value="栏位图片",notes="栏位图片")
    @RequestMapping(value="/getList",method= RequestMethod.POST)
    @ResponseBody
    public ResponseData getList(HttpServletRequest request, HttpServletResponse response,
                                     @ApiParam(required=true,name="status",value="status")@RequestParam(value="status",required=false)Integer status
    ) {
        ResponseData result = new ResponseData();
        ImageText imageText = new ImageText();
        imageText.setStatus(status);
        List<ImageText>list=imageTextService.selectByT(imageText);
        if(null == list || list.size() ==0 ){
            ImageText imageText1 = new ImageText();
            result.setState(200);
            result.setDatas(imageText1);
            result.setMessage("数据为空");

        }else{
            result.setState(200);
            result.setDatas(list.get(0));
            result.setMessage("成功");
        }
        return result;
    }

    }
