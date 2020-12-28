package com.ff.api.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ff.common.base.BaseController;
import com.ff.common.model.ResponseData;
import com.ff.common.util.Paging;
import com.ff.shop.model.ContentDetails;
import com.ff.shop.model.PerformanceDisplay;
import com.ff.shop.service.PerformanceDisplayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Api(value = "业绩获取",description = "业绩获取")
@Controller
@RequestMapping("/api/performanceDisplay")
public class DisplayPerformanceController extends BaseController {

    @Reference
    private PerformanceDisplayService performanceDisplayService;

    @CrossOrigin(origins = "*", maxAge = 3600)
    @ApiOperation(value="业绩获取",notes="业绩获取")
    @RequestMapping(value="/getList",method= RequestMethod.POST)
    @ResponseBody
    public ResponseData getList(HttpServletRequest request, HttpServletResponse response,
                                @ApiParam(required=true,name="type",value="type")@RequestParam(value="type",required=false)Integer type
    ) {

        ResponseData result = new ResponseData();
        Page<PerformanceDisplay> page = getPage(request);
        if(StringUtils.isEmpty(type.toString())){
            result.setState(500);
            result.setMessage("参数为空");
            return result;
        }
        EntityWrapper<PerformanceDisplay> ew=new EntityWrapper();
        ew.orderBy("id",true);
        ew.eq("type",type);
        Map<String,Object> data=new HashMap<>();


        Page<PerformanceDisplay> list=performanceDisplayService.selectPage(page,ew);
        data.put("pages", list.getPages());
        data.put("size", list.getSize());
        data.put("total", list.getTotal());
        data.put("data",list.getRecords());
        result.setDatas(data);
        result.setState(200);
        result.setMessage("登入成功");
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
        PerformanceDisplay performanceDisplay = performanceDisplayService.findByPrimaryKey(id.toString());
        if(performanceDisplay!=null){
            result.setState(200);
            result.setDatas(performanceDisplay);
            result.setMessage("成功");
        }else{
            result.setState(500);
            result.setDatas(null);
            result.setMessage("数据为空");
        }

        return result;
    }

    /*@CrossOrigin(origins = "*", maxAge = 3600)
    @ApiOperation(value="业绩展示",notes="业绩展示")
    @RequestMapping(value="/getPerformanceList",method= RequestMethod.POST)
    @ResponseBody
    public ResponseData getPerformanceList(HttpServletRequest request, HttpServletResponse response,
                                @ApiParam(required=true,name="type",value="type")@RequestParam(value="type",required=false)Integer type,
                                           @ApiParam(required=true,name="rows",value="rows")@RequestParam(value="rows",required=false)Integer rows,
                                           @ApiParam(required=true,name="page",value="page")@RequestParam(value="page",required=false)Integer page
    ) {

        ResponseData result = new ResponseData();
        if(type==1){
            PerformanceDisplay performanceDisplay = new PerformanceDisplay();
            performanceDisplay.setType(1);
            List<PerformanceDisplay> list = performanceDisplayService.selectByT(performanceDisplay);
            PerformanceDisplay performanceDisplay1 = new PerformanceDisplay();
            performanceDisplay1.setType(2);
            List<PerformanceDisplay> list1 = performanceDisplayService.selectByT(performanceDisplay1);
            list.addAll(list1);
            Collections.sort(list, new Comparator<PerformanceDisplay>() {
                @Override
                public int compare(PerformanceDisplay o1, PerformanceDisplay o2) {
                    //升序
                    return o1.getAge().compareTo(o2.getAge());
                }
            });

            Paging paging = Paging.pagination(list.size(), rows, page);
            int fromIndex = paging.getQueryIndex();
            int toIndex = 0;
            if (fromIndex + paging.getPageSize() >= list.size()) {
                toIndex = list.size();
            } else {
                toIndex = fromIndex + paging.getPageSize();
            }
            if (fromIndex > toIndex) {
                result.setMessage("");
                return result;
            }
            result.setDatas(list.subList(fromIndex, toIndex));
            result.setState(200);
            result.setMessage("成功");
            return result;
        }
        return result;
    }*/
}
