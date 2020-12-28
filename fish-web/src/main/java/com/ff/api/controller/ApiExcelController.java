/*
package com.ff.api.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ff.common.base.BaseController;
import com.ff.common.model.ResponseData;
import com.ff.common.util.ExcelUtil;
import com.ff.shop.model.InvoiceApplication;
import com.ff.shop.service.InvoiceApplicationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value="创建excel",description="创建excel")
@RequestMapping(value = "/api/create")
@Controller
public class ApiExcelController extends BaseController {

    @Reference
    private InvoiceApplicationService service;

    @ApiOperation(value="获取广告",notes="获取广告")
    @RequestMapping(value="/getExcel",method= RequestMethod.POST)
    @ResponseBody
    public ResponseData getExcel() throws Exception {
        ResponseData result = new ResponseData();
        Map<String, String> titleMape = new HashMap<String, String>();
        titleMape.put("value", "生成Excel文件测试");

        List<Map<String, String>> titleList = new ArrayList<Map<String, String>>();
        Map<String, String> tm1 = new HashMap<String, String>();
        tm1.put("value", "ID");
        titleList.add(tm1);
        Map<String, String> tm2 = new HashMap<String, String>();
        tm2.put("value", "项目负责人");
        titleList.add(tm2);
        Map<String, String> tm3 = new HashMap<String, String>();
        tm3.put("value", "合同类别");
        titleList.add(tm3);
        Map<String, String> tm4 = new HashMap<String, String>();
        tm4.put("value", "项目类型");
        titleList.add(tm4);
        Map<String, String> tm5 = new HashMap<String, String>();
        tm5.put("value", "合同额");
        titleList.add(tm5);
        Map<String, String> tm6 = new HashMap<String, String>();
        tm6.put("value", "发票金额");
        titleList.add(tm6);
        Map<String, String> tm7 = new HashMap<String, String>();
        tm7.put("value", "到账时间");
        titleList.add(tm7);
        Map<String, String> tm8 = new HashMap<String, String>();
        tm8.put("value", "项目名称");
        titleList.add(tm8);
        Map<String, String> tm9 = new HashMap<String, String>();
        tm9.put("value", "项目地址");
        titleList.add(tm9);
        Map<String, String> tm10 = new HashMap<String, String>();
        tm10.put("value", "单位名称");
        titleList.add(tm10);
        Map<String, String> tm11 = new HashMap<String, String>();
        tm11.put("value", "社会信用统一代码");
        titleList.add(tm11);
        Map<String, String> tm12 = new HashMap<String, String>();
        tm12.put("value", "银行账户");
        titleList.add(tm12);
        Map<String, String> tm13 = new HashMap<String, String>();
        tm13.put("value", "开户行");
        titleList.add(tm13);
        Map<String, String> tm14 = new HashMap<String, String>();
        tm14.put("value", "公司地址");
        titleList.add(tm14);
        Map<String, String> tm15 = new HashMap<String, String>();
        tm15.put("value", "公司电话");
        titleList.add(tm15);
        Map<String, String> tm16 = new HashMap<String, String>();
        tm16.put("value", "其它");
        titleList.add(tm16);
        Map<String, String> tm17 = new HashMap<String, String>();
        tm17.put("value", "提交时间");
        titleList.add(tm17);

        List<List<String>> contentListed = new ArrayList<List<String>>();


        //获取全部记录
        List<InvoiceApplication> list = service.selectAll();

        for (int i = 0; i < list.size(); i++) {
            List<String> cl2 = new ArrayList<>();
            InvoiceApplication invoiceApplication = list.get(i);
            cl2.add(invoiceApplication.getId().toString());
            cl2.add(invoiceApplication.getProjectLeader());//负责人
            Integer type = invoiceApplication.getContractCategories();
            String contractCategories = "";
            if (type == 1) {
                contractCategories = "施工";
            } else {
                contractCategories = "施工";
            }
            cl2.add(contractCategories);//合同类别
            Integer itemCategoryType = invoiceApplication.getItemCategory();
            String itemCategory = "";
            if (itemCategoryType == 1) {
                itemCategory = "一般计税(9%)";
            } else if (itemCategoryType == 2) {
                itemCategory = "简易计税(3%)";
            } else {
                itemCategory = "设计(6%)";
            }
            cl2.add(itemCategory);//项目类型
            cl2.add(invoiceApplication.getContractAmount());//合同额
            cl2.add(invoiceApplication.getInvoiceAmount());//发票金额
            Integer paymentDateType = Integer.parseInt(invoiceApplication.getPaymentDate());
            String paymentDate = "";
            if (paymentDateType == 1) {
                paymentDate = "本月";
            } else {
                paymentDate = "下月初";
            }
            cl2.add(paymentDate);
            cl2.add(invoiceApplication.getEntryName());//项目名称
            cl2.add(invoiceApplication.getProjectAddress());//项目地址
            cl2.add(invoiceApplication.getUnitName());//单位名称
            cl2.add(invoiceApplication.getCreditCode());//社会信用统一代码
            cl2.add(invoiceApplication.getBankAccount());//银行账户
            cl2.add(invoiceApplication.getOpeningBank());//开户行
            cl2.add(invoiceApplication.getCompanyAddress());//公司地址
            cl2.add(invoiceApplication.getCompanyTelephone());//公司电话
            cl2.add(invoiceApplication.getOthers());//其它
            cl2.add(invoiceApplication.getAddTime());//创建时间
            contentListed.add(cl2);
        }


        Map<String, String> contentStyle = new HashMap<String, String>();
        String dirName = "E:\\源码\\fish-web\\src\\main\\webapp\\uploads";//C:\WorkSpaces\dxc
        String fileName = "excel";

        String name = ExcelUtil.exportXlsExcel(titleMape, titleList, contentListed, contentStyle, dirName, fileName);
       */
/* System.out.println(name);*//*

        result.setDatas(name);
        result.setMessage("成功");
        result.setState(200);
        return result;
    }


    public File saveUrlAs(String url, String filePath, String method){
        String contentType ="application/x-xls";
        //创建不同的文件夹目录
        File file=new File(filePath);
        //判断文件夹是否存在
        if (!file.exists())
        {
            //如果文件夹不存在，则创建新的的文件夹
            file.mkdirs();
        }
        FileOutputStream fileOut = null;
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        try
        {
            // 建立链接
            URL httpUrl=new URL(url);
            conn=(HttpURLConnection) httpUrl.openConnection();
            conn.setRequestProperty("Content-Type", contentType);
            //以Post方式提交表单，默认get方式
            conn.setRequestMethod(method);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            // post方式不能使用缓存
            conn.setUseCaches(false);
            //连接指定的资源
            conn.connect();
            //获取网络输入流
            inputStream=conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            //判断文件的保存路径后面是否以/结尾
            if (!filePath.endsWith("/")) {
                filePath += "/";

            }
            //写入到文件（注意文件保存路径的后面一定要加上文件的名称）
            fileOut = new FileOutputStream(filePath+"excel.xls");
            BufferedOutputStream bos = new BufferedOutputStream(fileOut);

            byte[] buf = new byte[4096];
            int length = bis.read(buf);
            //保存文件
            while(length != -1)
            {
                bos.write(buf, 0, length);
                length = bis.read(buf);
            }
            bos.close();
            bis.close();
            conn.disconnect();
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("抛出异常！！");
        }

        return file;
    }


}
*/
