package com.ff.api.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ff.common.base.BaseController;
import com.ff.common.model.ResponseData;
import com.ff.shop.model.JumpButton;
import com.ff.shop.service.JumpButtonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@Api(value="按钮跳转获取内容接口",description="按钮跳转获取内容接口")
@Controller
@RequestMapping("/api/buttonJump")
public class ButtonJumpController extends BaseController {

    @Reference
    private JumpButtonService jumpButtonService;

    @CrossOrigin(origins = "*", maxAge = 3600)
    @ApiOperation(value="获取按钮跳转内容",notes="获取按钮跳转内容")
    @RequestMapping(value="/getList",method= RequestMethod.POST)
    @ResponseBody
    public ResponseData getList(HttpServletRequest request, HttpServletResponse response,
            @ApiParam(required=true,name="status",value="status")@RequestParam(value="status",required=false)Integer status
    ){
        ResponseData result = new ResponseData();
        JumpButton jumpButton = new JumpButton();
        jumpButton.setStatus(status);
        List<JumpButton> list=jumpButtonService.selectByT(jumpButton);
        if(list!=null && !list.isEmpty()){
            result.setState(200);
            result.setDatas(list.get(0));
            result.setMessage("成功");
        }else{
            JumpButton jumpButton1 = new JumpButton();
            result.setDatas(jumpButton1);
            result.setState(200);
            result.setMessage("数据为空");
        }
        return result;
    }
}
