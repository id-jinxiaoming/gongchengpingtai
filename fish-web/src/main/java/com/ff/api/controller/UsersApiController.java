package com.ff.api.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ff.common.base.BaseController;
import com.ff.common.model.ResponseData;
import com.ff.common.util.MD5Util;
import com.ff.common.util.MD5Utils;
import com.ff.common.util.UtilPath;
import com.ff.order.service.OrderService;
import com.ff.shop.service.*;
import com.ff.system.service.CommonService;
import com.ff.users.model.*;
import com.ff.users.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Api(value="用户接口",description="用户接口")
@Controller
@RequestMapping(value="/api/users")
public class UsersApiController extends BaseController{

    @Reference
    private UsersService usersService;

    @Reference
    private CommonService commonService;

    @Reference
    private UsersAccountService usersAccountService;

    @Reference
    private UsersCouponService usersCouponService;

    @Reference
    private UsersAccountLogService usersAccountLogService;

    @Reference
    private ShopService shopService;

    @Reference
    private UsersConsigneeService usersConsigneeService;

    @Reference
    private ShippingMethodService shippingMethodService;

    @Reference
    private GoodsService goodsService;
    @Reference
    private ProvinceService provinceService;

    @Reference
    private CityService cityService;

    @Reference
    private AreaService areaService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Reference
    private OrderService orderService;

    @Reference
    private SubscriberEntryService subscriberEntryService;


    @CrossOrigin(origins = "*", maxAge = 3600)
    @ApiOperation(value="判断登入状态是否失效",notes="判断登入状态是否失效")
    @RequestMapping(value="/verification",method= RequestMethod.POST)
    @ResponseBody
    public ResponseData verification(
            @ApiParam(required=true,name="token",value="唯一标识")@RequestParam(value="token",required=false)String token
    ){
        ResponseData result = new ResponseData();
        if(StringUtils.isEmpty(token)){
            result.setState(500);
            result.setMessage("参数为空");
            return result;
        }
        String id =MD5Utils.parseToken(token);
        Users users = usersService.findUserByToken(token);
        if(id==null|| users==null){
            result.setState(200);
            result.setMessage("登入失效");
            result.setDatas(1);
        }else{
            result.setState(200);
            result.setMessage("成功");
            result.setDatas(0);
        }
        return result;
    }


    @CrossOrigin(origins = "*", maxAge = 3600)
    @ApiOperation(value="用户登录",notes="用户登录")
    @RequestMapping(value="/login",method= RequestMethod.POST)
    @ResponseBody
    public ResponseData Login(
            @ApiParam(required=true,name="mobile",value="手机号")@RequestParam(value="mobile",required=false)String mobile,
            @ApiParam(required=true,name="password",value="密码")@RequestParam(value="password",required=false)String password
    ){
        ResponseData result = new ResponseData();
        Users user=new Users();
        user.setMobile(mobile);
        Users model=usersService.findOne(user);
        if(model==null)
        {
            result.setState(500);
            result.setMessage("用户不存在");
            return result;
        }else if(model.getStatus()==0){
            result.setState(500);
            result.setMessage("用户还未审核请稍后再试");
            return result;
        }
        Boolean boolen=MD5Util.verify(password,model.getPassword());
        if(boolen==false){
            result.setState(500);
            result.setMessage("密码不正确");
            return result;
        }
        else{
            String token=MD5Utils.createToken(model.getPassword());
            model.setToken(token);
            model.setUpateTime(new Date());
            usersService.updateByPrimaryKey(model);
            //返回数据
            Map<String,Object> map= new HashMap<>();
            map.put("token",model.getToken());
            map.put("name",model.getNickname());
            result.setState(200);
            result.setDatas(map);
            result.setMessage("登入成功");

        }
        return result;
    }

    /**
     * 手机号验证码登入
     * @param mobile
     * @param code
     * @return
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @ApiOperation(value="根据手机号登录",notes="根据手机号登录")
    @RequestMapping(value="/loginByMobile",method= RequestMethod.POST)
    @ResponseBody
    public ResponseData LoginByMobile(
            @ApiParam(required=true,name="mobile",value="手机号")@RequestParam(value="mobile",required=false)String mobile,
            @ApiParam(required=true,name="code",value="验证码")@RequestParam(value="code",required=false)String code
    ){
        ResponseData result = new ResponseData();
        String checkCode=redisTemplate.opsForValue().get("smsCode:"+mobile).toString();

        if(!checkCode.equals(code)){
            result.setState(500);
            result.setMessage("验证码不正确");
            return result;
        }
        Users user=new Users();
        user.setMobile(mobile);
        Users model=usersService.findOne(user);
        if(model==null)
        {
            result.setState(500);
            result.setMessage("用户不存在");
        }
        else if(model.getStatus()==0){

            result.setState(500);
            result.setMessage("用户已禁用");
        }
        else{
            //更新用户登录信息
            String token=MD5Utils.createToken(model.getPassword());
            model.setToken(token);
            model.setUpateTime(new Date());
            usersService.updateByPrimaryKey(model);
            //返回数据
            Map<String,Object> map= new HashMap<>();
            map.put("token",model.getToken());
            map.put("name",model.getNickname());
            result.setState(200);
            result.setDatas(map);
            result.setMessage(" 登入成功");
        }
        return result;
    }


    /**
     * 发送短信验证码
     * @param mobile
     * @return
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @ApiOperation(value="发送短信验证码",notes="发送短信验证码", httpMethod = "POST")
    @RequestMapping(value="/sendIdentifyingCode",method=RequestMethod.POST)
    @ResponseBody
    public ResponseData sendIdentifyingCode(
            @ApiParam(required=true,name="sms_code",value="验证码类型")@RequestParam(value="sms_code",required=false)String sms_code,
            @ApiParam(required=true,name="mobile",value="手机号")@RequestParam(value="mobile",required=false)String mobile){
        int code = (int) (Math.random() * (9999 - 1000 + 1)) + 1000;// 产生1000-9999的随机数


        String json="{\"code\":\""+code+"\"}";
        ResponseData result = new ResponseData();
        if(!UtilPath.isMobile(mobile)){//验证手机号码格式是否正确
            result.setState(500);
            result.setMessage("手机号码格式不正确！");
            return result;
        }
        commonService.sendSMS("sms_code", mobile, json);//调运发送短信的方法
        //创建session，并将手机号于验证码存入session
        redisTemplate.opsForValue().set("smsCode:"+mobile,code, 180000, TimeUnit.MILLISECONDS);
        result.setState(200);
        result.setMessage("短信发送成功！");
        result.setDatas(code);
        return result;
    }


    /**
     * 手机号注册（账号为手机号）
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @ApiOperation(value="会员注册",notes="会员注册", httpMethod = "POST")
    @RequestMapping(value="/register",method=RequestMethod.POST)
    @ResponseBody
    public ResponseData register(HttpServletRequest request,HttpServletResponse response,
            @ApiParam(required=true,name="username",value="用户名(手机号)")@RequestParam(value="username",required=false)String username,
            @ApiParam(required=true,name="password",value="密码")@RequestParam(value="password",required=false)String password,
            @ApiParam(required=true,name="name",value="姓名")@RequestParam(value="name",required=false)String name,
            @ApiParam(required=true,name="project",value="隶属项目或公司")@RequestParam(value="project",required=false)String project,
            @ApiParam(required=true,name="code",value="验证码")@RequestParam(value="code",required=false)String code){
        ResponseData result = new ResponseData();
        //验证手机号码格式是否正确
        if(!UtilPath.isMobile(username)){
            result.setState(500);
            result.setMessage("手机号码格式不正确！");
            return result;
        }
        String checkCode=redisTemplate.opsForValue().get("smsCode:"+username).toString();
        if(!checkCode.equals(code)){
            result.setState(500);
            result.setMessage("验证码不正确");
            return result;
        }
        if(password.equals("")){
            result.setState(500);
            result.setMessage("密码不能为空");
            return result;
        }
        List<SubscriberEntry> list = subscriberEntryService.selectAll();
        List<SubscriberEntry> listed = new ArrayList<>();
        for(SubscriberEntry subscriberEntry2:list){
            BigInteger telephone=new BigInteger(subscriberEntry2.getTelephone());
            BigInteger phone = new BigInteger(username);
            if(phone.compareTo(telephone)==0){
                listed.add(subscriberEntry2);
            }
        }

        Users user=new Users();
        user.setUsername(username);
        Users model=usersService.findOne(user);
        if(model!=null)
        {
            result.setState(500);
            result.setMessage("此手机号已存在");
            return result;
        }



        if(null == listed || listed.size() ==0 ){
            user.setStatus(0);
        }else{
            user.setStatus(1);
        }
        user.setPassword(MD5Util.generate(password));
        user.setMobile(username);
        user.setUsername(username);
        user.setCreateTime(new Date());
        user.setMobileStatus(1);
        user.setNickname(name);
        user.setAffiliateProject(project);
        Integer id=usersService.insert(user);
        result.setState(200);
        result.setMessage("注册成功！");
        return result;
    }



    /**
     * 修改密码
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @ApiOperation(value="修改密码",notes="修改密码", httpMethod = "POST")
    @RequestMapping(value="/setPwd",method=RequestMethod.POST)
    @ResponseBody
    public ResponseData setPwd(
                                 @ApiParam(required=true,name="token",value="令牌")@RequestParam(value="token",required=false)String token,
                                 @ApiParam(required=true,name="username",value="用户名(手机号)")@RequestParam(value="username",required=false)String username,
                                 @ApiParam(required=true,name="password",value="密码")@RequestParam(value="password",required=false)String password,
                                 @ApiParam(required=true,name="code",value="验证码")@RequestParam(value="code",required=false)String code){
        ResponseData result = new ResponseData();
        if(token==null||token.isEmpty()){
            result.setState(500);
            result.setMessage("参数为空");
            return result;
        }
        String id =MD5Utils.parseToken(token);
        if(id==null){
            result.setState(1000);
            result.setMessage("登入失效");
            return result;
        }
        //验证手机号码格式是否正确
        if(!UtilPath.isMobile(username)){
            result.setState(500);
            result.setMessage("手机号码格式不正确！");
            return result;
        }
        String checkCode=redisTemplate.opsForValue().get("smsCode:"+username).toString();

        if(!checkCode.equals(code)){
            result.setState(500);
            result.setMessage("验证码不正确");
            return result;
        }
        if(password.equals("")){
            result.setState(500);
            result.setMessage("密码不能为空");
            return result;
        }
        Users user=new Users();
        user.setToken(token);
        Users model=usersService.findOne(user);
        if(model==null)
        {
            result.setState(500);
            result.setMessage("此用户不存在");
            return result;
        }
        if(!model.getMobile().equals(username))
        {
            result.setState(500);
            result.setMessage("此手机号不正确");
            return result;
        }

        user.setPassword(MD5Util.generate(password));

        if(usersService.updateByPrimaryKey(model)>0){

            result.setState(200);
            result.setMessage("修改成功！");
            return result;
        }
        else
        {
            result.setState(500);
            result.setMessage("操作失败");
            return result;
        }


    }

    @ApiOperation(value="根据令牌获取消费记录",notes="根据令牌获取消费记录")
    @RequestMapping(value="/getBalances",method= RequestMethod.GET)
    @ResponseBody
    public ResponseData getBalances(HttpServletRequest request,
            @ApiParam(required=true,name="token",value="token")@RequestParam(value="token",required=false)String token,
            @ApiParam(required=true,name="status",value="0消费,1充值")@RequestParam(value="status",required=false)Integer status
    ){
        ResponseData result = new ResponseData();
        Users model=usersService.findUserByToken(token);
        if(model==null)
        {
            result.setState(500);
            result.setMessage("用户不存在或已禁用");
            return result;
        }
        //获取账户信息
        Page<UsersAccountLog> page = getPage(request);
        EntityWrapper<UsersAccountLog> ew=new EntityWrapper();
        if(status==0)
        {
            ew.lt("balance",0);
        }
        else if(status==1){
            ew.gt("balance",0);
        }
        ew.orderBy("id",false);
        ew.eq("user_id",model.getUserId());
        Map<String,Object> data=new HashMap<>();
        Page<UsersAccountLog> list=usersAccountLogService.selectPage(page,ew);
        data.put("accountLog",list.getRecords());
        result.setState(200);
        result.setDatas(data);
        result.setMessage("");


        return result;
    }




    @ApiOperation(value="根据令牌获取积分记录",notes="根据令牌获取积分记录")
    @RequestMapping(value="/getIntegral",method= RequestMethod.GET)
    @ResponseBody
    public ResponseData getIntegral(HttpServletRequest request,
                                    @ApiParam(required=true,name="token",value="token")@RequestParam(value="token",required=false)String token
    ){
        ResponseData result = new ResponseData();
        Users model=usersService.findUserByToken(token);
        if(model==null)
        {
            result.setState(500);
            result.setMessage("用户不存在或已禁用");
            return result;
        }
        //获取账户信息
        Page<UsersAccountLog> page = getPage(request);
        EntityWrapper<UsersAccountLog> ew=new EntityWrapper();
        ew.ne("points",0);
        ew.orderBy("id",false);
        ew.eq("user_id",model.getUserId());
        Map<String,Object> data=new HashMap<>();
        Page<UsersAccountLog> list=usersAccountLogService.selectPage(page,ew);
        data.put("accountLog",list.getRecords());
        result.setState(200);
        result.setDatas(data);
        result.setMessage("");


        return result;
    }


}
