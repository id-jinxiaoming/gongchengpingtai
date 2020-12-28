package com.ff.api.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ff.common.base.BaseController;
import com.ff.common.model.ResponseData;
import com.ff.common.util.DateUtils;
import com.ff.common.util.MD5Utils;
import com.ff.common.util.StringUtils;
import com.ff.shop.model.InvoiceApplication;
import com.ff.shop.service.InvoiceApplicationService;
import com.ff.system.service.CommonService;
import com.ff.users.model.LeadingCadre;
import com.ff.users.model.Users;
import com.ff.users.service.LeadingCadreService;
import com.ff.users.service.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value="发票申请接口",description="发票申请接口")
@Controller
@RequestMapping(value="/api/invoice")
public class InvoiceApplicationApiController extends BaseController {

    @Reference
    private UsersService usersService;

    @Reference
    private InvoiceApplicationService invoiceApplicationService;

    @Reference
    private LeadingCadreService leadingCadreService;

    @Reference
    private CommonService commonService;

    @CrossOrigin(origins = "*",maxAge = 3600)
    @ApiOperation(value="预约接口",notes="预约接口")
    @RequestMapping(value="/insertInvoiceApplication",method= RequestMethod.POST)
    @ResponseBody
    public ResponseData insertInvoiceApplication(HttpServletRequest request, HttpServletResponse response,
                                          @ApiParam(required=true,name="token",value="token")@RequestParam(value="token",required=false)String token,
                                          @ApiParam(required=true,name="invoiceApplication",value="invoiceApplication") InvoiceApplication invoice)
    {
        ResponseData result = new ResponseData();
        if(StringUtils.isEmpty(token)){
            result.setState(1000);
            result.setMessage("未登入");
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
        InvoiceApplication invoiceApplication = new InvoiceApplication();
        invoiceApplication.setUserId(model.getUserId());
        invoiceApplication.setUnitName(invoice.getUnitName());
        invoiceApplication.setCreditCode(invoice.getCreditCode());
        invoiceApplication.setBankAccount(invoice.getBankAccount());
        invoiceApplication.setOpeningBank(invoice.getOpeningBank());
        invoiceApplication.setCompanyAddress(invoice.getCompanyAddress());
        invoiceApplication.setCompanyTelephone(invoice.getCompanyTelephone());
        invoiceApplication.setProjectLeader(invoice.getProjectLeader());
        invoiceApplication.setContractCategories(invoice.getContractCategories());
        invoiceApplication.setItemCategory(invoice.getItemCategory());
        invoiceApplication.setInvoiceAmount(invoice.getInvoiceAmount());
        invoiceApplication.setPaymentDate(invoice.getPaymentDate());
        invoiceApplication.setEntryName(invoice.getEntryName());
        invoiceApplication.setProjectAddress(invoice.getProjectAddress());
        invoiceApplication.setContractAmount(invoice.getContractAmount());
        invoiceApplication.setOthers(invoice.getOthers());
        invoiceApplication.setAddTime(DateUtils.getDateTime());
        Integer ids = invoiceApplicationService.insert(invoiceApplication);
        if(ids>0){
            String name ="";
            String telephone="";
            List<LeadingCadre> list= leadingCadreService.selectAll();
            if(list.size()>0){
                for(LeadingCadre leadingCadre:list){
                    if(6==leadingCadre.getStatus()){
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
        }else {
            result.setState(500);
            result.setMessage("预约失败");
        }
        return result;
    }

    @CrossOrigin(origins = "*",maxAge = 3600)
    @ApiOperation(value="获取历史记录",notes="获取历史记录")
    @RequestMapping(value="/invoiceGetList",method= RequestMethod.POST)
    @ResponseBody
    public ResponseData invoiceGetList(HttpServletRequest request, HttpServletResponse response,
                                                 @ApiParam(required=true,name="token",value="token")@RequestParam(value="token",required=false)String token){
        ResponseData result  = new ResponseData();

        if(StringUtils.isEmpty(token)){
            result.setState(500);
            result.setMessage("参数为空");
            return result;
        }
        String id = MD5Utils.parseToken(token);
        if(id==null){
            result.setState(1000);
            result.setMessage("登入失效");
            return result;
        }

        Users model = usersService.findUserByToken(token);
        if(model==null)
        {
            result.setState(1000);
            result.setMessage("未登入");
            return result;
        }
        Map map = new HashMap();
        Page<InvoiceApplication> page = getPage(request);
        EntityWrapper<InvoiceApplication> ew = new EntityWrapper<>();
        ew.eq("user_id",model.getUserId());
        ew.orderBy("user_id",false);
        Page<InvoiceApplication> data = invoiceApplicationService.selectPage(page,ew);
        map.put("all",data.getRecords());
        map.put("page",data.getPages());
        map.put("total",data.getTotal());
        result.setDatas(map);
        result.setMessage("成功");
        result.setState(200);
        return result;
    }

    @CrossOrigin(origins = "*",maxAge = 3600)
    @ApiOperation(value="查看详情",notes="查看详情")
    @RequestMapping(value="/singleDetails",method= RequestMethod.POST)
    @ResponseBody
    public ResponseData singleDetails(HttpServletRequest request, HttpServletResponse response,
                                       @ApiParam(required=true,name="token",value="token")@RequestParam(value="token",required=false)String token,
                                      @ApiParam(required = true,name="id",value = "id")@RequestParam(value = "id",required = false)String id){
        ResponseData result = new ResponseData();

        if(StringUtils.isEmpty(token)){
            result.setState(500);
            result.setMessage("参数为空");
            return result;
        }
        if(StringUtils.isEmpty(id)){
            result.setState(500);
            result.setMessage("参数为空");
            return result;
        }
        String idS = MD5Utils.parseToken(token);
        if(idS==null){
            result.setState(1000);
            result.setMessage("登入失效");
            return result;
        }

        Users model = usersService.findUserByToken(token);
        if(model==null)
        {
            result.setState(1000);
            result.setMessage("未登入");
            return result;
        }

        InvoiceApplication invoiceApplication = invoiceApplicationService.findByPrimaryKey(id);
        if (invoiceApplication != null){
            result.setState(200);
            result.setDatas(invoiceApplication);
            result.setMessage("成功");
        }else{
            result.setDatas(invoiceApplication);
            result.setMessage("数据为空");
            result.setState(200);
        }
        return result;
    }
}
