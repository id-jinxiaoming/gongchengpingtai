package com.ff.api.controller;

import com.ff.common.base.BaseController;
import com.ff.common.model.ResponseData;
import com.ff.common.util.DateUtils;
import com.ff.common.util.MD5Utils;
import com.ff.common.util.StringUtils;
import com.ff.shop.model.ReservationInformation;
import com.ff.shop.service.ReservationInformationService;
import com.ff.system.service.CommonService;
import com.ff.users.model.LeadingCadre;
import com.ff.users.model.Users;
import com.ff.users.model.UsersConsignee;
import com.ff.users.service.LeadingCadreService;
import com.ff.users.service.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.alibaba.dubbo.config.annotation.Reference;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(value="预约接口",description="预约接口")
@Controller
@RequestMapping(value="/api/reservation")
public class ReservationApiController extends BaseController {
    @Reference
    private ReservationInformationService reservationInformationService;

    @Reference
    private UsersService usersService;

    @Reference
    private LeadingCadreService leadingCadreService;

    @Reference
    private CommonService commonService;

    @CrossOrigin(origins = "*",maxAge = 3600)
    @ApiOperation(value="预约接口",notes="预约接口")
    @RequestMapping(value="/insertReservation",method= RequestMethod.POST)
    @ResponseBody
    public ResponseData insertReservation(HttpServletRequest request, HttpServletResponse response,
                                      @ApiParam(required=true,name="token",value="token")@RequestParam(value="token",required=false)String token,
                                      @ApiParam(required=true,name="reservationInformation",value="reservationInformation") ReservationInformation information)
    {
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

        ReservationInformation reservationInformation = new ReservationInformation();
        reservationInformation.setUserId(model.getUserId());
        reservationInformation.setNameVisitor(information.getNameVisitor());
        reservationInformation.setPhoneNumber(information.getPhoneNumber());
        reservationInformation.setProjectName(information.getProjectName());
        reservationInformation.setDetailsCause(information.getDetailsCause());
        reservationInformation.setStatus(information.getStatus());
        reservationInformation.setCreateTime(DateUtils.getDateTime());
        Integer ids =reservationInformationService.insert(reservationInformation);
        if(ids>0){
            Integer status = information.getStatus();
            String name ="";
            String telephone="";
            List<LeadingCadre> list= leadingCadreService.selectAll();
            if(list.size()>0){
                for(LeadingCadre leadingCadre:list){
                    if(status==leadingCadre.getStatus()){
                        name=leadingCadre.getName();
                        telephone=leadingCadre.getTelephone();
                    }
                }
            }else{
                name="老板";
                telephone="13161602850";
            }
            String json="{\"name\":\""+name+"\"}";
            commonService.sendSMS("sms_codes", telephone, json);//调运发送短信的方法
            result.setState(200);
            result.setMessage("预约成功");
        }else{
            result.setState(500);
            result.setMessage("预约失败");
        }
        return result;
    }
}
