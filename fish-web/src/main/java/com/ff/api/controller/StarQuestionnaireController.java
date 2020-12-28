package com.ff.api.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ff.ad.model.Ad;
import com.ff.ad.model.QuestionnaireStar;
import com.ff.ad.service.QuestionnaireStarService;
import com.ff.common.base.BaseController;
import com.ff.common.model.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Api(value="问卷星跳转接口",description="问卷星跳转接口")
@Controller
@RequestMapping(value="/api/starQuestionnaire")
public class StarQuestionnaireController extends BaseController {

    @Reference
    private QuestionnaireStarService questionnaireStarService;


    @CrossOrigin(origins = "*",maxAge = 3600)
    @ApiOperation(value="获取图片与跳转id",notes="获取图片与跳转id")
    @RequestMapping(value="/getList",method= RequestMethod.POST)
    @ResponseBody
    public ResponseData getList(HttpServletRequest request, HttpServletResponse response, HttpSession sessio
    ){

        ResponseData result = new ResponseData();
        result.setState(200);
        List<QuestionnaireStar> list = questionnaireStarService.selectAll();
        if(null == list || list.size() ==0 ){
            QuestionnaireStar questionnaireStar = new QuestionnaireStar();
            result.setState(200);
            result.setDatas(questionnaireStar);
            result.setMessage("数据为空");
        }else{
            result.setDatas(list.get(0));
            result.setMessage("成功");
            result.setState(200);
        }

        return result;
    }

}
