package com.ff.InvoiceApplication;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ff.common.base.BaseController;
import com.ff.common.model.ResponseData;
import com.ff.common.util.ExcelUtil;
import com.ff.shop.model.InvoiceApplication;
import com.ff.shop.service.InvoiceApplicationService;
import com.ff.users.model.Users;
import com.ff.users.service.UsersService;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Controller
@RequestMapping("/invoice/application")
public class InvoiceApplicationController extends BaseController {

    @Reference
    private InvoiceApplicationService invoiceApplicationService;

    @Reference
    private UsersService usersService;

   /* @Autowired
    ApiExcelController apiExcelController;*/

    @RequiresPermissions(value = "invoice:application:list")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(String key, ModelMap map) {

        key= StringEscapeUtils.unescapeHtml(key);
        try {
            key = new String(key.getBytes("iso-8859-1"), "utf-8");
        } catch (Exception e) {
            // TODO: handle exception
        }

        map.put("key", key);
        return new ModelAndView("invoice/application/list",map);

    }

    @RequiresPermissions(value = "invoice:application:list")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doList(HttpServletRequest request, String key) {
        Page<InvoiceApplication> page = getPage(request);
        EntityWrapper<InvoiceApplication> ew=new EntityWrapper();
        ew.like("unit_name","%"+key+"%");
        Page<InvoiceApplication> data= invoiceApplicationService.selectPage(page,ew);
        Map<String, Object> resultMap = new LinkedHashMap();
        resultMap.put("data",data);
        return resultMap;
    }

    @RequiresPermissions("invoice:application:delete")
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(String[] id) {
        invoiceApplicationService.deleteByPrimaryKey(id);
        Map<String, Object> resultMap = new LinkedHashMap();
        resultMap.put("msg","删除成功");
        resultMap.put("status",200);
        return resultMap;
    }

    /*@RequiresPermissions("invoice:application:create")
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public Object create() throws Exception{

        Map<String, Object> resultMap = new LinkedHashMap();
        getExcel();
        String photoUrl = "http://39.100.82.67:8080/fish-web-0.0.1-SNAPSHOT/uploads/excel.xls";
        //文件名
        String fileName = photoUrl.substring(photoUrl.lastIndexOf("/"));

        //要保存的位置
        String filePath = "C:\\Users\\10312\\Desktop";


        File file= saveUrlAs(photoUrl, filePath + fileName,"GET");
        if(file.exists()) {
            resultMap.put("msg", "导出成功");
            resultMap.put("status", 200);
        }else{
            resultMap.put("msg","导出失败");
            resultMap.put("status",500);
        }
        return resultMap;
    }*/

    @RequiresPermissions("invoice:application:create")
    @RequestMapping(value = "/create",method = RequestMethod.GET)
    public ModelAndView create(ModelMap map) throws Exception{
        getExcel();

        Users users = new Users();
        users.setPassword("http://39.100.238.21:8082/fish-web-0.0.1-SNAPSHOT/uploads/excel.xls");
        map.put("url",users);

        return new ModelAndView("/invoice/application/create");
    }



    @RequiresPermissions("invoice:application:show")
    @RequestMapping(value="/show/{id}",method=RequestMethod.GET)
    public ModelAndView edit(@PathVariable("id")String id, ModelMap map){

        Users model = usersService.findByPrimaryKey(id);
        map.put("item", model);
        return new ModelAndView("/invoice/application/show",map);

    }


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
        List<InvoiceApplication> list = invoiceApplicationService.selectAll();

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
        String dirName ="C:\\Users\\Administrator\\Desktop\\tomcat\\gzh\\gcpt-controller\\webapps\\fish-web-0.0.1-SNAPSHOT\\uploads";

        //String dirName = "C:\\Users\\Administrator\\Desktop\\tomcat\\controller\\webapps\\fish-web-0.0.1-SNAPSHOT\\uploads";//C:\WorkSpaces\dxc
        //String dirName = "E:\\源码\\fish-web\\src\\main\\webapp\\uploads";
        String fileName = "excel";

        String name = ExcelUtil.exportXlsExcel(titleMape, titleList, contentListed, contentStyle, dirName, fileName);

        result.setDatas(name);
        result.setMessage("成功");
        result.setState(200);
        return result;
    }


    /*public File saveUrlAs(String url, String filePath, String method){
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
    }*/
}
