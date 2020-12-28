/*
import com.ff.common.util.ExcelUtil;
import com.ff.shop.model.InvoiceApplication;
import com.ff.shop.service.InvoiceApplicationService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class CreateExcelTest {
    @Test
    public static void main(String args[]) throws Exception {
        //exportXlsExcel_Test();
        //请求的路径
        String photoUrl = "http://39.100.82.67:8080/fish-web-0.0.1-SNAPSHOT/uploads/excel.xls";
        //文件名
        String fileName = photoUrl.substring(photoUrl.lastIndexOf("/"));
        //System.out.println("fileName---->"+fileName);
        //要保存的位置
        String filePath = "C:\\Users\\10312\\Desktop";
        File file = saveUrlAs(photoUrl, filePath + fileName,"GET");
        System.out.println("Run ok!/n<BR>Get URL file " + file);

    }

    private static void exportXlsExcel_Test() throws Exception {
        Map<String, String> titleMape = new HashMap<String, String>();
        titleMape.put("value", "生成Excel文件测试");

        List<Map<String, String>> titleList = new ArrayList<Map<String,String>>();
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

        List<List<String>> contentList = new ArrayList<List<String>>();
        List<String> cl1 = new ArrayList<String>();
        cl1.add("111111");
        cl1.add("111111");
        cl1.add("111111");
        cl1.add("111111");
        cl1.add("111111");
        cl1.add("111111");
        contentList.add(cl1);

        List<String> cl2 = new ArrayList<String>();
        cl2.add("222222");
        cl2.add("222222");
        cl2.add("222222");
        cl2.add("222222");
        cl2.add("222222");
        cl2.add("222222");
        contentList.add(cl2);

        List<String> cl3 = new ArrayList<String>();
        cl3.add("333333");
        cl3.add("333333");
        cl3.add("333333");
        cl3.add("333333");
        cl3.add("333333");
        cl3.add("333333");
        contentList.add(cl3);

        List<String> cl4 = new ArrayList<String>();
        cl4.add("444444");
        cl4.add("444444");
        cl4.add("444444");
        cl4.add("444444");
        cl4.add("444444");
        cl4.add("444444");
        contentList.add(cl4);

        List<String> cl5 = new ArrayList<String>();
        cl5.add("555555");
        cl5.add("555555");
        cl5.add("555555");
        cl5.add("555555");
        cl5.add("555555");
        cl5.add("555555");
        contentList.add(cl5);

        List<String> cl6 = new ArrayList<String>();
        cl6.add("666666");
        cl6.add("666666");
        cl6.add("666666");
        cl6.add("666666");
        cl6.add("666666");
        cl6.add("666666");
        contentList.add(cl6);

        List<String> cl7 = new ArrayList<String>();
        cl7.add("777777");
        cl7.add("777777");
        cl7.add("777777");
        cl7.add("777777");
        cl7.add("777777");
        cl7.add("777777");
        contentList.add(cl7);

        List<String> cl8 = new ArrayList<String>();
        cl8.add("888888");
        cl8.add("888888");
        cl8.add("888888");
        cl8.add("888888");
        cl8.add("888888");
        cl8.add("888888");
        contentList.add(cl8);

        List<String> cl9 = new ArrayList<String>();
        cl9.add("999999");
        cl9.add("999999");
        cl9.add("999999");
        cl9.add("999999");
        cl9.add("999999");
        cl9.add("999999");
        contentList.add(cl9);

        Map<String, String> contentStyle = new HashMap<String, String>();
        String dirName = "D:\\学习";//C:\WorkSpaces\dxc
        String fileName = "ExcelWriteUtil_exportXlsExcel_Test";

        String name = ExcelUtil.exportXlsExcel(titleMape, titleList, contentList, contentStyle, dirName, fileName);
        System.out.println(name);
    }
    */
/**
     * @功能 下载临时素材接口
     * @param filePath 文件将要保存的目录
     * @param method 请求方法，包括POST和GET
     * @param url 请求的路径
     * @return
     *//*



    public static File saveUrlAs(String url, String filePath, String method){
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
