package com.ff.api.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ff.common.base.BaseController;
import com.ff.common.model.ResponseData;
import com.ff.common.util.DateUtils;
import com.ff.common.util.MD5Utils;
import com.ff.common.util.StringUtils;
import com.ff.shop.model.ProjectCooperation;
import com.ff.shop.service.ProjectCooperationService;
import com.ff.users.model.Users;
import com.ff.users.service.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(value="项目合作",description="项目合作")
@Controller
@RequestMapping(value="/api/cooperation")
public class ProjectCooperationApiController extends BaseController {

    @Reference
    private ProjectCooperationService projectCooperationService;

    @Reference
    private UsersService usersService;

    @CrossOrigin(origins = "*", maxAge = 3600)
    @ApiOperation(value = "添加合作项目",notes = "添加合作项目")
    @RequestMapping(value = "/insertCooperation",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData insertCooperation(HttpServletRequest request,
                                          @ApiParam(required=true,name="token",value="token")@RequestParam(value="token",required=false)String token,
                                          @ApiParam(required=true,name="cooperationProject",value="cooperationProject") ProjectCooperation projectCooperation
    ){
        ResponseData result = new ResponseData();
        if(StringUtils.isEmpty(token)){
            result.setState(500);
            result.setMessage("token为空");
            return result;
        }
        String id = MD5Utils.parseToken(token);
        if(id==null){
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
        ProjectCooperation projectCooperation1 = new ProjectCooperation();
        projectCooperation1.setUserId(model.getUserId());
        projectCooperation1.setEntryName(projectCooperation.getEntryName());
        projectCooperation1.setProjectCost(projectCooperation.getProjectCost());
        projectCooperation1.setItemCategory(projectCooperation.getItemCategory());
        projectCooperation1.setItemAddress(projectCooperation.getItemAddress());
        projectCooperation1.setCooperationProject(projectCooperation.getCooperationProject());
        projectCooperation1.setOtherInstructions(projectCooperation.getOtherInstructions());
        projectCooperation1.setCreateTime(DateUtils.getDateTime());
        Integer ids = projectCooperationService.insert(projectCooperation1);
        if(ids>0){
            result.setState(200);
            result.setMessage("添加成功");
        }else{
            result.setState(500);
            result.setMessage("添加失败");
        }
        return result;
    }
}
